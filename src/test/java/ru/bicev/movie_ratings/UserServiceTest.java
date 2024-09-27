package ru.bicev.movie_ratings;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.exceptions.DuplicateUserException;
import ru.bicev.movie_ratings.repositories.UserRepository;
import ru.bicev.movie_ratings.services.UserService;
import ru.bicev.movie_ratings.utils.Role;

public class UserServiceTest {

    private String email = "test@example.com";
    private String username = "testuser";
    private String password = "password";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createNewUser_UserExists_ThrowsDuplicateUserException() {
        UserDto userDto = new UserDto(email, username, password, "USER");

        User user = new User(email, username, password, Role.USER);
        user.setId(1L);
        user.setReviews(List.of());

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        assertThrows(DuplicateUserException.class, () -> {
            userService.createNewUser(userDto);
        });

        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(userRepository, never()).save(any());

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void createNewUser_ShouldReturnDto() {
        UserDto userDto = new UserDto(email, username, password, "USER");
        User user = new User(email, username, passwordEncoder.encode(password), Role.USER);
        user.setId(1L);

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto createdUserDto = userService.createNewUser(userDto);

        assertEquals(1L, createdUserDto.getId());
        assertEquals(email, createdUserDto.getEmail());
        assertEquals(username, createdUserDto.getUserName());

        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    public void updateUser_Success() {
        Long userId = 1L;
        UserDto userDto = new UserDto(email, username, password, "USER");
        userDto.setId(userId);

        User existingUser = new User(email, username, password, Role.USER);
        existingUser.setId(userId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDto updatedUser = userService.updateUser(userDto);

        assertEquals(userId, updatedUser.getId());
        assertEquals(username, updatedUser.getUserName());
        verify(userRepository, times(1)).save(existingUser);

    }

    @Test
    public void deleteUser_Success() {
        User user = new User(email, username, password, Role.USER);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findUserById_Success() {
        Long userId = 1L;
        User user = new User(email, username, password, Role.ADMIN);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto foundUser = userService.getUserById(userId);

        assertEquals(userId, foundUser.getId());
        assertEquals(email, foundUser.getEmail());
        assertEquals("ADMIN", foundUser.getRole());
        verify(userRepository, times(1)).findById(userId);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findUserByEmail_Success() {
        User user = new User(email, username, password, Role.USER);
        user.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDto foundUser = userService.getUserByEmail(email);

        assertEquals(1L, foundUser.getId());
        assertEquals(email, foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(email);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void changePassword_Success() {
        UserDto userDto = new UserDto(email, username, password, "USER");
        User user = new User(email, username, password, Role.USER);
        String oldPassword = "password";
        String newPassword = "newpassword";

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        UserDto changedUserDto = userService.changePassword(userDto, oldPassword, newPassword);

        verify(userRepository, times(1)).save(argThat(u -> u.getPassword().equals("encodedNewPassword")));
    }

    @Test
    public void changePassword_throwsException() {
        UserDto userDto = new UserDto(email, username, "invalidPassword", "USER");
        User user = new User(email, username, password, Role.USER);
        String oldPassword = "invalidPassword";
        String newPassword = "newpassword";

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        assertThrows(ru.bicev.movie_ratings.exceptions.IllegalAccessException.class,
                () -> userService.changePassword(userDto, oldPassword, newPassword));

    }

}
