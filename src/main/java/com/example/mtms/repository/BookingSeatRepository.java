package com.example.mtms.repository;

import com.example.mtms.entity.BookingSeat;
import com.example.mtms.entity.BookingSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, BookingSeatId> {
}
