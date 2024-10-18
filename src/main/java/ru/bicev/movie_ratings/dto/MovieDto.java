package ru.bicev.movie_ratings.dto;

import java.time.Year;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object representing a movie.
 * <p>
 * This object is used to transfer movie data between the client and server.
 * </p>
 */
public class MovieDto {

    /**
     * The id of the movie.
     */
    private Long id;

    /**
     * The title of the movie. This field is mandatory. Size must be between 1 and
     * 50 symbols.
     */
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 1, max = 50, message = "Title should be from 1 to 50 symbols long")
    private String title;

    /**
     * The synopsis of the movie. Size must be less than 255 symbols.
     */
    @Size(max = 255, message = "Synopsis must be less than 256 symbols")
    private String synopsis;

    /**
     * The genre of the movie. This field is mandatory.
     */
    @NotEmpty(message = "Genre cannot be empty")
    private String genre;

    /**
     * The year movie was released. Must be between 1900 and curent year.
     */
    @Min(value = 1900, message = "Year must be between 1900 and the current year")
    @Max(value = Year.MAX_VALUE, message = "Year must be between 1900 and the current year")
    private int releaseYear;

    /**
     * List of review ids associated with the movie.
     */
    private List<Long> reviewIds;

    /**
     * The rating of the movie.
     */
    private Double rating;

    /**
     * Default constructor.
     */
    public MovieDto() {
    }

    /**
     * Constructor to create a new movie dto instance with the given parameters.
     * 
     * @param title       the title of the movie
     * @param synopsis    the synopsis of the movie
     * @param genre       the genre of the movie
     * @param releaseYear the year movie was released
     */
    public MovieDto(String title, String synopsis, String genre, int releaseYear) {
        this.title = title;
        this.synopsis = synopsis;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

    /**
     * Gets the movie dto id
     * 
     * @return the movie dto id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the movie dto id
     * 
     * @param id the movie dto id
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
     * Gets the genre of the movie
     * 
     * @return the genre of the movie
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the movie
     * 
     * @param genre the genre of the movie
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the year the movie was released
     * 
     * @return the year the movie was released
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
     * Gets the list of ids of reviews associated with the movie
     * 
     * @return list of review ids associated with the movie
     */
    public List<Long> getReviewIds() {
        return reviewIds;
    }

    /**
     * Sets the list of ids of reviews associated with the movie
     * 
     * @param reviewIds list of review ids associated with the movie
     */
    public void setReviewIds(List<Long> reviewIds) {
        this.reviewIds = reviewIds;
    }

    /**
     * Gets the rating of the movie
     * 
     * @return rating of the movie
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
