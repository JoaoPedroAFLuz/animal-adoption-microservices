'use client';

import { useRef, useState } from 'react';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { signIn } from 'next-auth/react';
import { toast } from 'react-toastify';

import { Button } from '@/components/Button';
import { Field } from '@/components/FormFields';
import { updatePassword, updateProfile, updateProfilePhoto } from '@/lib/actions';
import { imageSchema, passwordSchema, profileSchema } from '@/lib/schemas';

import type { Profile } from '@/types';

interface ProfileFormProps {
  profile: Profile;
}

export function ProfileForm({ profile }: ProfileFormProps) {
  const router = useRouter();

  return (
    <div className="mx-auto max-w-2xl space-y-10 px-6 py-12">
      <h1 className="text-2xl font-bold text-gray-900">Edit Profile</h1>

      <PhotoSection profile={profile} />

      <NameSection profile={profile} onSuccess={() => router.refresh()} />

      <PasswordSection />
    </div>
  );
}

function PhotoSection({ profile }: { profile: Profile }) {
  const [preview, setPreview] = useState<string | null>(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  async function handlePhotoChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0];
    if (!file) return;

    const result = imageSchema.safeParse(file);
    if (!result.success) {
      setError(result.error.issues[0].message);
      setPreview(null);
      return;
    }

    setError('');
    setPreview(URL.createObjectURL(file));
  }

  async function handleUpload() {
    const file = fileInputRef.current?.files?.[0];
    if (!file) return;

    setLoading(true);

    try {
      const formData = new FormData();
      formData.append('file', file);

      await updateProfilePhoto(formData);

      toast.success('Photo updated!');
      setPreview(null);

      await signIn('keycloak');
    } catch {
      toast.error('Failed to update photo');
    } finally {
      setLoading(false);
    }
  }

  const displayImage = preview || profile.picture;

  return (
    <section className="rounded-lg border border-gray-200 p-6">
      <h2 className="mb-4 text-lg font-semibold text-gray-900">Photo</h2>

      <div className="flex items-center gap-6">
        {displayImage ? (
          <Image
            src={displayImage}
            alt="Profile"
            width={80}
            height={80}
            className="rounded-full object-cover"
          />
        ) : (
          <div className="flex h-20 w-20 items-center justify-center rounded-full bg-gray-900 text-2xl font-medium text-white">
            {profile.firstName?.charAt(0) || '?'}
          </div>
        )}

        <div className="flex-1">
          <input
            ref={fileInputRef}
            type="file"
            accept="image/jpeg,image/png,image/webp"
            onChange={handlePhotoChange}
            className="hidden"
          />

          <button
            type="button"
            onClick={() => fileInputRef.current?.click()}
            className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Choose Photo
          </button>

          <p className="mt-1 text-xs text-gray-400">JPEG, PNG or WebP. Max 5MB.</p>

          {error && <p className="mt-1 text-xs text-red-500">{error}</p>}

          {preview && (
            <Button onClick={handleUpload} disabled={loading} className="mt-3">
              {loading ? 'Uploading...' : 'Save Photo'}
            </Button>
          )}
        </div>
      </div>
    </section>
  );
}

function NameSection({ profile, onSuccess }: { profile: Profile; onSuccess: () => void }) {
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
    e.preventDefault();

    const formData = new FormData(e.currentTarget);
    const data = Object.fromEntries(formData);

    const result = profileSchema.safeParse(data);
    if (!result.success) {
      setErrors(
        Object.fromEntries(result.error.issues.map((issue) => [issue.path[0], issue.message])),
      );
      return;
    }

    setErrors({});
    setLoading(true);

    try {
      await updateProfile(result.data);

      toast.success('Profile updated!');

      onSuccess();
    } catch {
      toast.error('Failed to update profile');
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="rounded-lg border border-gray-200 p-6">
      <h2 className="mb-4 text-lg font-semibold text-gray-900">Name</h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        <Field
          label="First Name"
          name="firstName"
          defaultValue={profile.firstName}
          error={errors.firstName}
        />

        <Field
          label="Last Name"
          name="lastName"
          defaultValue={profile.lastName}
          error={errors.lastName}
        />

        <Button type="submit" disabled={loading}>
          {loading ? 'Saving...' : 'Save Name'}
        </Button>
      </form>
    </section>
  );
}

function PasswordSection() {
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);
  const formRef = useRef<HTMLFormElement>(null);

  async function handleSubmit(e: React.SyntheticEvent<HTMLFormElement>) {
    e.preventDefault();

    const formData = new FormData(e.currentTarget);
    const data = Object.fromEntries(formData);

    const result = passwordSchema.safeParse(data);
    if (!result.success) {
      setErrors(
        Object.fromEntries(result.error.issues.map((issue) => [issue.path[0], issue.message])),
      );
      return;
    }

    setErrors({});
    setLoading(true);

    try {
      await updatePassword({
        currentPassword: result.data.currentPassword,
        newPassword: result.data.newPassword,
      });

      toast.success('Password updated!');

      formRef.current?.reset();
    } catch {
      toast.error('Failed to update password. Check your current password.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="rounded-lg border border-gray-200 p-6">
      <h2 className="mb-4 text-lg font-semibold text-gray-900">Password</h2>

      <form ref={formRef} onSubmit={handleSubmit} className="space-y-4">
        <Field
          label="Current Password"
          name="currentPassword"
          type="password"
          error={errors.currentPassword}
        />

        <Field label="New Password" name="newPassword" type="password" error={errors.newPassword} />

        <Field
          label="Confirm New Password"
          name="confirmPassword"
          type="password"
          error={errors.confirmPassword}
        />

        <Button type="submit" disabled={loading}>
          {loading ? 'Saving...' : 'Change Password'}
        </Button>
      </form>
    </section>
  );
}
