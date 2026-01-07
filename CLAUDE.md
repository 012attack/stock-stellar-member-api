# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

```bash
# Build the project
./gradlew build

# Run the application (starts with Docker Compose for PostgreSQL)
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "yi.memberapi.MemberApiApplicationTests"

# Run a specific test method
./gradlew test --tests "yi.memberapi.MemberApiApplicationTests.contextLoads"

# Clean build
./gradlew clean build
```

## Tech Stack

- **Spring Boot 4.0.0** with Java 21 and Kotlin 2.2
- **Database**: PostgreSQL (via Docker Compose on port 5433) with JPA/Hibernate and MyBatis
- **Security**: Spring Security with Thymeleaf integration
- **Messaging**: Kafka
- **Observability**: OpenTelemetry

## Architecture

The project follows a layered/hexagonal architecture pattern:

```
yi.memberapi/
├── adapter/           # External interfaces
│   ├── webapi/        # REST controllers (e.g., LoginApi)
│   └── security/      # Security adapters
├── application/       # Application services
│   ├── provided/      # Services this app provides
│   └── required/      # Ports for external dependencies
├── domain/            # Domain entities and business logic
│   ├── member/        # Member aggregate (Member entity)
│   └── shared/        # Shared domain objects
└── common/            # Cross-cutting concerns
    ├── annotation/
    ├── config/
    ├── constant/
    └── util/
```

## Configuration

- Application runs on port **8081** with context path `/member-api`
- PostgreSQL connection: `localhost:5433`, database `member`, user `myuser`
- Docker Compose auto-starts PostgreSQL when running with `bootRun`
