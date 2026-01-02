# A Comprehensive Guide to JWT Authentication in Spring Boot

This document provides a thorough explanation of how JWT-based authentication works in Spring Boot, from high-level concepts to a detailed code analysis and a step-by-step implementation guide.

***

## Part 1: The Big Picture - What is JWT and Spring Security?

Before diving into the code, let's understand the two main technologies at play.

### What is a JSON Web Token (JWT)?

Imagine you're going to an exclusive festival. When you arrive, you show your ID and ticket at the main gate. Instead of having to show your ID at every single ride and food stall, the staff gives you a special wristband. This wristband proves you are an authenticated guest, and different colors or markings on it might grant you access to VIP areas.

A **JWT** is that wristband. It's a secure, compact string of text that a server gives to a client (like a web browser or mobile app) after the client successfully logs in. The client then includes this JWT in the header of every subsequent request to the server.

A JWT consists of three parts separated by dots (`.`):

1.  **Header:** Contains metadata, like the type of token (`JWT`) and the signing algorithm being used (e.g., `HS256`).
2.  **Payload:** Contains the "claims" or data about the user. This is where you'd put information like the user's ID, username, and roles (e.g., `ROLE_USER`, `ROLE_ADMIN`). It also includes an expiration date (`exp`) to ensure the token doesn't live forever.
3.  **Signature:** This is the most critical part for security. It's created by taking the Header, the Payload, a secret key (known *only* to the server), and running them through the signing algorithm.

**Why is the signature so important?**

*   **It verifies the sender:** Only the server with the secret key can create a valid signature.
*   **It ensures data integrity:** If anyone tries to tamper with the payload (e.g., changing the username to "admin"), the signature will become invalid. The server will detect this and reject the token.

This system is called **stateless authentication**. The server doesn't need to keep a record of logged-in users (i.e., it doesn't manage "sessions"). It just needs to validate the JWT that comes with each request. This is highly scalable and works well for modern applications.

### What is Spring Security?

Spring Security is the framework responsible for protecting your application. Think of it as a security guard standing at the door of your application. Its main jobs are:

*   **Authentication:** Verifying who you are (e.g., checking your username and password).
*   **Authorization:** Deciding what you're allowed to do (e.g., a regular user can view their profile, but only an admin can delete users).

Spring Security works by using a **Filter Chain**. Every HTTP request that comes into your application must pass through this chain of filters before it can reach your controllers. You can add your own custom filters to this chain to implement custom security logic, which is exactly what you've done.

---

## Part 2: The End-to-End Authentication Flow (Connecting to Your Code)

Now let's walk through the entire process, from configuration to accessing a protected resource, and see how your code enables it.

### Step 1: Configuration (The Rulebook)

This is where you tell Spring Security how to behave.

*   **File:** `src/main/java/patil/parshwa/blog/security/WebSecurityConfig.java`

This is the central hub for your security setup. The most important part is the `securityFilterChain` bean.

