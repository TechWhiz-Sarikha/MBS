package com.example.mtms.controller;

import com.example.mtms.entity.Movie;
import com.example.mtms.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mtms.service.ShowtimeService;
import com.example.mtms.entity.Showtime;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final ObjectMapper objectMapper;
    private final ShowtimeService showtimeService;

    public MovieController(MovieService movieService, ObjectMapper objectMapper, ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.objectMapper = objectMapper;
        this.showtimeService = showtimeService;
    }

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody String jsonPayload) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonPayload);
            
            if (jsonNode.isArray()) {
                // Handle array of movies
                List<Movie> movies = objectMapper.readValue(jsonPayload, 
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Movie.class));
                
                List<Movie> newMovies = movies.stream()
                        .map(movieService::createMovie)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(newMovies, HttpStatus.CREATED);
            } else {
                // Handle single movie
                Movie movie = objectMapper.readValue(jsonPayload, Movie.class);
                Movie newMovie = movieService.createMovie(movie);
                return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
            }
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid JSON format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("Error creating movie(s): " + e.getMessage());
            return new ResponseEntity<>("Error creating movie(s): " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        try {
            Movie updatedMovie = movieService.updateMovie(id, movie);
            return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/showtimes")
    public ResponseEntity<List<Showtime>> getShowtimesByMovie(@PathVariable Long id) {
        List<Showtime> showtimes = showtimeService.getShowtimesByMovie(id);
        return new ResponseEntity<>(showtimes, HttpStatus.OK);
    }
}
