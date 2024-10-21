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
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.services.UserService;

/**
 * Controller responsible for handling user-related actions such as
 * registration,
 * password changes, and user information retrieval.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor to inject dependencies.
     * 
     * @param userService service that handles user operations.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the registration form for a new user.
     * 
     * @param model a holder for modle attributes
     * @return the view name for the registration form
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/register";
    }

    /**
     * Creates a new user based on the provided user data
     * 
     * @param userDto the data transfer object containing user information
     * @param model   a holder for model attributes
     * @return the view name for displaying the newly created user
     */
    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute UserDto userDto, Model model) {
        UserDto createdUser = userService.createNewUser(userDto);
        model.addAttribute("user", createdUser);
        return "user/view";
    }

    /**
     * Displays the change password form
     * 
     * @param model a holder for model attributes
     * @return the view name for the change password form
     */
    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/change-password";
    }

    /**
     * Handles the password change for a user.
     * 
     * @param userDto     the data transfer object containing user information
     * @param oldPassword the user's current password
     * @param newPassword the user's new password
     * @param model       a holder for model attributes
     * @return the view name for displaying the updated user information
     */
    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute UserDto userDto,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, Model model) {
        UserDto updatedUser = userService.changePassword(userDto, oldPassword, newPassword);
        model.addAttribute("user", updatedUser);
        return "user/view";
    }

    /**
     * Retrieves a user by their email and displays their information
     * 
     * @param email the email of the user
     * @param model a holder for model attributes
     * @return the view name for displaying the user information
     */
    @GetMapping("/findByEmail")
    public String getUserByEmail(@RequestParam("email") String email, Model model) {
        UserDto userDto = userService.getUserByEmail(email);
        model.addAttribute("user", userDto);
        return "user/view";
    }

    /**
     * Retrieves a user by their ID and displays their information
     * 
     * @param id    the ID of the user
     * @param model a holder for model attributes
     * @return the view name for displaying the user information
     */
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUserById(id);
        model.addAttribute("user", userDto);
        return "user/view";
    }

    /**
     * Displays the form for editing user information
     * 
     * @param model a holder for model attributes
     * @return the view name for the edit form
     */
    @GetMapping("/edit")
    public String showEditUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/edit";
    }

    /**
     * Updates user information based on provided data
     * 
     * @param userDto the data transfer object containing updated user information
     * @param model   a holer for model attributes
     * @return the view name for displaying the updated user information
     */
    @PostMapping("/edit")
    public String editUser(@Valid @ModelAttribute UserDto userDto, Model model) {
        UserDto updatedUser = userService.updateUser(userDto);
        model.addAttribute("user", updatedUser);
        return "user/view";
    }

    /**
     * Deletes user based on their ID.
     * 
     * @param id the ID of the user to delete
     * @return a redirection to the user list view
     */
    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

}
