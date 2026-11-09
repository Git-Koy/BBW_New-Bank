import ch.bbw.Booking;
import ch.bbw.accounts.Account;
import ch.bbw.accounts.SavingsAccount;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
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
	 * Creates a new instance of the test class, JUnit creates one for every test
	 */
	public BookingTests()
	{
	}

	/**
	 * Tests f�r die Erzeugung von Buchungen.
	 */
	@Test
	@DisplayName("Construct Simple Booking, should work ")
	public void testInitialization()
	{
		long date = 20829;
		long amount = 1500;
		String text = "This is a test text";

		Booking testBooking = new Booking(date, amount, text);

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
		List<Booking> bookings = List.of(new Booking(20829, 1500, "This is a test text"), new Booking(20829, 1500, "This is a test text"));
		bookings.forEach(System.out::println);

		assertEquals(2, bookings.size());
		assertTrue(bookings.get(0).toString().contains("This is a test text"));
	}

	/**
	 * The text of a booking survives the construction
	 */
	@Test
	@DisplayName("The text of a booking surviving construction should work")
	public void testText()
	{
		Booking testBooking = new Booking(20829, 1500, "This is a test text");

		assertEquals("This is a test text", testBooking.text());
	}

	/**
	 * A withdrawal is stored as a booking with a negative amount
	 */
	@Test
	@DisplayName("A booking with a negative amount should work")
	public void testNegativeAmount()
	{
		Booking testBooking = new Booking(20829, -1500, "This is a test text");

		assertEquals(-1500, testBooking.amount());
		assertEquals("This is a test text", testBooking.text());
	}

	/**
	 * Two bookings with the same values are equal
	 */
	@Test
	@DisplayName("Two bookings with the same values being equal should work")
	public void testEquals()
	{
		Booking booking1 = new Booking(20829, 1500, "This is a test text");
		Booking booking2 = new Booking(20829, 1500, "This is a test text");

		assertEquals(booking1, booking2);
		assertNotEquals(booking1, new Booking(20829, 1501, "This is a test text"));
	}

	/**
	 * Empty or blank bookings recieve default texts
	 */
	@Test
	@DisplayName("Empty or blank bookings recieving default texts should work")
	public void testEmptyOrBlankBookings() {
		assertEquals("Einzahlung", new Booking(20829, 14, "").text());
		assertEquals("Auszahlung", new Booking(20829, -13, null).text());
		assertEquals("Einzahlung", new Booking(20829, 12, "    ").text());
	}
}
