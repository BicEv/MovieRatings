package ru.bicev.movie_ratings.entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a review entity with details such as comment and rating.
 * This class is used to store and manage information about reviews in the
 * system.
 * This class is mapped to a database table using JPA annotations.
 */
@Entity
public class Review {

    /**
     * Unique identifier for the review, generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Comment for the review.
     */
    private String comment;

    /**
     * User associated with the review. This field is mandatory.
     * This field represents a many-to-one relationship between a user on user id
     * and reviews.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The movie associated with the review. This field is mandatory.
     * This field represents a many-to-one relationship between a movie on movie id
     * and reviews.
     */
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    /**
     * The rating of the movie in the review. This field is mandatory.
     */
    @Column(nullable = false)
    private int rating;

    /**
     * Default constructor for JPA.
     */
    public Review() {
    }

    /**
     * Constructor to create a new review instance with the given parameters.
     * 
     * @param comment the comment to the review
     * @param user    the user created the review
     * @param movie   the movie associated with the review
     * @param rating  the rating of the review
     */
    public Review(String comment, User user, Movie movie, int rating) {
        this.comment = comment;
        this.user = user;
        this.movie = movie;
        this.rating = rating;
    }

    /**
     * Gets the identifier of the review
     * 
     * @return the review id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the identifier of the review
     * 
     * @param id the review id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the review comment
     * 
     * @return the review comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the review comment
     * 
     * @param comment the review comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the user associated with the review
     * 
     * @return the user of the review
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the review
     * 
     * @param user the user of the review
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the movie associated with the review
     * 
     * @return the movie of the review
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Sets the movie associated with the review
     * 
     * @param movie the movie of the review
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Gets the revting of the review
     * 
     * @return the rating of the review
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating of the review
     * 
     * @param rating the rating of the review
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

}
