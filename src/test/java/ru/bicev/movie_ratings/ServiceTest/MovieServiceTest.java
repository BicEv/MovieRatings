package ru.bicev.movie_ratings.ServiceTest;

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

import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.exceptions.DuplicateMovieException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.repositories.MovieRepository;
import ru.bicev.movie_ratings.services.MovieService;

public class MovieServiceTest {

    private String title = "Casablanca";
    private String synopsis = "Synopsis";
    private String genre = "Drama";
    private int year = 1940;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createMovie_Success() {
        MovieDto movieDto = new MovieDto(title, synopsis, genre, year);
        Movie movie = new Movie(title, synopsis, genre, year);
        movie.setId(1L);

        when(movieRepository.findByTitle(movieDto.getTitle())).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        MovieDto savedMovieDto = movieService.createMovie(movieDto);

        assertEquals(title, savedMovieDto.getTitle());
        assertEquals(synopsis, savedMovieDto.getSynopsis());
        assertEquals(genre, savedMovieDto.getGenre());
        assertEquals(year, savedMovieDto.getReleaseYear());

        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void createMovie_ShouldThrowException() {
        MovieDto movieDto = new MovieDto(title, synopsis, genre, year);
        Movie movie = new Movie(title, synopsis, genre, year);
        movie.setId(1L);

        when(movieRepository.findByTitle(movieDto.getTitle())).thenReturn(Optional.of(movie));

        assertThrows(DuplicateMovieException.class, () -> movieService.createMovie(movieDto));
    }

    @Test
    public void deleteMovie_Success() {
        Movie movie = new Movie(title, synopsis, genre, year);
        movie.setId(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(1L);

        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteMovie_ShouldThrowException() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(1L));
    }

    @Test
    public void findMovieByTitle_Sucess() {
        Movie movie = new Movie(title, synopsis, genre, year);
        movie.setId(1L);

        when(movieRepository.findByTitle(title)).thenReturn(Optional.of(movie));

        MovieDto foundMovie = movieService.findMovieByTitle(title);

        assertEquals(title, foundMovie.getTitle());
        assertEquals(synopsis, foundMovie.getSynopsis());
        assertEquals(year, foundMovie.getReleaseYear());
        assertEquals(genre, foundMovie.getGenre());
    }

    @Test
    public void findMovieByTitle_ShouldThrowException() {
        when(movieRepository.findByTitle(title)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.findMovieByTitle(title));
    }

    @Test
    public void findMovieById_Sucess() {
        Movie movie = new Movie(title, synopsis, genre, year);
        movie.setId(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieDto foundMovie = movieService.findMovieById(1L);

        assertEquals(title, foundMovie.getTitle());
        assertEquals(synopsis, foundMovie.getSynopsis());
        assertEquals(year, foundMovie.getReleaseYear());
        assertEquals(genre, foundMovie.getGenre());
    }

    @Test
    public void findMovieById_ShouldThrowException() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.findMovieById(1L));
    }

    @Test
    public void updateMovie_Success() {
        MovieDto movieDto = new MovieDto(title, "Updated synopsis", "Updated genre", 2007);
        Movie movie = new Movie(title, synopsis, genre, year);
        movie.setId(1L);

        when(movieRepository.findByTitle(title)).thenReturn(Optional.of(movie));

        MovieDto updatedMovieDto = movieService.updateMovie(movieDto);

        assertEquals("Updated synopsis", updatedMovieDto.getSynopsis());
        assertEquals("Updated genre", updatedMovieDto.getGenre());
        assertEquals(2007, updatedMovieDto.getReleaseYear());
        assertEquals(1L, updatedMovieDto.getId());

        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void updateMovie_ShouldThrowException() {
        MovieDto movieDto = new MovieDto(title, synopsis, genre, year);
        when(movieRepository.findByTitle(title)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(movieDto));
    }

}
