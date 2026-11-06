package ch.bbw.accounts;


import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


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
    }

    /**
     * Testet das Abheben von einem Konto.
     */
    @Test
    @DisplayName("Simple withdraw from SalaryAccount should work")
    public void testWithdraw() throws  InvalidAmountException, InvalidDateException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        sav1.deposit(Scheduled.now(), 15,  "This is a test text");
        sav1.withdraw(Scheduled.now(), 13,  "This is a test text");
        assertEquals(2, sav1.getBalance());
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

}
