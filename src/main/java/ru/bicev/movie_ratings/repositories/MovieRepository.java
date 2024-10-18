package ru.bicev.movie_ratings.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.bicev.movie_ratings.entitites.Movie;

/**
 * Repository interface for managing {@link Movie} entities.
 * Provides methods for common CRUD operations and additional methods
 * to find movies by title and to find all movies sorted by rating desc.
 * 
 * This interface extends {@link JpaRepository}, which provides several standard
 * data access methods.
 * 
 * Annotated with {@link Repository}, it indicates that it's a Spring-managed
 * repository bean, and it is responsible for interacting with the data source.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Finds a movie by its title.
     * 
     * @param title
     * @return an {@link Optional} containing the found movie, or empty
     *         {@link Optional} if the movie was not found
     */
    Optional<Movie> findByTitle(String title);

    /**
     * Finds all movies sorted by rating desc.
     * 
     * @return a list of the movies sorted by its ratings desc.
     */
    @Query("SELECT m FROM Movie m ORDER BY (SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = m.id) DESC")
    List<Movie> findAllMoviesSortedByRating();

}
