package ru.bicev.movie_ratings.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object representing a movie review
 * <p>
 * This object is used to transfer data between the client and server.
 * </p>
 */
public class ReviewDto {

    /**
     * The id of the review.
     */
    private Long id;

    /**
     * The comment for the movie. Size must be less than 1000 symbols.
     */
    @Size(max = 1000, message = "Comment must be less than 1000 symbols")
    private String comment;

    /**
     * The id of the user associated with the review.
     */
    private Long userId;

    /**
     * The id of the movie associated with the review.
     */
    private Long movieId;

    /**
     * The rating of the review. Must be from 1 to 5.
     */
    @Min(value = 1, message = "Rating should be from 1 to 5")
    @Max(value = 5, message = "Rating should be from 1 to 5")
    private int rating;

    /**
     * Default constructor.
     */
    public ReviewDto() {
    }

    /**
     * Constructor to create a new review dto instance with the given parameters.
     * 
     * @param comment the comments of the review
     * @param userId  the id of the user associated with the review
     * @param movieId the id of the movie associated with the review
     * @param rating  the rating of the review
     */
    public ReviewDto(String comment, Long userId, Long movieId, int rating) {
        this.comment = comment;
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    /**
     * Gets the id of the review
     * 
     * @return the id of the review
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the review
     * 
     * @param id the id of the review
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the comment of the review
     * 
     * @return the comment of the review
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment of the review
     * 
     * @param comment the comment of the review
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the id of the user associated with the review
     * 
     * @return the user's id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the id of the user associated with the review
     * 
     * @param userId the user's id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the id of the movie associated with the review
     * 
     * @return the movie's id
     */
    public Long getMovieId() {
        return movieId;
    }

    /**
     * Sets the id of the user associated with the review
     * 
     * @param movieId the movie's id
     */
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    /**
     * Gets the rating of the review
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
