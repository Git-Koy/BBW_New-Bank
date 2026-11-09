package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

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
	 * Creates a new instance of the test class, JUnit creates one for every test
	 */
	public SavingsAccountTests()
	{
	}

	/**
	 * Tests the initialization of a savings account and a simple deposit
	 * @throws InvalidAmountException if an amount is not allowed
	 * @throws InvalidDateException if a transaction date is in the past
	 */
	@Test
	@DisplayName("Constructs a SavingsAccount and tests a simple deposit")
	public void test() throws InvalidAmountException, InvalidDateException
	{
		SavingsAccount sav1 = new SavingsAccount("CH-01");
		sav1.deposit(Scheduled.now(), 15, "This is a test text");

		assertEquals(15, sav1.getBalance());
	}

	/**
	 * Tests if withdrawing the exact balance suceedes and withdrawing more than the balance fails
	 * @throws InvalidAmountException if an amount is not allowed
	 * @throws InvalidDateException if a transaction date is in the past
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

	/**
	 * Withdrawing from a new account fails and adds no booking
	 */
	@Test
	@DisplayName("Withdrawing from an empty account fails")
	public void testWithdrawFromEmptyAccount()
	{
		SavingsAccount sav1 = new SavingsAccount("CH-01");

		assertThrows(InvalidAmountException.class, () -> {sav1.withdraw(Scheduled.now(), 1, "This is a test text");});
		assertEquals(0, sav1.getBalance());
		assertEquals(0, sav1.getBookings().size());
	}

	/**
	 * Withdrawing 0 or a negative amount is rejected
	 * @throws InvalidAmountException if an amount is not allowed
	 * @throws InvalidDateException if a transaction date is in the past
	 */
	@Test
	@DisplayName("Withdrawing 0 or a negative amount fails")
	public void testWithdrawInvalidAmount() throws InvalidAmountException, InvalidDateException
	{
		SavingsAccount sav1 = new SavingsAccount("CH-01");
		sav1.deposit(Scheduled.now(), 15, "This is a test text");

		assertThrows(InvalidAmountException.class, () -> {sav1.withdraw(Scheduled.now(), 0, "This is a test text");});
		assertThrows(InvalidAmountException.class, () -> {sav1.withdraw(Scheduled.now(), -5, "This is a test text");});
		assertEquals(15, sav1.getBalance());
	}

	/**
	 * Withdrawing with a date in the past is rejected
	 * @throws InvalidAmountException if an amount is not allowed
	 * @throws InvalidDateException if a transaction date is in the past
	 */
	@Test
	@DisplayName("Withdrawing with a date in the past fails")
	public void testWithdrawPastDate() throws InvalidAmountException, InvalidDateException
	{
		SavingsAccount sav1 = new SavingsAccount("CH-01");
		sav1.deposit(Scheduled.now(), 15, "This is a test text");

		assertThrows(InvalidDateException.class, () -> {sav1.withdraw(Scheduled.now().minus(1, ChronoUnit.DAYS), 1, "This is a test text");});
		assertEquals(15, sav1.getBalance());
	}

	/**
	 * The account type of a savings account
	 */
	@Test
	@DisplayName("The account type shows up correctly")
	public void testAccountType()
	{
		SavingsAccount sav1 = new SavingsAccount("CH-01");

		assertEquals("Savings Account", sav1.getAccountType());
	}
}

