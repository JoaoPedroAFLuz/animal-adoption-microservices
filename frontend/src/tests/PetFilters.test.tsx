import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';

import { PetFilters } from '@/components/PetFilters';

const mockPush = vi.fn();

vi.mock('next/navigation', () => ({
  useRouter: () => ({ push: mockPush }),
  useSearchParams: () => new URLSearchParams(),
}));

describe('PetFilters', () => {
  it('should render all filter dropdowns', () => {
    render(<PetFilters />);

    expect(screen.getByLabelText('Species')).toBeInTheDocument();
    expect(screen.getByLabelText('Gender')).toBeInTheDocument();
    expect(screen.getByLabelText('Size')).toBeInTheDocument();
    expect(screen.getByLabelText('Status')).toBeInTheDocument();
  });

  it('should navigate with filter param when selecting a value', async () => {
    const user = userEvent.setup();

    render(<PetFilters />);

    await user.selectOptions(screen.getByLabelText('Species'), 'DOG');

    expect(mockPush).toHaveBeenCalledWith('/pets?specie=DOG');
  });
});
