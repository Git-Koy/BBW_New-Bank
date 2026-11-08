package ch.bbw.web;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import ch.bbw.service.BankService;
import ch.bbw.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * REST endpoints for the accounts. Only DTOs cross the HTTP boundary, the domain classes stay inside the server.
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {
    /**
     * The service that talks to the bank
     */
    private final BankService bankService;

    /**
     * Creates a new account controller
     *
     * @param bankService the service that talks to the bank
     */
    public AccountController(BankService bankService) {
        this.bankService = bankService;
    }

    /**
     * Creates a new savings account
     *
     * @return the new account id
     */
    @PostMapping("/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountIdResponse createSavingsAccount() {
        return new AccountIdResponse(bankService.createSavingsAccount());
    }

    /**
     * Creates a new promo youth savings account
     *
     * @return the new account id
     */
    @PostMapping("/promo-youth")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountIdResponse createPromoYouthSavingsAccount() {
        return new AccountIdResponse(bankService.createPromoYouthSavingsAccount());
    }

    /**
     * Creates a new salary account
     *
     * @param request the credit limit
     * @return the new account id
     * @throws IllegalArgumentException if the credit limit is positive, answered with 400
     */
    @PostMapping("/salary")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountIdResponse createSalaryAccount(@RequestBody SalaryAccountRequest request) {
        return new AccountIdResponse(bankService.createSalaryAccount(request.creditLimit()));
    }

    /**
     * Returns an account with its type and balance
     *
     * @param id the account id
     * @return the account
     * @throws InvalidAmountException if the account doesn't exist, answered with 404
     */
    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable("id") String id) throws InvalidAmountException {
        return AccountResponse.from(bankService.getAccount(id));
    }

    /**
     * Returns the balance of an account
     *
     * @param id the account id
     * @return the balance
     * @throws InvalidAmountException if the account doesn't exist, answered with 404
     */
    @GetMapping("/{id}/balance")
    public BalanceResponse getBalance(@PathVariable("id") String id) throws InvalidAmountException {
        return new BalanceResponse(bankService.getAccount(id).getBalance());
    }

    /**
     * Returns all bookings of an account, the oldest first
     *
     * @param id the account id
     * @return the bookings
     * @throws InvalidAmountException if the account doesn't exist, answered with 404
     */
    @GetMapping("/{id}/bookings")
    public List<BookingResponse> getBookings(@PathVariable("id") String id) throws InvalidAmountException {
        List<BookingResponse> bookings = new ArrayList<>();

        for (var booking : bankService.getAccount(id).getBookings()) {
            bookings.add(BookingResponse.from(booking));
        }

        return bookings;
    }

    /**
     * Deposits an amount on an account
     *
     * @param id the account id
     * @param request the amount, the date (optional) and the text (optional)
     * @return the new balance
     * @throws InvalidAmountException if the account doesn't exist (404) or the amount is not allowed (400)
     * @throws InvalidDateException if the date is in the past, answered with 400
     */
    @PostMapping("/{id}/deposit")
    public BalanceResponse deposit(@PathVariable("id") String id, @RequestBody TransactionRequest request) throws InvalidAmountException, InvalidDateException {
        return new BalanceResponse(bankService.deposit(id, toScheduled(request.date()), request.amount(), request.text()));
    }

    /**
     * Withdraws an amount from an account
     *
     * @param id the account id
     * @param request the amount, the date (optional) and the text (optional)
     * @return the new balance
     * @throws InvalidAmountException if the account doesn't exist (404) or the rule of the account type doesn't allow the amount (400)
     * @throws InvalidDateException if the date is in the past, answered with 400
     */
    @PostMapping("/{id}/withdraw")
    public BalanceResponse withdraw(@PathVariable("id") String id, @RequestBody TransactionRequest request) throws InvalidAmountException, InvalidDateException {
        return new BalanceResponse(bankService.withdraw(id, toScheduled(request.date()), request.amount(), request.text()));
    }

    /**
     * Converts the date from the request into a Scheduled. The API uses ISO-8601 text, so the inner representation of Scheduled
     * (milliseconds until due) never leaks into the API.
     *
     * @param date the date as ISO-8601 text, empty means now
     * @return the Scheduled date
     * @throws java.time.format.DateTimeParseException if the text is not an ISO-8601 date, answered with 400
     */
    private Scheduled toScheduled(String date) {
        if (date == null || date.isBlank()) {
            return Scheduled.now();
        }

        long millisecondsUntilDue = Instant.parse(date).toEpochMilli() - System.currentTimeMillis();

        return new Scheduled(millisecondsUntilDue);
    }
}
