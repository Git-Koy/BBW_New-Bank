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
    @DisplayName("Tests the balance of two SalaryAccounts from the Bank")
    public void testBalance() {
        fail("ToDo");
    }

    /**
     * Tested die Ausgabe der "top 5" konten.
     */
    @Test
    @DisplayName("Tests the List of the top 5 balances")
    public void testTop5() {
        fail("ToDo");
    }

    /**
     * Tested die Ausgabe der "top 5" konten.
     */
    @Test
    @DisplayName("Tests the List of the bottom 5 balances")
    public void testBottom5() {
        fail("ToDo");
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
