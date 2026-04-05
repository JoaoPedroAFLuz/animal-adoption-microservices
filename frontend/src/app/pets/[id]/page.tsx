import Link from 'next/link';
import { notFound } from 'next/navigation';

import { api } from '@/lib/api';
import { formatDate } from '@/utils/format';

import type { Pet } from '@/types';

const specieEmoji: Record<string, string> = {
  DOG: '🐕',
  CAT: '🐈',
};

interface PetDetailsPageProps {
  params: Promise<{ id: string }>;
}

export default async function PetDetailsPage({ params }: PetDetailsPageProps) {
  const { id } = await params;

  let pet: Pet;

  try {
    pet = await api.get<Pet>(`/pets/${id}`);
  } catch {
    notFound();
  }

  return (
    <div className="mx-auto max-w-3xl px-6 py-12">
      <Link href="/pets" className="text-sm text-gray-500 hover:text-gray-700">
        ← Back to all pets
      </Link>

      <div className="mt-6 rounded-lg border border-gray-200 bg-white p-8 shadow-sm">
        <div className="mb-4 flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-900">
            {specieEmoji[pet.specie] || '🐾'} {pet.name}
          </h1>

          <span
            className={`rounded-full px-3 py-1 text-sm font-medium ${
              pet.status === 'AVAILABLE' ? 'bg-brand/20 text-gray-800' : 'bg-gray-100 text-gray-600'
            }`}
          >
            {pet.status}
          </span>
        </div>

        {pet.description && <p className="mb-6 text-gray-600">{pet.description}</p>}

        <div className="grid grid-cols-2 gap-4 text-sm">
          <Detail label="Species" value={pet.specie} />
          <Detail label="Breed" value={pet.breed} />
          <Detail label="Size" value={pet.size} />
          <Detail label="Gender" value={pet.gender} />
          <Detail label="Birth Date" value={formatDate(pet.birthDate)} />
        </div>

        {pet.status === 'AVAILABLE' && (
          <div className="mt-8">
            <button
              disabled
              className="w-full rounded-lg bg-brand px-6 py-3 font-medium text-gray-900 opacity-50"
            >
              Adopt {pet.name}
            </button>

            <p className="mt-2 text-center text-sm text-gray-500">
              Sign in to adopt this pet
            </p>
          </div>
        )}
      </div>
    </div>
  );
}

function Detail({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <span className="font-medium text-gray-500">{label}</span>
      <p className="text-gray-900">{value}</p>
    </div>
  );
}
