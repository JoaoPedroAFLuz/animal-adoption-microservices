import { z } from 'zod';

export const petFormSchema = z.object({
  name: z.string().min(1, 'Name is required').max(50, 'Name must be at most 50 characters'),
  description: z.string().max(255, 'Description must be at most 255 characters').optional(),
  specie: z.enum(['DOG', 'CAT'], { message: 'Species is required' }),
  breed: z.string().min(1, 'Breed is required'),
  size: z.enum(['SMALL', 'MEDIUM', 'LARGE'], { message: 'Size is required' }),
  gender: z.enum(['MALE', 'FEMALE'], { message: 'Gender is required' }),
  birthDate: z
    .string()
    .min(1, 'Birth date is required')
    .refine(
      (date) => !date || new Date(date + 'T00:00:00') <= new Date(),
      'Birth date must be in the past or present',
    ),
});

export type PetFormData = z.infer<typeof petFormSchema>;
