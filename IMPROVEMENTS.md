# Improvements

## Eureka Server

- [x] Change Dockerfile base image from JDK to JRE (`eclipse-temurin:21-jre`)
- [x] Add health check in `docker-compose.yml`

## API Gateway

- [x] Add rate limiting
- [x] Add circuit breaker (Resilience4j) with fallback
- [x] Configure CORS
- [x] Change Dockerfile base image from JDK to JRE
- [x] Remove leftover `compose.yaml` (Spring Initializr artifact)
- [x] Evaluate JWT validation at the gateway level to reject invalid requests before reaching downstream services

## Pet Service

- [x] **Bug**: Replace `@NotBlank` with `@NotNull` on enum fields (`Specie`, `Size`, `Gender`) in `PetRegistrationInputDTO` and `PetUpdateInputDTO`
- [x] Create `PetResponseDTO` to avoid exposing the JPA entity directly in the API
- [x] Fix race condition on adoption (optimistic locking with `@Version` or atomic UPDATE)
- [x] Remove redundant `id` field from `PetUpdateInputDTO`
- [x] Check pet existence before deleting (return 404)
- [x] Add `status` filter to `PetSpecification`
- [x] Fix `shouldCacheFeaturedPets` test — needs `@SpringBootTest` for caching to work
- [x] Fix `shouldUpdatePetWhenExists` test — `Specie.valueOf()` receiving enum instead of String
- [x] Add pagination to `findByOwnerId`

## Notification Service

- [x] Add error handling in the listener (retry + Dead Letter Queue)
- [x] Add logging (success and failure)
- [x] Evaluate HTML email templates
- [x] Add DLQ retry endpoint (`POST /dlq/retry`) to reprocess failed messages
- [x] Add unit tests for `NotificationService` and `EmailService`

## Infrastructure (Docker Compose + Prometheus)

- [x] Add `healthcheck` to compose services and use `depends_on` with `condition: service_healthy`
- [x] Fix commented-out pet-service in compose (configure Keycloak hostname accessible both inside and outside Docker)
- [x] Standardize Dockerfiles — all using JRE
- [x] Add `.dockerignore` to pet-service and notification-service
- [x] Extract duplicated code (`JsonUtils`, `RabbitProperties`, `AdoptionMessage`, `UserDTO`) into a shared module
- [ ] Review and improve `README.md`
