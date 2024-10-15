package ru.bicev.movie_ratings.ControllerTest;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

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
import ru.bicev.movie_ratings.controllers.UserReviewController;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.services.ReviewService;
import ru.bicev.movie_ratings.services.UserService;

@WebMvcTest({ ReviewController.class, UserReviewController.class })
@Import(TestSecurityConfig.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private UserService userService;

    private UserDto userDto = new UserDto("test@email.com", "testuser", "password", "USER");
    private ReviewDto reviewDto = new ReviewDto("Test comment", 1L, 1L, 4);
    private ReviewDto reviewDto2 = new ReviewDto("Test comment 2", 2L, 2L, 5);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto.setId(1L);
        reviewDto.setId(1L);
        reviewDto2.setId(2L);
    }

    @Test
    public void createReviewGet() throws Exception {
        mockMvc.perform(get("/movies/1/reviews/create")
                .with(user("test@email.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("review/create"))
                .andExpect(model().attributeExists("review"));
    }

    @Test
    public void createReviewPost() throws Exception {
        when(reviewService.createReview(any(ReviewDto.class))).thenReturn(reviewDto);
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);

        mockMvc.perform(post("/movies/1/reviews")
                .with(user("test@email.com").roles("USER"))
                .param("comment", reviewDto.getComment())
                .param("userId", String.valueOf(reviewDto.getUserId()))
                .param("movieId", String.valueOf(reviewDto.getMovieId()))
                .param("rating", String.valueOf(reviewDto.getRating())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/1"));
    }

    @Test
    public void deleteReview() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        doNothing().when(reviewService).deleteReview(anyLong(), anyLong());

        mockMvc.perform(delete("/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));

    }

    @Test
    public void editReviewGet() throws Exception {
        when(reviewService.findReviewById(anyLong())).thenReturn(reviewDto);
        mockMvc.perform(get("/movies/1/reviews/1/edit")
                .with(user("test@email.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("review/edit"))
                .andExpect(model().attributeExists("review"));
    }

    @Test
    public void editReviewPut() throws Exception {
        when(reviewService.updateReview(anyLong(), any(ReviewDto.class), anyLong())).thenReturn(reviewDto);
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);

        reviewDto.setUserId(userDto.getId());

        mockMvc.perform(put("/movies/1/reviews/1/edit")
                .with(user("test@email.com").roles("USER"))
                .param("id", String.valueOf(reviewDto.getId()))
                .param("comment", reviewDto.getComment())
                .param("movieId", String.valueOf(reviewDto.getMovieId()))
                .param("rating", String.valueOf(reviewDto.getRating())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/1"));
    }

    @Test
    public void getReviewsByMovie() throws Exception {
        when(reviewService.getReviewsByMovie(anyLong())).thenReturn(List.of(reviewDto, reviewDto2));

        mockMvc.perform(get("/movies/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(view().name("review/list"))
                .andExpect(model().attributeExists("reviews"));

    }

    @Test
    public void getReviewsByUser() throws Exception {
        when(reviewService.getReviewsByUser(anyLong())).thenReturn(List.of(reviewDto, reviewDto2));

        mockMvc.perform(get("/users/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(view().name("review/list"))
                .andExpect(model().attributeExists("reviews"));

    }

    @Test
    public void testHandleNotFoundException() throws Exception {
        doThrow(new ReviewNotFoundException("Review not found")).when(reviewService).findReviewById(anyLong());

        when(userService.getUserByEmail(anyString())).thenReturn(userDto);

        mockMvc.perform(get("/movies/1/reviews/1/edit")
                .with(user("test@email.com").roles("USER")))
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("message", "Not found: Review not found"));
    }

    @Test
    public void testHandleIllegalAccessException() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);
        doThrow(new IllegalAccessException("You are not allowed to do this")).when(reviewService)
                .deleteReview(anyLong(), anyLong());

        mockMvc.perform(delete("/movies/1/reviews/1")
                .with(user("test@email.com").roles("USER")))
                .andExpect(view().name("error/403"))
                .andExpect(model().attribute("message", "Forbidden: You are not allowed to do this"));
    }

    @Test
    public void testHandleException() throws Exception {
        when(reviewService.createReview(any(ReviewDto.class))).thenThrow(new RuntimeException("General error"));
        when(userService.getUserByEmail(anyString())).thenReturn(userDto);

        mockMvc.perform(post("/movies/1/reviews")
                .with(user("test@email.com").roles("USER"))
                .param("comment", reviewDto.getComment())
                .param("userId", String.valueOf(reviewDto.getUserId()))
                .param("movieId", String.valueOf(reviewDto.getMovieId()))
                .param("rating", String.valueOf(reviewDto.getRating())))
                .andExpect(view().name("error/500"))
                .andExpect(model().attribute("message", "Internal server error: General error"));
    }
}
