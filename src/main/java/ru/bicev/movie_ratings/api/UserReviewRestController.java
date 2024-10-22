package ru.bicev.movie_ratings.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.services.ReviewService;

/**
 * REST Controller responsible for handling review-related operations based on
 * user.
 * <p>
 * This controller provides endpoint for public method - get the reviews by
 * their author.
 * </p>
 */
@RestController
@RequestMapping("/api/users/{userId}/reviews")
public class UserReviewRestController {

    private final ReviewService reviewService;

    /**
     * Contstructor to inject dependencies.
     * 
     * @param reviewService service that handles review-related operations
     */
    public UserReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Retrieves a list of reviews by user ID
     * 
     * @param userId the ID of the user created the reviews
     * @return {@link ResponseEntity} containing list of {@link ReviewDto}, wrapped
     *         in HTTP status 200 (OK).
     */
    @Operation(summary = "Get list of reviews by user id")
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

}
