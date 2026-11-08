package ch.bbw.exceptions;

/**
 * Thrown when no account with the given id exists. It extends InvalidAmountException, so all code that already catches InvalidAmountException still works,
 * but the REST API can answer with 404 instead of 400.
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public class AccountNotFoundException extends InvalidAmountException {
    /**
     * Creates a new exception
     *
     * @param id the account id that doesn't exist
     */
    public AccountNotFoundException(String id) {
        super("No account with the id " + id);
    }
}
