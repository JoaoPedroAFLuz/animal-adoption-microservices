import { PetCard } from '@/components/PetCard';
import { api } from '@/lib/api';
import type { Pet } from '@/types';
import Link from 'next/link';

export default async function Home() {
  const pets = await api.get<Pet[]>('/pets/featured');

  return (
    <div className="mx-auto max-w-7xl px-6 py-12">
      <section className="mb-12 text-center">
        <h1 className="text-4xl font-bold text-gray-900">Find Your New Best Friend</h1>
        <p className="mt-3 text-lg text-gray-600">
          Browse our featured pets available for adoption
        </p>
      </section>

      {pets.length > 0 ? (
        <section>
          <h2 className="mb-6 text-2xl font-semibold text-gray-900">Featured Pets</h2>
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {pets.map((pet) => (
              <PetCard key={pet.id} pet={pet} />
            ))}
          </div>
        </section>
      ) : (
        <p className="text-center text-gray-500">No pets available at the moment.</p>
      )}

      <div className="mt-10 text-center">
        <Link
          href="/pets"
          className="inline-block rounded-lg bg-brand px-6 py-3 text-sm font-medium text-gray-900 hover:bg-brand-dark"
        >
          Browse All Pets
        </Link>
      </div>
    </div>
  );
}
