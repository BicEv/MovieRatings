package ru.bicev.movie_ratings.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling redirecting from base URL
 */
@Controller
public class HomeController {

    /**
     * Redirecting for base URL to the movies list
     * 
     * @return redirection to movies list view
     */
    @GetMapping("/")
    public String redirectToMovies() {
        return "redirect:/movies";
    }

}
