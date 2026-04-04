# Improvements

## Infrastructure

- [ ] Test full end-to-end flow in Docker (create pet → adopt → HTML email → DLQ retry)
- [ ] Clean up `.env.example` (remove stale variables, unused configs)
- [ ] Add `.dockerignore` to project root (build context is `.` for pet-service and notification-service)

## Testing

- [ ] Add integration tests with Testcontainers (PostgreSQL, RabbitMQ, Redis)

## Frontend

- [ ] Add a simple frontend for the pet adoption system
