package ru.bicev.movie_ratings.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.beans.Transient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.controllers.MovieController;
import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.services.MovieService;

@WebMvcTest(MovieController.class)
@Import(TestSecurityConfig.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    private String title = "Test title";
    private String synopsis = "Test synopsis";
    private String genre = "Test genre";
    private int year = 1940;
    private MovieDto movieDto = new MovieDto(title, synopsis, genre, year);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createMovieGet() throws Exception {
        mockMvc.perform(get("/movies/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie/create"))
                .andExpect(model().attributeExists("movie"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createMoviePost() throws Exception {
        when(movieService.createMovie(any(MovieDto.class))).thenReturn(movieDto);

        mockMvc.perform(post("/movies/create")
                .param("title", movieDto.getTitle())
                .param("synopsis", movieDto.getSynopsis())
                .param("genre", movieDto.getGenre())
                .param("releaseYear", String.valueOf(movieDto.getReleaseYear())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));

    }

    @Test
    public void deleteMovie() throws Exception {
        doNothing().when(movieService).deleteMovie(1L);

        mockMvc.perform(delete("/movies/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));
    }

    @Test
    public void getMovieByTitle() throws Exception {
        when(movieService.findMovieByTitle(anyString())).thenReturn(movieDto);

        mockMvc.perform(get("/movies/findByTitle").param("title", "Test title"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie/view"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attribute("movie", movieDto));

    }

    @Test
    public void getMovieById() throws Exception {
        when(movieService.findMovieById(anyLong())).thenReturn(movieDto);

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie/view"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attribute("movie", movieDto));

    }

    @Test
    public void editMovieGet() throws Exception {
        mockMvc.perform(get("/movies/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie/edit"))
                .andExpect(model().attributeExists("movie"));
    }

    @Test
    public void editMoviePost() throws Exception {
        when(movieService.updateMovie(any(MovieDto.class))).thenReturn(movieDto);

        mockMvc.perform(post("/movies/edit")
                .param("title", movieDto.getTitle())
                .param("synopsis", movieDto.getSynopsis())
                .param("genre", movieDto.getGenre())
                .param("releaseYear", String.valueOf(movieDto.getReleaseYear())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));
    }
}
