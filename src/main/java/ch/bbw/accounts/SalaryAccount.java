package ch.bbw.accounts;

import ch.bbw.MoneyFormatter;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

/**
 * Salary account. It extends Account and not SavingsAccount, because it may be overdrawn down to the credit limit and the no-overdraft rule of the savings account would forbid that.
 * The credit limit is a negative number or 0, with 0 it works like a savings account.
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
     * @throws IllegalArgumentException if the credit limit is positive
     */
    public SalaryAccount(String id, long creditLimit)
    {
        super(id);
        this.creditLimit = creditLimit;

        if (creditLimit > 0) {
            throw new IllegalArgumentException("Credit limit can't be positive");
        }
    }

    /**
     * Returns the credit limit
     *
     * @return the credit limit, a negative number or 0
     */
    public long getCreditLimit() {
        return creditLimit;
    }

    /**
     * Withdraws the given amount, the balance may go down to the credit limit but not below
     *
     * @param date the transaction date
     * @param amount the amount to withdraw
     * @param text the transaction text
     * @return the new balance
     * @throws InvalidAmountException if the amount is 0, negative or the balance would go below the credit limit
     * @throws InvalidDateException if the date is in the past
     */
    @Override
    public long withdraw(Scheduled date, long amount, String text ) throws InvalidDateException, InvalidAmountException {
        if (getBalance() - amount < creditLimit) {
            throw new InvalidAmountException("Amount is greater than credit limit of " + MoneyFormatter.toCHF(creditLimit));
        }

        return super.withdraw(date, amount, text);
    }

    /**
     * Returns the name of the account type
     *
     * @return "Salary Account"
     */
    @Override
    public String getAccountType() {
        return "Salary Account";
    }

}