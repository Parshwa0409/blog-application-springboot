# The Ultimate Guide to JWT Authentication with Spring Boot

## A Production-Ready Template for JWT, RBAC, and Refresh Tokens

### 1. Introduction

Welcome! This document provides a complete walkthrough of a production-ready authentication and authorization system in Spring Boot. By the end, you will understand not just *how* the code works, but *why* each piece is necessary.

#### What is Authentication?

**Authentication** (AuthN) is the process of verifying who a user is. It’s like showing your ID to a security guard. The system asks, "Are you really who you say you are?" In web applications, this is typically done with a username and password.

#### What is Authorization?

**Authorization** (AuthZ) is the process of verifying what an authenticated user is allowed to do. Once the security guard has verified your ID, authorization determines which doors you are allowed to open. Can you access the VIP lounge, or only the general admission areas? In our system, this is managed by roles (e.g., `ROLE_USER`, `ROLE_ADMIN`).

**Authentication comes first, then authorization.**

#### Session-Based vs. Token-Based Authentication

*   **Session-Based (Stateful):** In traditional applications, after you log in, the server creates a "session" and stores it in its memory or a database. It then gives you a small cookie (a Session ID) to store in your browser. On every request, you send the Session ID, and the server looks up your session to see who you are. The server has to *remember* you.

*   **Token-Based (Stateless):** In modern applications, after you log in, the server gives you a "token" (a JWT). This token contains all the information the server needs to know who you are and what you can do. You send this token with every request. The server doesn't need to remember you between requests; it just needs to validate the token. This is **stateless**, as the state (your login status) is stored on the client side, within the token itself.

#### Why JWT?

JSON Web Tokens (JWT) are the industry standard for token-based authentication in modern systems (especially for APIs, microservices, and single-page applications) because they are:
*   **Stateless and Scalable:** The server doesn't need to store session data, making it easier to scale the application across multiple servers.
*   **Self-Contained:** The token carries all the user's information (like user ID and roles), reducing the need for database lookups on every request.
*   **Secure:** A JWT is digitally signed by the server. If the client or an attacker tries to tamper with the data inside the token, the signature becomes invalid, and the server will reject it.

---

### 2. High-Level Authentication Flow

Before we touch any code, let's understand the end-to-end flow of our system.

1.  **User Registration:** A new user signs up with a username, email, and password. The system hashes the password and saves the new user to the database with a default role of `ROLE_USER`.
2.  **User Login:** The user sends their username and password to the `/auth/login` endpoint.
3.  **Credential Validation:** The server validates these credentials. It finds the user by username, hashes the provided password, and compares it to the stored hash.
4.  **Token Generation:** If the credentials are valid, the server generates two tokens:
    *   An **Access Token:** A short-lived JWT (e.g., 1-15 minutes) that contains the user's ID, username, and roles (`claims`). This token is used to access protected resources.
    *   A **Refresh Token:** A long-lived, random string (e.g., 7 days) that is stored in the database and linked to the user. Its only purpose is to get a new access token.
5.  **Token Storage:** The server sends both tokens to the client (e.g., a browser or mobile app). The client must store them securely. The access token is often stored in memory, while the refresh token is stored more persistently (e.g., in an HttpOnly cookie or secure storage).
6.  **Accessing a Protected Resource:** The client makes a request to a protected endpoint (e.g., `/api/me`) and includes the **Access Token** in the `Authorization` header.
    ```
    Authorization: Bearer <your_access_token>
    ```
7.  **Token Validation:** The server's JWT filter intercepts the request. It extracts the token from the header and performs three critical checks:
    *   Is the token's signature valid? (Verifies it was issued by this server and not tampered with).
    *   Has the token expired?
    *   Is the token well-formed?
