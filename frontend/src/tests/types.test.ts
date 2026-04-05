import { describe, it, expect } from 'vitest';

import type { Pet } from '@/types';

describe('types', () => {
  it('should create a valid Pet object', () => {
    const pet: Pet = {
      id: '123',
      ownerId: null,
      name: 'Luna',
      description: 'Friendly dog',
      specie: 'DOG',
      breed: 'Golden Retriever',
      size: 'LARGE',
      status: 'AVAILABLE',
      gender: 'FEMALE',
      birthDate: '2023-06-15',
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00',
    };

    expect(pet.name).toBe('Luna');
    expect(pet.status).toBe('AVAILABLE');
  });
});
