package com.example.mtms.service;

import com.example.mtms.entity.Theater;

import java.util.List;
import java.util.Optional;

public interface TheaterService {
    Theater createTheater(Theater theater);
    List<Theater> getAllTheaters();
    Optional<Theater> getTheaterById(Long id);
    Theater updateTheater(Long id, Theater theater);
    void deleteTheater(Long id);
}
