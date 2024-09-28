package ru.bicev.movie_ratings;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.entitites.Review;
import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;
import ru.bicev.movie_ratings.repositories.MovieRepository;
import ru.bicev.movie_ratings.repositories.ReviewRepository;
import ru.bicev.movie_ratings.repositories.UserRepository;
import ru.bicev.movie_ratings.services.ReviewService;
import ru.bicev.movie_ratings.utils.Role;

public class ReviewServiceTest {

    private String username = "test_username";
    private String email = "test@example.com";
    private String password = "password";
    User user = new User(email, username, password, Role.USER);
    User user2 = new User("test2@example.com", "test2_username", password, Role.USER);

    private String title = "Casablanca";
    private String synopsis = "Test synopsis";
    private String genre = "Drama";
    Movie movie = new Movie(title, synopsis, genre, 1940);
    Movie movie2 = new Movie("12 angry men", "Test2 synopsis", genre, 1938);

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createReview_Success() {
        user.setId(1L);
        movie.setId(1L);

        ReviewDto reviewDto = new ReviewDto("Test comment", 1L, 1L, 4);

        when(userRepository.findById(reviewDto.getUserId())).thenReturn(Optional.of(user));
        when(movieRepository.findById(reviewDto.getMovieId())).thenReturn(Optional.of(movie));

        ReviewDto createdReview = reviewService.createReview(reviewDto);

        assertEquals(reviewDto.getComment(), createdReview.getComment());
        assertEquals(reviewDto.getRating(), createdReview.getRating());
        assertEquals(1L, createdReview.getMovieId());
        assertEquals(1L, createdReview.getUserId());

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void createReview_ThrowsMovieNotFoundException() {
        ReviewDto reviewDto = new ReviewDto("Test comment", 1L, 1L, 4);
        user.setId(1L);
        when(userRepository.findById(reviewDto.getUserId())).thenReturn(Optional.of(user));
        when(movieRepository.findById(reviewDto.getMovieId())).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> reviewService.createReview(reviewDto));
    }

    @Test
    public void createReview_ThrowsUserNotFoundException() {
        ReviewDto reviewDto = new ReviewDto("Test comment", 1L, 1L, 4);
        movie.setId(1L);
        when(movieRepository.findById(reviewDto.getMovieId())).thenReturn(Optional.of(movie));
        when(userRepository.findById(reviewDto.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> reviewService.createReview(reviewDto));
    }

    @Test
    public void deleteReview_Success() {
        Review review = new Review("Test comment", user, movie, 4);
        review.setId(1L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteReview_ThrowsException() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(1L));
    }

    @Test
    public void getReviewsByMovie_Success() {
        Review review = new Review("Test comment", user, movie, 4);
        review.setId(1L);
        Review review2 = new Review("Test2 comment", user2, movie, 3);
        review2.setId(2L);

        when(reviewRepository.findByMovieId(1L)).thenReturn(List.of(review, review2));

        List<ReviewDto> reviewDtos = reviewService.getReviewsByMovie(1L);

        assertEquals("Test comment", reviewDtos.get(0).getComment());
        assertEquals(4, reviewDtos.get(0).getRating());
        assertEquals("Test2 comment", reviewDtos.get(1).getComment());
        assertEquals(3, reviewDtos.get(1).getRating());
    }

    @Test
    public void getReviewsByUser_Success() {
        Review review = new Review("Test comment", user, movie, 4);
        review.setId(1L);
        Review review2 = new Review("Test2 comment", user, movie2, 3);
        review2.setId(2L);

        when(reviewRepository.findByUserId(1L)).thenReturn(List.of(review, review2));

        List<ReviewDto> reviewDtos = reviewService.getReviewsByUser(1L);

        assertEquals("Test comment", reviewDtos.get(0).getComment());
        assertEquals(4, reviewDtos.get(0).getRating());
        assertEquals("Test2 comment", reviewDtos.get(1).getComment());
        assertEquals(3, reviewDtos.get(1).getRating());
    }

    @Test
    public void updateReview_Success() {
        Review review = new Review("Test comment", user, movie, 4);
        review.setId(1L);

        when(reviewRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(review));

        ReviewDto reviewDto = new ReviewDto("Changed comment", 1L, 1L, 2);

        ReviewDto changedReview = reviewService.updateReview(reviewDto);

        assertEquals(reviewDto.getComment(), changedReview.getComment());
        assertEquals(reviewDto.getRating(), changedReview.getRating());

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void updateReview_ThrowsException() {
        when(reviewRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.empty());
        ReviewDto reviewDto = new ReviewDto("Changed comment", 1L, 1L, 2);
        assertThrows(ReviewNotFoundException.class, () -> reviewService.updateReview(reviewDto));
    }

}
