# Frontend Context

## Tech Stack

- Next.js 16.2.2 (App Router) with React 19
- TypeScript (strict)
- Tailwind CSS v4
- ESLint 9 (flat config) with `eslint-config-next` + `eslint-config-prettier`
- Prettier with `prettier-plugin-tailwindcss`
- Vitest + React Testing Library + MSW (Mock Service Worker)

## Project Structure

- Lives in `frontend/` folder in the monorepo (alongside `pet-service/`, `api-gateway/`, etc.)
- Uses `src/` directory with App Router (`src/app/`)
- Types in `src/types/` — match backend DTOs exactly
- API client in `src/lib/api.ts` — fetch wrapper pointing to gateway
- Tests in `src/tests/` — setup, MSW mock server, and test files alongside components
- MSW handlers in `src/tests/mocks/server.ts` — mock API responses for tests

## API Communication

- Frontend → API Gateway (`http://localhost`) → Pet Service
- Gateway handles CORS, rate limiting, JWT validation
- `NEXT_PUBLIC_API_URL` env var configures the gateway URL
- Auth token passed via `Authorization: Bearer` header

## Backend API Endpoints

Public (no auth): `GET /pets`, `GET /pets/featured`, `GET /pets/species`, `GET /pets/sizes`
Public via gateway (auth in pet-service but gateway allows): `GET /pets/{id}`
Authenticated: `GET /pets/mines`, `PUT /pets/adopt/{id}`
Admin roles: `POST /pets` (REGISTER_PET), `PUT /pets/{id}` (UPDATE_PET), `DELETE /pets/{id}` (DELETE_PET), `GET /pets/owner/{ownerId}` (ADMIN)

## Backend DTOs

- `Pet`: id, ownerId, name, description, specie (DOG|CAT), breed, size (SMALL|MEDIUM|LARGE), status (AVAILABLE|ADOPTED), gender (MALE|FEMALE), birthDate, createdAt, updatedAt
- `PetFilter`: specie?, gender?, petSize?, status?
- `Page<T>`: content, totalElements, totalPages, number, size, first, last, empty
- `PetRegistrationInputDTO`: name, description, specie, breed, size, gender, birthDate (all required except description)

## Testing Approach

- Component tests with Vitest + React Testing Library
- MSW mocks API responses (no real backend needed)
- Tests verify what the user sees, not implementation details
- Each page/component gets tests alongside its implementation

## Scripts

- `npm run dev` — dev server (port 3000)
- `npm run build` — production build
- `npm run lint` — ESLint
- `npm run format` — Prettier write
- `npm run format:check` — Prettier check
- `npm run test` — Vitest run
- `npm run test:watch` — Vitest watch mode

## Auth (planned)

- NextAuth.js v5 with Keycloak provider
- Keycloak realm: `animal-adoption`, client: `frontend` (Authorization Code + PKCE)
- Roles from JWT: REGISTER_PET, UPDATE_PET, DELETE_PET, ADMIN

## Notes

- Port 3000 is used by Next.js (Grafana moved to 3001)
