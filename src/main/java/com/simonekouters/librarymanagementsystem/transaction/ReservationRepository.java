package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.book.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findByBookAndStatus(Book book, ReservationStatus status);
}
