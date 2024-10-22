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

/**
 * REST Controller responsible for handling review-related operations such as
 * creating, updating, deleting, and retrieving review information.
 * <p>
 * This controller provides endpoints for public access. *
 * </p>
 */
@RestController
@RequestMapping("/api/movies/{movieId}/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final UserService userService;

    /**
     * Constructor to inject dependencies.
     * 
     * @param reviewService service that handles review-related operations
     * @param userService   service that handles user-related operations
     */
    public ReviewRestController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    /**
     * Creates a new review entry in the system.
     * 
     * @param movieId   the ID of the movie to be reviewed
     * @param reviewDto data transfer object containing review details validated by
     *                  {@link Valid}
     * @param principal wrapper for the current user
     * @return {@link ResponseEntity} containing created {@link ReviewDto}, wrapped
     *         in HTTP status 201 (Created).
     */
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

    /**
     * Deletes review by its ID
     * 
     * @param reviewId  the ID of a review to be deleted
     * @param principal wrapper for current user
     * @return {@link ResponseEntity} wrapped in HTTP status 204 (No content).
     */
    @Operation(summary = "Delete review by id")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewService.deleteReview(reviewId, userDto.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves a review by its ID
     * 
     * @param reviewId the ID of the review to retrieve
     * @param movieId  the ID of the reviewed movie
     * @return {@link ResponseEntity} containing found {@link ReviewDto}, wrapped in
     *         HTTP status 200(OK) or {@link ResponseEntity} wrapped in HTTP status
     *         404 (Not found) if a review with the given ID was not found
     * 
     */
    @Operation(summary = "Get review by id")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long reviewId, @PathVariable Long movieId) {
        ReviewDto review = reviewService.findReviewById(reviewId);

        if (!review.getMovieId().equals(movieId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    /**
     * Retrieves a list of review by movie ID
     * 
     * @param movieId the ID of the movie
     * @return {@link ResponseEntity} containing list of {@link ReviewDto}, wrapped
     *         in HTTP status 200 (OK).
     */
    @Operation(summary = "Get list of reviews by movie id")
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByMovieId(@PathVariable Long movieId) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovie(movieId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Updates the review data
     * 
     * @param reviewId  the ID of the review to be updated
     * @param movieId   the ID of the reviewed movie
     * @param reviewDto data transfer object containing updated data validated by
     *                  {@link Valid}
     * @param principal the wrapper for the current user
     * @return {@link ResponseEntity} containing updated {@link ReviewDto}, wrapped
     *         in HTTP status 200 (OK).
     */
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
