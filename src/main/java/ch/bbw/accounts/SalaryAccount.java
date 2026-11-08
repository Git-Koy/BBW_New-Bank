package ch.bbw.accounts;

/**
 * Salary account
 *
 * @author Kay Ježek, kay@jeschis.ch
 * @version 1.0
 */
public class SalaryAccount extends Account{
    /**
     * The credit limit
     */
    private long creditlimit;

    /**
     * Creates a new salary account
     *
     * @param id the account number
     * @param creditlimit the credit limit
     */
    public SalaryAccount(String id, long creditlimit)
    {
        super(id);
        this.creditlimit = creditlimit;
    }
}