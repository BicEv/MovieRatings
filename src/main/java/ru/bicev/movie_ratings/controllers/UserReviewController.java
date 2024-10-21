package ru.bicev.movie_ratings.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.services.ReviewService;

/**
 * Controller responsible for handling review-related operations based on user
 * such as review information retrieval
 */
@Controller
@RequestMapping("/users/{userId}/reviews")
public class UserReviewController {

    private final ReviewService reviewService;

    /**
     * Constructor to inject dependencies
     * 
     * @param reviewService a service that handles review operations
     */
    public UserReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Retrieves a list of reviews based on users ID
     * 
     * @param userId the ID of the user created reviews
     * @param model  a holder for model attributes
     * @return the view name for displaying review list
     */
    @GetMapping
    public String getReviewsByUser(@PathVariable Long userId, Model model) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        model.addAttribute("reviews", reviews);
        return "review/list";
    }

}
