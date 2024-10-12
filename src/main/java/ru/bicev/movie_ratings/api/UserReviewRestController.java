package ru.bicev.movie_ratings.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.services.ReviewService;

@RestController
@RequestMapping("/api/users/{userId}/reviews")
public class UserReviewRestController {

    private final ReviewService reviewService;

    public UserReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

}
