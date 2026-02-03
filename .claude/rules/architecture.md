This project follows a layered/hexagonal architecture pattern:

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