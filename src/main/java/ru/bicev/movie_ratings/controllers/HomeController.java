package ru.bicev.movie_ratings.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToMovies() {
        return "redirect:/movies";
    }

}
