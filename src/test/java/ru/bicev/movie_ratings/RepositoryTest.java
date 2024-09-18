package ru.bicev.movie_ratings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.entitites.Review;
import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.repositories.MovieRepository;
import ru.bicev.movie_ratings.repositories.ReviewRepository;
import ru.bicev.movie_ratings.repositories.UserRepository;
import ru.bicev.movie_ratings.utils.Role;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private final Movie MOVIE = new Movie("Brazil", "Antiutopia by Terry Gilliam", "Drama", 1984);
    private final User USER = new User("John.Doe@example.com", "User123", "password", Role.USER);
    private final Review REVIEW = new Review("Test comment", USER, MOVIE, 4);

    @Transactional
    @Test
    public void testSaveMovie() {
        Movie savedMovie = movieRepository.save(MOVIE);

        assertNotNull(savedMovie.getId());
        assertEquals(MOVIE.getTitle(), savedMovie.getTitle());
        assertEquals(MOVIE.getSynopsis(), savedMovie.getSynopsis());
        assertEquals(MOVIE.getGenre(), savedMovie.getGenre());
        assertEquals(MOVIE.getReleaseYear(), savedMovie.getReleaseYear());
    }

    @Transactional
    @Test
    public void testFindMovie() {
        Movie savedMovie = movieRepository.save(MOVIE);

        Movie foundMovie = movieRepository.findById(savedMovie.getId()).get();

        assertEquals(MOVIE.getTitle(), foundMovie.getTitle());
        assertEquals(MOVIE.getSynopsis(), foundMovie.getSynopsis());
        assertEquals(MOVIE.getGenre(), foundMovie.getGenre());
        assertEquals(MOVIE.getReleaseYear(), foundMovie.getReleaseYear());
    }

    @Transactional
    @Test
    public void testDeleteMovie() {
        Movie savedMovie = movieRepository.save(MOVIE);
        Optional<Movie> movieBeforeDelete = movieRepository.findById(savedMovie.getId());
        assertTrue(movieBeforeDelete.isPresent());

        movieRepository.delete(savedMovie);

        assertEquals(Optional.empty(), movieRepository.findById(savedMovie.getId()));
    }

    @Transactional
    @Test
    public void testSaveUser() {
        User savedUser = userRepository.save(USER);

        assertNotNull(savedUser.getId());
        assertEquals(USER.getEmail(), savedUser.getEmail());
        assertEquals(USER.getUserName(), savedUser.getUserName());
        assertEquals(USER.getPassword(), savedUser.getPassword());
        assertEquals(USER.getRole(), savedUser.getRole());

    }

    @Transactional
    @Test
    public void testFindUser() {
        User savedUser = userRepository.save(USER);

        User foundUser = userRepository.findById(savedUser.getId()).get();
        assertEquals(USER.getEmail(), foundUser.getEmail());
        assertEquals(USER.getUserName(), foundUser.getUserName());
        assertEquals(USER.getPassword(), foundUser.getPassword());
        assertEquals(USER.getRole(), foundUser.getRole());
    }

    @Transactional
    @Test
    public void testDeleteUser() {
        User savedUser = userRepository.save(USER);
        Optional<User> userBeforeDelete = userRepository.findById(savedUser.getId());
        assertTrue(userBeforeDelete.isPresent());

        userRepository.delete(savedUser);

        assertEquals(Optional.empty(), userRepository.findById(savedUser.getId()));
    }

    @Transactional
    @Test
    public void testSaveReview() {
        Movie savedMovie = movieRepository.save(MOVIE);
        User savedUser = userRepository.save(USER);
        Review review = new Review("Test comment", savedUser, savedMovie, 4);
        Review savedReview = reviewRepository.save(review);

        assertNotNull(savedReview.getId());
        assertEquals(review.getComment(), savedReview.getComment());
        assertEquals(review.getRating(), savedReview.getRating());
    }

    @Transactional
    @Test
    public void testFindReview() {
        Movie savedMovie = movieRepository.save(MOVIE);
        User savedUser = userRepository.save(USER);
        Review review = new Review("Test comment", savedUser, savedMovie, 4);
        Review savedReview = reviewRepository.save(review);

        Review foundReview = reviewRepository.findById(savedReview.getId()).get();
        assertEquals(review.getComment(), foundReview.getComment());
        assertEquals(review.getRating(), foundReview.getRating());
    }

    @Transactional
    @Test
    public void testDeleteReview() {
        Movie savedMovie = movieRepository.save(MOVIE);
        User savedUser = userRepository.save(USER);
        Review review = new Review("Test comment", savedUser, savedMovie, 4);
        Review savedReview = reviewRepository.save(review);

        Optional<Review> reviewBeforeDelete = reviewRepository.findById(savedReview.getId());
        assertTrue(reviewBeforeDelete.isPresent());

        reviewRepository.delete(savedReview);

        assertEquals(Optional.empty(), reviewRepository.findById(savedReview.getId()));
    }

}
