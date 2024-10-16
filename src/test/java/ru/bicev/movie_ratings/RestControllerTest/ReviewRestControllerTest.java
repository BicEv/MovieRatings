package ru.bicev.movie_ratings.RestControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.api.ReviewRestController;
import ru.bicev.movie_ratings.api.UserReviewRestController;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;
import ru.bicev.movie_ratings.services.ReviewService;
import ru.bicev.movie_ratings.services.UserService;

@WebMvcTest({ ReviewRestController.class, UserReviewRestController.class })
@Import(TestSecurityConfig.class)
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewDto.setId(1L);
        userDto.setId(1L);
    }

    private UserDto userDto = new UserDto("test@email.com", "testuser", "password", "USER");
    private ReviewDto reviewDto = new ReviewDto("Test comment", 1L, 1L, 4);
    private List<ReviewDto> reviews = List.of(reviewDto, new ReviewDto("Another comment", 1L, 1L, 5));

    @Test
    public void createReview_ShouldReturnReview() throws Exception {
        when(reviewService.createReview(any(ReviewDto.class))).thenReturn(reviewDto);
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        String reviewToCreate = objectMapper.writeValueAsString(new ReviewDto("Test comment", 1L, 1L, 4));
        String expectedJson = objectMapper.writeValueAsString(reviewDto);

        mockMvc.perform(post("/api/movies/1/reviews")
                .with(user("test@email.com").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewToCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void createReview_ShouldThrowException() throws Exception {
        when(reviewService.createReview(any(ReviewDto.class))).thenThrow(new UserNotFoundException("User not found"));
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        String reviewToCreate = objectMapper.writeValueAsString(new ReviewDto("Test comment", 1L, 1L, 4));

        mockMvc.perform(post("/api/movies/1/reviews")
                .with(user("test@email.com").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewToCreate))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: User not found"));
    }

    @Test
    public void deleteReview_Success() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        doNothing().when(reviewService).deleteReview(1L, 1L);

        mockMvc.perform(delete("/api/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteReview_404() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        doThrow(new ReviewNotFoundException("Review not found")).when(reviewService).deleteReview(1L, 1L);

        mockMvc.perform(delete("/api/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: Review not found"));
    }

    @Test
    public void deleteReview_403() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        doThrow(new IllegalAccessException("You are not allowed to delete this review.")).when(reviewService)
                .deleteReview(1L, 1L);
        mockMvc.perform(delete("/api/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER")))
                .andExpect(status().isForbidden())
                .andExpect(content().string("403 Forbidden: You are not allowed to delete this review."));

    }

    @Test
    public void getReviewsByMovieId_ShouldReturnList() throws Exception {
        when(reviewService.getReviewsByMovie(1L)).thenReturn(reviews);
        String expectedList = objectMapper.writeValueAsString(reviews);

        mockMvc.perform(get("/api/movies/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedList));
    }

    @Test
    public void getReviewsByMovieId_ShouldThrowException() throws Exception {
        when(reviewService.getReviewsByMovie(1L)).thenThrow(new MovieNotFoundException("Movie not found"));

        mockMvc.perform(get("/api/movies/1/reviews"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: Movie not found"));
    }

    @Test
    public void getReviewsByUserId_ShouldReturnList() throws Exception {
        when(reviewService.getReviewsByUser(1L)).thenReturn(reviews);
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        String expectedList = objectMapper.writeValueAsString(reviews);

        mockMvc.perform(get("/api/users/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedList));
    }

    @Test
    public void getReviewsByUserId_ShouldThrowException() throws Exception {
        when(reviewService.getReviewsByUser(1L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/1/reviews"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: User not found"));
    }

    @Test
    public void editReview_ShouldReturnReview() throws Exception {
        when(reviewService.updateReview(anyLong(), any(ReviewDto.class), anyLong())).thenReturn(reviewDto);
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        String reviewToEdit = objectMapper.writeValueAsString(new ReviewDto("Test comment", 1L, 1L, 4));
        String expectedJson = objectMapper.writeValueAsString(reviewDto);

        mockMvc.perform(put("/api/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewToEdit))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void editReview_ShouldThrowException() throws Exception {
        when(reviewService.updateReview(anyLong(), any(ReviewDto.class), anyLong()))
                .thenThrow(new ReviewNotFoundException("Review not found"));
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        String reviewToEdit = objectMapper.writeValueAsString(new ReviewDto("Test comment", 1L, 1L, 4));

        mockMvc.perform(put("/api/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewToEdit))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: Review not found"));

    }

    @Test
    public void getReviewById_ShouldReturnReview() throws Exception {
        when(reviewService.findReviewById(anyLong())).thenReturn(reviewDto);
        String expectedJson = objectMapper.writeValueAsString(reviewDto);

        mockMvc.perform(get("/api/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getReviewById_ShouldThrowException() throws Exception {
        when(reviewService.findReviewById(anyLong())).thenThrow(new ReviewNotFoundException("Review not found"));

        mockMvc.perform(get("/api/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: Review not found"));
    }

}
