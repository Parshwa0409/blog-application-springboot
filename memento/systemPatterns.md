# System Patterns: JWT Authentication Architecture

This document outlines the design patterns and architectural components of the JWT-based authentication system in this Spring Boot application.

## 1. High-Level Architecture

The authentication system follows a token-based, stateless pattern. The key components and their interactions are as follows:

```mermaid
graph TD
    subgraph "User"
        C[Client]
    end

    subgraph "Spring Boot Application"
        subgraph "Public Endpoints"
            Login[POST /api/auth/login]
            Register[POST /api/auth/signup]
        end

        subgraph "Security Layer"
            Filter[JwtAuthFilter] --> AuthManager[AuthenticationManager]
            AuthManager --> UserDetailsSvc[UserDetailsService]
        end

        subgraph "Protected Resources"
            API[GET /api/user/me]
        end
    end

    C -- 1. Credentials --> Login
    Login -- 2. Authenticate --> AuthManager
    UserDetailsSvc -- 3. Fetches User --> DB[(Database)]
    AuthManager -- 4. Returns Principal --> Login
    Login -- 5. Generates JWT --> C
    C -- 6. Request with JWT --> API
    API -- 7. Validate Token --> Filter
    Filter -- 8. Sets SecurityContext --> API
    API -- 9. Returns Resource --> C
```

## 2. Key Architectural Patterns

- **Filter Chain:** Spring Security uses a chain of filters to process incoming HTTP requests. Our custom `JwtAuthFilter` is inserted into this chain to inspect for a JWT in the `Authorization` header.
- **Service-Oriented Design:**
    - `UserDetailsService`: A core Spring Security interface that acts as a bridge to the application's user store (e.g., a database). It's responsible for fetching user details by username.
    - **Authentication Service:** A dedicated service (`AuthService`) often encapsulates the logic for user registration and login, separating concerns from the controllers.
- **Dependency Injection:** Spring's DI framework is used to wire all components together (e.g., injecting `UserDetailsService` and `JwtUtil` into the security configuration and filters).
- **Stateless Authentication:** The server does not maintain session state. The JWT contains all the necessary information to identify the user, making each request self-contained.
## 3. Detailed Sequence Diagram

This diagram shows the detailed step-by-step flow for both authentication (login) and authorization (accessing a protected resource).

### Login Flow

```mermaid
sequenceDiagram
    participant Client
    participant AuthController
    participant AuthService
    participant AuthenticationManager
    participant UserDetailsServiceImpl
    participant AuthUtil

    Client->>+AuthController: POST /auth/login (username, password)
    AuthController->>+AuthService: login(loginRequestDto)
    AuthService->>+AuthenticationManager: authenticate(username, password)
    AuthenticationManager->>+UserDetailsServiceImpl: loadUserByUsername(username)
    UserDetailsServiceImpl-->>-AuthenticationManager: UserDetails (with hashed password)
    AuthenticationManager->>AuthenticationManager: Compare passwords
    AuthenticationManager-->>-AuthService: Authentication object (Principal)
    AuthService->>+AuthUtil: generateAccessToken(user)
    AuthUtil-->>-AuthService: JWT String
    AuthService-->>-AuthController: LoginResponseDto (with token)
    AuthController-->>-Client: 200 OK (JWT)
```

### Protected Resource Access Flow

```mermaid
sequenceDiagram
    participant Client
    participant JwtAuthFilter
    participant AuthUtil
    participant UserRepository
    participant SecurityContextHolder
    participant YourController

    Client->>+JwtAuthFilter: GET /api/some/resource (Header: "Bearer <JWT>")
    JwtAuthFilter->>+AuthUtil: getUsernameFromToken(jwt)
    AuthUtil->>AuthUtil: Verify signature & expiration
    AuthUtil-->>-JwtAuthFilter: username
    JwtAuthFilter->>+UserRepository: findByUsername(username)
    UserRepository-->>-JwtAuthFilter: User object
    JwtAuthFilter->>+SecurityContextHolder: setAuthentication(new authToken)
    SecurityContextHolder-->>-JwtAuthFilter:
    JwtAuthFilter->>+YourController: continue request
    YourController-->>-Client: 200 OK (Protected Data)
```
### API Contract
For a detailed overview of all API endpoints, refer to the [API Contract](api-contract.md).