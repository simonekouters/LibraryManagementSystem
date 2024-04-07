package com.simonekouters.librarymanagementsystem.exceptions;

public class BorrowingLimitExceededException extends RuntimeException {
    public BorrowingLimitExceededException() {
        super("Reached borrowing limit.");
    }
}
