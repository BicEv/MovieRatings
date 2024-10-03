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

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ UserNotFoundException.class, MovieNotFoundException.class, ReviewNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 Not found: " + ex.getMessage());
    }

    @ExceptionHandler({ DuplicateUserException.class, DuplicateMovieException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDuplicateExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("409 Already exists: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleIllegalAccessException(IllegalAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("403 Forbidden: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("500 Internal server error: " + ex.getMessage());
    }

}
