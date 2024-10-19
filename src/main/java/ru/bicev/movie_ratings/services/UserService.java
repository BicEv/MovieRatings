package ru.bicev.movie_ratings.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.exceptions.DuplicateUserException;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;
import ru.bicev.movie_ratings.repositories.UserRepository;
import ru.bicev.movie_ratings.utils.Role;
import ru.bicev.movie_ratings.utils.UserConverter;

/**
 * Service class for managing user-related operations.
 * Handles user registration, password management, and user detais retrieval.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for UserService, initializes required components.
     * 
     * @param userRepository  the repository for user data management
     * @param passwordEncoder the password encoder for secure password handling
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user.
     * 
     * @param userDto the data transfer object {@link UserDto} representing the new
     *                user
     * @return {@link UserDto} created UserDto with filled-in fields (e.g., ID)
     * @throws DuplicateUserException if a user with the same email or username
     *                                already exists
     */
    @Transactional
    public UserDto createNewUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateUserException("User with this email already exists.");
        }
        if (userRepository.findByUserName(userDto.getUserName()).isPresent()) {
            throw new DuplicateUserException("User with this username already exists.");
        }
        userDto.setRole("USER");
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(UserConverter.toEntity(userDto));

        return UserConverter.toDto(savedUser);
    }

    /**
     * Changes the user's password
     * 
     * @param userDto     the data transfer object {@link UserDto} representing the
     *                    user
     * @param oldPassword the old password for validation
     * @param newPassword the new paswword to be set
     * @return {@link UserDto} updated UserDto with the new password
     * @throws UserNotFoundException  if the user is not found by email
     * @throws IllegalAccessException if the old password does not match
     */
    @Transactional
    public UserDto changePassword(UserDto userDto, String oldPassword, String newPassword) {
        User foundUser = userRepository.findByEmail(userDto.getEmail()).orElseThrow(
                () -> new UserNotFoundException("User with email: " + userDto.getEmail() + " is not found"));
        if (!passwordEncoder.matches(oldPassword, foundUser.getPassword())) {
            throw new IllegalAccessException("Ivalid password.");
        }
        foundUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(foundUser);
        return UserConverter.toDto(foundUser);
    }

    /**
     * Retrieves a user by email
     * 
     * @param email the email of the user to retrieve
     * @return {@link UserDto} UserDto corresponding to the user found by email
     * @throws UserNotFoundException if no user with the given email exists
     */
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " is not found"));
        return UserConverter.toDto(user);
    }

    /**
     * Retrieves a user by ID
     * 
     * @param id the ID of the user to retrieve
     * @return {@link UserDto} UserDto corresponding to the user found by ID
     * @throws UserNotFoundException if no user with the given ID exists
     */
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " is not found"));
        return UserConverter.toDto(user);
    }

    /**
     * Updates the user details
     * 
     * @param userDto the data transfer object {@link UserDto} representing the
     *                updated user
     * @return {@link UserDto} updated UserDto
     * @throws UserNotFoundException if no user with given email exists
     */
    @Transactional
    public UserDto updateUser(UserDto userDto) {
        User foundUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException("User with email: " + userDto.getEmail() + " is not found"));
        foundUser.setUserName(userDto.getUserName());
        foundUser.setRole(Role.fromString(userDto.getRole()));
        userRepository.save(foundUser);

        return UserConverter.toDto(foundUser);
    }

    /**
     * Deletes a user by their id
     * 
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if no user with the given ID exists
     */
    @Transactional
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id: " + id + " is not found.");
        }
        userRepository.deleteById(id);
    }

}
