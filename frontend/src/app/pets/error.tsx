'use client';

import { ErrorMessage } from '@/components/ErrorMessage';

export default function PetsError({ error, reset }: { error: Error; reset: () => void }) {
  return <ErrorMessage error={error} reset={reset} />;
}
