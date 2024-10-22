package ru.bicev.movie_ratings.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.bicev.movie_ratings.exceptions.DuplicateMovieException;
import ru.bicev.movie_ratings.exceptions.DuplicateUserException;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;

/**
 * Global exception handler for handling specific exceptions in the REST API.
 * <p>
 * This class catches exceptions thrown by the REST controllers and returns
 * appropriate HTTP responses with relevant error messages.
 * </p>
 * 
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 * @see org.springframework.web.bind.annotation.RestControllerAdvice
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles exceptions when a requested user, movie, or review is not found.
     * 
     * @param ex the exception that was thrown.
     * @return {@link ResponseEntity} containing a 404 error message and HTTP
     *         status.
     */
    @ExceptionHandler({ UserNotFoundException.class, MovieNotFoundException.class, ReviewNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 Not found: " + ex.getMessage());
    }

    /**
     * Handles exceptions when attempting to create a user or movie that already
     * exists.
     * 
     * @param ex the exception that was thrown.
     * @return {@link ResponseEntity} containing a 409 conflict message and HTTP
     *         status.
     */
    @ExceptionHandler({ DuplicateUserException.class, DuplicateMovieException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDuplicateExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("409 Already exists: " + ex.getMessage());
    }

    /**
     * Handles exceptions when an illegal access operation is attempted, such as
     * accessing resources without proper authorization.
     * 
     * @param ex the exception that was thrown.
     * @return {@link ResponseEntity} containing a 403 forbidden message and HTTP
     *         status.
     */
    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleIllegalAccessException(IllegalAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("403 Forbidden: " + ex.getMessage());
    }

    /**
     * Handles general exceptions that are not specifically covered by other
     * handlers.
     * 
     * @param ex the exception that was thrown.
     * @return {@link ResponseEntity} containing a 500 internal server error message
     *         and HTTP status.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("500 Internal server error: " + ex.getMessage());
    }

}
