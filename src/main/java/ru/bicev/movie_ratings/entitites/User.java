package ru.bicev.movie_ratings.entitites;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import ru.bicev.movie_ratings.utils.Role;

/**
 * Represents a user entity with details such as email, username, password, role
 * and list of reviews.
 * This class is used to store and manage information about user in the system.
 * Each user cn have multiple reviews.
 * 
 * This class is mapped to a database table using JPA annotations.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Unique identifier, generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique email of the user. This field is mandatory.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Unique usernae of the user. This field is mandatory.
     */
    @Column(nullable = false, unique = true)
    private String userName;

    /**
     * Password of the user. This dield is mandatory.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role of the user. This field is mandatory.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * List of the reviews created by the user.
     * This field represents a one-to-many relationship between a user and reviews.
     * The reviews are cascaded when the user is persistet or removed.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    /**
     * Default constructor for JPA.
     */
    public User() {
    }

    /**
     * Constructor to create a new user instance with the given parameters.
     * 
     * @param email    the email of the user
     * @param userName the username of the user
     * @param password the password of the user
     * @param role     the role of the user
     */
    public User(String email, String userName, String password, Role role) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the unique identifier of the user
     * 
     * @return the user id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user
     * 
     * @param id the user id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the unique email of the user
     * 
     * @return the user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the unique email of the user
     * 
     * @param email the email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the unique username of the user
     * 
     * @return the user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the unique username of the user
     * 
     * @param userName the user's username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password of the user
     * 
     * @return the password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password of the user
     * 
     * @param password the password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user
     * 
     * @return role of the user
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role of the user
     * 
     * @param role the role of the user
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets the list of user's reviews
     * 
     * @return the list of reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Set the list of reviews associated with user
     * 
     * @param reviews the list of reviews
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
