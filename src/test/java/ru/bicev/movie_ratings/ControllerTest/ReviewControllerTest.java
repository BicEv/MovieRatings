package ru.bicev.movie_ratings.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.controllers.ReviewController;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.services.ReviewService;

@WebMvcTest(ReviewController.class)
@Import(TestSecurityConfig.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private ReviewDto reviewDto = new ReviewDto("Test comment", 1L, 1L, 4);
    private ReviewDto reviewDto2 = new ReviewDto("Test comment 2", 2L, 2L, 5);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewDto.setId(1L);
        reviewDto2.setId(2L);
    }

    @Test
    public void createReviewGet() throws Exception {
        mockMvc.perform(get("/reviews/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("review/create"))
                .andExpect(model().attributeExists("review"));
    }

    @Test
    public void createReviewPost() throws Exception {
        when(reviewService.createReview(any(ReviewDto.class))).thenReturn(reviewDto);

        mockMvc.perform(post("/reviews/create")
                .param("comment", reviewDto.getComment())
                .param("userId", String.valueOf(reviewDto.getUserId()))
                .param("movieId", String.valueOf(reviewDto.getMovieId()))
                .param("rating", String.valueOf(reviewDto.getRating())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"));
    }

    @Test
    public void deleteReview() throws Exception {
        doNothing().when(reviewService).deleteReview(anyLong());

        mockMvc.perform(delete("/reviews/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"));

    }

    @Test
    public void editReviewGet() throws Exception {
        mockMvc.perform(get("/reviews/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("review/edit"))
                .andExpect(model().attributeExists("review"));
    }

    @Test
    public void editReviewPost() throws Exception {
        when(reviewService.updateReview(any(ReviewDto.class))).thenReturn(reviewDto);

        mockMvc.perform(post("/reviews/edit")
                .param("comment", reviewDto.getComment())
                .param("userId", String.valueOf(reviewDto.getUserId()))
                .param("movieId", String.valueOf(reviewDto.getMovieId()))
                .param("rating", String.valueOf(reviewDto.getRating())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"));
    }

    @Test
    public void getReviewsByMovie() throws Exception {
        when(reviewService.getReviewsByMovie(anyLong())).thenReturn(List.of(reviewDto, reviewDto2));

        mockMvc.perform(get("/reviews/movie/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("review/list"))
                .andExpect(model().attributeExists("reviews"));

    }

    @Test
    public void getReviewsByUser() throws Exception {
        when(reviewService.getReviewsByUser(anyLong())).thenReturn(List.of(reviewDto, reviewDto2));

        mockMvc.perform(get("/reviews/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("review/list"))
                .andExpect(model().attributeExists("reviews"));

    }

}
