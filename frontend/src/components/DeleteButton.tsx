'use client';

import { useRouter } from 'next/navigation';
import { toast } from 'react-toastify';

import { deletePet } from '@/lib/actions';

interface DeleteButtonProps {
  petId: string;
  petName: string;
}

export function DeleteButton({ petId, petName }: DeleteButtonProps) {
  const router = useRouter();

  async function handleDelete() {
    if (!confirm(`Are you sure you want to delete ${petName}?`)) return;

    try {
      await deletePet(petId);

      toast.success(`${petName} deleted successfully.`);

      router.push('/pets');
    } catch {
      toast.error('Failed to delete pet. Please try again.');
    }
  }

  return (
    <button
      onClick={handleDelete}
      className="w-full rounded-lg border border-red-300 px-6 py-3 font-medium text-red-600 hover:bg-red-50"
    >
      Delete {petName}
    </button>
  );
}
