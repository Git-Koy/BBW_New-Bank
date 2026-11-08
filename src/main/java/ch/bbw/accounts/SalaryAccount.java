package ch.bbw.accounts;

import ch.bbw.MoneyFormatter;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

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
    private long creditLimit;

    /**
     * Creates a new salary account
     *
     * @param id the account number
     * @param creditLimit the credit limit
     */
    public SalaryAccount(String id, long creditLimit)
    {
        super(id);
        this.creditLimit = creditLimit;

        if (creditLimit > 0) {
            throw new IllegalArgumentException("Credit limit can't be positive");
        }
    }

    public long getCreditLimit() {
        return creditLimit;
    }

    @Override
    public long withdraw(Scheduled date, long amount, String text ) throws IllegalArgumentException, InvalidDateException, InvalidAmountException {
        if (getBalance() - amount < creditLimit) {
            throw new InvalidAmountException("Amount is greater than credit limit of " + MoneyFormatter.toCHF(creditLimit));
        }

        return super.withdraw(date, amount, text);
    }

    @Override
    public String getAccountType() {
        return "Salary Account";
    }

}