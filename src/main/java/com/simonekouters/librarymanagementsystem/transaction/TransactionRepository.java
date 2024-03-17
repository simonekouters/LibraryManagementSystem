package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
