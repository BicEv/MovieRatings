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

/**
 * Global exception handler that intercepts specific application exception
 * and returns appropriate error views with relevant messages.
 * 
 * Hanldes the following exceptions:
 * - {@link UserNotFoundException}, {@link MovieNotFoundException},
 * {@link ReviewNotFoundException} - returns a 404 error page.
 * - {@link DuplicateUserException}, {@link DuplicateMovieException} - returns a
 * 409 error page for conflict cases.
 * - {@link IllegalAccessException} - returns a 403 error page for access
 * denial.
 * - {@link Exception} - handles general exceptions, returning a 500 internal
 * server error page.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles not found exceptions for user, movie, and review entities.
     * 
     * @param ex    the exception being handled
     * @param model a holder for model attributes
     * @return the view name for the 404 page
     */
    @ExceptionHandler({ UserNotFoundException.class, MovieNotFoundException.class, ReviewNotFoundException.class })
    public String handleNotFoundExceptions(Exception ex, Model model) {
        model.addAttribute("message", "Not found: " + ex.getMessage());
        return "error/404";
    }

    /**
     * Handles duplicate entry exceptions for user and movie entities.
     * 
     * @param ex    the exception being handled
     * @param model a holder for model attributes
     * @return the view name for the 409 page
     */
    @ExceptionHandler({ DuplicateUserException.class, DuplicateMovieException.class })
    public String handleDuplicateExceptions(Exception ex, Model model) {
        model.addAttribute("message", "Duplicate: " + ex.getMessage());
        return "error/409";
    }

    /**
     * Handles illegal access exceptions, returning 403 page.
     * 
     * @param ex    the exception being handled
     * @param model a holder for model attributes
     * @return the view name fot the 403 page
     */
    @ExceptionHandler(IllegalAccessException.class)
    public String handleIllegalAccessException(IllegalAccessException ex, Model model) {
        model.addAttribute("message", "Access denied: " + ex.getMessage());
        return "error/403";
    }

    /**
     * Handles all other exceptions, returning a 500 internal server error page.
     * 
     * @param ex    the exception being handled
     * @param model a holder for model attributes
     * @return the view name for the 500 page
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("message", "Internal server error: " + ex.getMessage());
        return "error/500";
    }

}