8.  **Authorization:** If the token is valid, the filter extracts the user's roles from the token's payload. Spring Security then checks if the user's roles are sufficient to access the requested endpoint.
9.  **Access Granted/Denied:** If the user is authorized, the request proceeds to the controller, and a response is returned. If not, the server returns a `403 Forbidden` error. If the token was invalid or expired, it returns a `401 Unauthorized` error.
10. **Token Refresh:** When the access token expires, the client sends the **Refresh Token** to the `/auth/refresh` endpoint. The server validates the refresh token against the database and, if valid, issues a brand new access token.

---

### 3. Project Structure Overview

A well-organized project is easier to understand and maintain. Our security-related code is separated by concern.

*   `patil.parshwa.blog`
    *   `controllers/`: Defines the API endpoints.
        *   `AuthController.java`: Public endpoints for login, signup, and refresh.
        *   `UserController.java`: Protected endpoints for user data.
    *   `dto/`: Data Transfer Objects. Plain Java classes used to shape the JSON data sent to and from the API.
    *   `models/`: JPA Entities that map to database tables.
        *   `User.java`: Represents a user.
        *   `Role.java`: Represents a user role.
        *   `RefreshToken.java`: Represents a refresh token.
    *   `repositories/`: Spring Data JPA interfaces for database operations.
    *   `services/`: Contains the business logic.
        *   `AuthService.java`: Logic for registration and login.
        *   `RefreshTokenService.java`: Logic for managing refresh tokens.
    *   `security/`: **The heart of our security implementation.**
        *   `WebSecurityConfig.java`: Central configuration for Spring Security.
        *   `JwtAuthFilter.java`: The custom filter that validates JWTs on every request.
        *   `AuthUtil.java`: A helper class for creating and parsing JWTs.
        *   `UserDetailsServiceImpl.java`: Tells Spring Security how to load a user from our database.

---

### 4. Spring Security Fundamentals

*   **What is Spring Security?** It's a framework that provides authentication, authorization, and other security features for Spring applications. It's powerful but can feel complex because it's so customizable.
*   **The Filter Chain:** The most important concept to understand. Spring Security works by passing every incoming HTTP request through a chain of filters. One filter might check for a CSRF token, another might handle basic authentication, and we insert our own custom `JwtAuthFilter` into this chain to handle JWTs.
*   **`SecurityFilterChain`:** In modern Spring Security, you define a `@Bean` of this type inside your `WebSecurityConfig`. This bean defines the entire security policy for your application: which routes are public, which are private, which filters to use, etc.
*   **Stateless vs. Stateful:** We configure our filter chain to be **stateless**. This means we explicitly tell Spring Security *not* to create or use HTTP sessions, as we are managing user state entirely within our JWTs.

---

### 5. User & Role Model (RBAC)

**Role-Based Access Control (RBAC)** is a security design that restricts access to authorized users based on their role. Instead of saying "Alice can read posts," we say "Users with the `USER` role can read posts," and then we give Alice the `USER` role.

*   **`User.java` and `Role.java`:** We have two entities. A `User` can have many `Roles`, and a `Role` can be assigned to many `Users`. This is a many-to-many relationship, managed by a join table (`user_roles`) in the database.
*   **`UserDetails` Interface:** Our `User` entity implements `UserDetails`. This is a contract required by Spring Security. It forces our `User` class to have methods like `getUsername()`, `getPassword()`, `isAccountNonExpired()`, and `getAuthorities()`. This is how Spring Security can work with any user model.
*   **`GrantedAuthority` Interface:** The `getAuthorities()` method must return a collection of `GrantedAuthority` objects. This is Spring Security's representation of a permission. In our case, each `Role` (like "ROLE_USER") is converted into a `SimpleGrantedAuthority`.
*   **Role Naming Convention:** By convention, roles in Spring Security are prefixed with `ROLE_`. This allows us to use expressions like `hasRole('ADMIN')` instead of the more verbose `hasAuthority('ROLE_ADMIN')`.

---

### 6. Password Handling

**Passwords must never, ever be stored in plain text.** If your database is compromised, an attacker would have access to every user's password.

