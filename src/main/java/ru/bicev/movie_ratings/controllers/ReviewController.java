package ru.bicev.movie_ratings.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;
import ru.bicev.movie_ratings.services.ReviewService;
import ru.bicev.movie_ratings.services.UserService;

/**
 * Controller responsible for handling review-related actions, based on movie
 * such as creation, updating, deleting, review iformation retrieval and
 * handling the exceptions.
 */
@Controller
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    /**
     * Constructor to inject dependencies.
     * 
     * @param reviewService a service that handles review operations
     * @param userService   a service that handles user operations
     */
    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    /**
     * Displays the creation form for a new review.
     * 
     * @param movieId the ID of the reviewed movie
     * @param model   a holder for model attributes
     * @return the view name for creation form
     */
    @GetMapping("/create")
    public String showCreationForm(@PathVariable Long movieId, Model model) {
        model.addAttribute("movieId", movieId);
        model.addAttribute("review", new ReviewDto());
        return "review/create";
    }

    /**
     * Retrieves the review information based on its ID
     * 
     * @param movieId  the ID of the reviewed movie
     * @param reviewId the ID of the review
     * @param model    a holer for model attributes
     * @return the form name for displaying review information
     */
    @GetMapping("/{reviewId}")
    public String getReviewById(@PathVariable Long movieId, @PathVariable Long reviewId, Model model) {
        ReviewDto review = reviewService.findReviewById(reviewId);

        model.addAttribute("movieId", movieId);
        model.addAttribute("review", review);

        return "review/view";
    }

    /**
     * Handles the creation of a new review.
     * 
     * @param movieId   the ID of the reviewed movie
     * @param reviewDto the data transfer object containing review information
     * @param model     a holder for model attributes
     * @param principal a holder for a current user information
     * @return redirect to the reviewed movie dipslaying form
     */
    @PostMapping
    public String createReview(@PathVariable Long movieId, @Valid @ModelAttribute ReviewDto reviewDto, Model model,
            Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);
        reviewDto.setUserId(userDto.getId());
        reviewDto.setMovieId(movieId);
        ReviewDto createdReview = reviewService.createReview(reviewDto);
        model.addAttribute("review", createdReview);
        return "redirect:/movies/" + movieId;
    }

    /**
     * Deletes a review by its ID
     * 
     * @param id        the ID of the review to delete
     * @param principal a holder for a current user information
     * @return a redirect to the movies list form
     */
    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewService.deleteReview(id, userDto.getId());
        return "redirect:/movies";
    }

    /**
     * Displays the review editing form
     * 
     * @param reviewId the ID of the review to be edited
     * @param movieId  the ID of the reviewed movie
     * @param model    a holder for model attributes
     * @return a view name for editing form
     */
    @GetMapping("/{reviewId}/edit")
    public String showEditForm(@PathVariable Long reviewId, @PathVariable Long movieId, Model model) {
        ReviewDto review = reviewService.findReviewById(reviewId);

        model.addAttribute("review", review);
        model.addAttribute("movieId", movieId);

        return "review/edit";
    }

    /**
     * Handles the editing of a review
     * 
     * @param reviewId  the ID of the review to be edited
     * @param movieId   the ID of the reviewed movie
     * @param reviewDto a data transfer object containing editing review information
     * @param model     a holder for model attributes
     * @param principal a holder for current user information
     * @return a redirect to the reviewed movie displaying form
     */
    @PutMapping("/{reviewId}/edit")
    public String editReview(@PathVariable Long reviewId, @PathVariable Long movieId,
            @Valid @ModelAttribute ReviewDto reviewDto,
            Model model, Principal principal) {
        String email = principal.getName();
        Long userId = userService.getUserByEmail(email).getId();

        reviewService.updateReview(reviewId, reviewDto, userId);

        return "redirect:/movies/" + movieId;
    }

    /**
     * Retrieves the reviews for a movie by its ID
     * 
     * @param movieId the ID of the reviewed movie
     * @param model   a holder for a model attributes
     * @return the view name for displaying the list of the reviews
     */
    @GetMapping
    public String getReviewsByMovie(@PathVariable Long movieId, Model model) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovie(movieId);
        model.addAttribute("reviews", reviews);
        return "review/list";
    }

    /**
     * Handles *NotFoundException
     * 
     * @param ex    the exception being handled
     * @param model a holder for model attributes
     * @return a view name for the 404 page
     */
    @ExceptionHandler({ ReviewNotFoundException.class, UserNotFoundException.class, MovieNotFoundException.class })
    public String handleNotFoundException(ReviewNotFoundException ex, Model model) {
        model.addAttribute("message", "Not found: " + ex.getMessage());
        return "error/404";
    }

    /**
     * Handles IllegalAccessExceptions
     * 
     * @param ex    the exception being handled
     * @param model a holder for model attributes
     * @return a view name for the 403 page
     */
    @ExceptionHandler(IllegalAccessException.class)
    public String handleIllegalAccessException(IllegalAccessException ex, Model model) {
        model.addAttribute("message", "Forbidden: " + ex.getMessage());
        return "error/403";
    }

    /**
     * Handles general Exceptions
     * 
     * @param ex    the exception being handled
     * @param model a holder for model attributes
     * @return a view name for the 500 page
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("message", "Internal server error: " + ex.getMessage());
        return "error/500";
    }

}