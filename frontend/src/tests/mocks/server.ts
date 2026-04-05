import { http, HttpResponse } from 'msw';
import { setupServer } from 'msw/node';

import type { Pet } from '@/types';

export const mockPet: Pet = {
  id: '123e4567-e89b-12d3-a456-426614174000',
  ownerId: null,
  name: 'Luna',
  description: 'Friendly dog',
  specie: 'DOG',
  breed: 'Golden Retriever',
  size: 'LARGE',
  status: 'AVAILABLE',
  gender: 'FEMALE',
  birthDate: '2023-06-15',
  imageUrl: null,
  createdAt: '2024-01-01T00:00:00',
  updatedAt: '2024-01-01T00:00:00',
};

export const handlers = [
  http.get('http://localhost/pets/featured', () => {
    return HttpResponse.json([mockPet]);
  }),

  http.get('http://localhost/pets', ({ request }) => {
    const url = new URL(request.url);
    const page = Number(url.searchParams.get('page') || 0);
    const size = Number(url.searchParams.get('size') || 12);

    return HttpResponse.json({
      content: [mockPet],
      totalElements: 1,
      totalPages: 1,
      number: page,
      size,
      first: true,
      last: true,
      empty: false,
    });
  }),

  http.get('http://localhost/pets/:id', ({ params }) => {
    return HttpResponse.json({ ...mockPet, id: params.id });
  }),
];

export const server = setupServer(...handlers);
