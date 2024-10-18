package ru.bicev.movie_ratings.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object representing a user
 * <p>
 * This object is used to transfer data between the client and server.
 * </p>
 */
public class UserDto {

    /**
     * The id of the user
     */
    private Long id;

    /**
     * The email of the user. This field is mandatory.
     */
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Ivalid email format")
    private String email;

    /**
     * The username of the user. This field is mandatory. Size must be between 6 and
     * 14 symbols.
     */
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 6, max = 14, message = "Username must be from 6 to 14 symbols long")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contains only letters and numbers")
    private String userName;

    /**
     * The password of the user. This field is mandatory. Size must be between 6 and
     * 14 symbols.
     */
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 14, message = "Password must be from 6 to 14 symbols long")
    private String password;

    /**
     * The role of the user. This field is mandatory.
     */
    @NotEmpty(message = "Role cannot be null")
    private String role;

    /**
     * The list of review ids associated with the user.
     */
    private List<Long> reviewIds;

    /**
     * Default constructor
     */
    public UserDto() {
    }

    /**
     * Contstructor to create a new user dto istance with the given parameters
     * 
     * @param email    the email of the user
     * @param userName the username of the user
     * @param password the password of the user
     * @param role     the role of the user
     */
    public UserDto(String email, String userName, String password, String role) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the user's id
     * 
     * @return the user's id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user's id
     * 
     * @param id the user's id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user's email
     * 
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email
     * 
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's username
     * 
     * @return the user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user's username
     * 
     * @param userName the user's username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the user's password
     * 
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password
     * 
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's role
     * 
     * @return the user's role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role
     * 
     * @param role the user's role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the ids of the reviews associated with the user
     * 
     * @return the list of the review ids
     */
    public List<Long> getReviewIds() {
        return reviewIds;
    }

    /**
     * Sets the ids of the reviews associated of the user
     * 
     * @param reviewIds the list of the review ids
     */
    public void setReviewIds(List<Long> reviewIds) {
        this.reviewIds = reviewIds;
    }

}
