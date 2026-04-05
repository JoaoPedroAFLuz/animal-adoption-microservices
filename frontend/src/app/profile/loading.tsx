import { PetCardGridSkeleton } from '@/components/PetCardSkeleton';

export default function ProfileLoading() {
  return (
    <div className="mx-auto max-w-7xl px-6 py-12">
      <div className="mb-10 flex items-center gap-6">
        <div className="h-20 w-20 animate-pulse rounded-full bg-gray-200" />

        <div>
          <div className="h-7 w-48 animate-pulse rounded bg-gray-200" />
          <div className="mt-2 h-5 w-64 animate-pulse rounded bg-gray-200" />
        </div>
      </div>

      <div className="mb-6 h-6 w-32 animate-pulse rounded bg-gray-200" />
      <PetCardGridSkeleton />
    </div>
  );
}
