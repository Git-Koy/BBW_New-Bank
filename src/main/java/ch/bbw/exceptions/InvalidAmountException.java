package ch.bbw.exceptions;

/**
 * Thrown when an amount is not allowed, for example 0, negative or more than the account allows
 *
 * @author Luigi Cavuoti, lro@gmx.ch
 * @version 1.0
 */
public class InvalidAmountException extends Exception {
    /**
     * Creates a new exception
     *
     * @param message the reason why the amount is not allowed
     */
    public InvalidAmountException(String message) {
        super(message);
    }
}
