package ch.bbw;

/**
 * Formats amounts in millirappen as readable CHF amounts
 *
 * @author Kay Ježek, kay@jeschis.ch
 * @version 1.0
 */
public class MoneyFormatter {
    /**
     * Creates a new money formatter, not needed because toCHF() is static
     */
    public MoneyFormatter() {
    }

    /**
     * Formats an amount in millirappen, for example 123456 becomes "CHF 1.23456"
     *
     * @param millirappen the amount in millirappen, can be negative
     * @return the formatted amount with all 5 decimal places
     */
    public static String toCHF(long millirappen) {
        String sign = millirappen < 0 ? "-" : "";
        long absolute = Math.abs(millirappen);
        long francs = absolute / 100_000;
        long rest = absolute % 100_000;

        return "CHF " + sign + francs + "." + String.format("%05d", rest);
    }
}
