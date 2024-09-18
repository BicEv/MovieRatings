package ru.bicev.movie_ratings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.entitites.Review;
import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.utils.MovieConverter;
import ru.bicev.movie_ratings.utils.ReviewConverter;
import ru.bicev.movie_ratings.utils.Role;
import ru.bicev.movie_ratings.utils.UserConverter;

public class ConvertersTest {

    private final String EMAIL = "john.doe@example.com";
    private final String USERNAME = "User123";
    private final String PASSWORD = "password";

    private final String TITLE = "Brazil";
    private final String SYNOPSIS = "Antiutopian film by Terry Gilliam";
    private final String GENRE = "Antiutopia";
    private final int YEAR = 1984;

    private final String COMMENT = "Real masterpiece";
    private final int RATING = 5;

    @Test
    public void testUserToDto() {
        User user = new User(EMAIL, USERNAME, PASSWORD, Role.USER);
        user.setId(1L);
        user.setReviews(Collections.emptyList());

        UserDto userDto = UserConverter.toDto(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(EMAIL, userDto.getEmail());
        assertEquals(USERNAME, userDto.getUserName());
        assertEquals("USER", userDto.getRole());
        assertTrue(userDto.getReviewIds().isEmpty());
    }

    @Test
    public void testUserDtoToEntity() {
        UserDto userDto = new UserDto(EMAIL, USERNAME, PASSWORD, "USER");

        User user = UserConverter.toEntity(userDto);

        assertEquals(EMAIL, user.getEmail());
        assertEquals(USERNAME, user.getUserName());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    public void testMovieToDto() {
        Movie movie = new Movie(TITLE, SYNOPSIS, GENRE, YEAR);
        Review review = new Review(COMMENT, new User(EMAIL, USERNAME, PASSWORD, Role.ADMIN), movie, RATING);
        review.setId(5L);
        movie.setId(1L);
        movie.setReviews(List.of(review));

        MovieDto movieDto = MovieConverter.toDto(movie);

        assertEquals(TITLE, movieDto.getTitle());
        assertEquals(GENRE, movieDto.getGenre());
        assertEquals(SYNOPSIS, movieDto.getSynopsis());
        assertEquals(YEAR, movieDto.getReleaseYear());
        assertEquals(1L, movieDto.getId());
        assertEquals(5L, movieDto.getReviewIds().get(0));
    }

    @Test
    public void testMovieDtoToEntity() {
        MovieDto movieDto = new MovieDto(TITLE, SYNOPSIS, GENRE, YEAR);

        Movie movie = MovieConverter.toEntity(movieDto);

        assertEquals(TITLE, movie.getTitle());
        assertEquals(SYNOPSIS, movie.getSynopsis());
        assertEquals(GENRE, movie.getGenre());
        assertEquals(YEAR, movie.getReleaseYear());
    }

    @Test
    public void testReviewToDto() {
        User user = new User(EMAIL, USERNAME, PASSWORD, Role.ADMIN);
        user.setId(6L);
        Movie movie = new Movie(TITLE, SYNOPSIS, GENRE, YEAR);
        movie.setId(8L);
        Review review = new Review(COMMENT, user, movie, RATING);

        ReviewDto reviewDto = ReviewConverter.toDto(review);

        assertEquals(COMMENT, reviewDto.getComment());
        assertEquals(RATING, reviewDto.getRating());
        assertEquals(6L, reviewDto.getUserId());
        assertEquals(8L, reviewDto.getMovieId());
    }

    @Test
    public void testReviewDtoToEntity() {
        ReviewDto reviewDto = new ReviewDto(COMMENT, 5L, 10L, RATING);
        User user = new User(EMAIL, USERNAME, PASSWORD, Role.ADMIN);
        user.setId(5L);
        Movie movie = new Movie(TITLE, SYNOPSIS, GENRE, YEAR);
        movie.setId(10L);

        Review review = ReviewConverter.toEntity(reviewDto, user, movie);

        assertEquals(COMMENT, review.getComment());
        assertEquals(RATING, review.getRating());
        assertEquals(user.getId(), review.getUser().getId());
        assertEquals(movie.getId(), review.getMovie().getId());
    }

}
