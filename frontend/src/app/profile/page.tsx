import Image from 'next/image';

import { auth } from '@/auth';
import { PetCard } from '@/components/PetCard';
import { api } from '@/lib/api';

import type { Page, Pet } from '@/types';

export default async function ProfilePage() {
  const session = await auth();
  const page = await api.get<Page<Pet>>('/pets/mines', { token: session?.accessToken });

  return (
    <div className="mx-auto max-w-7xl px-6 py-12">
      <div className="mb-10 flex items-center gap-6">
        {session?.picture ? (
          <Image
            src={session.picture}
            alt={session.displayName || ''}
            width={80}
            height={80}
            className="rounded-full"
          />
        ) : (
          <div className="flex h-20 w-20 items-center justify-center rounded-full bg-gray-900 text-2xl font-medium text-white">
            {session?.displayName?.charAt(0) || '?'}
          </div>
        )}

        <div>
          <h1 className="text-2xl font-bold text-gray-900">{session?.user?.name}</h1>
          <p className="text-gray-500">{session?.user?.email}</p>
        </div>
      </div>

      <section>
        <h2 className="mb-6 text-xl font-semibold text-gray-900">Adopted Pets</h2>

        {page.content.length > 0 ? (
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {page.content.map((pet) => (
              <PetCard key={pet.id} pet={pet} />
            ))}
          </div>
        ) : (
          <p className="text-gray-500">You haven&apos;t adopted any pets yet.</p>
        )}
      </section>
    </div>
  );
}
