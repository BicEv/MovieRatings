package ru.bicev.movie_ratings.utils;

import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.entitites.Review;
import ru.bicev.movie_ratings.entitites.User;

/**
 * Utility class for converting between Review and ReviewDto objects
 * <p>
 * This class provides methods to convert a Review entity to a ReviewDto and
 * vice versa,
 * faciliating the transfer of review data between different layers of the
 * application.
 * </p>
 */
public class ReviewConverter {

    /**
     * Converts a Review entity to a ReviewDto
     * 
     * @param review the Review entity to be converted
     * @return a ReviewDto representing entity or {@code null} if the input is null
     */
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

    /**
     * Converts a ReviewDto to a Review entity
     * 
     * @param reviewDto the ReviewDto to be converted
     * @param user the User entity to associate with the Review
     * @param movie the Movie entity to associate with the Review
     * @return a Review entity based on the provided ReviewDto, user and movie
     */
    public static Review toEntity(ReviewDto reviewDto, User user, Movie movie) {
        Review review = new Review();

        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        review.setUser(user);
        review.setMovie(movie);

        return review;
    }
}
