import { redirect } from 'next/navigation';

import { auth } from '@/auth';
import { PetForm } from '@/components/PetForm';

export default async function NewPetPage() {
  const session = await auth();

  if (!session?.roles?.includes('REGISTER_PET')) {
    redirect('/');
  }

  return (
    <div className="mx-auto max-w-2xl px-6 py-12">
      <h1 className="mb-8 text-3xl font-bold text-gray-900">Register New Pet</h1>

      <div className="rounded-lg border border-gray-200 bg-white p-8 shadow-sm">
        <PetForm />
      </div>
    </div>
  );
}
