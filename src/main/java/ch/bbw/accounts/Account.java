package ch.bbw.accounts;

import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import ch.bbw.MoneyFormatter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Konto.
 *
 * @author Luigi Cavuoti, lro@gmx.ch
 * @version 2.1
 */
public abstract class Account {
    /**
     * Die Kontonummer (kann auch Buchstaben und Sonderzeichen enthalten).
     */
    private final String id;
    /**
     * Die Buchungen.
     */
    private final List<Booking> bookings;

    /**
     * Erzeugt ein neues Konto.
     *
     * @param id die Kontonummer
     */
    public Account(String id) {
        this.id = id;
        this.bookings = new ArrayList<Booking>();
    }

    /**
     * Gibt die Kontonummer zurück.
     *
     * @return die Kontonummer
     */
    public String getId() {
        return id;
    }

    /**
     * Gibt das Saldo zur�ck.
     *
     * @return Saldo
     */
    public long getBalance() {
        return bookings.stream()
                .mapToLong(Booking::amount)
                .sum();
    }

    /**
     * Checks if the transaction date is not in the past.
     *
     * @param date the transaction date
     * @return whether the transaction date is valid
     */
    public boolean isInvalidTransaction(Scheduled date) {
        return date.isPastDue();
    }

    /**
     * Deposits the given amount to the account.
     *
     * @param date   the transaction date
     * @param amount the amount to deposit
     * @param text the transaction text, without a text the booking gets "Einzahlung"
     * @return the new balance of the account
     * @throws InvalidAmountException if the amount is 0 or negative
     * @throws InvalidDateException if the date is in the past
     */
    public long deposit(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        if (amount <= 0)
            throw new InvalidAmountException("amount must be greater than 0");

        if (isInvalidTransaction(date))
            throw new InvalidDateException("date mustn't be in the past");

        bookings.add(new Booking(date.toUnix(), amount, text));

        return getBalance();
    }

    /**
     * Deposits the given amount to the account.
     *
     * @param date   the transaction date
     * @param amount the amount to deposit
     * @return the new balance of the account
     * @throws InvalidAmountException if the amount is 0 or negative
     * @throws InvalidDateException if the date is in the past
     */
    public long deposit(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        return deposit(date, amount, null);
    }

    /**
     * Hebt den gegebenen Betrag vom Konto ab.
     *
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag
     * @param text der Transaktionstext, ohne Text bekommt die Buchung "Auszahlung"
     * @return der neue Saldo
     * @throws InvalidAmountException if the amount is 0 or negative
     * @throws InvalidDateException if the date is in the past
     */
    public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        if (amount <= 0)
            throw new InvalidAmountException("amount must be greater than 0");

        if (isInvalidTransaction(date))
            throw new InvalidDateException("date mustn't be in the past");

        // Achtung: hier Buchung mit negativem Betrag!
        bookings.add(new Booking(date.toUnix(), -amount, text));

        return getBalance();
    }

    /**
     * Hebt den gegebenen Betrag vom Konto ab.
     *
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag
     * @return der neue Saldo
     * @throws InvalidAmountException if the amount is 0 or negative
     * @throws InvalidDateException if the date is in the past
     */
    public long withdraw(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        return withdraw(date, amount, null);
    }

    /**
     * Returns a copy of bookings of an account
     *
     * @return a copy of the Bookings
     */
    public List<Booking> getBookings() {
        return List.copyOf(bookings);
    }

    /**
     * Returns the transactions and balance of an account
     *
     * @return formatted string with transaction history and balance
     */
    public String printStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append("#-------------------- Your Account --------------------#").append("\n");
        for (Booking b : bookings) {
            sb.append(Instant.ofEpochMilli(b.date())).append(" ").append(MoneyFormatter.toCHF(b.amount())).append(" ").append(b.text()).append("\n");
        }
        sb.append("#------------- Your Balance is: ").append(MoneyFormatter.toCHF(getBalance())).append("------------#").append("\n");
        sb.append("#----------------------------------------------------#");

        return sb.toString();
    }

    /**
     * Returns the name of the account type.
     * @return the name of the account type.
     */
    public abstract String getAccountType();
}
