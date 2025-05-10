package com.example.mtms.service;

import com.example.mtms.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    Movie createMovie(Movie movie);
    List<Movie> getAllMovies();
    Optional<Movie> getMovieById(Long id);
    Movie updateMovie(Long id, Movie movie);
    void deleteMovie(Long id);
}
