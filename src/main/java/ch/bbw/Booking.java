package ch.bbw;

/**
 * Buchung.
 *
 * @param date   Datum der Transaktion (Unixzeitstempel in Millisekunden).
 * @param amount Transaktionsbetrag (Millirappen).
 * @param text Transaktionsbeschreibung.
 * @author Luigi Cavuoti, lro@gmx.ch
 * @version 2.1
 */
public record Booking(long date, long amount, String text) {
	/**
	 * Checks if text is blank or null and replaces it with "Einzahlung" for deposits and "Auszahlung" for withdrawals
	 * @param date the date
	 * @param amount the amount
	 * @param text the text
	 */
	public Booking {
		if (text == null || text.isBlank()) {
			if (amount > 0 ) {
				text = "Einzahlung";
			} else {
				text = "Auszahlung";
			}
		}
	}
}
