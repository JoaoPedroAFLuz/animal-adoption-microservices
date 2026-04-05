import { auth } from '@/auth';
import { PetCard } from '@/components/PetCard';
import { api } from '@/lib/api';

import type { Page, Pet } from '@/types';

export default async function MyPetsPage() {
  const session = await auth();
  const page = await api.get<Page<Pet>>('/pets/mines', { token: session?.accessToken });

  return (
    <div className="mx-auto max-w-7xl px-6 py-12">
      <h1 className="mb-8 text-3xl font-bold text-gray-900">My Pets</h1>

      {page.content.length > 0 ? (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {page.content.map((pet) => (
            <PetCard key={pet.id} pet={pet} />
          ))}
        </div>
      ) : (
        <p className="text-center text-gray-500">
          You haven&apos;t adopted any pets yet. Browse our available pets!
        </p>
      )}
    </div>
  );
}
