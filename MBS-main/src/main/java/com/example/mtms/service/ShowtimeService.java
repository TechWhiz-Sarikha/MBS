package com.example.mtms.service;

import com.example.mtms.entity.Showtime;

import java.util.List;
import java.util.Optional;

public interface ShowtimeService {
    Showtime createShowtime(Showtime showtime);
    List<Showtime> getAllShowtimes();
    Optional<Showtime> getShowtimeById(Long id);
    List<Showtime> getShowtimesByMovie(Long movieId);
    List<Showtime> getShowtimesByTheater(Long theaterId);
    Showtime updateShowtime(Long id, Showtime showtime);
    void deleteShowtime(Long id);
    Showtime updateAvailableSeats(Long id, int numberOfSeats);
}
