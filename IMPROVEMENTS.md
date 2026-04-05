# Improvements

## UI/UX

- [x] Active tab highlighting — underline or color change on the current nav link
- [x] Loading skeletons — placeholder shapes while pages load
- [x] Search by name — text input to search pets by name
- [ ] Error pages — friendly error boundaries with retry button when API is unavailable
- [ ] Featured pets — boolean flag on pets, admin toggle, home page shows only featured pets

## Auth & Users

- [x] Google login — add Google as social identity provider in Keycloak
- [x] User profile page — view/edit name, email, adoption history
- [x] User avatar — display user photo from Keycloak/Google in the header

## Backend

- [x] Pet image upload — S3/MinIO storage, upload endpoint, display on cards and details
- [ ] Edit profile — custom forms to change name, password, and photo via Keycloak Admin API
- [ ] OpenAPI/Swagger documentation — auto-generated API docs
- [ ] Audit log — track who created/updated/deleted pets and when

## DevOps

- [ ] GitHub Actions CI — run tests, lint, build on every push
- [ ] Production Docker config — nginx reverse proxy with SSL
