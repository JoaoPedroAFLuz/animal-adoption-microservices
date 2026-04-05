import Link from 'next/link';

export default function NotFound() {
  return (
    <div className="mx-auto flex max-w-7xl flex-col items-center px-6 py-24 text-center">
      <span className="text-6xl">🐾</span>

      <h1 className="mt-6 text-3xl font-bold text-gray-900">Page Not Found</h1>

      <p className="mt-3 text-lg text-gray-600">
        The page you are looking for does not exist or has been moved.
      </p>

      <Link
        href="/"
        className="bg-brand hover:bg-brand-dark mt-8 inline-block rounded-lg px-6 py-3 text-sm font-medium text-gray-900"
      >
        Back to Home
      </Link>
    </div>
  );
}
