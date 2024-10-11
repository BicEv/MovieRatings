package ru.bicev.movie_ratings.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.services.ReviewService;
import ru.bicev.movie_ratings.services.UserService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/movies/{movieId}/reviews/create")
    public String showCreationForm(@PathVariable Long movieId, Model model) {
        model.addAttribute("movieId", movieId);
        model.addAttribute("review", new ReviewDto());
        return "review/create";
    }

    @PostMapping("/movies/{movieId}")
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

    @DeleteMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewService.deleteReview(id, userDto.getId());
        return "redirect:/movies/";
    }

    @GetMapping("/movies/{movieId}/reviews/edit")
    public String showEditForm(@PathVariable Long movieId, Model model) {
        model.addAttribute("movieId", movieId);
        model.addAttribute("review", new ReviewDto());
        return "review/edit";
    }

    @PostMapping("/movies/{movieId}/edit")
    public String editReview(@PathVariable Long movieId, @Valid @ModelAttribute ReviewDto reviewDto,
            Model model, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewDto.setUserId(userDto.getId());
        reviewDto.setMovieId(movieId);

        ReviewDto updatedReview = reviewService.updateReview(reviewDto, userDto.getId());
        model.addAttribute("review", updatedReview);
        return "redirect:/movies/" + movieId;
    }

    @GetMapping("/movie/{movieId}")
    public String getReviewsByMovie(@PathVariable Long movieId, Model model) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovie(movieId);
        model.addAttribute("reviews", reviews);
        return "review/list";
    }

    @GetMapping("/user/{userId}")
    public String getReviewsByUser(@PathVariable Long userId, Model model) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        model.addAttribute("reviews", reviews);
        return "review/list";
    }

}
