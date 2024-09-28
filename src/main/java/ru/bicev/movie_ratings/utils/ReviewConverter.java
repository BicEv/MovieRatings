package ru.bicev.movie_ratings.utils;

import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.entitites.Review;
import ru.bicev.movie_ratings.entitites.User;

public class ReviewConverter {

    public static ReviewDto toDto(Review review) {
        if (review == null) {
            return null;
        }
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(review.getId());
        reviewDto.setComment(review.getComment());
        reviewDto.setRating(review.getRating());
        reviewDto.setMovieId(review.getMovie().getId());
        reviewDto.setUserId(review.getUser().getId());

        return reviewDto;
    }

    public static Review toEntity(ReviewDto reviewDto, User user, Movie movie) {
        Review review = new Review();

        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        review.setUser(user);
        review.setMovie(movie);

        return review;
    }
}
