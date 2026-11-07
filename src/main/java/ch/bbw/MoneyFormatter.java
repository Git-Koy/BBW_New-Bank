package ch.bbw;

public class MoneyFormatter {
    public static String toCHF(long millirappen) {
        String sign = millirappen < 0 ? "-" : "";
        long absolute = Math.abs(millirappen);
        long francs = absolute / 100_000;
        long rest = absolute % 100_000;

        return "CHF " + sign + francs + "." + String.format("%05d", rest);
    }
}
