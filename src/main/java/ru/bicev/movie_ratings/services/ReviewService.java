package ru.bicev.movie_ratings.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.entitites.Review;
import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;
import ru.bicev.movie_ratings.repositories.MovieRepository;
import ru.bicev.movie_ratings.repositories.ReviewRepository;
import ru.bicev.movie_ratings.repositories.UserRepository;
import ru.bicev.movie_ratings.utils.ReviewConverter;

/**
 * Service class for managing review-related operations.
 * Handles creation, updating, retrieving and deleting reviews.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for ReviewService, initializes required components.
     * 
     * @param reviewRepository the repository for review data management
     * @param movieRepository  the repository for movie data management
     * @param userRepository   the repository for user data management
     */
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, MovieRepository movieRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the review by its ID
     * 
     * @param reviewId the ID of review to retrieve
     * @return {@link ReviewDto} ReviewDto corresponding to the review found by ID
     * @throws ReviewNotFoundException if no review with the given ID exists
     */
    public ReviewDto findReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: " + reviewId + " is not found"));
        return ReviewConverter.toDto(review);
    }

    /**
     * Creates a new review
     * 
     * @param reviewDto the data transfer object {@link ReviewDto} representing review to create
     * @return {@link ReviewDto} created ReviewDto
     * @throws UserNotFoundException  if no user with given in the reviewDto ID
     *                                exists
     * @throws MovieNotFoundException if no movie with given in the reviewDto ID
     *                                exists
     */
    @Transactional
    public ReviewDto createReview(ReviewDto reviewDto) {
        User user = userRepository.findById(reviewDto.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User with id: " + reviewDto.getUserId() + " is not found"));
        Movie movie = movieRepository.findById(reviewDto.getMovieId())
                .orElseThrow(
                        () -> new MovieNotFoundException("Movie with id: " + reviewDto.getMovieId() + " is not found"));
        Review review = ReviewConverter.toEntity(reviewDto, user, movie);
        reviewRepository.save(review);
        return ReviewConverter.toDto(review);
    }

    /**
     * Deletes review by its ID
     * 
     * @param reviewId      the ID of the review to delete
     * @param currentUserId the ID of the user deleting the review
     * @throws ReviewNotFoundException if no review with the given ID exists
     * @throws IllegalAccessException  if currentUserId is not match with the ID in
     *                                 review to delete
     */
    @Transactional
    public void deleteReview(Long reviewId, Long currentUserId) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: " + reviewId + " is not found"));
        if (!foundReview.getUser().getId().equals(currentUserId)) {
            throw new IllegalAccessException("You are not allowed to edit this review.");
        }
        reviewRepository.deleteById(reviewId);
    }

    /**
     * Retrieves a list of the reviews by movie ID
     * 
     * @param movieId the ID of the movie
     * @return a List of {@link ReviewDto} reviews associated with the given movie
     *         ID
     */
    public List<ReviewDto> getReviewsByMovie(Long movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        return reviews.stream()
                .map(ReviewConverter::toDto)
                .collect(Collectors.toList());

    }

    /**
     * Retrieves a list of the reviews by user ID
     * 
     * @param userId the ID of the user
     * @return a List of {@link ReviewDto} reviews associated with the given use ID
     */
    public List<ReviewDto> getReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(ReviewConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the review details
     * 
     * @param reviewId      the ID of the review to update
     * @param reviewDto     the data transfer object {@link ReviewDto} representing
     *                      the updated review
     * @param currentUserId the ID of the current user
     * @return {@link ReviewDto} updated ReviewDto
     * @throws ReviewNotFoundException if no review with the given ID exists
     * @throws IllegalAccessException  if currentUserId is no match with the user ID
     *                                 in the retrieved review
     */
    @Transactional
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto, Long currentUserId) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        if (!foundReview.getUser().getId().equals(currentUserId)) {
            throw new IllegalAccessException("You are not allowed to edit this review.");
        }

        foundReview.setComment(reviewDto.getComment());
        foundReview.setRating(reviewDto.getRating());

        reviewRepository.save(foundReview);
        return ReviewConverter.toDto(foundReview);
    }

}
