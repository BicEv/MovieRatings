package ru.bicev.movie_ratings.services;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.exceptions.DuplicateMovieException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.repositories.MovieRepository;
import ru.bicev.movie_ratings.repositories.ReviewRepository;
import ru.bicev.movie_ratings.utils.MovieConverter;

/**
 * Service class for managing movie-related operations.
 * Handles creation, deleting, updating and retrieving movies.
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    private final ReviewRepository reviewRepository;

    /**
     * Constructor for MovieService, initializes required components.
     * 
     * @param movieRepository
     * @param reviewRepository
     */
    @Autowired
    public MovieService(MovieRepository movieRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Retrieves a list of all movies int the system sorted by its rating desc.
     * 
     * @return a List of the movies sorted by its rating desc.
     */
    public List<MovieDto> getAllMoviesWithRatingsSortedByRatingDesc() {
        List<Movie> movies = movieRepository.findAll();

        for (Movie movie : movies) {
            double averageRating = reviewRepository.findAverageRatingByMovieId(movie.getId());
            movie.setRating(averageRating);
        }

        return movies.stream()
                .sorted(Comparator.comparing(Movie::getRating, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(MovieConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new movie
     * 
     * @param movieDto the data transfer object {@link MovieDto} representing the movie to create
     * @return {@link MovieDto} created MovieDto
     * @throws DuplicateMovieException if a movie with the same details already
     *                                 exists
     */
    @Transactional
    public MovieDto createMovie(MovieDto movieDto) {
        Optional<Movie> foundMovie = movieRepository.findByTitle(movieDto.getTitle());
        if (foundMovie.isPresent() && foundMovie.get().getGenre().equalsIgnoreCase(movieDto.getGenre())
                && foundMovie.get().getReleaseYear() == movieDto.getReleaseYear()) {
            throw new DuplicateMovieException("Movie " + movieDto.getTitle() + " already exists");
        }

        Movie createdMovie = movieRepository.save(MovieConverter.toEntity(movieDto));
        return MovieConverter.toDto(createdMovie);
    }

    /**
     * Deletes movie by its ID
     * 
     * @param id the ID of movie to be deleted
     * @throws MovieNotFoundException if no movie with the given ID exists
     */
    @Transactional
    public void deleteMovie(Long id) {
        if (movieRepository.findById(id).isEmpty()) {
            throw new MovieNotFoundException("Movie with id: " + id + " is not found");
        }
        movieRepository.deleteById(id);
    }

    /**
     * Retrieves a movie by title
     * 
     * @param title the title of the movie to retrieve
     * @return {@link MovieDto} MovieDto corresponding to the movie found by title
     * @throws MovieNotFoundException if no movie with the given title exists
     */
    public MovieDto findMovieByTitle(String title) {
        Movie foundMovie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new MovieNotFoundException("Movie: " + title + " is not found"));
        foundMovie.setRating(reviewRepository.findAverageRatingByMovieId(foundMovie.getId()));
        return MovieConverter.toDto(foundMovie);
    }

    /**
     * Retrieves a movie by ID
     * 
     * @param id the ID of the movie to retrieve
     * @return {@link MovieDto} MovieDto corresponding to the movie found by ID
     * @throws MovieNotFoundException if no movie with the given ID exists
     */
    public MovieDto findMovieById(Long id) {
        Movie foundMovie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with id: " + id + " is not found"));
        foundMovie.setRating(reviewRepository.findAverageRatingByMovieId(foundMovie.getId()));
        return MovieConverter.toDto(foundMovie);
    }

    /**
     * Updates the movie details
     * 
     * @param movieDto the data transfer object {@link MovieDto} representing the updated movie
     * @return {@link MovieDto} updated MovieDto
     * @throws MovieNotFoundException if no movie with the given in movieDto title
     *                                exists
     */
    @Transactional
    public MovieDto updateMovie(MovieDto movieDto) {
        Movie foundMovie = movieRepository.findByTitle(movieDto.getTitle())
                .orElseThrow(() -> new MovieNotFoundException("Movie: " + movieDto.getTitle() + " is not found"));
        foundMovie.setGenre(movieDto.getGenre());
        foundMovie.setReleaseYear(movieDto.getReleaseYear());
        foundMovie.setSynopsis(movieDto.getSynopsis());
        movieRepository.save(foundMovie);

        return MovieConverter.toDto(foundMovie);
    }

}
