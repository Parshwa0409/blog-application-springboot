# System Patterns: Standardized Blog API

This document outlines the design patterns and architectural components of the blog application's backend API.

## 1. High-Level Architecture

The backend follows a standard RESTful pattern with stateless JWT-based authentication. All endpoints are grouped under the `/api/v1/` prefix.

```mermaid
graph TD
    subgraph "Client"
        C[Frontend App]
    end

    subgraph "Spring Boot Application"
        subgraph "Publicly Accessible Endpoints"
            Auth[POST /api/v1/auth/...]
            Feed[GET /api/v1/feed]
            Tags[GET /api/v1/tags]
            ReadPosts[GET /api/v1/posts/{id}/...]
        end

        subgraph "Security Layer (Spring Security)"
            Filter[JwtAuthFilter]
            Config[WebSecurityConfig]
        end

        subgraph "Authenticated Endpoints"
            WritePosts[POST, PUT, DELETE /api/v1/posts/...]
            Comments[POST, PUT, DELETE /api/v1/posts/{id}/comments/...]
            Likes[POST, DELETE /api/v1/posts/{id}/likes]
            Favorites[POST, DELETE /api/v1/posts/{id}/favorites]
            Me[GET /api/v1/me/...]
            Admin[GET /api/v1/users]
        end
    end

    C -- HTTP Request --> Config
    Config -- Routes to --> Publicly Accessible Endpoints
    Config -- JWT Check --> Filter
    Filter -- Authenticates --> Authenticated Endpoints
```

## 2. Key Architectural Patterns

- **Standardized URL Structure:** All API endpoints are consistently structured under `/api/v1/`, improving predictability and developer experience.
- **Stateless JWT Authentication:** The application uses JSON Web Tokens for stateless authentication, ensuring scalability and simplifying session management.
- **Granular Security:** `WebSecurityConfig` defines fine-grained access rules:
    - **Public (No Auth):** `auth`, `feed`, `tags`, and read-only `posts` endpoints are public.
    - **Authenticated (User Role):** Write operations (`POST`, `PUT`, `DELETE`) on `posts`, `comments`, `likes`, and `favorites` require a valid JWT.
    - **Admin (Admin Role):** The `/api/v1/users` endpoint is restricted to administrators.
- **Cross-Origin Resource Sharing (CORS):** The application is configured to accept requests from `http://localhost:3000` and `http://localhost:5173`, enabling frontend integration.
- **Dependency Injection:** Spring's DI framework is used to wire all components together, promoting loose coupling and testability.

## 3. API Contract

The API contract is defined by the combination of controller request mappings and security rules.

| Endpoint                                    | HTTP Method | Access        | Description                                     |
| ------------------------------------------- | ----------- | ------------- | ----------------------------------------------- |
| `/api/v1/auth/login`                        | `POST`      | Public        | Authenticates a user and returns a JWT.         |
| `/api/v1/auth/signup`                       | `POST`      | Public        | Registers a new user.                           |
| `/api/v1/feed`                              | `GET`       | Public        | Retrieves the main content feed.                |
| `/api/v1/tags`                              | `GET`       | Public        | Retrieves all tags.                             |
| `/api/v1/posts/{id}`                        | `GET`       | Public        | Retrieves a single post.                        |
| `/api/v1/posts/{id}/comments`               | `GET`       | Public        | Retrieves comments for a post.                  |
| `/api/v1/posts`                             | `POST`      | Authenticated | Creates a new post.                             |
| `/api/v1/posts/{id}`                        | `PUT`       | Authenticated | Updates an existing post.                       |
| `/api/v1/posts/{id}`                        | `DELETE`    | Authenticated | Deletes a post.                                 |
| `/api/v1/posts/{postId}/comments`           | `POST`      | Authenticated | Adds a comment to a post.                       |
| `/api/v1/posts/{postId}/likes`              | `POST`      | Authenticated | Likes a post.                                   |
| `/api/v1/posts/{postId}/favorites`          | `POST`      | Authenticated | Favorites a post.                               |
| `/api/v1/me`                                | `GET`       | Authenticated | Retrieves the current user's profile.           |
| `/api/v1/users`                             | `GET`       | Admin         | Retrieves a list of all users.                  |

For a detailed overview of all API endpoints, refer to the [API Contract](api-contract.md).