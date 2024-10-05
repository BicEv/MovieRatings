package ru.bicev.movie_ratings.RestControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;
import ru.bicev.movie_ratings.services.ReviewService;

@WebMvcTest(ReviewRestController.class)
@Import(TestSecurityConfig.class)
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewDto.setId(1L);
    }

    private ReviewDto reviewDto = new ReviewDto("Test comment", 1L, 1L, 4);
    private List<ReviewDto> reviews = List.of(reviewDto, new ReviewDto("Another comment", 1L, 1L, 5));

    @Test
    public void createReview_ShouldReturnReview() throws Exception {
        when(reviewService.createReview(any(ReviewDto.class))).thenReturn(reviewDto);
        String reviewToCreate = objectMapper.writeValueAsString(new ReviewDto("Test comment", 1L, 1L, 4));
        String expectedJson = objectMapper.writeValueAsString(reviewDto);

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewToCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void createReview_ShouldThrowException() throws Exception {
        when(reviewService.createReview(any(ReviewDto.class))).thenThrow(new UserNotFoundException("User not found"));
        String reviewToCreate = objectMapper.writeValueAsString(new ReviewDto("Test comment", 1L, 1L, 4));

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewToCreate))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: User not found"));
    }

    @Test
    public void deleteReview_Success() throws Exception {
        doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteReview_ShouldThrowException() throws Exception {
        doThrow(new ReviewNotFoundException("Review not found")).when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: Review not found"));
    }

    @Test
    public void getReviewsByMovieId_ShouldReturnList() throws Exception {
        when(reviewService.getReviewsByMovie(1L)).thenReturn(reviews);
        String expectedList = objectMapper.writeValueAsString(reviews);

        mockMvc.perform(get("/api/reviews/movie/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedList));
    }

    @Test
    public void getReviewsByMovieId_ShouldThrowException() throws Exception {
        when(reviewService.getReviewsByMovie(1L)).thenThrow(new MovieNotFoundException("Movie not found"));

        mockMvc.perform(get("/api/reviews/movie/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: Movie not found"));
    }

    @Test
    public void getReviewsByUserId_ShouldReturnList() throws Exception {
        when(reviewService.getReviewsByUser(1L)).thenReturn(reviews);
        String expectedList = objectMapper.writeValueAsString(reviews);

        mockMvc.perform(get("/api/reviews/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedList));
    }

    @Test
    public void getReviewsByUserId_ShouldThrowException() throws Exception {
        when(reviewService.getReviewsByUser(1L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/reviews/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: User not found"));
    }

    @Test
    public void editReview_ShouldReturnReview() throws Exception {
        when(reviewService.updateReview(any(ReviewDto.class))).thenReturn(reviewDto);
        String reviewToEdit = objectMapper.writeValueAsString(new ReviewDto("Test comment", 1L, 1L, 4));
        String expectedJson = objectMapper.writeValueAsString(reviewDto);

        mockMvc.perform(put("/api/reviews/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewToEdit))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void editReview_ShouldThrowException() throws Exception {
        when(reviewService.updateReview(any(ReviewDto.class)))
                .thenThrow(new ReviewNotFoundException("Review not found"));
        String reviewToEdit = objectMapper.writeValueAsString(new ReviewDto("Test comment", 1L, 1L, 4));

        mockMvc.perform(put("/api/reviews/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewToEdit))
                .andExpect(status().isNotFound())
                .andExpect(content().string("404 Not found: Review not found"));

    }

}
