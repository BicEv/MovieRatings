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

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto createNewUser(UserDto userDto) {
        if (userExistsByEmail(userDto.getEmail())) {
            throw new DuplicateUserException("User with this email already exists.");
        }
        if (userExistsByUserName(userDto.getUserName())) {
            throw new DuplicateUserException("User with this username already exists.");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(UserConverter.toEntity(userDto));

        return UserConverter.toDto(savedUser);
    }

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

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " is not found"));
        return UserConverter.toDto(user);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " is not found"));
        return UserConverter.toDto(user);
    }

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

    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id: " + id + " is not found.");
        }
        userRepository.deleteById(id);
    }

    private boolean userExistsByEmail(String email) {
        return userRepository.userExistsByEmail(email);
    }

    private boolean userExistsByUserName(String userName) {
        return userRepository.userExistsByUserName(userName);
    }

}
