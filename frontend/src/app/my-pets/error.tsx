'use client';

import { ErrorMessage } from '@/components/ErrorMessage';

export default function MyPetsError({ error, reset }: { error: Error; reset: () => void }) {
  return <ErrorMessage error={error} reset={reset} />;
}
