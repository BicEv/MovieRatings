package ru.bicev.movie_ratings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalAccessException extends RuntimeException {
    public IllegalAccessException(String message) {
        super(message);
    }

}
