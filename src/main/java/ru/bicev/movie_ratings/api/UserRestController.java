package ru.bicev.movie_ratings.api;

import org.springframework.beans.factory.annotation.Autowired;
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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createNewUser(userDto);
        return new ResponseEntity<UserDto>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit user")
    @PutMapping("/edit")
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userDto);
        return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto foundUser = userService.getUserById(id);
        return new ResponseEntity<UserDto>(foundUser, HttpStatus.OK);
    }

    @Operation(summary = "Get user by email")
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        UserDto foundUser = userService.getUserByEmail(email);
        return new ResponseEntity<UserDto>(foundUser, HttpStatus.OK);
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @Operation(summary = "Change password")
    @PutMapping("/change-password")
    public ResponseEntity<UserDto> changePassword(@Valid @RequestBody UserDto userDto,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        UserDto changedUser = userService.changePassword(userDto, oldPassword, newPassword);
        return new ResponseEntity<UserDto>(changedUser, HttpStatus.OK);
    }
}
