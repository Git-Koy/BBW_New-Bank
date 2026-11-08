package ch.bbw.web.dto;

import ch.bbw.Booking;

import java.time.Instant;

/**
 * A booking as it is sent over HTTP
 *
 * @param date the date as ISO-8601 text, for example "2026-11-08T10:15:30Z"
 * @param amount the amount in millirappen, negative for a withdrawal
 * @param text the booking text
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public record BookingResponse(String date, long amount, String text) {
    /**
     * Creates the response from a booking
     *
     * @param booking the booking
     * @return the response
     */
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(Instant.ofEpochMilli(booking.date()).toString(), booking.amount(), booking.text());
    }
}
