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
 * Tests der Klasse SalaryAccount.
 *
 * @author Luigi Cavuoti
 * @version 1.1
 */
public class SalaryAccountTests
{
	/**
	 * Tests if a salary account can be created and also tests a simple deposit
	 */
	@Test
	@DisplayName("Constructs a SalaryAccount and tests a simple deposit")
	public void testInitialization() throws InvalidDateException, InvalidAmountException {
		SalaryAccount sal1 = new SalaryAccount("CH-01", -12);
		sal1.deposit(Scheduled.now(), 13, "This is a test text");
	}

	/**
	 * Withdrawing down to the credit limit works, but withdrawing more throws IllegalArgumentException. After withdrawing under 0, the balance is negative.
	 * @throws InvalidDateException
	 * @throws InvalidAmountException
	 */
	@Test
	@DisplayName("Withdrawing down to the credit limit suceeds, one more and it throws and the balance afterwards is negative")
	public void testWithdrawing() throws InvalidDateException, InvalidAmountException {
		SalaryAccount sal1 = new SalaryAccount("CH-01", -12);

		sal1.withdraw(Scheduled.now(), 12, "This is a test text");
		assertEquals(-12, sal1.getBalance());

		assertThrows(IllegalArgumentException.class, () -> {sal1.withdraw(Scheduled.now(), 1, "This is a test text");});
	}

	/**
	 * An account that has a credit limit of 0 works like a savings account.
	 * @throws InvalidDateException
	 * @throws InvalidAmountException
	 */
	@Test
	@DisplayName("An Account with credit limit 0 works like a savings account")
	public void testAccountWithZeroCreditLimit() throws InvalidDateException, InvalidAmountException {
		SalaryAccount sal1 = new SalaryAccount("CH-01", 0);
		sal1.deposit(Scheduled.now(), 12, "This is a test text");
		sal1.withdraw(Scheduled.now(), 12, "This is a test text");

		assertEquals(0, sal1.getBalance());
		assertThrows(IllegalArgumentException.class, () -> {sal1.withdraw(Scheduled.now(), 1, "This is a test text");});
	}

	/**
	 * A rejected withdrawl leaves the booking list untouched.
	 * @throws InvalidDateException
	 * @throws InvalidAmountException
	 */
	@Test
	@DisplayName("A rejected withdrawal leaves the booking list untouched")
	public void testRejectedWithdrawalLeavesBookingListUntouched() throws InvalidDateException, InvalidAmountException {
		SalaryAccount sal1 = new SalaryAccount("CH-01", -12);
		assertEquals(0, sal1.getBookings().size());
		assertThrows(IllegalArgumentException.class, () -> {sal1.withdraw(Scheduled.now(), 13, "This is a test text");});
		assertEquals(0, sal1.getBookings().size());
	}


}
