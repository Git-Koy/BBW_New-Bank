import ch.bbw.Bank;
import ch.bbw.MoneyFormatter;
import ch.bbw.Scheduled;
import ch.bbw.accounts.*;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests f�r die Klasse 'Bank'.
 *
 * @author luigicavuoti
 * @version 1.0
 */
public class BankTests {
    /**
     * Creates two savings accounts through the bank and checks the ids
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("Constructor Test, creates two SavingsAccounts")
    public void testCreate() throws InvalidAmountException {
        Bank bank = Bank.getInstance();
        String sav1 = bank.createSavingsAccount();
        String sav2 = bank.createSavingsAccount();

        assertNotNull(sav1);
        assertNotNull(sav2);
        assertNotEquals(sav1, sav2);
        assertEquals(sav1, bank.getAccount(sav1).getId());
        assertEquals(sav2, bank.getAccount(sav2).getId());
    }

    /**
     * Deposits on a savings account through the bank
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("Simple deposit on Bank-SavingsAccount should work")
    public void testDeposit() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        String sav1 = bank.createSavingsAccount();

        bank.deposit(sav1, Scheduled.now(), 15, "This is a test text");

        assertEquals(15, bank.getBalance(sav1));
        assertEquals(1, bank.getAccount(sav1).getBookings().size());
    }

    /**
     * Withdraws from a savings account through the bank, withdrawing more than the balance throws
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("Simple withdraw on Bank-SavingsAccount should work")
    public void testWithdraw() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        String sav1 = bank.createSavingsAccount();
        bank.deposit(sav1, Scheduled.now(), 15, "This is a test text");

        assertEquals(2, bank.withdraw(sav1, Scheduled.now(), 13, "This is a test text"));
        assertEquals(2, bank.getBalance(sav1));
        assertThrows(InvalidAmountException.class, () -> {bank.withdraw(sav1, Scheduled.now(), 3, "This is a test text");});
    }

    /**
     * Experimente mit print().
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("Not a real Test, it prints the Balance of the account")
    public void testPrint() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        String sav1 = bank.createSavingsAccount();
        bank.deposit(sav1, Scheduled.now(), 15, "This is a test text");
        bank.withdraw(sav1, Scheduled.now(), 13, "This is a test text");
        System.out.println(bank.getAccount(sav1).printStatement());
        assertTrue(bank.getAccount(sav1).printStatement().contains("CHF 0.00002"));
    }

    /**
     * Experimente mit print(year, month).
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("Not a real Test, it prints the Balance of the account")
    public void testMonthlyPrint() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        String sav1 = bank.createSavingsAccount();
        bank.deposit(sav1, Scheduled.now(), 15, "This is a test text");
        bank.withdraw(sav1, Scheduled.now(), 13, "This is a test text");
        System.out.println("Your monthly balance is:" + MoneyFormatter.toCHF(bank.getBalance(sav1)));
        assertEquals(2, bank.getBalance(sav1));
    }

    /**
     * Testet den Gesamtkontostand der Bank.
     */
    @Test
    @DisplayName("Tests the balance of two SavingsAccounts from the Bank")
    public void testBalance() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        String sav1 = bank.createSavingsAccount();
        String sav2 = bank.createSavingsAccount();

        bank.deposit(sav1, Scheduled.now(), 100);
        bank.deposit(sav2, Scheduled.now(), 500);

        assertEquals(600, bank.getBalance());
    }

    /**
     * Tested die Ausgabe der "top 5" konten.
     */
    @Test
    @DisplayName("Tests the List of the top 5 balances")
    public void testTop5() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        for (int i = 1; i <= 6; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, Scheduled.now(), i * 100);
        }

        List<Account> top5 = bank.top5HighestBalances();

        assertEquals(5, top5.size());
        assertEquals(600, top5.get(0).getBalance());
        assertEquals(200, top5.get(4).getBalance());
    }

    /**
     * Tested die Ausgabe der "bottom 5" konten.
     */
    @Test
    @DisplayName("Tests the List of the bottom 5 balances")
    public void testBottom5() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        for (int i = 1; i <= 6; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, Scheduled.now(), i * 100);
        }

        List<Account> bottom5 = bank.top5LowestBalances();

        assertEquals(5, bottom5.size());
        assertEquals(100, bottom5.get(0).getBalance());
        assertEquals(500, bottom5.get(4).getBalance());
    }

    /**
     * Puts all types of accounts in a list, sums the balances and asserts the total
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("Adds one account of each type into a list, sums the balances and asserts the total")
    public void testAssertAccounts() throws InvalidDateException, InvalidAmountException {
        SavingsAccount sav1 = new SavingsAccount("CH-01");
        SalaryAccount sal1 = new SalaryAccount("CH-02", -12);
        PromoYouthSavingsAccount pys1 = new PromoYouthSavingsAccount("CH-03");

        sav1.deposit(Scheduled.now(), 15, "This is a test text");
        sal1.deposit(Scheduled.now(), 15, "This is a test text");
        pys1.deposit(Scheduled.now(), 15, "This is a test text");

        long total = 0;
        List<Account> accounts = List.of(sav1, sal1, pys1);

        for (Account account : accounts) {
            total += account.getBalance();
        }

        assertEquals(45, total);
    }

    /**
     * The bank starts empty and a deposit only shows up in this test. Together with testIsolationSecond it checks that the order of the tests doesn't matter
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("A deposit not leaking into another test should work")
    public void testIsolationFirst() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        assertEquals(0, bank.getBalance());

        String sav1 = bank.createSavingsAccount();
        bank.deposit(sav1, Scheduled.now(), 15, "This is a test text");

        assertEquals(15, bank.getBalance());
    }

    /**
     * The bank starts empty and a deposit only shows up in this test. Together with testIsolationFirst it checks that the order of the tests doesn't matter
     * @throws InvalidDateException
     * @throws InvalidAmountException
     */
    @Test
    @DisplayName("A deposit from another test not showing up should work")
    public void testIsolationSecond() throws InvalidDateException, InvalidAmountException {
        Bank bank = Bank.getInstance();
        assertEquals(0, bank.getBalance());

        String sav1 = bank.createSavingsAccount();
        bank.deposit(sav1, Scheduled.now(), 20, "This is a test text");

        assertEquals(20, bank.getBalance());
    }

}
