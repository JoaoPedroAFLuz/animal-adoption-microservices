# Improvements

## Eureka Server

- [ ] Change Dockerfile base image from JDK to JRE (`eclipse-temurin:21-jre`)
- [ ] Add health check in `docker-compose.yml`

## API Gateway

- [ ] Add rate limiting
- [ ] Add circuit breaker (Resilience4j) with fallback
- [ ] Configure CORS
- [ ] Change Dockerfile base image from JDK to JRE
- [ ] Remove leftover `compose.yaml` (Spring Initializr artifact)
- [ ] Evaluate JWT validation at the gateway level to reject invalid requests before reaching downstream services

## Pet Service

- [x] **Bug**: Replace `@NotBlank` with `@NotNull` on enum fields (`Specie`, `Size`, `Gender`) in `PetRegistrationInputDTO` and `PetUpdateInputDTO`
- [ ] Create `PetResponseDTO` to avoid exposing the JPA entity directly in the API
- [x] Fix race condition on adoption (optimistic locking with `@Version` or atomic UPDATE)
- [x] Remove redundant `id` field from `PetUpdateInputDTO`
- [ ] Check pet existence before deleting (return 404)
- [ ] Add `status` filter to `PetSpecification`
- [ ] Fix `shouldCacheFeaturedPets` test — needs `@SpringBootTest` for caching to work
- [x] Fix `shouldUpdatePetWhenExists` test — `Specie.valueOf()` receiving enum instead of String
- [ ] Add pagination to `findByOwnerId`

## Notification Service

- [ ] Add error handling in the listener (retry + Dead Letter Queue)
- [ ] Add logging (success and failure)
- [ ] Evaluate HTML email templates
- [ ] Add unit tests for `NotificationService` and `EmailService`

## Infrastructure (Docker Compose + Prometheus)

- [ ] Add `healthcheck` to compose services and use `depends_on` with `condition: service_healthy`
- [ ] Fix commented-out pet-service in compose (configure Keycloak hostname accessible both inside and outside Docker)
- [ ] Standardize Dockerfiles — all using JRE
- [ ] Add `.dockerignore` to pet-service and notification-service
- [ ] Extract duplicated code (`JsonUtils`, `RabbitProperties`, `AdoptionMessage`, `UserDTO`) into a shared module
