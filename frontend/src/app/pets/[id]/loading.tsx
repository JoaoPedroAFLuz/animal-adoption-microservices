export default function PetDetailsLoading() {
  return (
    <div className="mx-auto max-w-3xl px-6 py-12">
      <div className="h-4 w-32 animate-pulse rounded bg-gray-200" />

      <div className="mt-6 animate-pulse rounded-lg border border-gray-200 bg-white p-8 shadow-sm">
        <div className="mb-4 flex items-center justify-between">
          <div className="h-9 w-48 rounded bg-gray-200" />
          <div className="h-7 w-24 rounded-full bg-gray-200" />
        </div>

        <div className="mb-6 h-4 w-full rounded bg-gray-200" />

        <div className="grid grid-cols-2 gap-4">
          {Array.from({ length: 5 }, (_, i) => (
            <div key={i}>
              <div className="h-4 w-16 rounded bg-gray-200" />
              <div className="mt-1 h-5 w-24 rounded bg-gray-200" />
            </div>
          ))}
        </div>

        <div className="mt-8 h-12 w-full rounded-lg bg-gray-200" />
      </div>
    </div>
  );
}
