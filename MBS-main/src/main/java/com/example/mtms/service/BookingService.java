package com.example.mtms.service;

import com.example.mtms.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(Booking booking);
    List<Booking> getAllBookings();
    Optional<Booking> getBookingById(Long id);
    List<Booking> getBookingsByUserId(Long userId);
    Booking updateBooking(Long id, Booking booking);
    void deleteBooking(Long id);
}
