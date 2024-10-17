package ru.bicev.movie_ratings.api;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.services.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {

    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Get list of all movies sorted by rating desc")
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMoviesSortedByRating() {
        List<MovieDto> movieDtos = movieService.getAllMoviesWithRatingsSortedByRatingDesc();
        return new ResponseEntity<>(movieDtos, HttpStatus.OK);
    }

    @Operation(summary = "Create new movie, only for admins")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        return new ResponseEntity<MovieDto>(createdMovie, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete movie, only for admins")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get movie by title")
    @GetMapping("/title")
    public ResponseEntity<MovieDto> getMovieByTitle(@RequestParam String title) {
        MovieDto foundMovie = movieService.findMovieByTitle(title);
        return new ResponseEntity<MovieDto>(foundMovie, HttpStatus.OK);
    }

    @Operation(summary = "Get movie by id")
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        MovieDto foundMovie = movieService.findMovieById(id);
        return new ResponseEntity<MovieDto>(foundMovie, HttpStatus.OK);
    }

    @Operation(summary = "Edit movie, only for admins")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<MovieDto> editMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto updatedMovie = movieService.updateMovie(movieDto);
        return new ResponseEntity<MovieDto>(updatedMovie, HttpStatus.OK);
    }

}
