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
import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.services.MovieService;

/**
 * REST Controller responsible for handling movie-related operations such as
 * creation,
 * updating, and deleting, and movie information retrieval.
 * <p>
 * This controller provides endpoints for both public and admin-level access.
 * Public users can retrieve information about movies, while admins can perform
 * movie creation and modification operations.
 * </p>
 */
@RestController
@RequestMapping("/api/movies")
public class MovieRestController {

    private final MovieService movieService;

    /**
     * Constructor to inject dependencies.
     * 
     * @param movieService service that handles movie-related operations
     */
    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Retrieves a list of all movies, sorted by its ratings in descending order.
     * 
     * @return {@link ResponseEntity} containing a list of {@link MovieDto} sorted
     *         by rating in descending order, wrapped in HTTP status 200 (OK).
     */
    @Operation(summary = "Get list of all movies sorted by rating desc")
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMoviesSortedByRating() {
        List<MovieDto> movieDtos = movieService.getAllMoviesWithRatingsSortedByRatingDesc();
        return new ResponseEntity<>(movieDtos, HttpStatus.OK);
    }

    /**
     * Creates a new movie entry int the system. This operation is restricted to
     * admin users.
     * 
     * @param movieDto the movie data to be created, validated using {@link Valid}
     * @return {@link ResponseEntity} containing the created {@link MovieDto},
     *         wrapped in HTTP status 201 (Created).
     * @throws AccessDeniedException if the user does not have the ADMIN role.
     */
    @Operation(summary = "Create new movie, only for admins")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        return new ResponseEntity<MovieDto>(createdMovie, HttpStatus.CREATED);
    }

    /**
     * Deletes a movie entry in the system. This operation is restricted to admin
     * users.
     * 
     * @param id the ID of the movie to delete
     * @return {@link ResponseEntity} wrapped in HTTP status 204 {No content}.
     * @throws AccessDeniedException if the user does not have ADMIN role.
     */
    @Operation(summary = "Delete movie, only for admins")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves a movie by its title
     * 
     * @param title the title of the movie to retrieve
     * @return {@link ResponseEntity} containing found {@link MovieDto} wrapped in
     *         HTTP status 200 (OK).
     */
    @Operation(summary = "Get movie by title")
    @GetMapping("/title")
    public ResponseEntity<MovieDto> getMovieByTitle(@RequestParam String title) {
        MovieDto foundMovie = movieService.findMovieByTitle(title);
        return new ResponseEntity<MovieDto>(foundMovie, HttpStatus.OK);
    }

    /**
     * Retrieves a movie by its ID
     * 
     * @param id the ID of the movie to retrieve
     * @return {@link ResponseEntity} containing found {@link MovieDto} wrapped in
     *         HTTP status 200 (OK).
     */
    @Operation(summary = "Get movie by id")
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        MovieDto foundMovie = movieService.findMovieById(id);
        return new ResponseEntity<MovieDto>(foundMovie, HttpStatus.OK);
    }

    /**
     * Updates the data in the movie. This operation is restricted to admin users.
     * 
     * @param movieDto a data transfer object representing updated movie
     * @return {@link ResponseEntity} containing updated {@link MovieDto} wrapped in
     *         HTTP status 200 (OK).
     */
    @Operation(summary = "Edit movie, only for admins")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<MovieDto> editMovie(@Valid @RequestBody MovieDto movieDto) {
        MovieDto updatedMovie = movieService.updateMovie(movieDto);
        return new ResponseEntity<MovieDto>(updatedMovie, HttpStatus.OK);
    }

}
