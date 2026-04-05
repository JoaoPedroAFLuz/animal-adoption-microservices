'use client';

import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { toast } from 'react-toastify';

import { Field, SelectField } from '@/components/FormFields';
import { api } from '@/lib/api';
import { petFormSchema } from '@/lib/schemas';

import type { Pet } from '@/types';

interface PetFormProps {
  token: string;
  pet?: Pet;
}

export function PetForm({ token, pet }: PetFormProps) {
  const router = useRouter();
  const [errors, setErrors] = useState<Record<string, string>>({});
  const isEdit = !!pet;

  async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
    e.preventDefault();

    const formData = new FormData(e.currentTarget);
    const data = Object.fromEntries(formData);

    const result = petFormSchema.safeParse(data);

    if (!result.success) {
      const fieldErrors: Record<string, string> = {};

      result.error.issues.forEach((issue) => {
        const key = String(issue.path[0]);
        fieldErrors[key] = issue.message;
      });

      setErrors(fieldErrors);
      return;
    }

    setErrors({});

    try {
      if (isEdit) {
        const updated = await api.put<Pet>(`/pets/${pet.id}`, result.data, { token });

        toast.success(`${updated.name} updated successfully!`);
      } else {
        const created = await api.post<Pet>('/pets', result.data, { token });

        toast.success(`${created.name} registered successfully!`);

        router.push(`/pets/${created.id}`);

        return;
      }

      router.push(`/pets/${pet.id}`);
    } catch {
      toast.error(`Failed to ${isEdit ? 'update' : 'register'} pet. Please try again.`);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-5">
      <Field label="Name" name="name" error={errors.name} maxLength={50} defaultValue={pet?.name} />

      <Field
        label="Description"
        name="description"
        error={errors.description}
        maxLength={255}
        optional
        defaultValue={pet?.description}
      />

      <div className="grid grid-cols-2 gap-4">
        <SelectField
          label="Species"
          name="specie"
          error={errors.specie}
          options={['DOG', 'CAT']}
          defaultValue={pet?.specie}
        />

        <Field label="Breed" name="breed" error={errors.breed} defaultValue={pet?.breed} />
      </div>

      <div className="grid grid-cols-3 gap-4">
        <SelectField
          label="Size"
          name="size"
          error={errors.size}
          options={['SMALL', 'MEDIUM', 'LARGE']}
          defaultValue={pet?.size}
        />

        <SelectField
          label="Gender"
          name="gender"
          error={errors.gender}
          options={['MALE', 'FEMALE']}
          defaultValue={pet?.gender}
        />

        <Field
          label="Birth Date"
          name="birthDate"
          type="date"
          error={errors.birthDate}
          defaultValue={pet?.birthDate}
        />
      </div>

      <button
        type="submit"
        className="bg-brand hover:bg-brand-dark w-full rounded-lg px-6 py-3 font-medium text-gray-900"
      >
        {isEdit ? 'Update Pet' : 'Register Pet'}
      </button>
    </form>
  );
}
