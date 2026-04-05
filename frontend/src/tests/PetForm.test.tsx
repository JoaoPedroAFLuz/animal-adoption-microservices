import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';

import { PetForm } from '@/components/PetForm';

vi.mock('next/navigation', () => ({
  useRouter: () => ({ push: vi.fn() }),
}));

vi.mock('react-toastify', () => ({
  toast: { success: vi.fn(), error: vi.fn() },
}));

describe('PetForm', () => {
  it('should render all form fields', () => {
    render(<PetForm token="test-token" />);

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

    render(<PetForm token="test-token" />);

    await user.click(screen.getByRole('button', { name: /register pet/i }));

    expect(screen.getByText('Name is required')).toBeInTheDocument();
    expect(screen.getByText('Species is required')).toBeInTheDocument();
    expect(screen.getByText('Breed is required')).toBeInTheDocument();
    expect(screen.getByText('Size is required')).toBeInTheDocument();
    expect(screen.getByText('Gender is required')).toBeInTheDocument();
    expect(screen.getByText('Birth date is required')).toBeInTheDocument();
  });
});
