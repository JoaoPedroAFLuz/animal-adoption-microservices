'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

interface NavLink {
  href: string;
  label: string;
}

interface NavLinksProps {
  links: NavLink[];
}

export function NavLinks({ links }: NavLinksProps) {
  const pathname = usePathname();

  return (
    <>
      {links.map(({ href, label }) => {
        const isActive = href === '/' ? pathname === '/' : pathname.startsWith(href);

        return (
          <Link
            key={href}
            href={href}
            className={`text-sm font-medium ${
              isActive
                ? 'border-b-2 border-gray-900 text-gray-950'
                : 'text-gray-800 hover:text-gray-950'
            }`}
          >
            {label}
          </Link>
        );
      })}
    </>
  );
}
