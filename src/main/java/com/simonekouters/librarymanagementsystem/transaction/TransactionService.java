package com.simonekouters.librarymanagementsystem.transaction;

import com.simonekouters.librarymanagementsystem.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
