# Movie Ratings Application

## Overview

The **Movie Ratings Application** is a RESTful web service built with Java and Spring Boot that allows users to browse, rate, and review movies. Administrators have additional rights to create, update, and delete movies. The app also includes user authentication and role-based access control to secure certain endpoints.

## Features

- **User Authentication**: Users can sign in using their email and password.
- **Role-based Access Control**: Only users with the "ADMIN" role can manage movies (create, update, delete).
- **Movie Ratings**: Users can rate and review movies.
- **Sorted Movie List**: The list of movies can be retrieved in descending order based on their average rating.
- **Exception Handling**: Global exception handling for REST endpoints (e.g., 404 for not found, 409 for duplicate entries).
  
## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**: For authentication and authorization.
- **Spring Data JPA**: For database interactions.
- **PostgreSQL**: As the database for persistent storage.
- **Thymeleaf**: For rendering views (if applicable).
- **JUnit & Mockito**: For unit and integration testing.

## Endpoints

### Public Endpoints

- `GET /api/movies`: Retrieve a list of all movies, sorted by rating.
  
### Admin Endpoints (Require Admin Role)

- `POST /api/movies/create`: Create a new movie.
- `PUT /api/movies/{id}`: Update an existing movie.
- `DELETE /api/movies/{id}`: Delete a movie by its ID.

### User Endpoints

- `POST /api/movies/{movieId}/reviews`: Add a review for a movie.
- `PUT /api/movies/{movieId}/reviews/{reviewId}`: Update a review for a movie.

## Security

- User authentication is managed using **Spring Security**. Users sign in with their email and password.
- **BCrypt** is used for password encryption.
- Access to movie creation, updates, and deletion is restricted to users with the `ADMIN` role.

## Exception Handling

The application has a global exception handler implemented in the `RestExceptionHandler` class that provides consistent error responses for different error scenarios, such as:

- **404 Not Found**: When a movie, user, or review is not found.
- **409 Conflict**: When attempting to create duplicate users or movies.
- **403 Forbidden**: When unauthorized access is attempted.

## Setup and Installation

### Prerequisites

- **Java 17**
- **Maven** or **Gradle**
- **PostgreSQL** (or any other database of your choice)

### Steps

### Testing

The project includes unit and integration tests. To run the tests, use:
```bash
./mvnw test
