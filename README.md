# Pet Adoption System

This project is a microservices-based architecture for managing the pet adoption system. It consists of independent services, each responsible for a specific part of the process. The system was developed using Spring Boot and utilizes Eureka and API Gateway.

## Project Structure

### 1. Eureka
Eureka is used for service discovery, allowing microservices to locate and communicate with each other dynamically. The service registry is hosted at `http://localhost:8761/eureka/`.

### 2. API Gateway
The API Gateway manages incoming requests and handles JWT authentication. It validates user credentials, routes requests to the appropriate microservices (such as Pet Service and User Service), and enforces authorization on configured routes.

- **JWT Validation**: The API Gateway validates JWT tokens to ensure requests are authenticated.
- **Routing**: It directs requests to the correct microservices based on the URL path.

### 3. Pet Service
This service is responsible for managing pets and their adoptions. The main functionalities include:

- CRUD operations for pets (create, read, update, delete).
- Pet adoption: A pet can be adopted by a user, and the system updates the pet's owner, status, and adoption date.

### 4. User Service
The User Service handles user registration and login. It validates the user's identity and issues a JWT token, which is used for authentication in other services.

### 5. Database
Each microservice has its own database to ensure separation of concerns:

- **Pet Service**: Database with information about pets.
- **User Service**: Database with information about users.

## Next Steps

### 1. Pet Adoption Notification Service
The first feature to be implemented will be a notification service. Whenever a pet is adopted, the system should notify the user or any other interested systems. The idea is to create a service that sends notifications (via email, SMS, etc.) when an adoption occurs.

### 2. Message Queue Integration (RabbitMQ)
To improve communication between microservices, we will integrate RabbitMQ. This will allow one service to send messages to other interested services whenever an event occurs (like a pet adoption). The message queue will allow for better decoupling between services and improve scalability and reliability.

### 3. Monitoring with Prometheus + Grafana
We will integrate Prometheus to collect metrics about the performance of microservices and Grafana to create monitoring dashboards that help identify bottlenecks, failures, and understand the overall system behavior. With this setup, you will get real-time insights into the status of your microservices.

## How to Run the Project

### Prerequisites
- Docker

### Step-by-Step
1. Run `docker compose up --build -d`
2. Await for containers to run
3. Services are ready to go

## Technologies Used
- **Spring Boot**: Framework for developing microservices.
- **Spring Cloud Gateway**: Gateway for request routing and JWT validation.
- **Spring Cloud Eureka**: Service discovery for communication between microservices.
- **PostgreSQL**: Relational database for storing pet and user information.