*   **Hashing:** We use a one-way cryptographic function called **hashing**. Hashing transforms a password into a fixed-length, scrambled string. It's "one-way" because you cannot reverse the hash to get the original password.
*   **`BCryptPasswordEncoder`:** We use `BCrypt`, the industry-standard password hashing algorithm. `BCrypt` is intentionally slow to compute, which makes it resistant to brute-force attacks. It also includes a "salt" (a random value) with each hash, so even if two users have the same password, their stored hashes will be completely different.
*   **How Verification Works:** During login, we don't "decrypt" the stored password. Instead, we take the plain-text password provided by the user, hash it using the same salt stored with the original hash, and then compare the two resulting hashes. If they match, the password is correct. The `passwordEncoder.matches(rawPassword, encodedPassword)` method handles all of this for us.

---

### 7. JWT Fundamentals

*   **JWT Structure:** A JWT is just a long string, divided into three parts by dots (`.`):
    1.  **Header:** A JSON object containing metadata, like the algorithm used to sign the token (e.g., `HS256`). This part is Base64Url encoded.
    2.  **Payload:** A JSON object containing the "claims" — the data about the user. This is where we put the username, user ID, and roles. It also has standard claims like `exp` (expiration time) and `sub` (subject). This part is also Base64Url encoded.
    3.  **Signature:** The server takes the encoded header, the encoded payload, and a secret key (known only to the server), and signs them with the algorithm specified in the header. The signature ensures the token hasn't been tampered with.

*   **Statelessness:** The server doesn't need to look up the user in the database on every request (though we do for freshness in our filter). All the necessary information (`username`, `roles`) is right there in the payload.

---

### 8. JWT Utility / Service (`AuthUtil.java`)

This class is a helper responsible for all JWT-related operations.

*   `generateAccessToken(User user)`:
    *   **Purpose:** To create a new JWT for a given user.
    *   **Implementation:** It uses the `jjwt` library's builder to construct the token.
        1.  Sets the subject (`sub`) to the user's username.
        2.  Adds custom claims, like `userId` and `roles`.
        3.  Sets an expiration date (`exp`). This is **critical**. Our access tokens are short-lived (1 minute).
        4.  Signs the token using the `jwtSecretKey` and the HMAC-SHA algorithm. This secret key must be long, random, and stored securely (ideally as an environment variable, not in `application.properties`).
*   `getUsernameFromToken(String jwt)` and `getClaims(String jwt)`:
    *   **Purpose:** To validate and parse an incoming JWT.
    *   **Implementation:** It uses the `jjwt` library's parser.
        1.  It sets the secret key to use for verification (`verifyWith(getSecretKey())`). This is the most important step. The library re-calculates the signature and compares it to the signature on the token. If they don't match, it throws a `SignatureException`.
        2.  It also automatically checks the `exp` claim and throws an `ExpiredJwtException` if the token is expired.
        3.  If successful, it returns the `Claims` object (the payload), from which we can extract the username and roles.

---

### 9. Authentication Filter (`JwtAuthFilter.java`)

This is the "guard" that stands in front of our API. It extends `OncePerRequestFilter` to ensure it runs exactly once for every request.

*   **Why is it needed?** Spring Security doesn't know how to handle JWTs out of the box. We need to create a custom filter to teach it.
*   **How it works (`doFilterInternal`):**
    1.  It looks for an `Authorization` header in the request.
    2.  It checks if the header value starts with `"Bearer "`. If not, it assumes this isn't an authenticated request and simply passes the request down the filter chain.
    3.  If a Bearer token is found, it extracts the JWT string.
    4.  It uses our `AuthUtil` to validate the token and extract the username.
    5.  If the token is valid and there is no user currently authenticated in the `SecurityContext`, it loads the `UserDetails` from our `UserDetailsServiceImpl`.
    6.  It creates a new `UsernamePasswordAuthenticationToken`, which is Spring Security's standard representation of an authenticated user. It populates this token with the user details and, most importantly, their authorities (roles).
    7.  It sets this token in the `SecurityContextHolder`. **This is the key step.** This is how we manually tell Spring Security, "For the rest of this single request, this user is authenticated and has these specific roles."
    8.  If any exception occurs during token validation (e.g., token expired, signature invalid), the `catch` block delegates the exception to the `HandlerExceptionResolver`, which ensures a consistent JSON error response is sent to the client.

