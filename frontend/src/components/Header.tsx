import Link from 'next/link';

export function Header() {
  return (
    <header className="bg-brand shadow-sm">
      <nav className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
        <Link href="/" className="text-xl font-bold text-gray-900">
          🐾 Pet Adoption
        </Link>

        <div className="flex items-center gap-6">
          <Link href="/pets" className="text-sm font-medium text-gray-800 hover:text-gray-950">
            Browse Pets
          </Link>
          <button className="rounded-lg bg-gray-900 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800">
            Sign In
          </button>
        </div>
      </nav>
    </header>
  );
}
