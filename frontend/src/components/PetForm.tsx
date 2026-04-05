'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { toast } from 'react-toastify';

import { Field, SelectField } from '@/components/FormFields';
import { createPet, updatePet, uploadPetImage } from '@/lib/actions';
import { petFormSchema } from '@/lib/schemas';

import type { Pet } from '@/types';

interface PetFormProps {
  pet?: Pet;
}

export function PetForm({ pet }: PetFormProps) {
  const router = useRouter();
  const [errors, setErrors] = useState<Record<string, string>>({});
  const isEdit = !!pet;

  async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
    e.preventDefault();

    const formData = new FormData(e.currentTarget);
    const imageFile = formData.get('image') as File;
    const data = Object.fromEntries(formData);
    delete data.image;

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
      let savedPet: Pet;

      if (isEdit) {
        savedPet = await updatePet(pet.id, result.data);

        toast.success(`${result.data.name} updated successfully!`);
      } else {
        savedPet = await createPet(result.data);

        toast.success(`${savedPet.name} registered successfully!`);
      }

      if (imageFile && imageFile.size > 0) {
        const imageFormData = new FormData();
        imageFormData.append('file', imageFile);

        await uploadPetImage(savedPet.id, imageFormData);
      }

      router.push(`/pets/${savedPet.id}`);
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

      <div>
        <label htmlFor="image" className="mb-1 block text-sm font-medium text-gray-700">
          Image <span className="text-gray-400">(optional)</span>
        </label>
        <input
          id="image"
          name="image"
          type="file"
          accept="image/jpeg,image/png,image/webp"
          className="file:bg-brand w-full rounded-lg border border-gray-300 px-4 py-2 text-sm file:mr-4 file:rounded-lg file:border-0 file:px-4 file:py-2 file:text-sm file:font-medium"
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
