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

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/register";
    }

    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute UserDto userDto, Model model) {
        UserDto createdUser = userService.createNewUser(userDto);
        model.addAttribute("user", createdUser);
        return "user/view";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute UserDto userDto,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, Model model) {
        UserDto updatedUser = userService.changePassword(userDto, oldPassword, newPassword);
        model.addAttribute("user", updatedUser);
        return "user/view";
    }

    @GetMapping("/findByEmail")
    public String getUserByEmail(@RequestParam("email") String email, Model model) {
        UserDto userDto = userService.getUserByEmail(email);
        model.addAttribute("user", userDto);
        return "user/view";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUserById(id);
        model.addAttribute("user", userDto);
        return "user/view";
    }

    @GetMapping("/edit")
    public String showEditUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/edit";
    }

    @PostMapping("/edit")
    public String editUser(@Valid @ModelAttribute UserDto userDto, Model model) {
        UserDto updatedUser = userService.updateUser(userDto);
        model.addAttribute("user", updatedUser);
        return "user/view";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

}
