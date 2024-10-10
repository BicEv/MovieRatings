package ru.bicev.movie_ratings.dto;

import java.time.Year;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class MovieDto {
    private Long id;

    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 1, max = 50, message = "Title should be from 1 to 50 symbols long")
    private String title;

    @Size(max = 255, message = "Synopsis must be less than 256 symbols")
    private String synopsis;

    @NotEmpty(message = "Genre cannot be empty")
    private String genre;

    @Min(value = 1900, message = "Year must be between 1900 and the current year")
    @Max(value = Year.MAX_VALUE, message = "Year must be between 1900 and the current year")
    private int releaseYear;

    private List<Long> reviewIds;

    private Double rating;

    public MovieDto() {
    }

    public MovieDto(String title, String synopsis, String genre, int releaseYear) {
        this.title = title;
        this.synopsis = synopsis;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<Long> getReviewIds() {
        return reviewIds;
    }

    public void setReviewIds(List<Long> reviewIds) {
        this.reviewIds = reviewIds;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
