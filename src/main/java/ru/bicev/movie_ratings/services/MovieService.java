package ru.bicev.movie_ratings.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bicev.movie_ratings.dto.MovieDto;
import ru.bicev.movie_ratings.entitites.Movie;
import ru.bicev.movie_ratings.exceptions.DuplicateMovieException;
import ru.bicev.movie_ratings.exceptions.MovieNotFoundException;
import ru.bicev.movie_ratings.repositories.MovieRepository;
import ru.bicev.movie_ratings.utils.MovieConverter;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

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

    @Transactional
    public void deleteMovie(Long id) {
        if (movieRepository.findById(id).isEmpty()) {
            throw new MovieNotFoundException("Movie with id: " + id + " is not found");
        }
        movieRepository.deleteById(id);
    }

    public MovieDto findMovieByTitle(String title) {
        Movie foundMovie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new MovieNotFoundException("Movie: " + title + " is not found"));
        return MovieConverter.toDto(foundMovie);
    }

    public MovieDto findMovieById(Long id) {
        Movie foundMovie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with id: " + id + " is not found"));
        return MovieConverter.toDto(foundMovie);
    }

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
