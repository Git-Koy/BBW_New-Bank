package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
