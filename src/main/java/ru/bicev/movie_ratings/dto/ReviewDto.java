package ru.bicev.movie_ratings.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewDto {

    private Long id;

    @Size(max = 1000, message = "Comment must be less than 1000 symbols")
    private String comment;

    @NotNull(message = "User Id cannot be null")
    private Long userId;

    @NotNull(message = "Movie Id cannot be null")
    private Long movieId;

    @Min(value = 1, message = "Rating should be from 1 to 5")
    @Max(value = 5, message = "Rating should be from 1 to 5")
    private int rating;

    public ReviewDto() {
    }

    public ReviewDto(String comment, Long userId, Long movieId, int rating) {
        this.comment = comment;
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
