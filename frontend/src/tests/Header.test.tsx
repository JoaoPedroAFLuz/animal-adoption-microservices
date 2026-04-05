import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';

vi.mock('next/link', () => ({
  default: ({ children, href }: { children: React.ReactNode; href: string }) => (
    <a href={href}>{children}</a>
  ),
}));

vi.mock('@/auth', () => ({
  auth: vi.fn(),
  signIn: vi.fn(),
  signOut: vi.fn(),
}));

import { auth } from '@/auth';

const mockAuth = vi.mocked(auth);

async function renderHeader() {
  const { Header } = await import('@/components/Header');
  const HeaderResolved = await Header();
  render(HeaderResolved);
}

describe('Header', () => {
  it('should show sign in button when not authenticated', async () => {
    mockAuth.mockResolvedValue(null);

    await renderHeader();

    expect(screen.getByText('Sign In')).toBeInTheDocument();
    expect(screen.queryByText('Sign Out')).not.toBeInTheDocument();
    expect(screen.queryByText('My Pets')).not.toBeInTheDocument();
  });

  it('should show user name, my pets link, and sign out when authenticated', async () => {
    mockAuth.mockResolvedValue({
      user: { name: 'João' },
      displayName: 'João Luz',
      expires: '',
    });

    await renderHeader();

    expect(screen.getByText('João Luz')).toBeInTheDocument();
    expect(screen.getByText('Sign Out')).toBeInTheDocument();
    expect(screen.getByText('My Pets')).toBeInTheDocument();
    expect(screen.queryByText('Sign In')).not.toBeInTheDocument();
  });
});
