# API Contract

This document provides a detailed overview of all API endpoints, including their access levels and descriptions.

## Endpoints

| Endpoint                                    | HTTP Method | Access        | Description                                     |
| ------------------------------------------- | ----------- | ------------- | ----------------------------------------------- |
| `/api/v1/auth/login`                        | `POST`      | Public        | Authenticates a user and returns a JWT.         |
| `/api/v1/auth/signup`                       | `POST`      | Public        | Registers a new user.                           |
| `/api/v1/auth/refresh`                      | `POST`      | Public        | Refreshes an expired JWT.                       |
| `/api/v1/feed`                              | `GET`       | Public        | Retrieves the main content feed.                |
| `/api/v1/tags`                              | `GET`       | Public        | Retrieves all tags.                             |
| `/api/v1/tags`                              | `POST`      | Authenticated | Creates a new tag.                              |
| `/api/v1/tags/{id}`                         | `GET`       | Public        | Retrieves a single tag.                         |
| `/api/v1/tags/{id}`                         | `PUT`       | Authenticated | Updates an existing tag.                        |
| `/api/v1/tags/{id}`                         | `DELETE`    | Authenticated | Deletes a tag.                                  |
| `/api/v1/tags/{id}/posts`                   | `GET`       | Public        | Retrieves all posts associated with a tag.      |
| `/api/v1/posts`                             | `POST`      | Authenticated | Creates a new post.                             |
| `/api/v1/posts/{id}`                        | `GET`       | Public        | Retrieves a single post.                        |
| `/api/v1/posts/{id}`                        | `PUT`       | Authenticated | Updates an existing post.                       |
| `/api/v1/posts/{id}`                        | `DELETE`    | Authenticated | Deletes a post.                                 |
| `/api/v1/posts/{postId}/comments`           | `GET`       | Public        | Retrieves comments for a post.                  |
| `/api/v1/posts/{postId}/comments`           | `POST`      | Authenticated | Adds a comment to a post.                       |
| `/api/v1/posts/{postId}/comments/{commentId}`| `GET`       | Public        | Retrieves a single comment.                     |
| `/api/v1/posts/{postId}/comments/{commentId}`| `PUT`       | Authenticated | Updates a comment.                              |
| `/api/v1/posts/{postId}/comments/{commentId}`| `DELETE`    | Authenticated | Deletes a comment.                              |
| `/api/v1/posts/{postId}/tags/{tagId}`       | `POST`      | Authenticated | Associates a tag with a post.                   |
| `/api/v1/posts/{postId}/tags/{tagId}`       | `DELETE`    | Authenticated | Disassociates a tag from a post.                |
| `/api/v1/posts/{postId}/likes`              | `POST`      | Authenticated | Likes a post.                                   |
| `/api/v1/posts/{postId}/likes`              | `DELETE`    | Authenticated | Unlikes a post.                                 |
| `/api/v1/posts/{postId}/favorites`          | `POST`      | Authenticated | Favorites a post.                               |
| `/api/v1/posts/{postId}/favorites`          | `DELETE`    | Authenticated | Unfavorites a post.                             |
| `/api/v1/me`                                | `GET`       | Authenticated | Retrieves the current user's profile.           |
| `/api/v1/me/posts`                          | `GET`       | Authenticated | Retrieves the current user's posts.             |
| `/api/v1/me/favorites`                      | `GET`       | Authenticated | Retrieves the current user's favorite posts.    |
| `/api/v1/users`                             | `GET`       | Admin         | Retrieves a list of all users.                  |