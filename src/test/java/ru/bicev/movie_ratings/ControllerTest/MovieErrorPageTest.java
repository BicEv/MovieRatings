package ru.bicev.movie_ratings.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import ru.bicev.movie_ratings.controllers.MovieController;
import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.exceptions.DuplicateMovieException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.services.MovieService;

@WebMvcTest(MovieController.class)
@Import(TestSecurityConfig.class)
public class MovieErrorPageTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MovieController(movieService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testMovieNotFoundException_ShouldReturnMovieNotFoundPage() throws Exception {
        when(movieService.findMovieById(1L)).thenThrow(new MovieNotFoundException("Movie not found"));

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Not found: Movie not found"));
    }

    @Test
    public void testDuplicateMovieException_ShouldReturnDuplicatePage() throws Exception {
        when(movieService.createMovie(any(MovieDto.class)))
                .thenThrow(new DuplicateMovieException("Movie already exists"));

        mockMvc.perform(post("/movies/create")
                .param("title", "Test title")
                .param("synopsis", "Test synosis")
                .param("genre", "Drama")
                .param("releaseYear", "1940"))
                .andExpect(status().isConflict())
                .andExpect(view().name("error/409"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Duplicate: Movie already exists"));
    }

    @Test
    public void testGenericException_ShouldReturnGeneralErrorPage() throws Exception {
        when(movieService.findMovieById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message",
                        "Internal server error: Unexpected error"));
    }

}
