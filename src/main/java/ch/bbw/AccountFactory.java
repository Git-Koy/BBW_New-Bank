package ch.bbw;

import ch.bbw.accounts.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Account factory. It is the only place that decides which account type gets created and which id it gets, so Bank never needs to know the concrete account classes.
 * This is a simple factory (one class with a switch) and not the Factory Method pattern, where every account type would have its own creator subclass.
 * All account types share one id counter (S-1000, Y-1001, P-1002), so the number alone is already unique and an account can't be mixed up with another type.
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public class AccountFactory {
    /**
     * Next account id. It is an AtomicLong, so two threads can never get the same id.
     */
    private final AtomicLong nextAccountId;

    /**
     * Creates a new account factory, the ids start at 1000
     */
    public AccountFactory() {
        this(1000);
    }

    /**
     * Creates a new account factory, so tests can choose the first id they expect
     *
     * @param startId the first account id
     */
    public AccountFactory(long startId) {
        this.nextAccountId = new AtomicLong(startId);
    }

    /**
     * Creates a new account of the given type. Bank uses the three named methods instead, because the credit limit is meaningless for savings accounts
     * and the named methods don't need that useless parameter.
     *
     * @param type the account type
     * @param creditLimit the credit limit, only used for salary accounts
     * @return the new account
     * @throws IllegalArgumentException if the type is missing or the credit limit of a salary account is positive
     */
    public Account create(AccountType type, long creditLimit) {
        if (type == null) {
            throw new IllegalArgumentException("Account type can't be null");
        }

        String id = type.getPrefix() + "-" + nextAccountId.getAndIncrement();

        return switch (type) {
            case SAVINGS -> new SavingsAccount(id);
            case PROMO_YOUTH -> new PromoYouthSavingsAccount(id);
            case SALARY -> new SalaryAccount(id, creditLimit);
        };
    }

    /**
     * Creates a new promo youth savings account
     *
     * @return the new account
     */
    public Account createPromoYouthSavingsAccount() {
        return create(AccountType.PROMO_YOUTH, 0);
    }

    /**
     * Creates a new salary account
     *
     * @param creditLimit the credit limit (negative number)
     * @return the new account
     * @throws IllegalArgumentException if the credit limit is positive
     */
    public Account createSalaryAccount(long creditLimit) {
        return create(AccountType.SALARY, creditLimit);
    }

    /**
     * Creates a new savings account
     *
     * @return the new account
     */
    public Account createSavingsAccount(){
        return create(AccountType.SAVINGS, 0);
    }

}
