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

@Controller
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreationForm(@PathVariable Long movieId, Model model) {
        model.addAttribute("movieId", movieId);
        model.addAttribute("review", new ReviewDto());
        return "review/create";
    }

    @GetMapping("/{reviewId}")
    public String getReviewById(@PathVariable Long movieId, @PathVariable Long reviewId, Model model) {
        ReviewDto review = reviewService.findReviewById(reviewId);

        model.addAttribute("movieId", movieId);
        model.addAttribute("review", review);

        return "review/view";
    }

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

    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewService.deleteReview(id, userDto.getId());
        return "redirect:/movies";
    }

    @GetMapping("/{reviewId}/edit")
    public String showEditForm(@PathVariable Long reviewId, @PathVariable Long movieId, Model model) {
        ReviewDto review = reviewService.findReviewById(reviewId);

        model.addAttribute("review", review);
        model.addAttribute("movieId", movieId);

        return "review/edit";
    }

    @PutMapping("/{reviewId}/edit")
    public String editReview(@PathVariable Long reviewId, @PathVariable Long movieId,
            @Valid @ModelAttribute ReviewDto reviewDto,
            Model model, Principal principal) {
        String email = principal.getName();
        Long userId = userService.getUserByEmail(email).getId();

        reviewService.updateReview(reviewId, reviewDto, userId);

        return "redirect:/movies/" + movieId;
    }

    @GetMapping
    public String getReviewsByMovie(@PathVariable Long movieId, Model model) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovie(movieId);
        model.addAttribute("reviews", reviews);
        return "review/list";
    }

    @ExceptionHandler({ ReviewNotFoundException.class, UserNotFoundException.class, MovieNotFoundException.class })
    public String handleNotFoundException(ReviewNotFoundException ex, Model model) {
        model.addAttribute("message", "Not found: " + ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(IllegalAccessException.class)
    public String handleIllegalAccessException(IllegalAccessException ex, Model model) {
        model.addAttribute("message", "Forbidden: " + ex.getMessage());
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("message", "Internal server error: " + ex.getMessage());
        return "error/500";
    }

}