---

### 10. Security Configuration (`WebSecurityConfig.java`)

This is the central configuration file where we wire everything together.

*   `@Configuration`, `@EnableWebSecurity`, `@EnableMethodSecurity`: These annotations activate Spring Security and enable method-level security (like `@PreAuthorize`).
*   `passwordEncoder()`: Defines the `BCryptPasswordEncoder` bean for use across the application.
*   `authenticationManager()`: Exposes the `AuthenticationManager` bean, which we need in our `AuthService` to handle the login process.
*   `securityFilterChain(HttpSecurity http)`: This is the main configuration block.
    *   `.csrf(disable())`: CSRF is a stateful protection mechanism. Since our API is stateless, we can safely disable it.
    *   `.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))`: This is critical. It tells Spring Security not to create or manage HTTP sessions.
    *   `.authorizeHttpRequests(...)`: This is where we define our URL-based authorization rules.
        *   `.requestMatchers("/auth/**").permitAll()`: Allows anyone to access the login, signup, and refresh endpoints.
        *   `.requestMatchers("/api/admin/**").hasRole("ADMIN")`: A broad rule stating that any endpoint under `/api/admin/` requires the `ADMIN` role.
        *   `.anyRequest().authenticated()`: A catch-all rule that requires any other request to be authenticated.
    *   `.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)`: This is how we insert our custom JWT filter into Spring Security's filter chain. We place it before the standard username/password filter because we want to handle JWT authentication first.

---

### 11. Login Flow Implementation

*   **`AuthController`:** The `/auth/login` endpoint simply takes the `LoginRequestDto` and passes it to the `AuthService`.
*   **`AuthService`:**
    *   The `login` method calls `authenticationManager.authenticate(...)`.
    *   The `AuthenticationManager` uses the provided `UserDetailsServiceImpl` to fetch the user by username from the database.
    *   It then uses the `PasswordEncoder` to compare the request's raw password with the user's stored hashed password.
    *   If they match, it returns a fully populated `Authentication` object. If not, it throws an exception.
    *   Our `AuthService` then takes this authenticated `User` object and uses it to generate the `accessToken` and `refreshToken`, which are returned to the client.

---

### 12. Refresh Token Mechanism

*   **Why?** Access tokens should be short-lived to minimize the damage if they are stolen. But we don't want to force users to log in every 5 minutes. Refresh tokens solve this.
*   **The Difference:**
    *   **Access Token:** Short-lived, contains user data, sent with every API request. It's what proves you are authenticated *right now*.
    *   **Refresh Token:** Long-lived, is just a random, opaque string. It is stored in the database and is sent **only** to the `/auth/refresh` endpoint. Its sole purpose is to prove you have a valid long-term session and to request a new access token.
*   **The Flow:**
    1.  When an access token expires, the client's API call fails with a `401 Unauthorized` status.
    2.  The client's logic should catch this and automatically send its stored refresh token to the `/auth/refresh` endpoint.
    3.  Our `AuthService` calls the `RefreshTokenService`.
    4.  The service looks up the refresh token in the database.
    5.  It verifies the token hasn't expired (`verifyExpiration`). If it has, it's deleted from the database, and an exception is thrown, forcing the user to log in again.
    6.  If the token is valid, a new access token is generated and returned to the client.
    7.  The client can then automatically retry the original API request that failed, this time with the new access token.

---

### 13. Refresh Token Security Best Practices

