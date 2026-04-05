'use client';

import { useRouter, useSearchParams } from 'next/navigation';

interface PaginationProps {
  totalPages: number;
  currentPage: number;
}

export function Pagination({ totalPages, currentPage }: PaginationProps) {
  const router = useRouter();
  const searchParams = useSearchParams();

  if (totalPages <= 1) return null;

  function goToPage(page: number) {
    const params = new URLSearchParams(searchParams.toString());

    params.set('page', String(page));
    router.push(`/pets?${params.toString()}`);
  }

  return (
    <div className="flex items-center justify-center gap-2">
      <button
        onClick={() => goToPage(currentPage - 1)}
        disabled={currentPage === 0}
        className="rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:opacity-50"
      >
        Previous
      </button>

      {Array.from({ length: totalPages }, (_, i) => (
        <button
          key={i}
          onClick={() => goToPage(i)}
          className={`rounded-lg px-3 py-2 text-sm ${
            i === currentPage
              ? 'bg-brand font-medium text-gray-900'
              : 'border border-gray-300 hover:bg-gray-100'
          }`}
        >
          {i + 1}
        </button>
      ))}

      <button
        onClick={() => goToPage(currentPage + 1)}
        disabled={currentPage === totalPages - 1}
        className="rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:opacity-50"
      >
        Next
      </button>
    </div>
  );
}
