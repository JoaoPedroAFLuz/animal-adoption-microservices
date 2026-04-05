'use client';

interface ErrorMessageProps {
  error: Error;
  reset: () => void;
}

export function ErrorMessage({ error, reset }: ErrorMessageProps) {
  return (
    <div className="mx-auto flex max-w-7xl flex-col items-center px-6 py-24 text-center">
      <h1 className="text-2xl font-bold text-gray-900">Something went wrong</h1>
      <p className="mt-2 text-gray-500">We couldn&apos;t load this page. Please try again.</p>

      {process.env.NODE_ENV === 'development' && (
        <pre className="mt-4 max-w-full overflow-auto rounded-lg bg-red-50 p-4 text-left text-sm text-red-700">
          {error.message}
        </pre>
      )}

      <button
        onClick={reset}
        className="bg-brand hover:bg-brand-dark mt-6 rounded-lg px-6 py-3 font-medium text-gray-900"
      >
        Try Again
      </button>
    </div>
  );
}
