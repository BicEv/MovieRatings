package ru.bicev.movie_ratings.controllers;

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
import ru.bicev.movie_ratings.services.ReviewService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/create")
    public String showCreationForm(Model model) {
        model.addAttribute("review", new ReviewDto());
        return "review/create";
    }

    @PostMapping("/create")
    public String createReview(@Valid @ModelAttribute ReviewDto reviewDto, Model model) {
        ReviewDto createdReview = reviewService.createReview(reviewDto);
        model.addAttribute("review", createdReview);
        return "redirect:/reviews";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }

    @GetMapping("/edit")
    public String showEditReviewForm(Model model) {
        model.addAttribute("review", new ReviewDto());
        return "review/edit";
    }

    @PostMapping("/edit")
    public String editReview(@Valid @ModelAttribute ReviewDto reviewDto, Model model) {
        ReviewDto updatedReview = reviewService.updateReview(reviewDto);
        model.addAttribute("review", updatedReview);
        return "redirect:/reviews";
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
