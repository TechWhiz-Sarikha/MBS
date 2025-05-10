package com.example.mtms.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column(nullable = false, length = 10)
    private String seatNumber;

    @Column(length = 50)
    private String seatType;
}
