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
     * @param text the transaction text
     * @return the new balance of the account
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
     */
    public long deposit(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        return deposit(date, amount, null);
    }

    /**
     * Hebt den gegebenen Betrag vom Konto ab.
     *
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag
     * @param text der Transaktionstext
     * @return boolean <code>true</code>, falls die
     * Abhebung erfolgreich war, andernfalls (z.B.
     * bei negativem Betrag) <code>false</code>.
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
     * @return boolean <code>true</code>, falls die
     * Abhebung erfolgreich war, andernfalls (z.B.
     * bei negativem Betrag) <code>false</code>.
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
}
