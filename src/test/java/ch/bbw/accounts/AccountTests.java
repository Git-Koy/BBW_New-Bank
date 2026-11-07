package ch.bbw.accounts;


import ch.bbw.Booking;
import ch.bbw.MoneyFormatter;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests für die Klasse Account.
 *
 * @author luigicavuoti
 * @version 2.0
 */
public class AccountTests {
    /**
     * Tested die Initialisierung eines Kontos.
     */
    @Test
    @DisplayName("Simple constructor-Test for SavingsAccount should work")
    public void testInit() {
        SavingsAccount sav1 = new SavingsAccount("CH-01");

        assertEquals("CH-01", sav1.getId());
    }

    /**
     * Testet das Einzahlen auf ein Konto.
     */
    @Test
    @DisplayName("Simple deposit on SavingsAccount should work")
    public void testDeposit() throws InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");

        sav1.deposit(Scheduled.now(), 15, "This is a test text");

        assertEquals(15, sav1.getBalance());
        assertEquals(1, sav1.getBookings().size());
        assertTrue(sav1.getBookings().get(0).amount() > 0);
        assertEquals("This is a test text", sav1.getBookings().get(0).text());
    }

    /**
     * Testet das Abheben von einem Konto.
     */
    @Test
    @DisplayName("Simple withdraw from SavingsAccount should work")
    public void testWithdraw() throws  InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        sav1.deposit(Scheduled.now(), 3, "This is a test text");
        sav1.withdraw(Scheduled.now(), 1,  "This is a test text");

        assertEquals(2, sav1.getBalance());
        assertEquals(2, sav1.getBookings().size());
        assertTrue(sav1.getBookings().get(1).amount() < 0);
        assertEquals("This is a test text", sav1.getBookings().get(1).text());
    }

    @Test
    @DisplayName("TreeMap of two Account-References should work")
    public void testReferences() {
        fail("ToDo");
    }

    @Test
    @DisplayName("TreeMap of two Account-References should work")
    public void testCanTransact() {
        fail("ToDo");
    }

    /**
     * Experimente mit print().
     */
    @Test
    @DisplayName("Not a real test, it prints a list of bookings")
    public void testPrint() throws  InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        sav1.deposit(Scheduled.now(), 15,   "This is a test text");
        sav1.withdraw(Scheduled.now(), 13,  "This is a test text");
        System.out.println("Your balance is:" + sav1.getBalance());
    }

    /**
     * Experimente mit print(year,month).
     */
    @Test
    @DisplayName("Not a real test, it prints a list of bookings")
    public void testMonthlyPrint() throws InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        sav1.deposit(Scheduled.now(), 15,    "This is a test text");
        sav1.withdraw(Scheduled.now(), 13, "This is a test text");
        System.out.println("Your monthly balance is:" + sav1.getBalance());
    }

    /**
     * Tests if a new account has a balance of 0
     */
    @Test
    @DisplayName("Account starting with a balance of 0 should work")
    public void testAccountStartingWithBalance0() throws InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        assertEquals(0, sav1.getBalance());
        assertEquals(0, sav1.getBookings().size());
    }

    /**
     * Tests if the balance after n operations equals the sum of the booking amounts
     */
    @Test
    @DisplayName("Sum of booking amount equaling sum of operations should work")
    public void testAccountBookingSumEqualsOperationSum() throws InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        sav1.deposit(Scheduled.now(), 15, "This is a test text");
        sav1.withdraw(Scheduled.now(), 13,  "This is a test text");
        sav1.withdraw(Scheduled.now(), 1,  "This is a test text");
        sav1.deposit(Scheduled.now(), 23,  "This is a test text");

        long sum = 0;
        for (Booking b : sav1.getBookings()) {
            sum += b.amount();
        }

        assertEquals(sum, sav1.getBalance());
        assertEquals(4, sav1.getBookings().size());
    }

    /**
     * Makes sure that getBookings() cannot be used to modify the account
     */
    @Test
    @DisplayName("Account being unable to be edited by getBookings() should work")
    public void testAccountCannotBeModifiedbyGetBookings() throws InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        sav1.deposit(Scheduled.now(), 15,  "This is a test text");

        assertThrows(UnsupportedOperationException.class, () -> sav1.getBookings().add(new Booking(0, 1, "This is a test text")));
    }

    /**
     * A rejected operation should add no booking and leave the balance unchanged
     */
    @Test
    @DisplayName("Rejected operations leaving the balance and bookings unchanged should work")
    public void testRejectedOperationsLeavingTheBalanceAndBookingsUnchanged() throws InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        sav1.deposit(Scheduled.now(), 15,  "This is a test text");

        assertThrows(InvalidAmountException.class, () -> sav1.deposit(Scheduled.now(), 0,  "This is a test text"));
        assertThrows(InvalidDateException.class, () -> sav1.deposit(Scheduled.now().minus(2, ChronoUnit.MILLIS), 1,  "This is a test text"));
        assertEquals(1, sav1.getBookings().size());
        assertEquals(15, sav1.getBalance());

    }

    /**
     * Tests if a transaction fails if the amount is greater than the balance of a SavingsAccount
     */
    @Test
    @DisplayName("The amount of a transaction not being allowed to be greater than the balance of a SavingsAccount should work")
    public void testAmountIsNotGreaterThanTheBalanceOfASavingsAccount() throws InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        sav1.deposit(Scheduled.now(), 15,  "This is a test text");

        assertThrows(InvalidAmountException.class, () -> sav1.withdraw(Scheduled.now(), 16,  "This is a test text"));
        assertEquals(1, sav1.getBookings().size());
    }

    /**
     * Tests if the formating of currency works
     */
    @Test
    @DisplayName("Formatting currency should work")
    public void testFormatCurrency() throws InvalidAmountException, InvalidDateException {
        assertEquals("CHF 1.23456", MoneyFormatter.toCHF(123456));
        assertEquals("CHF 1.00001", MoneyFormatter.toCHF(100001));
        assertEquals("CHF -1.00001", MoneyFormatter.toCHF(-100001));
    }
}