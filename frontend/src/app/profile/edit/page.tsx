import { redirect } from 'next/navigation';

import { auth } from '@/auth';
import { ProfileForm } from '@/components/ProfileForm';
import { api } from '@/lib/api';

import type { Profile } from '@/types';

export default async function EditProfilePage() {
  const session = await auth();

  if (!session?.accessToken) {
    redirect('/api/auth/signin');
  }

  const profile = await api.get<Profile>('/profile', { token: session.accessToken });

  return <ProfileForm profile={profile} />;
}
