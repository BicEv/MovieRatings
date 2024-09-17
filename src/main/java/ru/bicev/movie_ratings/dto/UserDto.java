package ru.bicev.movie_ratings.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

    private Long id;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Ivalid email format")
    private String email;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 6, max = 14, message = "Username must be from 6 to 14 symbols long")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contains only letters and numbers")
    private String userName;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 14, message = "Password must be from 6 to 14 symbols long")
    private String password;

    @NotEmpty(message = "Role cannot be null")
    private String role;

    private List<Long> reviewIds;

    public UserDto() {
    }

    public UserDto(String email, String userName, String password, String role) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Long> getReviewIds() {
        return reviewIds;
    }

    public void setReviewIds(List<Long> reviewIds) {
        this.reviewIds = reviewIds;
    }

}
