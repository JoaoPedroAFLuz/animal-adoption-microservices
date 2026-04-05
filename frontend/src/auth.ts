import NextAuth from 'next-auth';
import Keycloak from 'next-auth/providers/keycloak';

declare module 'next-auth' {
  interface Session {
    accessToken?: string;
    roles?: string[];
    displayName?: string;
    error?: string;
  }
}

declare module '@auth/core/jwt' {
  interface JWT {
    accessToken?: string;
    refreshToken?: string;
    expiresAt?: number;
    roles?: string[];
    error?: string;
  }
}

export const { handlers, signIn, signOut, auth } = NextAuth({
  providers: [
    Keycloak({
      clientId: process.env.AUTH_KEYCLOAK_ID,
      issuer: process.env.AUTH_KEYCLOAK_ISSUER,
    }),
  ],
  callbacks: {
    async jwt({ token, account, profile }) {
      if (account) {
        token.accessToken = account.access_token;
        token.refreshToken = account.refresh_token;
        token.expiresAt = account.expires_at;
        token.roles = extractRoles(account.access_token);
        token.displayName = buildDisplayName(profile);

        return token;
      }

      if (token.expiresAt && Date.now() < token.expiresAt * 1000 - 60_000) {
        return token;
      }

      return refreshAccessToken(token);
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken;
      session.roles = token.roles;
      session.displayName = token.displayName as string;
      session.error = token.error;

      return session;
    },
  },
});

function buildDisplayName(profile?: { given_name?: string; family_name?: string; name?: string }) {
  if (!profile) return '';

  const firstName = profile.given_name || '';
  const lastName = profile.family_name || '';
  const lastWord = lastName.trim().split(/\s+/).pop() || '';

  return `${firstName} ${lastWord}`.trim();
}

function extractRoles(accessToken?: string): string[] {
  if (!accessToken) return [];

  try {
    const payload = JSON.parse(atob(accessToken.split('.')[1]));
    return payload.resource_access?.['animal-adoption']?.roles || [];
  } catch {
    return [];
  }
}

async function refreshAccessToken(token: import('@auth/core/jwt').JWT) {
  try {
    const response = await fetch(
      `${process.env.AUTH_KEYCLOAK_ISSUER}/protocol/openid-connect/token`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
          client_id: process.env.AUTH_KEYCLOAK_ID!,
          grant_type: 'refresh_token',
          refresh_token: token.refreshToken!,
        }),
      },
    );

    const data = await response.json();

    if (!response.ok) throw data;

    token.accessToken = data.access_token;
    token.refreshToken = data.refresh_token;
    token.expiresAt = Math.floor(Date.now() / 1000) + data.expires_in;
    token.roles = extractRoles(data.access_token);
    token.error = undefined;

    return token;
  } catch {
    token.error = 'RefreshTokenError';
    return token;
  }
}
