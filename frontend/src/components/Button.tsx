interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

export function Button({ children, className = '', ...props }: ButtonProps) {
  return (
    <button
      className={`bg-brand hover:bg-brand-dark w-full rounded-lg px-6 py-3 font-medium text-gray-900 disabled:opacity-50 ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}
