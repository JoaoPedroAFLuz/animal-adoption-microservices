'use server';

import { auth } from '@/auth';
import { api } from '@/lib/api';

import type { Pet } from '@/types';

import type { PetFormData } from '@/lib/schemas';

async function getToken() {
  const session = await auth();

  if (!session?.accessToken || session.error === 'RefreshTokenError') {
    throw new Error('Not authenticated');
  }

  return session.accessToken;
}

export async function createPet(data: PetFormData): Promise<Pet> {
  const token = await getToken();

  return api.post<Pet>('/pets', data, { token });
}

export async function updatePet(id: string, data: PetFormData): Promise<Pet> {
  const token = await getToken();

  return api.put<Pet>(`/pets/${id}`, data, { token });
}

export async function deletePet(id: string): Promise<void> {
  const token = await getToken();

  await api.delete(`/pets/${id}`, { token });
}

export async function adoptPet(id: string): Promise<Pet> {
  const token = await getToken();

  return api.put<Pet>(`/pets/adopt/${id}`, null, { token });
}

export async function uploadPetImage(petId: string, formData: FormData): Promise<Pet> {
  const token = await getToken();
  const apiUrl = process.env.API_URL || process.env.NEXT_PUBLIC_API_URL || 'http://localhost';

  const response = await fetch(`${apiUrl}/pets/${petId}/image`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token}` },
    body: formData,
  });

  if (!response.ok) {
    throw new Error(`API error: ${response.status}`);
  }

  return response.json();
}
