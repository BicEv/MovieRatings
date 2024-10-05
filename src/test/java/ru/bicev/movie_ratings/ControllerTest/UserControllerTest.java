package ru.bicev.movie_ratings.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.controllers.UserController;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.services.UserService;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private String email = "example@email.com";
    private String userName = "Username";
    private String password = "password";
    private String newPassword = "newPassword";
    private String role = "USER";
    private UserDto userDto = new UserDto(email, userName, password, role);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerUserGet() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"));
    }

    @Test
    public void registerUserPost() throws Exception {

        when(userService.createNewUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users/register")
                .param("email", userDto.getEmail())
                .param("userName", userDto.getUserName())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/view"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userDto));

    }

    @Test
    public void changePasswordGet() throws Exception {
        mockMvc.perform(get("/users/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/change-password"));
    }

    @Test
    public void changePasswordPut() throws Exception {
        when(userService.changePassword(any(UserDto.class), eq(password), eq(newPassword))).thenReturn(userDto);

        mockMvc.perform(post("/users/change-password")
                .param("email", userDto.getEmail())
                .param("userName", userDto.getUserName())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole())
                .param("oldPassword", password)
                .param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(view().name("user/view"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userDto));

    }

    @Test
    public void getUserByEmail() throws Exception {
        when(userService.getUserByEmail("example@email.com")).thenReturn(userDto);

        mockMvc.perform(get("/users/findByEmail")
                .param("email", "example@email.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/view"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userDto));

    }

    @Test
    public void getUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/view"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userDto));

    }

    @Test
    public void editUserGet() throws Exception {
        mockMvc.perform(get("/users/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"));
    }

    @Test
    public void changePasswordPost() throws Exception {
        when(userService.updateUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users/edit")
                .param("email", userDto.getEmail())
                .param("userName", userDto.getUserName())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/view"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userDto));

    }

    @Test
    public void deleteUser() throws Exception {
        doNothing().when(userService).deleteUserById(anyLong());

        mockMvc.perform(delete("/users/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

}
