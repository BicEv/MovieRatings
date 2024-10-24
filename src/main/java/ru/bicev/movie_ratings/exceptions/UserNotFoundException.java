package ru.bicev.movie_ratings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when attempting to get a user that does not exist
 * in the system.
 * 
 * Annotated with {@link ResponseStatus}, which will cause Spring to return
 * an HTTP 404 Not Found status when this exception is thrown.
 * 
 * @see RuntimeException
 * 
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code UserNotFoundException} with the specified detail
     * message.
     *
     * @param message the detail message that explains the reason for the exception
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
