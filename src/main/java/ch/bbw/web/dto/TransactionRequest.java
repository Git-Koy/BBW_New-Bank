package ch.bbw.web.dto;

/**
 * Request to deposit or withdraw
 *
 * @param amount the amount in millirappen
 * @param date the transaction date as ISO-8601 text, empty means now
 * @param text the booking text, empty means "Einzahlung" or "Auszahlung"
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public record TransactionRequest(long amount, String date, String text) {
}
