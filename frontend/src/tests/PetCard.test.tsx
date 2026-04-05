import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';

import { PetCard } from '@/components/PetCard';
import { mockPet } from '@/tests/mocks/server';

describe('PetCard', () => {
  it('should render pet name and breed', () => {
    render(<PetCard pet={mockPet} />);

    expect(screen.getByText('Luna')).toBeInTheDocument();
    expect(screen.getByText('Golden Retriever')).toBeInTheDocument();
  });

  it('should render status badge', () => {
    render(<PetCard pet={mockPet} />);

    expect(screen.getByText('AVAILABLE')).toBeInTheDocument();
  });

  it('should render gender and size', () => {
    render(<PetCard pet={mockPet} />);

    expect(screen.getByText('FEMALE · LARGE')).toBeInTheDocument();
  });

  it('should link to pet details page', () => {
    render(<PetCard pet={mockPet} />);

    const link = screen.getByRole('link');

    expect(link).toHaveAttribute('href', `/pets/${mockPet.id}`);
  });
});
