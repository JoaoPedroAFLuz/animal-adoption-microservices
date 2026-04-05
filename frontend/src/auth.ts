import NextAuth from 'next-auth';
import Keycloak from 'next-auth/providers/keycloak';

declare module 'next-auth' {
  interface Session {
    accessToken?: string;
    roles?: string[];
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
    async jwt({ token, account }) {
      if (account) {
        token.accessToken = account.access_token;
        token.roles = extractRoles(account.access_token);
      }

      return token;
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken as string;
      session.roles = token.roles as string[];

      return session;
    },
  },
});

function extractRoles(accessToken?: string): string[] {
  if (!accessToken) return [];

  try {
    const payload = JSON.parse(atob(accessToken.split('.')[1]));
    return payload.resource_access?.['animal-adoption']?.roles || [];
  } catch {
    return [];
  }
}
