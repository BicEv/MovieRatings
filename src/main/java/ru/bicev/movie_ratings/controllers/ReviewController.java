package ru.bicev.movie_ratings.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
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

    @GetMapping("create")
    public String showCreationForm(@PathVariable Long movieId, Model model) {
        model.addAttribute("movieId", movieId);
        model.addAttribute("review", new ReviewDto());
        return "review/create";
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

    @DeleteMapping("{id}")
    public String deleteReview(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewService.deleteReview(id, userDto.getId());
        return "redirect:/movies";
    }

    @GetMapping("edit")
    public String showEditForm(@PathVariable Long movieId, Model model) {
        model.addAttribute("movieId", movieId);
        model.addAttribute("review", new ReviewDto());
        return "review/edit";
    }

    @PostMapping("edit")
    public String editReview(@PathVariable Long movieId, @Valid @ModelAttribute ReviewDto reviewDto,
            Model model, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewDto.setUserId(userDto.getId());
        reviewDto.setMovieId(movieId);
        ReviewDto updatedReview = reviewService.updateReview(reviewDto.getId(), reviewDto, userDto.getId());

        model.addAttribute("review", updatedReview);
        return "redirect:/movies/" + movieId;
    }

    @GetMapping
    public String getReviewsByMovie(@PathVariable Long movieId, Model model) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovie(movieId);
        model.addAttribute("reviews", reviews);
        return "review/list";
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(ReviewNotFoundException ex, Model model) {
        System.out.println("Handling ReviewNotFoundException: " + ex.getMessage());
        model.addAttribute("message", "Caught in controller: " + ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model) {
        System.out.println("Handling ReviewNotFoundException: " + ex.getMessage());
        model.addAttribute("message", "Caught in controller: " + ex.getMessage());
        return "error/500";
    }

}