package com.example.mtms.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BookingSeatId.class)
public class BookingSeat {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id")
    private Seat seat;
}
