package com.simonekouters.librarymanagementsystem.exceptions;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BadInputException.class)
    public ResponseEntity<ProblemDetail> badInputExceptionHandler(BadInputException exception) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> notFoundExceptionHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ProblemDetail> bookNotAvailableExceptionHandler(BookNotAvailableException exception) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(BorrowingLimitExceededException.class)
    public ResponseEntity<ProblemDetail> borrowingLimitExceededExceptionHandler(BorrowingLimitExceededException exception) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
