package com.simonekouters.librarymanagementsystem.exceptions;

public class ReservationLimitExceededException extends RuntimeException {
    public ReservationLimitExceededException() {
        super("Reservation limit exceeded");
    }
}
