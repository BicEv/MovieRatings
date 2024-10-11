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

import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.services.ReviewService;
import ru.bicev.movie_ratings.services.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewRestController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping("/movies/{movieId}")
    public ResponseEntity<ReviewDto> createReview(@PathVariable Long movieId, @Valid @RequestBody ReviewDto reviewDto,
            Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewDto.setUserId(userDto.getId());
        reviewDto.setMovieId(movieId);

        ReviewDto createdReview = reviewService.createReview(reviewDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewService.deleteReview(id, userDto.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByMovieId(@PathVariable Long movieId) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovie(movieId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PutMapping("/movies/{movieId}/reviews")
    public ResponseEntity<ReviewDto> editReview(@PathVariable Long movieId, @Valid @RequestBody ReviewDto reviewDto,
            Principal principal) {
        String email = principal.getName();
        UserDto userDto = userService.getUserByEmail(email);

        reviewDto.setUserId(userDto.getId());
        reviewDto.setMovieId(movieId);

        ReviewDto updatedReview = reviewService.updateReview(reviewDto, userDto.getId());
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

}
