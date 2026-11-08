package ch.bbw.web;

import ch.bbw.exceptions.AccountNotFoundException;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import ch.bbw.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

/**
 * Turns the exceptions of the domain into JSON answers with a fitting HTTP status, so the client never gets a 500 for an expected error.
 * Spring always picks the most specific handler, so AccountNotFoundException gets 404 even though it is an InvalidAmountException.
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
@RestControllerAdvice
public class ErrorHandler {
    /**
     * An unknown account id
     *
     * @param exception the exception
     * @return 404 with the error message
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(AccountNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    /**
     * An amount that is not allowed, a date in the past, a date that can't be read or a positive credit limit
     *
     * @param exception the exception
     * @return 400 with the error message
     */
    @ExceptionHandler({InvalidAmountException.class, InvalidDateException.class, DateTimeParseException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }
}
