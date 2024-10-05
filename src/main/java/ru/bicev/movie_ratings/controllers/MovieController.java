package ru.bicev.movie_ratings.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/create")
    public String showCreationForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        return "movie/create";
    }

    @PostMapping("/create")
    public String createMovie(@Valid @ModelAttribute MovieDto movieDto, Model model) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        model.addAttribute("movie", createdMovie);
        return "redirect:/movies";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }

    @GetMapping("/findByTitle")
    public String findMovieByTitle(@RequestParam("title") String title, Model model) {
        MovieDto movieDto = movieService.findMovieByTitle(title);
        model.addAttribute("movie", movieDto);
        return "movie/view";
    }

    @GetMapping("/{id}")
    public String findMovieById(@PathVariable Long id, Model model) {
        MovieDto movieDto = movieService.findMovieById(id);
        model.addAttribute("movie", movieDto);
        return "movie/view";
    }

    @GetMapping("/edit")
    public String showEditMovieForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        return "movie/edit";
    }

    @PostMapping("/edit")
    public String editMovie(@Valid @ModelAttribute MovieDto movieDto, Model model) {
        MovieDto updatedMovie = movieService.updateMovie(movieDto);
        model.addAttribute("movie", updatedMovie);
        return "redirect:/movies";
    }
}
