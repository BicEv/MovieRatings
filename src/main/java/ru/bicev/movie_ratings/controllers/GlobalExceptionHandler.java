package ru.bicev.movie_ratings.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.bicev.movie_ratings.exceptions.DuplicateMovieException;
import ru.bicev.movie_ratings.exceptions.DuplicateUserException;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ UserNotFoundException.class, MovieNotFoundException.class, ReviewNotFoundException.class })
    public String handleNotFoundExceptions(Exception ex, Model model) {
        model.addAttribute("message", "Not found: " + ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler({ DuplicateUserException.class, DuplicateMovieException.class })
    public String handleDuplicateExceptions(Exception ex, Model model) {
        model.addAttribute("message", "Duplicate: " + ex.getMessage());
        return "error/409";
    }

    @ExceptionHandler(IllegalAccessException.class)
    public String handleIllegalAccessException(IllegalAccessException ex, Model model) {
        model.addAttribute("message", "Access denied: " + ex.getMessage());
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("message", "Internal server error: " + ex.getMessage());
        return "error/500";
    }

}
