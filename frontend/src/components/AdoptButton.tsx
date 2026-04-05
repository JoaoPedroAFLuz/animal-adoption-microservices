'use client';

import { useRouter } from 'next/navigation';
import { toast } from 'react-toastify';

import { api } from '@/lib/api';

interface AdoptButtonProps {
  petId: string;
  petName: string;
  token: string;
}

export function AdoptButton({ petId, petName, token }: AdoptButtonProps) {
  const router = useRouter();

  async function handleAdopt() {
    try {
      await api.put(`/pets/adopt/${petId}`, null, { token });

      toast.success(`You adopted ${petName}!`);
      router.refresh();
    } catch {
      toast.error('Failed to adopt. Please try again.');
    }
  }

  return (
    <button
      onClick={handleAdopt}
      className="w-full rounded-lg bg-brand px-6 py-3 font-medium text-gray-900 hover:bg-brand-dark"
    >
      Adopt {petName}
    </button>
  );
}
