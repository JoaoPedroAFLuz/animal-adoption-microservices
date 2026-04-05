import { NextRequest, NextResponse } from 'next/server';

import { auth } from '@/auth';

const protectedPaths = ['/my-pets', '/admin'];

export async function proxy(request: NextRequest) {
  const session = await auth();
  const { pathname } = request.nextUrl;

  const isProtected = protectedPaths.some((path) => pathname.startsWith(path));

  if (isProtected && !session) {
    return NextResponse.redirect(new URL('/api/auth/signin', request.url));
  }

  return NextResponse.next();
}
