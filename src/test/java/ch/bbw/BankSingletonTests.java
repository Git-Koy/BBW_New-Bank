package ch.bbw;

import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the singleton of the class Bank
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public class BankSingletonTests {
    /**
     * getInstance() always returns the same bank
     */
    @Test
    @DisplayName("getInstance() returning the same bank every time should work")
    public void testGetInstanceReturnsSameBank() {
        Bank bank1 = Bank.getInstance();
        Bank bank2 = Bank.getInstance();

        assertSame(bank1, bank2);
    }

    /**
     * The bank has no public constructor, so nobody can create a second bank
     */
    @Test
    @DisplayName("Bank having no public constructor should work")
    public void testNoPublicConstructor() {
        assertThrows(NoSuchMethodException.class, () -> {Bank.class.getConstructor();});
        assertEquals(0, Bank.class.getConstructors().length);
    }

    /**
     * The bank uses the account factory it gets through the constructor
     */
    @Test
    @DisplayName("Bank using the injected AccountFactory should work")
    public void testInjectedFactory() {
        Bank bank = new Bank(new AccountFactory(5000));

        assertEquals("S-5000", bank.createSavingsAccount());
        assertEquals("P-5001", bank.createSalaryAccount(-50_000));
    }

    /**
     * A bank created with the constructor is independent from the singleton bank
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("A bank from the constructor being independent from the singleton should work")
    public void testConstructedBankIsIndependent() throws InvalidDateException, InvalidAmountException {
        Bank bank = new Bank(new AccountFactory(900_000));
        String sav1 = bank.createSavingsAccount();
        bank.deposit(sav1, Scheduled.now(), 15, "This is a test text");

        assertNotSame(Bank.getInstance(), bank);
        assertEquals(15, bank.getBalance());
        assertThrows(InvalidAmountException.class, () -> {Bank.getInstance().getAccount(sav1);});
    }

    /**
     * reset() removes all accounts from the singleton bank
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("reset() emptying the bank should work")
    public void testReset() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        String sav1 = bank.createSavingsAccount();
        bank.deposit(sav1, Scheduled.now(), 15, "This is a test text");

        Bank.reset();

        assertEquals(0, bank.getBalance());
        assertThrows(InvalidAmountException.class, () -> {bank.getAccount(sav1);});
    }
}
