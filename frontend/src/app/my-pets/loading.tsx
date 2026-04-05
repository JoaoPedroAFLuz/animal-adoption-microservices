import { PetCardGridSkeleton } from '@/components/PetCardSkeleton';

export default function MyPetsLoading() {
  return (
    <div className="mx-auto max-w-7xl px-6 py-12">
      <div className="mb-8 h-9 w-32 animate-pulse rounded bg-gray-200" />
      <PetCardGridSkeleton />
    </div>
  );
}
