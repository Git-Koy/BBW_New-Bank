package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class PromoYouthSavingsAccountTests {
    /**
     * There is a withdrawal limit of CHF 200 per month
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("The monthly withdrawal limit works")
    public void testWithdrawLimit() throws InvalidDateException, InvalidAmountException {
        PromoYouthSavingsAccount pys1 = new PromoYouthSavingsAccount("CH-01");
        pys1.deposit(Scheduled.now(), 30_000_000, "This is a test text");
        pys1.withdraw(Scheduled.now(), 20_000_000, "This is a test text");

        assertEquals(10_000_000, pys1.getBalance());
        assertThrows(InvalidAmountException.class, () -> {pys1.withdraw(Scheduled.now(), 1, "This is a test text");});
    }

    /**
     * The monthly limit counts all withdrawals of the month together
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("The monthly limit over several withdrawals works")
    public void testWithdrawLimitOverSeveralWithdrawals() throws InvalidDateException, InvalidAmountException {
        PromoYouthSavingsAccount pys1 = new PromoYouthSavingsAccount("CH-01");
        pys1.deposit(Scheduled.now(), 30_000_000, "This is a test text");
        pys1.withdraw(Scheduled.now(), 10_000_000, "This is a test text");
        pys1.withdraw(Scheduled.now(), 10_000_000, "This is a test text");

        assertEquals(10_000_000, pys1.getBalance());
        assertThrows(InvalidAmountException.class, () -> {pys1.withdraw(Scheduled.now(), 1, "This is a test text");});
    }

    /**
     * The monthly limit starts again in the next month
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("The monthly limit starting again in the next month works")
    public void testWithdrawLimitNextMonth() throws InvalidDateException, InvalidAmountException {
        PromoYouthSavingsAccount pys1 = new PromoYouthSavingsAccount("CH-01");
        pys1.deposit(Scheduled.now(), 30_000_000, "This is a test text");
        pys1.withdraw(Scheduled.now(), 20_000_000, "This is a test text");
        pys1.withdraw(Scheduled.now().plus(31, ChronoUnit.DAYS), 1, "This is a test text");

        assertEquals(9_999_999, pys1.getBalance());
    }

    /**
     * A promo youth savings account can't be overdrawn, a rejected withdrawal adds no booking
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("A promo youth savings account can't be overdrawn")
    public void testNoOverdraft() throws InvalidDateException, InvalidAmountException {
        PromoYouthSavingsAccount pys1 = new PromoYouthSavingsAccount("CH-01");
        pys1.deposit(Scheduled.now(), 15, "This is a test text");

        assertThrows(InvalidAmountException.class, () -> {pys1.withdraw(Scheduled.now(), 16, "This is a test text");});
        assertEquals(15, pys1.getBalance());
        assertEquals(1, pys1.getBookings().size());
    }

    /**
     * A promo youth savings account is a savings account with an extra rule
     */
    @Test
    @DisplayName("A promo youth savings account is a savings account")
    public void testIsSavingsAccount() {
        Account pys1 = new PromoYouthSavingsAccount("CH-01");

        assertTrue(pys1 instanceof SavingsAccount);
    }

    /**
     * The account type of a promo youth savings account
     */
    @Test
    @DisplayName("The account type shows up correctly")
    public void testAccountType() {
        PromoYouthSavingsAccount pys1 = new PromoYouthSavingsAccount("CH-01");

        assertEquals("Promo Youth Savings Account", pys1.getAccountType());
    }
}
