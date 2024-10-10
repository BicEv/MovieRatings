package ru.bicev.movie_ratings.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.bicev.movie_ratings.entitites.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    @Query("SELECT m FROM Movie m ORDER BY (SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = m.id) DESC")
    List<Movie> findAllMoviesSortedByRating();
    
}
