interface FieldProps {
  label: string;
  name: string;
  error?: string;
  type?: string;
  maxLength?: number;
  optional?: boolean;
  defaultValue?: string;
}

export function Field({
  label,
  name,
  error,
  type = 'text',
  maxLength,
  optional,
  defaultValue,
}: FieldProps) {
  return (
    <div>
      <label htmlFor={name} className="mb-1 block text-sm font-medium text-gray-700">
        {label} {optional && <span className="text-gray-400">(optional)</span>}
      </label>

      <input
        id={name}
        name={name}
        type={type}
        maxLength={maxLength}
        defaultValue={defaultValue}
        className={`w-full rounded-lg border px-3 py-2 text-sm ${
          error ? 'border-red-500' : 'border-gray-300'
        }`}
      />

      {error && <p className="mt-1 text-xs text-red-500">{error}</p>}
    </div>
  );
}

interface SelectFieldProps {
  label: string;
  name: string;
  error?: string;
  options: string[];
  defaultValue?: string;
}

export function SelectField({ label, name, error, options, defaultValue }: SelectFieldProps) {
  return (
    <div>
      <label htmlFor={name} className="mb-1 block text-sm font-medium text-gray-700">
        {label}
      </label>

      <select
        id={name}
        name={name}
        defaultValue={defaultValue}
        className={`w-full rounded-lg border px-3 py-2 text-sm ${
          error ? 'border-red-500' : 'border-gray-300'
        }`}
      >
        <option value="">Select</option>
        {options.map((opt) => (
          <option key={opt} value={opt}>
            {opt}
          </option>
        ))}
      </select>

      {error && <p className="mt-1 text-xs text-red-500">{error}</p>}
    </div>
  );
}
