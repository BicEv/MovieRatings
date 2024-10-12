package ru.bicev.movie_ratings.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.services.ReviewService;

@Controller
@RequestMapping("/users/{userId}/reviews")
public class UserReviewController {

    private final ReviewService reviewService;

    public UserReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String getReviewsByUser(@PathVariable Long userId, Model model) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        model.addAttribute("reviews", reviews);
        return "review/list";
    }

}
