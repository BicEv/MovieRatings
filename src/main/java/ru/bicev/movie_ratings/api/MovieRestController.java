package ru.bicev.movie_ratings.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/create")
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        return new ResponseEntity<MovieDto>(createdMovie, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/title")
    public ResponseEntity<MovieDto> getMovieByTitle(@RequestParam String title) {
        MovieDto foundMovie = movieService.findMovieByTitle(title);
        return new ResponseEntity<MovieDto>(foundMovie, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        MovieDto foundMovie = movieService.findMovieById(id);
        return new ResponseEntity<MovieDto>(foundMovie, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<MovieDto> editMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto updatedMovie = movieService.updateMovie(movieDto);
        return new ResponseEntity<MovieDto>(updatedMovie, HttpStatus.OK);
    }

}
