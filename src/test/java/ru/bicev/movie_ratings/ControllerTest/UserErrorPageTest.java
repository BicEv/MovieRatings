package ru.bicev.movie_ratings.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.controllers.GlobalExceptionHandler;
import ru.bicev.movie_ratings.controllers.UserController;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.exceptions.DuplicateUserException;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;
import ru.bicev.movie_ratings.services.UserService;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserErrorPageTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testUserNotFoundException_ShouldReturnUserNotFoundPage() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Not found: User not found"));
    }

    @Test
    public void testDuplicateUserException_ShouldReturnDuplicatePage() throws Exception {
        when(userService.createNewUser(any(UserDto.class)))
                .thenThrow(new DuplicateUserException("User already exists"));

        mockMvc.perform(post("/users/register")
                .param("email", "example@email.com")
                .param("userName", "Username")
                .param("password", "Password")
                .param("role", "USER"))
                .andExpect(status().isConflict())
                .andExpect(view().name("error/409"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Duplicate: User already exists"));
    }

    @Test
    public void testGenericException_ShouldReturnGeneralErrorPage() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message",
                        "Internal server error: Unexpected error"));
    }

    @Test
    public void testIllegalAccessException_ShouldReturnForbiddenPage() throws Exception {
        when(userService.changePassword(any(UserDto.class), eq("Invalidpassword"), eq("newpassword")))
                .thenThrow(new IllegalAccessException("Invalid password"));

        mockMvc.perform(post("/users/change-password")
                .param("email", "example@email.com")
                .param("userName", "username")
                .param("password", "password")
                .param("role", "USER")
                .param("oldPassword", "Invalidpassword")
                .param("newPassword", "newpassword")).andExpect(status().isForbidden())
                .andExpect(view().name("error/403"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message",
                        "Access denied: Invalid password"));
    }

}
