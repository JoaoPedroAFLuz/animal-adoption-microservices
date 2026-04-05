import { NextRequest, NextResponse } from 'next/server';

import { auth } from '@/auth';

const protectedPaths = ['/my-pets', '/admin', '/profile'];

export async function proxy(request: NextRequest) {
  const session = await auth();
  const { pathname } = request.nextUrl;

  const isProtected = protectedPaths.some((path) => pathname.startsWith(path));

  if (isProtected && (!session || session.error === 'RefreshTokenError')) {
    return NextResponse.redirect(new URL('/api/auth/signin', request.url));
  }

  return NextResponse.next();
}
