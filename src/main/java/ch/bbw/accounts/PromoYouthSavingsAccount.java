package ch.bbw.accounts;

import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;

/**
 * Promo youth savings account. It extends Account instead of SavingsAccount so it can stay independent, even though that means the no-overdraft rule needs to be duplicated.
 *
 * @author Kay Ježek, kay@jeschis.ch
 * @version 1.0
 */
public class PromoYouthSavingsAccount extends Account{

    /**
     * The monthly limit on the account
     */
    private long monthlyLimit = 20_000_000;

    /**
     * Creates a new promo youth savings account
     * @param id the account id
     */
    public PromoYouthSavingsAccount(String id) {
        super(id);
    }

    /**
     * Withdraw has a limit of CHF 200 that cannot be exceeded
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag
     * @param text der Transaktionstext
     * @return
     * @throws InvalidAmountException
     * @throws InvalidDateException
     */
    @Override
    public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        if (getBalance() < amount)
            throw new InvalidAmountException("Amount is greater than balance");

        long allowance = 0;


        for (Booking b : getBookings())  {
            YearMonth bookingMonth = YearMonth.from(Instant.ofEpochMilli(b.date()).atZone(ZoneId.of("Europe/Zurich")));
            YearMonth targetMonth = YearMonth.from(date.toInstant().atZone(ZoneId.of("Europe/Zurich")));

            if (bookingMonth.equals(targetMonth) && b.amount() < 0) {
                allowance += -b.amount();
            }
        }

        if (allowance + amount > monthlyLimit) {
            throw new InvalidAmountException("Amount is greater than monthly limit");
        }

        return super.withdraw(date, amount, text);
    }

    @Override
    public String getAccountType() {
        return "Promo Youth Savings Account";
    }

}
