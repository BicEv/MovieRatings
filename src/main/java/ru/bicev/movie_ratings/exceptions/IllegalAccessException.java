package ru.bicev.movie_ratings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when attempting to access method with no rights to
 * do it.
 * 
 * Annotated with {@link ResponseStatus}, which will cause Spring to return
 * an HTTP 403 Forbidden status when this exception is thrown.
 * 
 * @see RuntimeException
 * 
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalAccessException extends RuntimeException {

    /**
     * Constructs a new {@code IllegalAccessException} with the specified detail
     * message.
     *
     * @param message the detail message that explains the reason for the exception
     */
    public IllegalAccessException(String message) {
        super(message);
    }

}
