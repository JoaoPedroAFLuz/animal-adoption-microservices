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

const MAX_IMAGE_SIZE = 5 * 1024 * 1024;
const ACCEPTED_IMAGE_TYPES = ['image/jpeg', 'image/png', 'image/webp'];

export const imageSchema = z
  .instanceof(File)
  .refine((file) => file.size <= MAX_IMAGE_SIZE, 'Image must be less than 5MB')
  .refine(
    (file) => ACCEPTED_IMAGE_TYPES.includes(file.type),
    'Only JPEG, PNG, and WebP images are allowed',
  );

export const profileSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
});

export type ProfileFormData = z.infer<typeof profileSchema>;

export const passwordSchema = z
  .object({
    currentPassword: z.string().min(1, 'Current password is required'),
    newPassword: z.string().min(8, 'Password must be at least 8 characters'),
    confirmPassword: z.string().min(1, 'Please confirm your password'),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: 'Passwords do not match',
    path: ['confirmPassword'],
  });

export type PasswordFormData = z.infer<typeof passwordSchema>;
