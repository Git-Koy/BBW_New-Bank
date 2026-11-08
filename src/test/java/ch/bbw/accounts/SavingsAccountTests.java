package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests für die Klasse SavingsAccount.
 *
 * @author Luigi Cavuoti
 * @version 1.0
 */
public class SavingsAccountTests
{
	/**
	 * Tests the initialization of a savings account and a simple deposit
	 * @throws InvalidAmountException
	 * @throws InvalidDateException
	 */
	@Test
	@DisplayName("Constructs a SavingsAccount and tests a simple deposit")
	public void test() throws InvalidAmountException, InvalidDateException
	{
		SavingsAccount sav1 = new SavingsAccount("CH-01");
		sav1.deposit(Scheduled.now(), 15, "This is a test text");
	}

	/**
	 * Tests if withdrawing the exact balance suceedes and withdrawing more than the balance fails
	 * @throws InvalidAmountException
	 * @throws InvalidDateException
	 */
	@Test
	@DisplayName("Withdrawing the exact balance succeeds, 1 millirapen above and it fails")
	public void withdrawingTest() throws InvalidAmountException, InvalidDateException
	{
		SavingsAccount sav1 = new SavingsAccount("CH-01");
		sav1.deposit(Scheduled.now(), 15, "This is a test text");
		sav1.withdraw(Scheduled.now(), 15, "This is a test text");

		assertEquals(0, sav1.getBalance());
		assertThrows(InvalidAmountException.class, () -> {sav1.withdraw(Scheduled.now(), 1, "This is a test text");});
	}
}

