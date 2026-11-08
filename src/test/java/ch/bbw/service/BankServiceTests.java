package ch.bbw.service;

import ch.bbw.Bank;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.AccountNotFoundException;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the class BankService
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public class BankServiceTests {
    private BankService bankService;

    /**
     * Empties the bank and creates the service around it before every test
     */
    @BeforeEach
    public void setUp() {
        Bank.reset();
        bankService = new BankService(Bank.getInstance());
    }

    /**
     * The service creates all three account types
     */
    @Test
    @DisplayName("Creating all account types through the service should work")
    public void testCreateAccounts() {
        assertTrue(bankService.createSavingsAccount().startsWith("S-"));
        assertTrue(bankService.createPromoYouthSavingsAccount().startsWith("Y-"));
        assertTrue(bankService.createSalaryAccount(-50_000).startsWith("P-"));
    }

    /**
     * A deposit returns the new balance
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("Depositing through the service should work")
    public void testDeposit() throws InvalidDateException, InvalidAmountException {
        String sav1 = bankService.createSavingsAccount();

        assertEquals(15, bankService.deposit(sav1, Scheduled.now(), 15, "This is a test text"));
    }

    /**
     * A withdrawal returns the new balance
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("Withdrawing through the service should work")
    public void testWithdraw() throws InvalidDateException, InvalidAmountException {
        String sav1 = bankService.createSavingsAccount();
        bankService.deposit(sav1, Scheduled.now(), 15, "This is a test text");

        assertEquals(2, bankService.withdraw(sav1, Scheduled.now(), 13, "This is a test text"));
    }

    /**
     * An unknown account throws an AccountNotFoundException
     */
    @Test
    @DisplayName("An unknown account throwing AccountNotFoundException should work")
    public void testUnknownAccount() {
        assertThrows(AccountNotFoundException.class, () -> {bankService.getAccount("S-9999");});
    }

    /**
     * The balance of the bank is the sum of all accounts
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("The balance of the bank through the service should work")
    public void testBalance() throws InvalidDateException, InvalidAmountException {
        String sav1 = bankService.createSavingsAccount();
        String sav2 = bankService.createSavingsAccount();
        bankService.deposit(sav1, Scheduled.now(), 100, "This is a test text");
        bankService.deposit(sav2, Scheduled.now(), 500, "This is a test text");

        assertEquals(600, bankService.getBalance());
        assertEquals(2, bankService.top5HighestBalances().size());
        assertEquals(100, bankService.top5LowestBalances().get(0).getBalance());
    }
}
