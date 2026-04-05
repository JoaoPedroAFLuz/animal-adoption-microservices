import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';

import { Pagination } from '@/components/Pagination';

vi.mock('next/navigation', () => ({
  useRouter: () => ({ push: vi.fn() }),
  useSearchParams: () => new URLSearchParams(),
}));

describe('Pagination', () => {
  it('should not render when there is only one page', () => {
    const { container } = render(<Pagination totalPages={1} currentPage={0} />);

    expect(container.innerHTML).toBe('');
  });

  it('should render page buttons', () => {
    render(<Pagination totalPages={3} currentPage={0} />);

    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument();
    expect(screen.getByText('3')).toBeInTheDocument();
  });

  it('should disable previous button on first page', () => {
    render(<Pagination totalPages={3} currentPage={0} />);

    expect(screen.getByText('Previous')).toBeDisabled();
    expect(screen.getByText('Next')).not.toBeDisabled();
  });

  it('should disable next button on last page', () => {
    render(<Pagination totalPages={3} currentPage={2} />);

    expect(screen.getByText('Next')).toBeDisabled();
    expect(screen.getByText('Previous')).not.toBeDisabled();
  });
});
