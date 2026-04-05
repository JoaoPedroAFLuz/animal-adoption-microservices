'use client';

import { useRouter } from 'next/navigation';
import { toast } from 'react-toastify';

import { adoptPet } from '@/lib/actions';

interface AdoptButtonProps {
  petId: string;
  petName: string;
}

export function AdoptButton({ petId, petName }: AdoptButtonProps) {
  const router = useRouter();

  async function handleAdopt() {
    try {
      await adoptPet(petId);

      toast.success(`You adopted ${petName}!`);

      router.refresh();
    } catch {
      toast.error('Failed to adopt. Please try again.');
    }
  }

  return (
    <button
      onClick={handleAdopt}
      className="bg-brand hover:bg-brand-dark w-full rounded-lg px-6 py-3 font-medium text-gray-900"
    >
      Adopt {petName}
    </button>
  );
}
