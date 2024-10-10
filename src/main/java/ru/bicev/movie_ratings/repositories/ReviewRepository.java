package ru.bicev.movie_ratings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.bicev.movie_ratings.entitites.Review;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovieId(Long movieId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByUserIdAndMovieId(Long userId, Long movieId);

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM Review r WHERE r.movie.id = :movieId")
    Double findAverageRatingByMovieId(@Param("movieId") Long movieId);

}
