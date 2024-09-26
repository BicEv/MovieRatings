package ru.bicev.movie_ratings.utils;

import java.util.List;
import java.util.stream.Collectors;

import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.entitites.Review;

public class MovieConverter {

    public static MovieDto toDto(Movie movie) {
        MovieDto movieDto = new MovieDto();

        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setSynopsis(movie.getSynopsis());
        movieDto.setReleaseYear(movie.getReleaseYear());
        movieDto.setGenre(movie.getGenre());
        List<Long> reviewIds = (movie.getReviews() != null) ? movie.getReviews().stream()
                .map(Review::getId)
                .collect(Collectors.toList()) : List.of();
        movieDto.setReviewIds(reviewIds);

        return movieDto;
    }

    public static Movie toEntity(MovieDto movieDto) {
        Movie movie = new Movie();

        movie.setTitle(movieDto.getTitle());
        movie.setSynopsis(movieDto.getSynopsis());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setGenre(movieDto.getGenre());

        return movie;
    }

}
