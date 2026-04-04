# Project Guidelines

## Workflow Rules

- Never commit automatically — always wait for explicit confirmation
- Always run tests before considering a task done
- Review changes before finalizing (check for missed edge cases, unused imports, consistency)
- When suggesting commits, follow the existing commit message pattern: lowercase, English, no conventional commit prefixes, descriptive of the action

## Code Style & Patterns

- Java 21, Spring Boot 3.x
- Use Lombok (`@RequiredArgsConstructor`, `@Getter`, `@Setter`, `@Builder`, etc.)
- Use Java records for DTOs
- Use `@ConfigurationProperties` for external config (not `@Value`)
- Use `var` for local variables when the type is obvious
- Prefer constructor injection (via Lombok `@RequiredArgsConstructor`)
- Follow existing package structure: `api.controller`, `api.exceptionhandler`, `domain.service`, `domain.model`, `domain.dto`, `domain.enums`, `domain.exception`, `domain.repository`, `config`, `util`, `factory`

## Database

- Flyway for all schema changes (never `ddl-auto: update/create`)
- Migration naming: `V{number}__{description}.sql`
- PostgreSQL

## Testing

- JUnit 5 + Mockito for unit tests
- Test factories in `factory` package for test data
- Use `@ExtendWith(MockitoExtension.class)` for unit tests

## Architecture

- Microservices communicate via Eureka service discovery
- Async messaging via RabbitMQ (TopicExchange)
- Auth via Keycloak (OAuth 2.0 / JWT)
- Redis for caching with `@Cacheable` / `@CacheEvict`
- API Gateway routes requests, does not handle auth
- Each service has its own database (database per service pattern)

## Notes

<!-- Add new preferences and learnings below as they come up -->

## Service-Specific Notes

### Pet Service (port 8081)
- Runs locally (outside Docker) because JWT validation requires Keycloak via localhost:8080
- Flyway migrations in `classpath:database/migration`
- Redis cache on `findFeatured()` with 1h TTL
- Keycloak client: `animal-adoption` (roles: REGISTER_PET, UPDATE_PET, DELETE_PET, ADMIN)
- Public endpoints: GET /pets, /pets/featured, /pets/species, /pets/sizes, /actuator/prometheus

### Notification Service
- No database — purely event-driven
- Consumes RabbitMQ queue `pet.adoption.notification`
- Sends emails via MailHog (dev) on port 1025, UI on 8025

### API Gateway (port 80)
- Routes `/pets/**` to pet-service via Eureka load balancer
- No authentication — pure proxy

### Eureka Server (port 8761)
- Service discovery only
- Does not register itself

### Keycloak (port 8080)
- Realm: `animal-adoption`
- Clients: `animal-adoption` (backend), `frontend` (Insomnia/UI with Authorization Code + PKCE)
- Dev mode (`start-dev`) — no HTTPS
