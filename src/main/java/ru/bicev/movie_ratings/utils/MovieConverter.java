package ru.bicev.movie_ratings.utils;

import java.util.List;
import java.util.stream.Collectors;

import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.entitites.Review;

/**
 * Utility class for converting between Movie entities and MovieDto objects.
 * <p>
 * This class provides methods to convert a Movie entity to a MovieDto and
 * vice versa, facilitating the transfer of movie data between different layers
 * of the application.
 * </p>
 */
public class MovieConverter {

    /**
     * Converts a Movie entity to a MovieDto.
     * 
     * @param movie the Movie to be coverted
     * @return a MovieDto representing the movie entity or {@code null} if the input
     *         is null
     */
    public static MovieDto toDto(Movie movie) {
        if (movie == null) {
            return null;
        }

        MovieDto movieDto = new MovieDto();

        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setSynopsis(movie.getSynopsis());
        movieDto.setReleaseYear(movie.getReleaseYear());
        movieDto.setGenre(movie.getGenre());
        List<Long> reviewIds = (movie.getReviews() != null) ? movie.getReviews().stream()
                .map(Review::getId)
                .collect(Collectors.toList()) : List.of();
        movieDto.setRating(movie.getRating());
        movieDto.setReviewIds(reviewIds);

        return movieDto;
    }

    /**
     * Converts a MovieDto to a Movie entity
     * 
     * @param movieDto the MovieDto to be converted
     * @return a Movie entity based on the provided MovieDto
     */
    public static Movie toEntity(MovieDto movieDto) {
        Movie movie = new Movie();

        movie.setTitle(movieDto.getTitle());
        movie.setSynopsis(movieDto.getSynopsis());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setGenre(movieDto.getGenre());

        return movie;
    }

}
