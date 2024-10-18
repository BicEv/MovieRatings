package ru.bicev.movie_ratings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when attempting to add a user that already exists
 * in the system.
 * 
 * Annotated with {@link ResponseStatus}, which will cause Spring to return
 * an HTTP 409 Conflict status when this exception is thrown.
 * 
 * @see RuntimeException
 * 
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUserException extends RuntimeException {

    /**
     * Constructs a new {@code DuplicateUserException} with the specified detail
     * message.
     *
     * @param message the detail message that explains the reason for the exception
     */
    public DuplicateUserException(String message) {
        super(message);
    }
}
