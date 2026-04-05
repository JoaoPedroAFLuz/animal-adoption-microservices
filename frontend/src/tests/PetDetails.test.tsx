import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi, beforeAll, afterAll, afterEach } from 'vitest';

import { formatDate } from '@/utils/format';
import { mockPet, server } from '@/tests/mocks/server';

vi.mock('next/link', () => ({
  default: ({ children, href }: { children: React.ReactNode; href: string }) => (
    <a href={href}>{children}</a>
  ),
}));

vi.mock('next/navigation', () => ({
  notFound: vi.fn(),
}));

function PetDetails({ pet }: { pet: typeof mockPet }) {
  const specieEmoji: Record<string, string> = { DOG: '🐕', CAT: '🐈' };

  return (
    <div>
      <h1>
        {specieEmoji[pet.specie] || '🐾'} {pet.name}
      </h1>
      <span>{pet.status}</span>
      {pet.description && <p>{pet.description}</p>}
      <span>{pet.specie}</span>
      <span>{pet.breed}</span>
      <span>{pet.size}</span>
      <span>{pet.gender}</span>
      <span>{formatDate(pet.birthDate)}</span>
      {pet.status === 'AVAILABLE' && <button disabled>Adopt {pet.name}</button>}
    </div>
  );
}

describe('PetDetails', () => {
  beforeAll(() => server.listen());
  afterEach(() => server.resetHandlers());
  afterAll(() => server.close());

  it('should render all pet information', () => {
    render(<PetDetails pet={mockPet} />);

    expect(screen.getByRole('heading', { name: /Luna/ })).toBeInTheDocument();
    expect(screen.getByText('Friendly dog')).toBeInTheDocument();
    expect(screen.getByText('Golden Retriever')).toBeInTheDocument();
    expect(screen.getByText('DOG')).toBeInTheDocument();
    expect(screen.getByText('LARGE')).toBeInTheDocument();
    expect(screen.getByText('FEMALE')).toBeInTheDocument();
    expect(screen.getByText('15/06/2023')).toBeInTheDocument();
  });

  it('should show disabled adopt button for available pets', () => {
    render(<PetDetails pet={mockPet} />);

    const button = screen.getByRole('button', { name: /adopt luna/i });

    expect(button).toBeDisabled();
  });

  it('should not show adopt button for adopted pets', () => {
    render(<PetDetails pet={{ ...mockPet, status: 'ADOPTED' }} />);

    expect(screen.queryByRole('button', { name: /adopt/i })).not.toBeInTheDocument();
  });
});
