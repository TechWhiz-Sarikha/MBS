package com.example.mtms.service.impl;

import com.example.mtms.entity.Booking;
import com.example.mtms.entity.BookingSeat;
import com.example.mtms.entity.Seat;
import com.example.mtms.entity.User;
import com.example.mtms.entity.Showtime;
import com.example.mtms.repository.BookingRepository;
import com.example.mtms.repository.BookingSeatRepository;
import com.example.mtms.repository.SeatRepository;
import com.example.mtms.repository.ShowtimeRepository;
import com.example.mtms.repository.UserRepository;
import com.example.mtms.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              ShowtimeRepository showtimeRepository,
                              SeatRepository seatRepository,
                              BookingSeatRepository bookingSeatRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    @Override
    public Booking createBooking(Booking booking) {
        // Load User entity
        Long userId = booking.getUser() != null ? booking.getUser().getId() : null;
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required for booking");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Load Showtime entity
        Long showtimeId = booking.getShowtime() != null ? booking.getShowtime().getId() : null;
        if (showtimeId == null) {
            throw new IllegalArgumentException("Showtime ID is required for booking");
        }
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found with id: " + showtimeId));

        // Set booking time to now
        booking.setBookingTime(LocalDateTime.now());

        // Set user and showtime entities
        booking.setUser(user);
        booking.setShowtime(showtime);

        // Prepare BookingSeats set
        Set<BookingSeat> bookingSeats = new HashSet<>();

        if (booking.getBookingSeats() != null) {
            for (BookingSeat bs : booking.getBookingSeats()) {
                Long seatId = bs.getSeat() != null ? bs.getSeat().getId() : null;
                if (seatId == null) {
                    throw new IllegalArgumentException("Seat ID is required for booking seat");
                }
                Seat seat = seatRepository.findById(seatId)
                        .orElseThrow(() -> new IllegalArgumentException("Seat not found with id: " + seatId));

                BookingSeat bookingSeat = new BookingSeat();
                bookingSeat.setBooking(booking);
                bookingSeat.setSeat(seat);
                bookingSeats.add(bookingSeat);
            }
        }

        booking.setBookingSeats(bookingSeats);

        // Save booking (cascade will save bookingSeats)
        Booking savedBooking = bookingRepository.save(booking);

        // Save bookingSeats explicitly if needed (optional)
        bookingSeatRepository.saveAll(bookingSeats);

        return savedBooking;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setNumTickets(bookingDetails.getNumTickets());
        booking.setBookingTime(bookingDetails.getBookingTime());
        booking.setShowtime(bookingDetails.getShowtime());
        booking.setUser(bookingDetails.getUser());
        booking.setBookingSeats(bookingDetails.getBookingSeats());

        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        bookingRepository.delete(booking);
    }
}
