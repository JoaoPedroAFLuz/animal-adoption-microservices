import { PetCardGridSkeleton } from '@/components/PetCardSkeleton';

export default function PetsLoading() {
  return (
    <div className="mx-auto max-w-7xl px-6 py-12">
      <div className="mb-8 h-9 w-48 animate-pulse rounded bg-gray-200" />

      <div className="flex gap-4">
        {Array.from({ length: 4 }, (_, i) => (
          <div key={i} className="h-10 w-40 animate-pulse rounded-lg bg-gray-200" />
        ))}
      </div>

      <div className="mt-8">
        <PetCardGridSkeleton count={12} />
      </div>
    </div>
  );
}
