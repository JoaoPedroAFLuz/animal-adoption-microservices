'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { toast } from 'react-toastify';

import { Field, SelectField } from '@/components/FormFields';
import { api } from '@/lib/api';
import { petFormSchema } from '@/lib/schemas';

import type { Pet } from '@/types';

interface PetFormProps {
  token: string;
}

export function PetForm({ token }: PetFormProps) {
  const router = useRouter();
  const [errors, setErrors] = useState<Record<string, string>>({});

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
      const pet = await api.post<Pet>('/pets', result.data, { token });

      toast.success(`${pet.name} registered successfully!`);
      router.push(`/pets/${pet.id}`);
    } catch {
      toast.error('Failed to register pet. Please try again.');
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-5">
      <Field label="Name" name="name" error={errors.name} maxLength={50} />

      <Field
        label="Description"
        name="description"
        error={errors.description}
        maxLength={255}
        optional
      />

      <div className="grid grid-cols-2 gap-4">
        <SelectField label="Species" name="specie" error={errors.specie} options={['DOG', 'CAT']} />

        <Field label="Breed" name="breed" error={errors.breed} />
      </div>

      <div className="grid grid-cols-3 gap-4">
        <SelectField
          label="Size"
          name="size"
          error={errors.size}
          options={['SMALL', 'MEDIUM', 'LARGE']}
        />

        <SelectField
          label="Gender"
          name="gender"
          error={errors.gender}
          options={['MALE', 'FEMALE']}
        />

        <Field label="Birth Date" name="birthDate" type="date" error={errors.birthDate} />
      </div>

      <button
        type="submit"
        className="bg-brand hover:bg-brand-dark w-full rounded-lg px-6 py-3 font-medium text-gray-900"
      >
        Register Pet
      </button>
    </form>
  );
}
