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

/**
 * REST Controller responsible for handling user-related operations such as
 * register, updating, password changes and retrieving user information.
 * <p>
 * This controller provides endpoints for public access.
 * </p>
 */
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    /**
     * Constructor to inject dependencies
     * 
     * @param userService service the handles user-related operations
     */
    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers new user in the system
     * 
     * @param userDto the user data to be created, validated using {@link Valid}
     * @return {@link ResponseEntity} containing the created {@link UserDto},
     *         wrapped in HTTP status 201 (Created).
     */
    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createNewUser(userDto);
        return new ResponseEntity<UserDto>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Updates user information
     * 
     * @param userDto the user data to be updated, validated using {@link Valid}
     * @return {@link ResponseEntity} containing updated {@link UserDto}, wrapped in
     *         HTTP status 200 (OK).
     */
    @Operation(summary = "Edit user")
    @PutMapping("/edit")
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userDto);
        return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
    }

    /**
     * Retrieves a user by their ID
     * 
     * @param id the ID of the user to retrieve
     * @return {@link ResponseEntity} containing found {@link UserDto}, wrapped in
     *         HTTP status 200 (OK).
     */
    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto foundUser = userService.getUserById(id);
        return new ResponseEntity<UserDto>(foundUser, HttpStatus.OK);
    }

    /**
     * Retrieves user by their email
     * 
     * @param email the email of the user to retrieve
     * @return {@link ResponseEntity} containing found {@link UserDto}, wrapped in
     *         HTTP status 200 (OK).
     */
    @Operation(summary = "Get user by email")
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        UserDto foundUser = userService.getUserByEmail(email);
        return new ResponseEntity<UserDto>(foundUser, HttpStatus.OK);
    }

    /**
     * Deletes user by their ID
     * 
     * @param id the ID of the user to be deleted
     * @return {@link ResponseEntity} wrapped in HTTP status 204 (No content).
     */
    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    /**
     * Changes password of the user
     * 
     * @param userDto     the user data validated by {@link Valid}
     * @param oldPassword current user's password
     * @param newPassword new user's password
     * @return {@link ResponseEntity} containing updated {@link UserDto}, wrapped in
     *         HTTP status 200 (OK).
     */
    @Operation(summary = "Change password")
    @PutMapping("/change-password")
    public ResponseEntity<UserDto> changePassword(@Valid @RequestBody UserDto userDto,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        UserDto changedUser = userService.changePassword(userDto, oldPassword, newPassword);
        return new ResponseEntity<UserDto>(changedUser, HttpStatus.OK);
    }
}
