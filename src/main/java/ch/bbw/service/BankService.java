package ch.bbw.service;

import ch.bbw.Bank;
import ch.bbw.Scheduled;
import ch.bbw.accounts.Account;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service between the REST controllers and the bank. The controllers never call the bank directly, so the web layer can be replaced later.
 * Spring serves every HTTP request in its own thread, but the TreeMap in the bank is not thread safe, so every method is synchronized.
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
@Service
public class BankService {
    /**
     * The bank, injected by Spring
     */
    private final Bank bank;

    /**
     * Creates a new bank service
     *
     * @param bank the bank to use
     */
    public BankService(Bank bank) {
        this.bank = bank;
    }

    /**
     * Creates a new savings account
     *
     * @return the new account id
     */
    public synchronized String createSavingsAccount() {
        return bank.createSavingsAccount();
    }

    /**
     * Creates a new promo youth savings account
     *
     * @return the new account id
     */
    public synchronized String createPromoYouthSavingsAccount() {
        return bank.createPromoYouthSavingsAccount();
    }

    /**
     * Creates a new salary account
     *
     * @param creditLimit the credit limit (0 or negative)
     * @return the new account id
     * @throws IllegalArgumentException if the credit limit is positive
     */
    public synchronized String createSalaryAccount(long creditLimit) {
        return bank.createSalaryAccount(creditLimit);
    }

    /**
     * Returns the account with the given id
     *
     * @param id the account id
     * @return the account
     * @throws InvalidAmountException an AccountNotFoundException if no account with this id exists
     */
    public synchronized Account getAccount(String id) throws InvalidAmountException {
        return bank.getAccount(id);
    }

    /**
     * Deposits the given amount on an account
     *
     * @param id the account id
     * @param date the transaction date
     * @param amount the amount to deposit
     * @param text the transaction text, can be empty
     * @return the new balance of the account
     * @throws InvalidAmountException if the account doesn't exist or the amount is not allowed
     * @throws InvalidDateException if the date is in the past
     */
    public synchronized long deposit(String id, Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        bank.deposit(id, date, amount, text);

        return bank.getBalance(id);
    }

    /**
     * Withdraws the given amount from an account
     *
     * @param id the account id
     * @param date the transaction date
     * @param amount the amount to withdraw
     * @param text the transaction text, can be empty
     * @return the new balance of the account
     * @throws InvalidAmountException if the account doesn't exist or the rule of the account type doesn't allow the amount
     * @throws InvalidDateException if the date is in the past
     */
    public synchronized long withdraw(String id, Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        return bank.withdraw(id, date, amount, text);
    }

    /**
     * Returns the balance of all accounts together
     *
     * @return the balance of the bank
     */
    public synchronized long getBalance() {
        return bank.getBalance();
    }

    /**
     * Returns the 5 accounts with the highest balance
     *
     * @return the accounts, the highest first
     */
    public synchronized List<Account> top5HighestBalances() {
        return bank.top5HighestBalances();
    }

    /**
     * Returns the 5 accounts with the lowest balance
     *
     * @return the accounts, the lowest first
     */
    public synchronized List<Account> top5LowestBalances() {
        return bank.top5LowestBalances();
    }
}
