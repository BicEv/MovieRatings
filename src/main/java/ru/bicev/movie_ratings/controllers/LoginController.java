package ru.bicev.movie_ratings.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for login operation.
 */
@Controller
public class LoginController {

    /**
     * Displays the login form
     * 
     * @return the view for the login form
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

}
