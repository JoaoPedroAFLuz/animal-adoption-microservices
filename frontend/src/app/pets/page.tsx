import { Suspense } from 'react';

import { Pagination } from '@/components/Pagination';
import { PetCard } from '@/components/PetCard';
import { PetFilters } from '@/components/PetFilters';
import { api } from '@/lib/api';

import type { Page, Pet } from '@/types';

interface PetsPageProps {
  searchParams: Promise<Record<string, string | undefined>>;
}

export default async function PetsPage({ searchParams }: PetsPageProps) {
  const params = await searchParams;

  const query = new URLSearchParams();

  if (params.name) query.set('name', params.name);
  if (params.specie) query.set('specie', params.specie);
  if (params.gender) query.set('gender', params.gender);
  if (params.petSize) query.set('petSize', params.petSize);
  if (params.status) query.set('status', params.status);

  query.set('page', params.page || '0');
  query.set('size', '12');

  const page = await api.get<Page<Pet>>(`/pets?${query.toString()}`);

  return (
    <div className="mx-auto max-w-7xl px-6 py-12">
      <h1 className="mb-8 text-3xl font-bold text-gray-900">Browse Pets</h1>

      <Suspense>
        <PetFilters />
      </Suspense>

      {page.content.length > 0 ? (
        <div className="mt-8 grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {page.content.map((pet) => (
            <PetCard key={pet.id} pet={pet} />
          ))}
        </div>
      ) : (
        <p className="mt-8 text-center text-gray-500">No pets found matching your filters.</p>
      )}

      <div className="mt-10">
        <Suspense>
          <Pagination totalPages={page.totalPages} currentPage={page.number} />
        </Suspense>
      </div>
    </div>
  );
}
