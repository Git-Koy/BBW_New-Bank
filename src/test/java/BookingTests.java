import ch.bbw.Booking;
import ch.bbw.accounts.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests für die Klasse Booking.
 *
 * @author Luigi Cavuoti
 * @version 1.1
 */
public class BookingTests
{
	/**
	 * Tests f�r die Erzeugung von Buchungen.
	 */
	@Test
	@DisplayName("Construct Simple Booking, should work ")
	public void testInitialization()
	{
		long date = 20829;
		long amount = 1500;

		Booking testBooking = new Booking(date, amount);

		assertEquals(date, testBooking.date(), "Date must survive construction");
		assertEquals(amount, testBooking.amount(), "Amount must survive construction");
	}

	/**
	 * Experimente mit print().
	 */
	@Test
	@DisplayName("Not a real test, it prints a list of bookings")
	public void testPrint()
	{
		List<Booking> bookings = List.of(new Booking(20829, 1500), new Booking(20829, 1500));
		bookings.forEach(System.out::println);

	}
}
