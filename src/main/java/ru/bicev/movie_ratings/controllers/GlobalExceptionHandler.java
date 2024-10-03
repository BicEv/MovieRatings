package ru.bicev.movie_ratings.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.bicev.movie_ratings.exceptions.DuplicateMovieException;
import ru.bicev.movie_ratings.exceptions.DuplicateUserException;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ UserNotFoundException.class, MovieNotFoundException.class, ReviewNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundExceptions(Exception ex, Model model) {
        model.addAttribute("message", "Not found: " + ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler({ DuplicateUserException.class, DuplicateMovieException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateExceptions(Exception ex, Model model) {
        model.addAttribute("message", "Duplicate: " + ex.getMessage());
        return "error/409";
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleIllegalAccessException(IllegalAccessException ex, Model model) {
        model.addAttribute("message", "Access denied: " + ex.getMessage());
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("message", "Internal server error: " + ex.getMessage());
        return "error/500";
    }

}
