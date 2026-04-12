import { ProfileForm } from '@/components/ProfileForm';
import { api } from '@/lib/api';
import { requireAuth } from '@/lib/auth-utils';

import type { Profile } from '@/types';

export default async function EditProfilePage() {
  const session = await requireAuth();
  const profile = await api.get<Profile>('/profile', { token: session.accessToken });

  return <ProfileForm profile={profile} />;
}
