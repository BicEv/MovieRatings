package ru.bicev.movie_ratings.entitites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1000, message = "Comment must be less than 1000 symbols")
    private String comment;

    @ManyToOne
    @NotNull(message = "User cannot be null")
    private User user;

    @ManyToOne
    @NotNull(message = "Movie cannot be null")
    private Movie movie;

    @Min(value = 1, message = "Rating should be from 1 to 5")
    @Max(value = 5, message = "Rating should be from 1 to 5")
    private int rating;

    public Review() {
    }

    public Review(String comment, User user, Movie movie, int rating) {
        this.comment = comment;
        this.user = user;
        this.movie = movie;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