*   **Token Revocation & Logout:** When a user logs out, the client should discard both tokens. The server-side logout implementation should find and delete the user's refresh token from the database. This effectively invalidates their session.
*   **Token Reuse Protection (Rotation):** For even higher security, you can implement refresh token rotation. When a refresh token is used to get a new access token, the old refresh token is invalidated, and a new refresh token is issued along with the new access token. This helps detect if a refresh token has been stolen and reused. (This template does not implement rotation for simplicity, but it's a recommended next step for high-security applications).
*   **Database Cleanup:** You should have a scheduled job that periodically deletes expired refresh tokens from the database to keep it clean.

---

### 14. End-to-End Example

1.  **Registration:** A user `POST`s to `/auth/signup`. `AuthService` creates a new `User` in the database with `ROLE_USER`.
2.  **Login:** The user `POST`s to `/auth/login`. `AuthService` authenticates them and returns an `accessToken` (valid for 1 min) and a `refreshToken` (valid for 7 days).
3.  **Access Protected Data:** The user `GET`s `/api/me`, including the `accessToken`. `JwtAuthFilter` validates the token, sets the `SecurityContext`, and `@PreAuthorize` allows the request because the user has `ROLE_USER`.
4.  **Access Admin Data:** The user `GET`s `/api/admin/data`. The request is blocked by the rule in `WebSecurityConfig` (`.requestMatchers("/api/admin/**").hasRole("ADMIN")`), and a `403 Forbidden` error is returned.
5.  **Token Expiration:** After 1 minute, the user `GET`s `/api/me` again. `JwtAuthFilter` sees the `accessToken` is expired and throws an exception. A `401 Unauthorized` error is returned.
6.  **Token Refresh:** The client sends its `refreshToken` to `POST /auth/refresh`. `RefreshTokenService` validates it and returns a new `accessToken`.
7.  **Retry:** The client retries the `GET /api/me` request with the new `accessToken`. The request now succeeds.

---

### 15. Common Mistakes & Debugging Tips

*   **401 vs. 403:**
    *   `401 Unauthorized`: Authentication failed. This means the JWT was missing, invalid, or expired. The user is not known or not properly logged in.
    *   `403 Forbidden`: Authorization failed. The user is authenticated (their JWT is valid), but they do not have the required role to access the resource.
*   **Role Mismatches:** Remember the `ROLE_` prefix. `hasRole('ADMIN')` looks for an authority named `ROLE_ADMIN`. If your `Role` entity stores the name as just `ADMIN`, the check will fail. Be consistent.
*   **Filter Order:** Our `JwtAuthFilter` must come *before* `UsernamePasswordAuthenticationFilter`. The order in the filter chain is critical.
*   **`@EnableMethodSecurity` is missing:** If your `@PreAuthorize` annotations are being ignored, you likely forgot to add `@EnableMethodSecurity` to `WebSecurityConfig`.

---

### 16. How to Reuse This in Any New Project

This project is designed as a template. To use it in a new project:

1.  **Copy the Code:** Copy the entire `security` package, the `dto` package, the `models` (User, Role, RefreshToken), the `repositories`, and the `services`.
2.  **Add Dependencies:** Make sure your new project's `pom.xml` has `spring-boot-starter-security` and the `jjwt` dependencies.
3.  **Configure `application.properties`:**
    *   Set up your database connection.
    *   **IMPORTANT:** Add the `app.jwt.secret` and `app.jwt.refreshToken.duration.ms` properties. For production, the secret **must** be set as an environment variable, not in the file itself.
4.  **Seed Roles:** Ensure your `data.sql` file (or a `CommandLineRunner`) creates the initial `ROLE_USER` and `ROLE_ADMIN`.
5.  **Adjust `UserController`:** Customize the protected endpoints to match your new application's needs.
6.  **Customize `User` entity:** Add any additional fields your new application requires for a user (e.g., `firstName`, `lastName`).

You now have a fully functional, secure, and production-ready authentication and authorization system.