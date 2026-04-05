import Image from 'next/image';
import Link from 'next/link';

import { auth, signIn, signOut } from '@/auth';
import { NavLinks } from '@/components/NavLinks';

export async function Header() {
  const session = await auth();

  const links = [
    { href: '/pets', label: 'Browse Pets' },
    ...(session ? [{ href: '/my-pets', label: 'My Pets' }] : []),
    ...(session?.roles?.includes('REGISTER_PET')
      ? [{ href: '/admin/pets/new', label: 'Register Pet' }]
      : []),
  ];

  return (
    <header className="bg-brand shadow-sm">
      <nav className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
        <Link href="/" className="text-xl font-bold text-gray-900">
          🐾 Pet Adoption
        </Link>

        <div className="flex items-center gap-6">
          <NavLinks links={links} />

          {session ? (
            <>
              <Link href="/profile" className="flex items-center gap-2">
                {session.picture ? (
                  <Image
                    src={session.picture}
                    alt={session.displayName || ''}
                    width={32}
                    height={32}
                    className="rounded-full"
                  />
                ) : (
                  <div className="flex h-8 w-8 items-center justify-center rounded-full bg-gray-900 text-sm font-medium text-white">
                    {session.displayName?.charAt(0) || '?'}
                  </div>
                )}
                <span className="text-sm text-gray-800">{session.displayName}</span>
              </Link>

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
