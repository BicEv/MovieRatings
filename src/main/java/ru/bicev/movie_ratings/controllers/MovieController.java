package ru.bicev.movie_ratings.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.services.MovieService;

/**
 * Controller responsible for handling movie-related operations such as
 * creation,
 * updating, and deleting, and movie information retrieval.
 */
@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    /**
     * Constructor to inject dependencies.
     * 
     * @param movieService service that handles movie operations
     */
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Displays a list of the movies sorted by rating in descending order.
     * 
     * @param model a holder for a model attributes
     * @return the view name for displaying the list of the movies
     */
    @GetMapping
    public String showMovieList(Model model) {
        model.addAttribute("movies", movieService.getAllMoviesWithRatingsSortedByRatingDesc());
        return "movie/list";
    }

    /**
     * Displays the form for crating a new movie.
     * This action is restricted to users with the 'ADMIN' role.
     * 
     * @param model a holder for a model attributes
     * @return the view name for the movie creation dorm
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String showCreationForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        return "movie/create";
    }

    /**
     * Handles the creation of new movie based on the provided movie data.
     * This action is restricted to users with the 'ADMIN' role.
     * 
     * @param movieDto the data transfer object containing movie details
     * @param model    a holder for a model attributes
     * @return a redirect to the movies list after successful operation
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createMovie(@Valid @ModelAttribute MovieDto movieDto, Model model) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        model.addAttribute("movie", createdMovie);
        return "redirect:/movies";
    }

    /**
     * Deletes a movie based on its ID.
     * This action is restricted to users with the 'ADMIN' role.
     * 
     * @param id the ID of the movie to delete
     * @return a redirect to the movies list after successful operation
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }

    /**
     * Retrieves a movie by its title
     * 
     * @param title the title of the movie to retireve
     * @param model a holder for model attributes
     * @return the view name for displaying movie information
     */
    @GetMapping("/findByTitle")
    public String findMovieByTitle(@RequestParam("title") String title, Model model) {
        MovieDto movieDto = movieService.findMovieByTitle(title);
        model.addAttribute("movie", movieDto);
        return "movie/view";
    }

    /**
     * Retrieves a movie by its ID
     * 
     * @param id    the ID of the movie to retrieve
     * @param model a holder for model attributes
     * @return the view name for displaying movie information
     */
    @GetMapping("/{id}")
    public String findMovieById(@PathVariable Long id, Model model) {
        MovieDto movieDto = movieService.findMovieById(id);
        model.addAttribute("movie", movieDto);
        return "movie/view";
    }

    /**
     * Shows the editing form.
     * This action is restricted to users with the 'ADMIN' role.
     * 
     * @param model a holder for model attributes
     * @return the view name for editing form
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit")
    public String showEditMovieForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        return "movie/edit";
    }

    /**
     * Handles the editing of the movie details.
     * This action is restricted to users with the 'ADMIN' role.
     * 
     * @param movieDto a data transfer object containing movie information
     * @param model    a holder for model attributes
     * @return a redirect to the movie list after successful operation
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit")
    public String editMovie(@Valid @ModelAttribute MovieDto movieDto, Model model) {
        MovieDto updatedMovie = movieService.updateMovie(movieDto);
        model.addAttribute("movie", updatedMovie);
        return "redirect:/movies";
    }
}
