package ru.bicev.movie_ratings.api;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.services.ReviewService;
import ru.bicev.movie_ratings.services.UserService;

@RestController
@RequestMapping("/api/movies/{movieId}/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewRestController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @Operation(summary = "Create review")
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@PathVariable Long movieId, @Valid @RequestBody ReviewDto reviewDto,
            Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewDto.setUserId(userDto.getId());
        reviewDto.setMovieId(movieId);

        ReviewDto createdReview = reviewService.createReview(reviewDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete review by id")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewService.deleteReview(reviewId, userDto.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get review by id")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long reviewId, @PathVariable Long movieId) {
        ReviewDto review = reviewService.findReviewById(reviewId);

        if (!review.getMovieId().equals(movieId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @Operation(summary = "Get list of reviews by movie id")
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByMovieId(@PathVariable Long movieId) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovie(movieId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @Operation(summary = "Edit review by id")
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> editReview(@PathVariable Long reviewId, @PathVariable Long movieId,
            @Valid @RequestBody ReviewDto reviewDto,
            Principal principal) {

        String email = principal.getName();
        Long userId = userService.getUserByEmail(email).getId();

        ReviewDto updatedReview = reviewService.updateReview(reviewId, reviewDto, userId);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

}
