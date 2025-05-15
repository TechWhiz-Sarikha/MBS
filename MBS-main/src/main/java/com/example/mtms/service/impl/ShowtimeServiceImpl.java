package com.example.mtms.service.impl;

import com.example.mtms.entity.Movie;
import com.example.mtms.entity.Showtime;
import com.example.mtms.entity.Theater;
import com.example.mtms.repository.MovieRepository;
import com.example.mtms.repository.ShowtimeRepository;
import com.example.mtms.repository.TheaterRepository;
import com.example.mtms.service.ShowtimeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository,
                               MovieRepository movieRepository,
                               TheaterRepository theaterRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    @Override
    public Showtime createShowtime(Showtime showtime) {
        Movie movie = movieRepository.findById(showtime.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + showtime.getMovie().getId()));

        Theater theater = theaterRepository.findById(showtime.getTheater().getId())
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + showtime.getTheater().getId()));

        if (showtime.getAvailableSeats() == null) {
            showtime.setAvailableSeats(theater.getCapacity());
        }

        showtime.setMovie(movie);
        showtime.setTheater(theater);

        return showtimeRepository.save(showtime);
    }

    @Override
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    @Override
    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    @Override
    public List<Showtime> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    @Override
    public List<Showtime> getShowtimesByTheater(Long theaterId) {
        return showtimeRepository.findByTheaterId(theaterId);
    }

    @Override
    public Showtime updateShowtime(Long id, Showtime showtimeDetails) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + id));

        if (showtimeDetails.getMovie() != null && showtimeDetails.getMovie().getId() != null) {
            Movie movie = movieRepository.findById(showtimeDetails.getMovie().getId())
                    .orElseThrow(() -> new RuntimeException("Movie not found with id: " + showtimeDetails.getMovie().getId()));
            showtime.setMovie(movie);
        }

        if (showtimeDetails.getTheater() != null && showtimeDetails.getTheater().getId() != null) {
            Theater theater = theaterRepository.findById(showtimeDetails.getTheater().getId())
                    .orElseThrow(() -> new RuntimeException("Theater not found with id: " + showtimeDetails.getTheater().getId()));
            showtime.setTheater(theater);
        }

        if (showtimeDetails.getShowtime() != null) {
            showtime.setShowtime(showtimeDetails.getShowtime());
        }

        if (showtimeDetails.getPrice() != null) {
            showtime.setPrice(showtimeDetails.getPrice());
        }

        if (showtimeDetails.getAvailableSeats() != null) {
            showtime.setAvailableSeats(showtimeDetails.getAvailableSeats());
        }

        return showtimeRepository.save(showtime);
    }

    @Override
    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + id));
        showtimeRepository.delete(showtime);
    }

    @Override
    public Showtime updateAvailableSeats(Long id, int numberOfSeats) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + id));

        if (showtime.getAvailableSeats() < numberOfSeats) {
            throw new RuntimeException("Not enough seats available");
        }

        showtime.setAvailableSeats(showtime.getAvailableSeats() - numberOfSeats);
        return showtimeRepository.save(showtime);
    }
}
