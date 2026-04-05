'use client';

import { useRouter, useSearchParams } from 'next/navigation';

import type { Gender, Size, Specie, Status } from '@/types';

const species: Specie[] = ['DOG', 'CAT'];
const genders: Gender[] = ['MALE', 'FEMALE'];
const sizes: Size[] = ['SMALL', 'MEDIUM', 'LARGE'];
const statuses: Status[] = ['AVAILABLE', 'ADOPTED'];

export function PetFilters() {
  const router = useRouter();
  const searchParams = useSearchParams();

  function handleChange(key: string, value: string) {
    const params = new URLSearchParams(searchParams.toString());

    if (value) {
      params.set(key, value);
    } else {
      params.delete(key);
    }

    params.delete('page');
    router.push(`/pets?${params.toString()}`);
  }

  return (
    <div className="flex flex-wrap gap-3">
      <FilterSelect
        label="Species"
        value={searchParams.get('specie') || ''}
        options={species}
        onChange={(v) => handleChange('specie', v)}
      />
      <FilterSelect
        label="Gender"
        value={searchParams.get('gender') || ''}
        options={genders}
        onChange={(v) => handleChange('gender', v)}
      />
      <FilterSelect
        label="Size"
        value={searchParams.get('petSize') || ''}
        options={sizes}
        onChange={(v) => handleChange('petSize', v)}
      />
      <FilterSelect
        label="Status"
        value={searchParams.get('status') || ''}
        options={statuses}
        onChange={(v) => handleChange('status', v)}
      />
    </div>
  );
}

interface FilterSelectProps {
  label: string;
  value: string;
  options: string[];
  onChange: (value: string) => void;
}

function FilterSelect({ label, value, options, onChange }: FilterSelectProps) {
  return (
    <select
      aria-label={label}
      value={value}
      onChange={(e) => onChange(e.target.value)}
      className="rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm text-gray-700"
    >
      <option value="">All {label}</option>
      {options.map((option) => (
        <option key={option} value={option}>
          {option}
        </option>
      ))}
    </select>
  );
}
