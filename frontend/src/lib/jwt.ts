export function decodeJwtPayload(token: string): Record<string, unknown> {
  const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
  const padded = base64 + '='.repeat((4 - (base64.length % 4)) % 4);
  const binary = Uint8Array.from(
    typeof globalThis.atob === 'function'
      ? globalThis
          .atob(padded)
          .split('')
          .map((c) => c.charCodeAt(0))
      : [...Buffer.from(padded, 'base64')],
  );

  return JSON.parse(new TextDecoder().decode(binary));
}
