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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.api.MovieRestController;
import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.exceptions.DuplicateMovieException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.services.MovieService;

@WebMvcTest(MovieRestController.class)
@Import(TestSecurityConfig.class)
public class MovieRestControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private MovieService movieService;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
                movieDto.setId(1L);
        }

        private MovieDto movieDto = new MovieDto("Test title", "Test synopsis", "Test genre", 2000);
        private ObjectMapper objectMapper = new ObjectMapper();

        @Test  
        @WithMockUser(roles = "ADMIN")      
        public void createMovie_ShouldReturnMovie() throws Exception {
                String createdJson = objectMapper
                                .writeValueAsString(new MovieDto("Test title", "Test synopsis", "Test genre", 2000));
                when(movieService.createMovie(any(MovieDto.class))).thenReturn(movieDto);
                String expectedJson = objectMapper.writeValueAsString(movieDto);

                mockMvc.perform(post("/api/movies/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createdJson))
                                .andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json(expectedJson));
        }

        @Test
        public void createMovie_ShouldThrowException() throws Exception {
                String createdJson = objectMapper
                                .writeValueAsString(new MovieDto("Test title", "Test synopsis", "Test genre", 2000));
                when(movieService.createMovie(any(MovieDto.class)))
                                .thenThrow(new DuplicateMovieException("Movie already exists"));

                mockMvc.perform(post("/api/movies/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createdJson))
                                .andExpect(status().isConflict())
                                .andExpect(content().string("409 Already exists: Movie already exists"));
        }

        @Test
        public void deleteMovie_Success() throws Exception {
                doNothing().when(movieService).deleteMovie(1L);
                mockMvc.perform(delete("/api/movies/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        public void deleteMovie_ShouldThrowException() throws Exception {
                doThrow(new MovieNotFoundException("Movie not found")).when(movieService).deleteMovie(1L);
                mockMvc.perform(delete("/api/movies/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("404 Not found: Movie not found"));
        }

        @Test
        public void getMovieByTitle_ShouldReturnMovie() throws Exception {
                String expectedJson = objectMapper.writeValueAsString(movieDto);
                when(movieService.findMovieByTitle("Test title")).thenReturn(movieDto);
                mockMvc.perform(get("/api/movies/title").param("title", "Test title"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json(expectedJson));
        }

        @Test
        public void getMovieByTitle_ShouldThrowException() throws Exception {
                when(movieService.findMovieByTitle("Test title"))
                                .thenThrow(new MovieNotFoundException("Movie not found"));

                mockMvc.perform(get("/api/movies/title").param("title", "Test title"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("404 Not found: Movie not found"));
        }

        @Test
        public void getMovieById_ShouldReturnMovie() throws Exception {
                when(movieService.findMovieById(1L)).thenReturn(movieDto);
                String expectedJson = objectMapper.writeValueAsString(movieDto);

                mockMvc.perform(get("/api/movies/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json(expectedJson));
        }

        @Test
        public void getMovieById_ShouldThrowException() throws Exception {
                when(movieService.findMovieById(1L)).thenThrow(new MovieNotFoundException("Movie not found"));

                mockMvc.perform(get("/api/movies/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("404 Not found: Movie not found"));
        }

        @Test
        public void editMovie_ShouldReturnMovie() throws Exception {
                when(movieService.updateMovie(any(MovieDto.class))).thenReturn(movieDto);
                String editJson = objectMapper
                                .writeValueAsString(new MovieDto("Test title", "Test synopsis", "Test genre", 2000));
                String expectedJson = objectMapper.writeValueAsString(movieDto);

                mockMvc.perform(put("/api/movies/edit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(editJson))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json(expectedJson));
        }

        @Test
        public void editMovie_ShouldThrowException() throws Exception {
                when(movieService.updateMovie(any(MovieDto.class)))
                                .thenThrow(new MovieNotFoundException("Movie not found"));
                String editJson = objectMapper
                                .writeValueAsString(new MovieDto("Test title", "Test synopsis", "Test genre", 2000));

                mockMvc.perform(put("/api/movies/edit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(editJson))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("404 Not found: Movie not found"));
        }

}
