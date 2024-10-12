package ru.bicev.movie_ratings.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ru.bicev.movie_ratings.TestConfig;
import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.controllers.GlobalExceptionHandler;
import ru.bicev.movie_ratings.controllers.ReviewController;
import ru.bicev.movie_ratings.dto.ReviewDto;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.exceptions.ReviewNotFoundException;
import ru.bicev.movie_ratings.services.ReviewService;
import ru.bicev.movie_ratings.services.UserService;

@WebMvcTest(controllers = ReviewController.class)
@Import({ GlobalExceptionHandler.class, TestConfig.class, TestSecurityConfig.class })
public class ReviewErrorPageTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private WebApplicationContext webApplicationContext;

        @MockBean
        private ReviewService reviewService;

        @MockBean
        private UserService userService;

        private ReviewDto reviewDto = new ReviewDto("Test comment", 1L, 1L, 4);
        private UserDto userDto = new UserDto("admin@example.com", "testuser", "password", "USER");

        @BeforeEach
        public void setUp() {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                .apply(springSecurity())
                                .build();
                reviewDto.setId(1L);
                userDto.setId(1L);
        }

        @Test
        public void testReviewNotFoundException_ShouldReturnReviewNotFoundPage() throws Exception {
                Principal mockPrincipal = Mockito.mock(Principal.class);
                when(mockPrincipal.getName()).thenReturn("admin@example.com");
                when(userService.getUserByEmail(anyString())).thenReturn(userDto);
                when(reviewService.updateReview(anyLong(), any(ReviewDto.class), anyLong()))
                                .thenThrow(new ReviewNotFoundException("Review not found"));
                try {
                        mockMvc.perform(post("/movies/1/reviews/edit")
                                        .with(user("test@email.com").roles("USER"))
                                        .param("comment", reviewDto.getComment())
                                        .param("userId", String.valueOf(reviewDto.getUserId()))
                                        .param("movieId", String.valueOf(reviewDto.getMovieId()))
                                        .param("rating", String.valueOf(reviewDto.getRating())))
                                        .andDo(MockMvcResultHandlers.print())
                                        .andExpect(status().isNotFound())
                                        .andExpect(view().name("error/404"))
                                        .andExpect(model().attributeExists("message"))
                                        .andExpect(model().attribute("message", "Not found: Review not found"));
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        @Test
        public void testGenericException_ShouldReturnGeneralErrorPage() throws Exception {
                Principal mockPrincipal = Mockito.mock(Principal.class);
                when(mockPrincipal.getName()).thenReturn("admin@example.com");
                when(userService.getUserByEmail(anyString())).thenReturn(userDto);
                doThrow(new RuntimeException("Unexpected error")).when(reviewService).deleteReview(anyLong(),
                                anyLong());
                try {
                        mockMvc.perform(delete("/movies/1/reviews/1")
                                        .with(user("test@email.com").roles("USER")))
                                        .andDo(MockMvcResultHandlers.print())
                                        .andExpect(status().isInternalServerError())
                                        .andExpect(view().name("error/500"))
                                        .andExpect(model().attributeExists("message"))
                                        .andExpect(model().attribute("message",
                                                        "Internal server error: Unexpected error"));
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

}
