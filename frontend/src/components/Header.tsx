import Link from 'next/link';

import { auth, signIn, signOut } from '@/auth';

export async function Header() {
  const session = await auth();

  return (
    <header className="bg-brand shadow-sm">
      <nav className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
        <Link href="/" className="text-xl font-bold text-gray-900">
          🐾 Pet Adoption
        </Link>

        <div className="flex items-center gap-6">
          <Link href="/pets" className="text-sm font-medium text-gray-800 hover:text-gray-950">
            Browse Pets
          </Link>

          {session ? (
            <>
              <Link
                href="/my-pets"
                className="text-sm font-medium text-gray-800 hover:text-gray-950"
              >
                My Pets
              </Link>

              <span className="text-sm text-gray-800">{session.displayName}</span>

              <form
                action={async () => {
                  'use server';
                  const idToken = session.idToken;
                  await signOut({ redirect: false });

                  const keycloakLogoutUrl =
                    `${process.env.AUTH_KEYCLOAK_ISSUER}/protocol/openid-connect/logout` +
                    `?id_token_hint=${idToken}` +
                    `&post_logout_redirect_uri=${encodeURIComponent('http://localhost:3000')}`;

                  const { redirect } = await import('next/navigation');
                  redirect(keycloakLogoutUrl);
                }}
              >
                <button
                  type="submit"
                  className="rounded-lg bg-gray-900 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800"
                >
                  Sign Out
                </button>
              </form>
            </>
          ) : (
            <form
              action={async () => {
                'use server';
                await signIn('keycloak');
              }}
            >
              <button
                type="submit"
                className="rounded-lg bg-gray-900 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800"
              >
                Sign In
              </button>
            </form>
          )}
        </div>
      </nav>
    </header>
  );
}
