package ch.bbw;

/**
 * The account types with the prefix of their account id. The type and its prefix are kept together, so they can't drift apart.
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public enum AccountType {
    /**
     * Savings account, the account id starts with S
     */
    SAVINGS("S"),
    /**
     * Promo youth savings account, the account id starts with Y
     */
    PROMO_YOUTH("Y"),
    /**
     * Salary account, the account id starts with P
     */
    SALARY("P");

    /**
     * The prefix of the account id
     */
    private final String prefix;

    /**
     * Creates a new account type
     *
     * @param prefix the prefix of the account id
     */
    AccountType(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the prefix of the account id
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }
}
