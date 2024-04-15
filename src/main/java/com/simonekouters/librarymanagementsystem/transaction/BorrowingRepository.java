package com.simonekouters.librarymanagementsystem.transaction;

import org.springframework.data.repository.CrudRepository;

public interface BorrowingRepository extends CrudRepository<Borrowing, Long> {
}
