# Improvements

## Observability & Reliability

- [x] Distributed tracing — Micrometer Tracing + Zipkin to trace requests across gateway, pet-service, and notification-service
- [ ] Custom health checks — health indicators for Keycloak, MinIO, and RabbitMQ connectivity

## Database & Performance

- [x] Database indexing — add indexes to frequently queried columns (owner_id, status, specie) and benchmark with EXPLAIN ANALYZE
- [ ] Soft delete — mark pets as deleted instead of hard-deleting, preserve data history

## API Design & Resilience

- [ ] Idempotent endpoints — make adopt endpoint safe to call multiple times without side effects
- [ ] API rate limiting per user — rate limit by JWT subject instead of global IP-based limiting
- [ ] API versioning — `/v1/pets` vs `/v2/pets` for backward compatibility
- [ ] Graceful error handling — standardize ProblemDetail responses across all services

## Testing

- [ ] User-service tests — integration and unit tests for profile management endpoints
- [ ] Pagination improvements — add pagination to `/pets/mines` and `/pets/featured`
