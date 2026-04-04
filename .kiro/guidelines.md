# Project Guidelines

## Workflow Rules

- Never commit automatically — always wait for explicit confirmation
- Always run tests before considering a task done
- Review changes before finalizing (check for missed edge cases, unused imports, consistency)
- When suggesting commits, follow the existing commit message pattern: lowercase, English, no conventional commit prefixes, descriptive of the action
- When the user grants trust to a tool, add it to `.kiro/agents/pet-adoption.json` allowedTools
- Never add destructive commands (`rm`, `rmdir`) to allowedCommands — always ask for confirmation

## Autonomy Rules

- **Technical decisions** (variable names, code structure, refactoring): proceed without asking
- **Business decisions** (logic, flow, rules, user-facing behavior): ask before implementing
- **Git** (commit, push): only when explicitly requested by the user
- **New patterns learned**: when a new convention or preference is defined during development, save it immediately in this file

## Code Style & Patterns

- Java 21, Spring Boot 3.x
- Use Lombok (`@RequiredArgsConstructor`, `@Getter`, `@Setter`, `@Builder`, etc.)
- Use Java records for DTOs
- Use `@ConfigurationProperties` for external config (not `@Value`)
- Use `var` for local variables when the type is obvious — never change already-instantiated variables just to follow this pattern
- Prefer constructor injection (via Lombok `@RequiredArgsConstructor`)
- Always import classes explicitly — never use inline references (e.g. `implements java.io.Serializable`)
- Follow existing package structure: `api.controller`, `api.exceptionhandler`, `domain.service`, `domain.model`, `domain.dto`, `domain.enums`, `domain.exception`, `domain.repository`, `config`, `util`, `factory`

### Line Spacing

- Blank line between blocks with **different purposes** (log, service call, return, variable declaration)
- Keep together lines of the **same logical action** (consecutive setters, consecutive validations, builder chain)

```java
public Pet adopt(UUID petId, UserDTO user) {
    var pet = findByIdOrThrow(petId);

    if (pet.getOwnerId() != null || Status.ADOPTED.equals(pet.getStatus())) {
        throw new PetAlreadyAdoptedException();
    }

    pet.setOwnerId(user.id());
    pet.setStatus(Status.ADOPTED);

    pet = save(pet);

    notificationService.sendAdoptionNotification(new AdoptionMessage(pet.getId(), pet.getName(), user));

    return pet;
}
```

### Method Chaining Alignment

- Align dots (`.`) at the same column as the first dot in the chain:

```java
return Pet.builder()
          .name(dto.name())
          .specie(dto.specie())
          .breed(dto.breed())
          .status(Status.AVAILABLE)
          .build();
```

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
- API Gateway validates JWT (authentication) + routes requests; pet-service handles authorization (`@PreAuthorize`)
- Each service has its own database (database per service pattern)

## Notes

<!-- Add new preferences and learnings below as they come up -->

## Service-Specific Notes

### Pet Service (port 8081)
- Runs locally via IntelliJ for hot reload; stop Docker container first (`docker stop pet-service`)
- For full integration testing, run everything in Docker (`docker compose --profile full up --build -d`)
- Docker profile available with `SPRING_PROFILES_ACTIVE=docker` + custom JwtDecoder for internal Keycloak access
- Flyway migrations in `classpath:database/migration`
- Redis cache on `findFeatured()` with 1h TTL
- Keycloak client: `animal-adoption` (roles: REGISTER_PET, UPDATE_PET, DELETE_PET, ADMIN)
- Public endpoints: GET /pets, /pets/featured, /pets/species, /pets/sizes, /actuator/prometheus

### Notification Service (port 8082)
- No database — purely event-driven
- Consumes RabbitMQ queue `pet.adoption.notification`
- DLQ: `pet.adoption.notification.dlq` with retry endpoint `POST /dlq/retry`
- Sends HTML emails via Thymeleaf + MailHog (dev) on port 1025, UI on 8025

### API Gateway (port 80)
- Routes `/pets/**` to pet-service via Eureka load balancer
- JWT validation (authentication), rate limiting (Redis), circuit breaker (Resilience4j)
- CORS enabled for all origins (dev)

### Eureka Server (port 8761)
- Service discovery only
- Does not register itself

### Keycloak (port 8080)
- Realm: `animal-adoption`
- Clients: `animal-adoption` (backend), `frontend` (Insomnia/UI with Authorization Code + PKCE)
- Dev mode (`start-dev`) — no HTTPS
