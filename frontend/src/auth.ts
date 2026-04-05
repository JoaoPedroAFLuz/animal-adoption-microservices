import NextAuth from 'next-auth';
import Keycloak from 'next-auth/providers/keycloak';

import { decodeJwtPayload } from '@/lib/jwt';

declare module 'next-auth' {
  interface Session {
    accessToken?: string;
    idToken?: string;
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
        token.idToken = account.id_token;
        token.expiresAt = account.expires_at;
        token.displayName = buildDisplayName(profile);

        try {
          const payload = decodeJwtPayload(account.access_token as string);
          token.roles =
            (payload.resource_access as Record<string, { roles: string[] }>)?.['animal-adoption']
              ?.roles || [];
        } catch {
          token.roles = [];
        }

        return token;
      }

      if (token.expiresAt && Date.now() < token.expiresAt * 1000 - 60_000) {
        return token;
      }

      return refreshAccessToken(token);
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken;
      session.idToken = token.idToken as string;
      session.roles = token.roles;
      session.displayName = token.displayName as string;
      session.error = token.error;

      return session;
    },
  },
});

function buildDisplayName(profile?: Record<string, unknown>) {
  if (!profile) return '';

  const firstName = (profile.given_name as string) || '';
  const lastName = (profile.family_name as string) || '';
  const lastWord = lastName.trim().split(/\s+/).pop() || '';

  return `${firstName} ${lastWord}`.trim();
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
    token.idToken = data.id_token;
    token.expiresAt = Math.floor(Date.now() / 1000) + data.expires_in;
    token.error = undefined;

    try {
      const payload = decodeJwtPayload(data.access_token as string);
      token.roles =
        (payload.resource_access as Record<string, { roles: string[] }>)?.['animal-adoption']
          ?.roles || [];
    } catch {
      token.roles = [];
    }

    return token;
  } catch {
    token.error = 'RefreshTokenError';
    return token;
  }
}
