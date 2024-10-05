package ru.bicev.movie_ratings.RestControllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.bicev.movie_ratings.TestSecurityConfig;
import ru.bicev.movie_ratings.api.UserRestController;
import ru.bicev.movie_ratings.dto.UserDto;
import ru.bicev.movie_ratings.exceptions.DuplicateUserException;
import ru.bicev.movie_ratings.exceptions.IllegalAccessException;
import ru.bicev.movie_ratings.exceptions.UserNotFoundException;
import ru.bicev.movie_ratings.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@Import(TestSecurityConfig.class)
public class UserRestControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
                userDto.setId(1L);
        }

        private UserDto userDto = new UserDto("example@email.com", "testUser", "password", "USER");

        @Test
        public void getUserById_ShouldReturnUser() throws Exception {
                when(userService.getUserById(1L)).thenReturn(userDto);
                mockMvc.perform(get("/api/users/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json("{'id': 1, 'userName': 'testUser', 'role': 'USER'}"));

        }

        @Test
        public void getUserById_ShouldReturn404WhenUserNotFound() throws Exception {
                when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("User not found"));
                mockMvc.perform(get("/api/users/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("404 Not found: User not found"));
        }

        @Test
        public void registerUser_ShouldReturnUser() throws Exception {
                when(userService.createNewUser(any(UserDto.class))).thenReturn(userDto);

                UserDto userToCreate = new UserDto("example@email.com", "testUser", "password", "USER");
                ObjectMapper objectMapper = new ObjectMapper();
                String userJson = objectMapper.writeValueAsString(userToCreate);

                mockMvc.perform(post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
                                .andExpect(status().isCreated())
                                .andExpect(content().json("{'id': 1, 'userName': 'testUser', 'role': 'USER'}"));
        }

        @Test
        public void registerUser_ShouldThrowException() throws Exception {
                when(userService.createNewUser(any(UserDto.class)))
                                .thenThrow(new DuplicateUserException("User already exists"));
                UserDto userToCreate = new UserDto("example@email.com", "testUser", "password", "USER");
                ObjectMapper objectMapper = new ObjectMapper();
                String userJson = objectMapper.writeValueAsString(userToCreate);

                mockMvc.perform(post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
                                .andExpect(status().isConflict())
                                .andExpect(content().string("409 Already exists: User already exists"));
        }

        @Test
        public void editUser_ShouldReturnUser() throws Exception {
                when(userService.updateUser(any(UserDto.class))).thenReturn(userDto);
                UserDto userToUpdate = new UserDto("example@email.com", "testUser", "password", "USER");
                ObjectMapper objectMapper = new ObjectMapper();
                String userJson = objectMapper.writeValueAsString(userToUpdate);

                mockMvc.perform(put("/api/users/edit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
                                .andExpect(status().isOk())
                                .andExpect(content().json("{'id': 1, 'userName': 'testUser', 'role': 'USER'}"));
        }

        @Test
        public void editUser_ShouldThrowException() throws Exception {
                when(userService.updateUser(any(UserDto.class))).thenThrow(new UserNotFoundException("User not found"));
                UserDto userToUpdate = new UserDto("example@email.com", "testUser", "password", "USER");
                ObjectMapper objectMapper = new ObjectMapper();
                String userJson = objectMapper.writeValueAsString(userToUpdate);

                mockMvc.perform(put("/api/users/edit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("404 Not found: User not found"));
        }

        @Test
        public void getUserByEmail_ShouldReturnUser() throws Exception {
                when(userService.getUserByEmail("example@email.com")).thenReturn(userDto);
                mockMvc.perform(get("/api/users/email").param("email", "example@email.com"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json("{'id': 1, 'userName': 'testUser', 'role': 'USER'}"));
        }

        @Test
        public void getUserByEmail_ShouldThrowException() throws Exception {
                when(userService.getUserByEmail("example@email.com"))
                                .thenThrow(new UserNotFoundException("User not found"));
                mockMvc.perform(get("/api/users/email").param("email", "example@email.com"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("404 Not found: User not found"));
        }

        @Test
        public void deleteUser_Success() throws Exception {
                doNothing().when(userService).deleteUserById(1L);
                mockMvc.perform(delete("/api/users/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        public void deleteUser_ShouldThrowException() throws Exception {
                doThrow(new UserNotFoundException("User not found")).when(userService).deleteUserById(1L);
                mockMvc.perform(delete("/api/users/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("404 Not found: User not found"));
        }

        @Test
        public void changePassword_ShouldReturnUser() throws Exception {
                UserDto userToUpdate = new UserDto("example@email.com", "testUser", "password", "USER");
                userToUpdate.setId(1L);

                ObjectMapper objectMapper = new ObjectMapper();
                String userJson = objectMapper.writeValueAsString(userToUpdate);
                System.out.println("Request JSON: " + userJson);

                when(userService.changePassword(any(UserDto.class), eq("oldPassword"), eq("newPassword")))
                                .thenReturn(userToUpdate);

                String expectedJson = objectMapper.writeValueAsString(userToUpdate);

                mockMvc.perform(put("/api/users/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson)
                                .param("oldPassword", "oldPassword")
                                .param("newPassword", "newPassword"))
                                .andExpect(status().isOk())
                                .andExpect(content().json(expectedJson));
        }

        @Test
        public void changePassword_ShouldThrowException() throws Exception {
                UserDto userToUpdate = new UserDto("example@email.com", "testUser", "password", "USER");
                userToUpdate.setId(1L);

                ObjectMapper objectMapper = new ObjectMapper();
                String userJson = objectMapper.writeValueAsString(userToUpdate);
                System.out.println("Request JSON: " + userJson);

                when(userService.changePassword(any(UserDto.class), eq("oldPassword"), eq("newPassword")))
                                .thenThrow(new IllegalAccessException("Invalid password"));

                mockMvc.perform(put("/api/users/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson)
                                .param("oldPassword", "oldPassword")
                                .param("newPassword", "newPassword"))
                                .andExpect(status().isForbidden())
                                .andExpect(content().string("403 Forbidden: Invalid password"));
        }

}
