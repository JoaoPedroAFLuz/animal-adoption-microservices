'use client';

import { useRouter } from 'next/navigation';
import { toast } from 'react-toastify';

import { toggleFeatured } from '@/lib/actions';

interface FeaturedButtonProps {
  petId: string;
  featured: boolean;
}

export function FeaturedButton({ petId, featured }: FeaturedButtonProps) {
  const router = useRouter();

  async function handleToggle() {
    try {
      await toggleFeatured(petId);

      toast.success(featured ? 'Removed from featured' : 'Added to featured');

      router.refresh();
    } catch {
      toast.error('Failed to update featured status.');
    }
  }

  return (
    <button
      onClick={handleToggle}
      className={`w-full rounded-lg border px-6 py-3 font-medium ${
        featured
          ? 'border-yellow-400 bg-yellow-50 text-yellow-700'
          : 'border-gray-300 text-gray-700 hover:bg-gray-50'
      }`}
    >
      {featured ? '⭐ Featured' : '☆ Mark as Featured'}
    </button>
  );
}
