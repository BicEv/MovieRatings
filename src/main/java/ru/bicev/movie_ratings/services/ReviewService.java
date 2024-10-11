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

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, MovieRepository movieRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    public ReviewDto findReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: " + reviewId + " is not found"));
        return ReviewConverter.toDto(review);
    }

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

    @Transactional
    public void deleteReview(Long reviewId, Long currentUserId) {
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id: " + reviewId + " is not found"));
        if (!foundReview.getUser().getId().equals(currentUserId)) {
            throw new IllegalAccessException("You are not allowed to edit this review.");
        }
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewDto> getReviewsByMovie(Long movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        return reviews.stream()
                .map(ReviewConverter::toDto)
                .collect(Collectors.toList());

    }

    public List<ReviewDto> getReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(ReviewConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto updateReview(ReviewDto reviewDto, Long currentUserId) {
        Review foundReview = reviewRepository.findByUserIdAndMovieId(reviewDto.getUserId(), reviewDto.getMovieId())
                .orElseThrow(() -> new ReviewNotFoundException("Review not found for this user and movie"));

        if (!foundReview.getUser().getId().equals(currentUserId)) {
            throw new IllegalAccessException("You are not allowed to edit this review.");
        }

        foundReview.setComment(reviewDto.getComment());
        foundReview.setRating(reviewDto.getRating());
        reviewRepository.save(foundReview);
        return ReviewConverter.toDto(foundReview);
    }

}
