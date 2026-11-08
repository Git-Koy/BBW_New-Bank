package ch.bbw.exceptions;

/**
 * Thrown when a transaction date is in the past
 *
 * @author Luigi Cavuoti, lro@gmx.ch
 * @version 1.0
 */
public class InvalidDateException extends Exception {
    /**
     * Creates a new exception
     *
     * @param message the reason why the date is not allowed
     */
    public InvalidDateException(String message) {
        super(message);
    }
}
