# Pet Adoption System

A microservices-based system for managing pet adoptions, built with Spring Boot and Spring Cloud. The project uses
Keycloak for identity management, RabbitMQ for async messaging, Redis for caching, and is fully containerized with
Docker Compose.

## Architecture

![Application Architecture Diagram](./assets/diagram.jpeg)
![Dashboard](./assets/dashboard.png)

### Eureka Server (port 8761)

Service discovery server. All microservices register themselves and discover each other dynamically through Eureka,
enabling load-balanced communication without hardcoded URLs.

### API Gateway (port 80)

Single entry point for all client requests. Routes traffic to downstream services via Eureka and provides:

* JWT validation — rejects invalid/expired tokens before they reach downstream services
* Rate limiting — Redis-based token bucket algorithm (10 req/s sustained, 20 burst)
* Circuit breaker — Resilience4j with fallback response when downstream services are unavailable
* CORS — configured for cross-origin requests

### Keycloak (port 8080)

Centralized identity and access management:

* Realm: `animal-adoption` with OAuth 2.0 / OpenID Connect
* Backend client (`animal-adoption`) with roles: `REGISTER_PET`, `UPDATE_PET`, `DELETE_PET`, `ADMIN`
* Frontend client (`frontend`) with Authorization Code + PKCE flow for Insomnia/UI testing
* JWT tokens used by all services for authentication and authorization

### Pet Service (port 8081)

Core service for managing pets and adoptions:

* CRUD operations with role-based access control (`@PreAuthorize`)
* Adoption flow: updates pet status, assigns owner, publishes event to RabbitMQ
* Optimistic locking (`@Version`) to prevent race conditions on adoption
* Redis caching on featured pets with 1h TTL
* Pagination and filtering with JPA Specifications (by species, gender, size, status)
* Flyway for database migrations
* Response DTOs to avoid exposing JPA entities

### Notification Service (port 8082)

Event-driven service for adoption notifications:

* Consumes adoption events from RabbitMQ queue (`pet.adopted.notification`)
* Sends styled HTML emails using Thymeleaf templates
* Retry mechanism (3 attempts with exponential backoff) + Dead Letter Queue
* DLQ retry endpoint (`POST /dlq/retry`) to reprocess failed messages

### Shared Module

Common library used by pet-service and notification-service:

* `AdoptionMessage` and `UserDTO` — shared domain records (event contract between services)
* `JsonUtils` — JSON serialization/deserialization utility

### Monitoring

* **Prometheus** — scrapes metrics from all services via `/actuator/prometheus` every 15 seconds
* **Grafana** (port 3000) — dashboards for Spring Boot metrics, JVM performance, and service health
* **RedisInsight** (port 8001) — visual UI for monitoring Redis keys, memory, and performance

### Databases

Each service has its own dedicated database (database-per-service pattern):

* **Pet Service** — PostgreSQL (port 5433) with Flyway migrations
* **Keycloak** — PostgreSQL (port 5432) for users, roles, sessions, and tokens
* **Notification Service** — no database (purely event-driven)

## How to Run

### Prerequisites

* Docker

### Running the Project

```bash
# Copy the environment file
cp .env.example .env

# Start all infrastructure + notification-service (pet-service runs locally via IntelliJ)
docker compose up --build -d

# Or start everything including pet-service in Docker
docker compose --profile full up --build -d
```

Wait until all services are healthy, then the application is ready.

### Keycloak Setup (first time only)

The `animal-adoption` realm is automatically imported on first startup (via `keycloak/animal-adoption-realm.json`).
It includes the clients and roles pre-configured. You only need to:

1. Open `http://localhost:8080` and log in to the admin console (credentials from `.env`)
2. Create a test user in the `animal-adoption` realm and assign the desired roles

### Development Workflow

For day-to-day development, run pet-service locally via IntelliJ (with hot reload) and everything else in Docker:

1. `docker compose up -d` — starts infrastructure + notification-service
2. Run pet-service from IntelliJ (default profile uses `localhost` for all dependencies)
3. Access the API through the gateway at `http://localhost`

### Remote Debugging

Both services expose debug ports when running in Docker:

* Pet Service: `localhost:5005`
* Notification Service: `localhost:5006`

In IntelliJ: **Run** → **Edit Configurations** → **Remote JVM Debug** → set the host and port.

### Useful URLs

| Service       | URL                          |
|---------------|------------------------------|
| API Gateway   | http://localhost              |
| Pet Service   | http://localhost:8081         |
| Eureka        | http://localhost:8761         |
| Keycloak      | http://localhost:8080         |
| RabbitMQ      | http://localhost:15672        |
| MailHog       | http://localhost:8025         |
| Grafana       | http://localhost:3000         |
| Prometheus    | http://localhost:9090         |
| RedisInsight  | http://localhost:8001         |

💡 A preconfigured `insomnia.json` file is available in the root directory. Import it into Insomnia to test all
available routes.

## Technologies

### Core

* Java 21, Spring Boot 3.5, Spring Cloud 2025.0
* Spring Cloud Gateway — routing, rate limiting, circuit breaker
* Spring Cloud Eureka — service discovery
* Spring Security + OAuth 2.0 — authentication and authorization
* Keycloak — identity and access management
* PostgreSQL + Flyway — relational database with versioned migrations
* RabbitMQ — async messaging with DLQ support
* Redis — distributed caching

### Libraries

* Spring Data JPA — database access
* Resilience4j — circuit breaker and time limiter
* Thymeleaf — HTML email templates
* Lombok — boilerplate reduction
* Micrometer — metrics exporter for Prometheus
* Jackson — JSON serialization

### Infrastructure

* Docker Compose — container orchestration with health checks
* Prometheus + Grafana — monitoring and dashboards
* MailHog — email testing (dev)
* RedisInsight — Redis monitoring UI
