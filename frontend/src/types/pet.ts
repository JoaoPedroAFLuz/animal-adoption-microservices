export type Specie = 'DOG' | 'CAT';

export type Gender = 'MALE' | 'FEMALE';

export type Size = 'SMALL' | 'MEDIUM' | 'LARGE';

export type Status = 'AVAILABLE' | 'ADOPTED';

export interface Pet {
  id: string;
  ownerId: string | null;
  name: string;
  description: string;
  specie: Specie;
  breed: string;
  size: Size;
  status: Status;
  gender: Gender;
  birthDate: string;
  imageUrl: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface PetFilter {
  specie?: Specie;
  gender?: Gender;
  petSize?: Size;
  status?: Status;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}
