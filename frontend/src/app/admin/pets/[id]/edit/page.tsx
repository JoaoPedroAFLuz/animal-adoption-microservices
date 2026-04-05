import { notFound, redirect } from 'next/navigation';

import { auth } from '@/auth';
import { PetForm } from '@/components/PetForm';
import { api } from '@/lib/api';

import type { Pet } from '@/types';

interface EditPetPageProps {
  params: Promise<{ id: string }>;
}

export default async function EditPetPage({ params }: EditPetPageProps) {
  const { id } = await params;
  const session = await auth();

  if (!session?.roles?.includes('UPDATE_PET')) {
    redirect('/');
  }

  let pet: Pet;

  try {
    pet = await api.get<Pet>(`/pets/${id}`);
  } catch {
    notFound();
  }

  return (
    <div className="mx-auto max-w-2xl px-6 py-12">
      <h1 className="mb-8 text-3xl font-bold text-gray-900">Edit {pet.name}</h1>

      <div className="rounded-lg border border-gray-200 bg-white p-8 shadow-sm">
        <PetForm token={session.accessToken!} pet={pet} />
      </div>
    </div>
  );
}
