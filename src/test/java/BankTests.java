import ch.bbw.Bank;
import ch.bbw.Scheduled;
import ch.bbw.accounts.*;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Tests f�r die Klasse 'Bank'.
 *
 * @author luigicavuoti
 * @version 1.0
 */
public class BankTests {
    @Test
    @DisplayName("Constructor Test, creates two SavingsAccounts")
    public void testCreate() {
        fail("ToDo");
    }

    @Test
    @DisplayName("Simple deposit on Bank-SavingsAccount should work")
    public void testDeposit() {
        fail("ToDo");
    }

    @Test
    @DisplayName("Simple withdraw on Bank-SavingsAccount should work")
    public void testWithdraw() {
        fail("ToDo");
    }

    /**
     * Experimente mit print().
     */
    @Test
    @DisplayName("Not a real Test, it prints the Balance of the account")
    public void testPrint() {
        fail("ToDo");
    }

    /**
     * Experimente mit print(year, month).
     */
    @Test
    @DisplayName("Not a real Test, it prints the Balance of the account")
    public void testMonthlyPrint() {
        fail("ToDo");
    }

    /**
     * Testet den Gesamtkontostand der Bank.
     */
    @Test
    @DisplayName("Tests the balance of two SavingsAccounts from the Bank")
    public void testBalance() throws InvalidDateException, InvalidAmountException {
        Bank bank = new Bank();
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
        Bank bank = new Bank();
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
        Bank bank = new Bank();
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

}
