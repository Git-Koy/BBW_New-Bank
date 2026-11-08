package ch.bbw.web.dto;

import ch.bbw.accounts.Account;

/**
 * An account as it is sent over HTTP. The domain class Account never leaves the server.
 *
 * @param id the account id
 * @param type the name of the account type
 * @param balance the balance in millirappen
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public record AccountResponse(String id, String type, long balance) {
    /**
     * Creates the response from an account
     *
     * @param account the account
     * @return the response
     */
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getId(), account.getAccountType(), account.getBalance());
    }
}
