import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';

import { PetForm } from '@/components/PetForm';
import { mockPet } from '@/tests/mocks/server';

vi.mock('next/navigation', () => ({
  useRouter: () => ({ push: vi.fn() }),
}));

vi.mock('react-toastify', () => ({
  toast: { success: vi.fn(), error: vi.fn() },
}));

vi.mock('@/lib/actions', () => ({
  createPet: vi.fn(),
  updatePet: vi.fn(),
}));

describe('PetForm', () => {
  it('should render all form fields', () => {
    render(<PetForm />);

    expect(screen.getByLabelText(/name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/description/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/species/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/breed/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/size/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/gender/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/birth date/i)).toBeInTheDocument();
  });

  it('should show validation errors on empty submit', async () => {
    const user = userEvent.setup();

    render(<PetForm />);

    await user.click(screen.getByRole('button', { name: /register pet/i }));

    expect(screen.getByText('Name is required')).toBeInTheDocument();
    expect(screen.getByText('Species is required')).toBeInTheDocument();
    expect(screen.getByText('Breed is required')).toBeInTheDocument();
    expect(screen.getByText('Size is required')).toBeInTheDocument();
    expect(screen.getByText('Gender is required')).toBeInTheDocument();
    expect(screen.getByText('Birth date is required')).toBeInTheDocument();
  });

  it('should pre-fill values and show update button in edit mode', () => {
    render(<PetForm pet={mockPet} />);

    expect(screen.getByLabelText(/name/i)).toHaveValue('Luna');
    expect(screen.getByLabelText(/breed/i)).toHaveValue('Golden Retriever');
    expect(screen.getByLabelText(/species/i)).toHaveValue('DOG');
    expect(screen.getByLabelText(/size/i)).toHaveValue('LARGE');
    expect(screen.getByLabelText(/gender/i)).toHaveValue('FEMALE');
    expect(screen.getByRole('button', { name: /update pet/i })).toBeInTheDocument();
  });
});
