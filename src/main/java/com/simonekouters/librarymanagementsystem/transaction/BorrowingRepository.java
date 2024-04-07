package com.simonekouters.librarymanagementsystem.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
}
