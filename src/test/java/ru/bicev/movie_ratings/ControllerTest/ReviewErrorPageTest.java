package ru.bicev.movie_ratings.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.controllers.GlobalExceptionHandler;
import ru.bicev.movie_ratings.controllers.ReviewController;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.services.ReviewService;

@WebMvcTest(ReviewController.class)
@Import(TestSecurityConfig.class)
public class ReviewErrorPageTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviewService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testReviewNotFoundException_ShouldReturnReviewNotFoundPage() throws Exception {
        when(reviewService.updateReview(any(ReviewDto.class)))
                .thenThrow(new ReviewNotFoundException("Review not found"));

        mockMvc.perform(post("/reviews/edit")
                .param("comment", "Test comment")
                .param("userId", "1")
                .param("movieId", "1")
                .param("rating", "4"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Not found: Review not found"));
    }

    @Test
    public void testGenericException_ShouldReturnGeneralErrorPage() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(reviewService).deleteReview(anyLong());

        mockMvc.perform(delete("/reviews/1/delete"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message",
                        "Internal server error: Unexpected error"));
    }

}
