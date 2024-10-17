package ru.bicev.movie_ratings.entitites;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

/**
 * Represents a movie entity with details such as title, genre, release year,
 * and reviews.
 * This class is used to store and manage information about movies in the
 * system.
 * Each movie can have multiple reviews.
 * 
 * An additional field for rating is transient and is calculated based on the
 * associated reviews.
 * 
 * This class is mapped to a database table using JPA annotations.
 */
@Entity
public class Movie {

    /**
     * Unique identifier for the movie, generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the movie. This field is mandatory.
     */
    @Column(nullable = false)
    private String title;

    /**
     * A short synopsis or description of the movie.
     */
    private String synopsis;

    /**
     * The genre of the movie. This field is mandatory.
     */
    @Column(nullable = false)
    private String genre;

    /**
     * The year when the movie was released.
     */
    private int releaseYear;

    /**
     * List of reviews associated with the movie.
     * This field represents a one-to-many relationship between a movie and a
     * reviews.
     * The reviews are cascaded when the movie is persisted or removed.
     */
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviews;

    /**
     * A transient field that holds the average rating of the movie.
     * This field is not stored in the database but is calculated at runtime.
     */
    @Transient
    private Double rating;

    /**
     * Default constructor for JPA.
     */
    public Movie() {
    }

    /**
     * Constructor to create a new movie instance with the given parameters.
     * 
     * @param title       the title of the movie
     * @param synopsis    a short description of the movie
     * @param genre       the genre of the movie
     * @param releaseYear the year the movie was released
     */
    public Movie(String title, String synopsis, String genre, int releaseYear) {
        this.title = title;
        this.synopsis = synopsis;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

    /**
     * Gets the unique identifier of the movie.
     * 
     * @return id of the movie
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the identifier of the movie
     * 
     * @param id identifier of the movie
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the title of the movie
     * 
     * @return the title of the movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the movie
     * 
     * @param title the title of the movie
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the genre of the movie
     * 
     * @return the genre of the movie
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the moive
     * 
     * @param genre the genre of the movie
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the year the movie was released
     * 
     * @return the release year of the movie
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the year the movie was released
     * 
     * @param releaseYear the year the movie was released
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Gets the list of reviews associated with the movie
     * 
     * @return the list of reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Sets the list of reviews associated with the movie
     * 
     * @param reviews the list of reviews
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Gets the synopsis of the movie
     * 
     * @return the synopsis of the movie
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * Sets the synopsis of the movie
     * 
     * @param synopsis the synopsis of the movie
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * Gets the rating of the movie
     * 
     * @return the rating of the movie
     */
    public Double getRating() {
        return rating;
    }

    /**
     * Sets the rating of the movie
     * 
     * @param rating the rating of the movie
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

}
