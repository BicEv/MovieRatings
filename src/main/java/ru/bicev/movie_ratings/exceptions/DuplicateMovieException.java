package ru.bicev.movie_ratings.exceptions;

public class DuplicateMovieException extends RuntimeException {

    public DuplicateMovieException(String message) {
        super(message);
    }

}