```java
// From WebSecurityConfig.java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            // 1. No sessions, we are using JWTs (stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable) // 2. Disable CSRF for stateless APIs
            .authorizeHttpRequests(auth ->
                    auth
                    // 3. Allow anyone to access login/signup endpoints
                    .requestMatchers("/auth/**").permitAll()
                    // 4. All other requests must be authenticated
                    .anyRequest().authenticated()
            )
            // 5. Add our custom JWT filter before the standard username/password filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

**Explanation:**

1.  `sessionCreationPolicy(SessionCreationPolicy.STATELESS)`: This tells Spring Security **not** to create or manage user sessions. We're going fully stateless with JWTs.
2.  `csrf(AbstractHttpConfigurer::disable)`: CSRF protection is a stateful security measure. Since we are stateless, it's not needed and can be disabled for APIs.
3.  `.requestMatchers("/auth/**").permitAll()`: We declare that any URL starting with `/auth/` (like `/auth/login` and `/auth/signup`) does not require authentication. Anyone can access them.
4.  `.anyRequest().authenticated()`: Any other request to any other URL in the application **must** be authenticated.
5.  `.addFilterBefore(jwtAuthFilter, ...)`: This is the most crucial line. We are inserting our custom filter, `JwtAuthFilter`, into the security filter chain. It will run for every request and be responsible for checking for a JWT.

### Step 2: Signing Up & Logging In (Getting the Wristband)

When a user logs in, they trade their username and password for a JWT.

*   **Files:**
    *   `src/main/java/patil/parshwa/blog/controllers/AuthController.java` (The API endpoint)
    *   `src/main/java/patil/parshwa/blog/services/AuthService.java` (The logic)
    *   `src/main/java/patil/parshwa/blog/security/AuthUtil.java` (The token creator)

**The Flow:**

1.  A user sends a `POST` request to `/auth/login` with their username and password in the request body.
2.  The `login` method in `AuthController` calls the `login` method in `AuthService`.
3.  Inside `AuthService`, this is the key part:
    ```java
    // From AuthService.java
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword()
            )
    );
    ```
    The `AuthenticationManager` (a standard Spring Security component) takes the username and password. It uses your `UserDetailsServiceImpl` to find the user in the database and your `PasswordEncoder` (defined in `WebSecurityConfig`) to check if the password is correct. If they don't match, it throws an exception.
4.  If authentication is successful, the `AuthService` gets the authenticated `User` object.
5.  It then calls `authUtil.generateAccessToken(user)`.
6.  The `generateAccessToken` method in `AuthUtil` builds the JWT:
    ```java
    // From AuthUtil.java
    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getUsername()) // Payload: who the user is
                .claim("userId", user.getId()) // Payload: custom data
                .expiration(...) // Payload: when the token expires
                .signWith(getSecretKey()) // Signature: using the secret key
                .compact();
    }
    ```
    The secret key itself is loaded from your `application.properties` file. **This key must be kept secret!**
7.  The service returns the generated JWT to the controller, which sends it back to the user. The user now has their "wristband."

### Step 3: Accessing Protected Resources (Using the Wristband)

Now, the user wants to access something restricted, like their profile page at `/api/user/me`.

*   **File:** `src/main/java/patil/parshwa/blog/security/JwtAuthFilter.java` (The Token Checker)

**The Flow:**

1.  The user makes a `GET` request to `/api/user/me`. They **must** include the JWT in a header like this: `Authorization: Bearer <the_jwt_string>`
2.  Because of our rule in `WebSecurityConfig`, Spring Security intercepts this request. The `JwtAuthFilter` runs first.
3.  The `doFilterInternal` method in `JwtAuthFilter` executes:
    *   It checks for the `Authorization` header and makes sure it starts with "Bearer ".
    *   It extracts the JWT string.
    *   It calls `authUtil.getUsernameFromToken(jwt)` to parse the token.
    ```java
    // From AuthUtil.java
    public String getUsernameFromToken(String jwt) {
        Claims claims=  Jwts.parser()
                            .verifyWith(getSecretKey()) // 1. Verify the signature!
                            .build()
                            .parseSignedClaims(jwt)     // 2. Parse the token
                            .getPayload();
        return claims.getSubject(); // 3. Get the username from the payload
    }
    ```
    The `.verifyWith(getSecretKey())` step is where the magic happens. The JJWT library re-calculates the signature using the header, payload, and your secret key. If it matches the signature in the token, the token is valid. If not, it throws an exception (e.g., `SignatureException`), which you correctly catch in your filter. It also checks if the token has expired.
4.  Back in `JwtAuthFilter`, if the token is valid, it now has the username. It checks if the user is already authenticated for this request (`SecurityContextHolder.getContext().getAuthentication() == null`).
5.  If not, it loads the `User` from the database.
6.  It then creates a new `UsernamePasswordAuthenticationToken`. This time, it represents an **authenticated** user.
7.  Finally, it does this:
    ```java
    // From JwtAuthFilter.java
    SecurityContextHolder.getContext().setAuthentication(authToken);
    ```
    This line is **critical**. You are manually telling Spring Security: "I have verified this user via their JWT. For the rest of this request, this user is authenticated."
8.  The filter chain continues, and the request is finally allowed to reach the controller for the protected resource.

### Summary of Key Files and Their Roles

| File | Purpose | Why it's needed |
| :--- | :--- | :--- |
| **`pom.xml`** | Dependencies | To include `spring-boot-starter-security` and the `jjwt` library for handling JWTs. |
| **`application.properties`** | Configuration | To store the `app.jwt.secret`, which is essential for signing and validating tokens. |
| **`WebSecurityConfig.java`** | **Security Rulebook** | The main configuration class. It defines which endpoints are public vs. protected and, most importantly, **adds your custom JWT filter to the chain**. |
| **`UserDetailsServiceImpl.java`**| **User Finder** | Implements a standard Spring Security interface to tell the framework how to find a user by their username in your database. |
| **`AuthUtil.java`** | **Token Factory & Parser** | A helper class dedicated to creating new JWTs upon login and parsing/validating them for incoming requests. |
| **`AuthService.java`** | **Authentication Logic** | Orchestrates the login and signup process. It uses the `AuthenticationManager` to validate credentials and the `AuthUtil` to generate a token. |
| **`AuthController.java`** | **Entrypoint** | The REST controller that exposes the `/login` and `/signup` endpoints to the outside world. |
| **`JwtAuthFilter.java`** | **The Token Guard** | This is the heart of your JWT implementation. It intercepts **every** request, looks for a JWT, validates it, and sets the security context, effectively logging the user in for that single request. |

---

## Part 3: Your Guide to Implementing JWT Auth From Scratch

This guide outlines the sequence of steps and the conceptual reasoning for building a JWT authentication system in Spring Boot.

### Step 1: Project Setup & Dependencies

**Goal:** Get a blank Spring Boot project with the necessary tools.

1.  **Create a New Spring Boot Project:** Use the Spring Initializr (start.spring.io) to create a new project.
2.  **Add Dependencies:** You will need the following starters:
    *   `Spring Web`: For building a web application.
    *   `Spring Security`: The core security framework.
    *   `Spring Data JPA`: To interact with a database.
    *   `Lombok`: To reduce boilerplate code (optional but recommended).
    *   A database driver (`H2 Database`, `PostgreSQL`, etc.).
3.  **Add JWT Library:** Manually add the `jjwt` library to your `pom.xml` (or `build.gradle`). This library is the de-facto standard for working with JWTs in Java.
    ```xml
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.6</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.12.6</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.12.6</version>
    </dependency>
    ```

### Step 2: Create the User Model and Repository

**Goal:** Define what a "user" is and how to find one in the database.

1.  **`User` Entity:** Create a `User` class. It must implement Spring Security's `UserDetails` interface. This is a contract that forces your `User` class to have methods like `getUsername()`, `getPassword()`, and `getAuthorities()`, which Spring Security needs to perform its duties.
2.  **`UserRepository`:** Create a standard Spring Data JPA repository interface for your `User` entity. It should have a method like `Optional<User> findByUsername(String username)`.

### Step 3: Configure Spring Security (`WebSecurityConfig`)

**Goal:** Set up the main security rules and create essential beans.

1.  **Create `WebSecurityConfig` class:** Annotate it with `@Configuration` and `@EnableWebSecurity`.
2.  **Create a `PasswordEncoder` Bean:** This bean is responsible for securely hashing passwords. `BCryptPasswordEncoder` is the standard choice. You MUST NOT store plain text passwords.
    ```java
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    ```
3.  **Create a `UserDetailsService` Bean:** This is the bridge between Spring Security and your user database. You've already done this in your code with `UserDetailsServiceImpl`. It has one job: load a user by their username.
4.  **Create an `AuthenticationManager` Bean:** This is the component that orchestrates authentication. You expose it as a bean so you can inject it into your `AuthService` later.
5.  **Create the `SecurityFilterChain` Bean:** This is the most critical configuration step. Here you define:
    *   **Statelessness:** `.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))`
    *   **Public vs. Protected Routes:** `.authorizeHttpRequests(...)` to define which URLs are open (`/auth/**`) and which are protected (`.anyRequest().authenticated()`).
    *   **Custom Filter Integration:** At this point, you will have a compilation error because you haven't created your JWT filter yet. You will come back here and add `.addFilterBefore(yourJwtFilter, ...)` in Step 5.

### Step 4: Create the JWT Utility (`AuthUtil`)

**Goal:** Isolate all JWT-related logic in one place.

1.  **Create an `AuthUtil` class:** Annotate it with `@Component`.
2.  **Load the Secret Key:** Use `@Value("${app.jwt.secret}")` to inject your secret key from `application.properties`. Keep it long and random.
3.  **Create `generateAccessToken()`:** A method that takes a `User` object and uses the `jjwt` library to build a token. It should set the subject (username), any custom claims (like user ID), an expiration date, and sign it with your secret key.
4.  **Create `getUsernameFromToken()` (or `validateToken()`):** A method that takes a JWT string, verifies its signature using the secret key, and extracts the claims. This method will throw exceptions if the token is expired, malformed, or has an invalid signature.

### Step 5: Create the JWT Authentication Filter (`JwtAuthFilter`)

**Goal:** Create the "guard" that inspects every request for a valid JWT.

1.  **Create `JwtAuthFilter` class:** It must extend `OncePerRequestFilter` to ensure it runs exactly once per request. Annotate it with `@Component`.
2.  **Implement `doFilterInternal()`:**
    *   Get the `Authorization` header from the `HttpServletRequest`.
    *   Check if it's null or doesn't start with `"Bearer "`. If so, just continue the filter chain (`filterChain.doFilter(...)`) and do nothing else. The user is not trying to authenticate with a token.
    *   If a bearer token exists, extract it.
    *   Use your `AuthUtil` to validate the token and get the username.
    *   **Use a `try-catch` block** to handle potential exceptions from the validation step (e.g., `ExpiredJwtException`, `SignatureException`).
    *   If the token is valid and the user is not yet authenticated in the current context (`SecurityContextHolder.getContext().getAuthentication() == null`), load the `UserDetails` from your `UserDetailsService`.
    *   Create an `UsernamePasswordAuthenticationToken` representing the authenticated user.
    *   Set this token in the `SecurityContext`: `SecurityContextHolder.getContext().setAuthentication(token)`. This is how you tell Spring Security the user is now authenticated for this request.
3.  **Wire the Filter:** Go back to `WebSecurityConfig` and add your `JwtAuthFilter` to the filter chain using `addFilterBefore`.

### Step 6: Create the Authentication Controller and Service

**Goal:** Expose endpoints for users to register and log in.

1.  **`AuthController`:** A simple REST controller with `@PostMapping` methods for `/signup` and `/login`. These endpoints should be public (as configured in Step 3).
2.  **`AuthService`:**
    *   **`signup()` method:** Takes user details, encodes the password using the `PasswordEncoder`, saves the new `User` to the database.
    *   **`login()` method:**
        *   Takes a username and password.
        *   Calls `authenticationManager.authenticate(...)`. This triggers the authentication flow you configured (using your `UserDetailsService` and `PasswordEncoder`).
        *   If authentication is successful, it returns an `Authentication` object.
        *   Use the `User` from this object to generate a JWT with your `AuthUtil`.
        *   Return the JWT to the user.
---

## Part 4: Implemented Improvements and Best Practices

To elevate this implementation to a production-ready and reusable template, several best practices have been incorporated.

### 1. Centralized Exception Handling in `JwtAuthFilter`

**Before:** The filter manually caught specific JWT exceptions (`ExpiredJwtException`, etc.) and wrote a raw JSON error to the response. This approach bypasses Spring's central error handling mechanism.

**After:** The filter now has a single `try-catch (Exception ex)` block. Instead of writing to the response directly, it delegates the exception to the `HandlerExceptionResolver`.

```java
// In JwtAuthFilter.java
} catch (Exception ex) {
    log.error("Exception in JwtAuthFilter: ", ex);
    handlerExceptionResolver.resolveException(request, response, null, ex);
}
```

**Benefit:** This ensures that authentication errors are handled by your `GlobalExceptionHandler` just like any other application error, leading to consistent error response formats, cleaner code, and better adherence to Spring's design principles.

### 2. CORS (Cross-Origin Resource Sharing) Configuration

**Problem:** By default, browsers block web pages from making requests to a different domain (or port) than the one that served the page. This would prevent a React/Angular/Vue frontend from calling your API.

**Solution:** A `CorsConfigurationSource` bean has been added to `WebSecurityConfig`.

```java
// In WebSecurityConfig.java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // Specify allowed origins, methods, and headers
    configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```
This configuration is then enabled in the `securityFilterChain` with `.cors(...)`.

**Benefit:** Your API is now ready to be consumed by frontend applications hosted on different origins, which is a standard requirement for modern web development.
### 3. Refresh Token Mechanism

**Problem:** Short-lived access tokens are good for security, but they create a poor user experience by forcing users to log in frequently. Long-lived access tokens are a security risk if they are stolen.

**Solution:** A refresh token system has been implemented.

**How It Works:**

1.  **Login:** When a user logs in via `/auth/login`, they now receive both a short-lived `accessToken` (1 minute) and a long-lived `refreshToken` (7 days). The refresh token is stored in the database.
2.  **Access Token Expiration:** The client uses the `accessToken` to access protected resources. When it expires, the API will return a 401 Unauthorized error.
3.  **Token Refresh:** The client application should then send the `refreshToken` to the new `POST /auth/refresh` endpoint.
4.  **Validation:** The server validates the refresh token:
    *   It checks if the token exists in the database.
    *   It verifies that the token has not expired.
5.  **New Token Issuance:** If the refresh token is valid, the server generates a brand new `accessToken` and returns it to the client. The client can then retry the original request with the new token.

**Benefit:** This provides the best of both worlds: the high security of short-lived access tokens and the great user experience of long-term sessions, without the need for the user to constantly re-enter their credentials.