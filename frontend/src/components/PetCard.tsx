import Image from 'next/image';
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
      className="hover:border-brand block overflow-hidden rounded-lg border border-gray-200 bg-white shadow-sm transition hover:shadow-md"
    >
      {pet.imageUrl ? (
        <div className="relative h-48 w-full">
          <Image
            src={pet.imageUrl}
            alt={pet.name}
            fill
            className="object-cover"
            sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
          />
        </div>
      ) : (
        <div className="flex h-48 items-center justify-center bg-gray-100 text-5xl">
          {specieEmoji[pet.specie] || '🐾'}
        </div>
      )}

      <div className="p-5">
        <div className="mb-3 flex items-center justify-between">
          <h3 className="text-lg font-semibold text-gray-900">{pet.name}</h3>
          <span
            className={`rounded-full px-2.5 py-0.5 text-xs font-medium ${
              pet.status === 'AVAILABLE' ? 'bg-brand/20 text-gray-800' : 'bg-gray-100 text-gray-600'
            }`}
          >
            {pet.status}
          </span>
        </div>

        <div className="space-y-1 text-sm text-gray-500">
          <p>{pet.breed}</p>
          <p>
            {pet.gender} · {pet.size}
          </p>
        </div>
      </div>
    </Link>
  );
}
