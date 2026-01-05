# API Contract

This document outlines the API endpoints for the blog application.

## Auth Controller (`/auth`)

- **POST `/auth/login`**: Authenticates a user and returns a JWT.
  - Request Body: `LoginRequestDto` (`username`, `password`)
  - Response: `LoginResponseDto` (contains JWT)

- **POST `/auth/signup`**: Registers a new user.
  - Request Body: `SignUpRequestDto` (`username`, `email`, `password`)
  - Response: `SignUpResponseDto` (confirmation message)

- **POST `/auth/refresh`**: Refreshes an expired JWT.
  - Request Body: `RefreshTokenRequestDto` (`refreshToken`)
  - Response: `RefreshTokenResponseDto` (new access token)

## User Controller (`/api`)

- **GET `/api/me`**: Retrieves the currently authenticated user's details.
  - Authorization: `USER` or `ADMIN` role required.
  - Response: `UserResponseDto`

- **GET `/api/users`**: Retrieves a list of all users.
  - Authorization: `ADMIN` role required.
  - Response: `List<UserResponseDto>`

- **GET `/api/admin/data`**: Retrieves sensitive admin data.
  - Response: `String`

## Post Controller (`/api/v1/posts`)

- **POST `/api/v1/posts`**: Creates a new post.
  - Request Body: `PostRequestDto`
  - Response: `PostResponseDto`

- **GET `/api/v1/posts/{postId}`**: Retrieves a post by its ID.
  - Response: `PostResponseDto`

- **PUT `/api/v1/posts/{postId}`**: Updates a post.
  - Request Body: `PostRequestDto`
  - Response: `PostResponseDto`

- **DELETE `/api/v1/posts/{postId}`**: Deletes a post.
  - Response: `204 No Content`

## Comment Controller (`/api/v1/posts/{postId}/comments`)

- **POST `/api/v1/posts/{postId}/comments`**: Adds a comment to a post.
  - Request Body: `CommentRequestDto`
  - Response: `CommentResponseDto`

- **GET `/api/v1/posts/{postId}/comments/{commentId}`**: Retrieves a specific comment.
  - Response: `CommentResponseDto`

- **PUT `/api/v1/posts/{postId}/comments/{commentId}`**: Updates a comment.
  - Request Body: `CommentRequestDto`
  - Response: `CommentResponseDto`

- **DELETE `/api/v1/posts/{postId}/comments/{commentId}`**: Deletes a comment.
  - Response: `204 No Content`

## Like Controller (`/api/v1/posts/{postId}/likes`)

- **POST `/api/v1/posts/{postId}/likes`**: Likes a post.
  - Response: `200 OK`

- **DELETE `/api/v1/posts/{postId}/likes`**: Unlikes a post.
  - Response: `204 No Content`

## Favorite Controller (`/api/v1/users/favorites`)

- **POST `/api/v1/users/favorites`**: Adds a post to the user's favorites.
  - Request Body: `FavoriteRequestDto` (`postId`)
  - Response: `200 OK`

- **DELETE `/api/v1/users/favorites`**: Removes a post from the user's favorites.
  - Request Body: `FavoriteRequestDto` (`postId`)
  - Response: `204 No Content`

## Tag Controller (`/api/v1/tags`)

- **POST `/api/v1/tags`**: Creates a new tag.
  - Request Body: `TagRequestDto`
  - Response: `TagResponseDto`

- **GET `/api/v1/tags/{tagId}`**: Retrieves a tag by its ID.
  - Response: `TagResponseDto`

- **PUT `/api/v1/tags/{tagId}`**: Updates a tag.
  - Request Body: `TagRequestDto`
  - Response: `TagResponseDto`

- **DELETE `/api/v1/tags/{tagId}`**: Deletes a tag.
  - Response: `204 No Content`