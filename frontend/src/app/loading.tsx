import { PetCardGridSkeleton } from '@/components/PetCardSkeleton';

export default function HomeLoading() {
  return (
    <div className="mx-auto max-w-7xl px-6 py-12">
      <section className="mb-12 text-center">
        <div className="mx-auto h-10 w-80 animate-pulse rounded bg-gray-200" />
        <div className="mx-auto mt-3 h-6 w-64 animate-pulse rounded bg-gray-200" />
      </section>

      <section>
        <div className="mb-6 h-8 w-40 animate-pulse rounded bg-gray-200" />
        <PetCardGridSkeleton />
      </section>
    </div>
  );
}
