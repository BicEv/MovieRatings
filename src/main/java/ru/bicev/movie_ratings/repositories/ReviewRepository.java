package ru.bicev.movie_ratings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.bicev.movie_ratings.entitites.Review;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Review} entities.
 * Provides methods for common CRUD operations and additional methods
 * to find review by movie id, user id, user and movie id and to find average
 * rating of the movie based on review's ratings.
 * 
 * This interface extends {@link JpaRepository}, which provides several standard
 * data access methods.
 * 
 * Annotated with {@link Repository}, it indicates that it's a Spring-managed
 * repository bean, and it is responsible for interacting with the data source.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Finds the list of the reviews by movie id
     * 
     * @param movieId the movie id
     * @return the list of the reviews associated with the movie
     */
    List<Review> findByMovieId(Long movieId);

    /**
     * Finds the list of the reviews by user id
     * 
     * @param userId the user id
     * @return the list of the reviews associated with the user
     */
    List<Review> findByUserId(Long userId);

    /**
     * Finds the review associated with the user and the movie.
     * 
     * @param userId  the user's id
     * @param movieId the movie's id
     * @return an {@link Optional} containing the found review, or empty
     *         {@link Optional} if the review was not found
     */
    Optional<Review> findByUserIdAndMovieId(Long userId, Long movieId);

    /**
     * Find the average rating of the movie based on the reviews.
     * 
     * @param movieId the movie's id
     * @return the average rating of the movie or {0.0} if there are no reviews
     */
    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM Review r WHERE r.movie.id = :movieId")
    Double findAverageRatingByMovieId(@Param("movieId") Long movieId);

}
