import Link from 'next/link';

import type { Pet } from '@/types';

interface PetCardProps {
  pet: Pet;
}

const specieEmoji: Record<string, string> = {
  DOG: '🐕',
  CAT: '🐈',
};

export function PetCard({ pet }: PetCardProps) {
  return (
    <Link
      href={`/pets/${pet.id}`}
      className="hover:border-brand block rounded-lg border border-gray-200 bg-white p-5 shadow-sm transition hover:shadow-md"
    >
      <div className="mb-3 flex items-center justify-between">
        <span className="text-2xl">{specieEmoji[pet.specie] || '🐾'}</span>
        <span
          className={`rounded-full px-2.5 py-0.5 text-xs font-medium ${
            pet.status === 'AVAILABLE' ? 'bg-brand/20 text-gray-800' : 'bg-gray-100 text-gray-600'
          }`}
        >
          {pet.status}
        </span>
      </div>

      <h3 className="text-lg font-semibold text-gray-900">{pet.name}</h3>

      <div className="mt-2 space-y-1 text-sm text-gray-500">
        <p>{pet.breed}</p>
        <p>
          {pet.gender} · {pet.size}
        </p>
      </div>
    </Link>
  );
}
