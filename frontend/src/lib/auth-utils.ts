import { redirect } from 'next/navigation';

import { auth } from '@/auth';

export async function requireAuth() {
  const session = await auth();

  if (!session?.accessToken || session.error === 'RefreshTokenError') {
    redirect('/api/auth/signin');
  }

  return session;
}

export async function requireRole(role: string) {
  const session = await requireAuth();

  if (!session.roles?.includes(role)) {
    redirect('/');
  }

  return session;
}
