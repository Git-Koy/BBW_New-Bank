package ch.bbw;


import ch.bbw.accounts.Account;
import ch.bbw.exceptions.*;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Die Bank. It only manages the accounts, the AccountFactory decides which account type gets created, so Bank only knows the abstract Account.
 * The bank is a singleton, because there is conceptually only one bank.
 * For the singleton:
 * <ul>
 *     <li>There is only one bank in the real world too.</li>
 *     <li>One global access point, so the bank doesn't have to be passed through every layer.</li>
 *     <li>The account id counter can't be duplicated by a second bank.</li>
 * </ul>
 * Against the singleton:
 * <ul>
 *     <li>Hidden dependency: a method that calls Bank.getInstance() depends on the bank, but its signature doesn't say so.</li>
 *     <li>Global mutable state: any code in the application can deposit into any account.</li>
 *     <li>Testability: no test double can replace it and two independent scenarios can't run in parallel, so the tests need the reset() workaround.</li>
 *     <li>Lifetime: the bank lives until the JVM stops, there is no way to tear it down.</li>
 *     <li>"There is one bank" is a fact about the world, "there is one Bank object in a static field" is a much stronger claim about the code.</li>
 * </ul>
 * Because of that the bank keeps a constructor with the injected AccountFactory, and getInstance() should only be called at the outermost entry point
 * (main, later the Spring configuration). Everything else should get the bank as a parameter.
 *
 * @author luigicavuoti, lro@gmx.ch
 * @version 2.1
 */
public class Bank {
    /**
     * Liste aller Konti.
     */
    private final TreeMap<String, Account> accounts;
    /**
     * The factory that creates the accounts
     */
    private final AccountFactory accountFactory;
    /**
     * Initialisiert eine neue Bank. The account factory is injected instead of created here, so a test can pass a factory with a known start id.
     * It is package-private and not public, so outside of ch.bbw the only bank comes from getInstance().
     *
     * @param accountFactory the factory that creates the accounts
     */
    Bank(AccountFactory accountFactory) {
        this.accounts = new TreeMap<String, Account>();
        this.accountFactory = accountFactory;
    }

    /**
     * Holds the only bank. The JVM loads this class the first time getInstance() is called.
     */
    private static class Holder {
        static final Bank INSTANCE = new Bank(new AccountFactory());
    }

    /**
     * Returns the only bank of the application. It uses the holder idiom, because it is lazy and thread safe without any cost per call.
     * Comparison of the three variants:
     * <ul>
     *     <li>Eager (static final field): thread safe because the JVM creates the bank when the class is loaded, not lazy, no cost per call.</li>
     *     <li>Lazy with synchronized: thread safe and lazy, but every call has to take the lock, even long after the bank exists.</li>
     *     <li>Holder idiom: thread safe and lazy, because the JVM only loads the Holder class on the first call, no cost per call.</li>
     * </ul>
     * Double-checked locking without volatile is wrong: the JVM is allowed to reorder the writes, so another thread can see the
     * reference to the bank before the constructor has finished and use a half-built bank.
     *
     * @return the bank
     */
    public static Bank getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Removes all accounts from the bank. This is a workaround only for the tests: all tests share the one bank, so every test has to empty it first.
     * It is a code smell and not a solution, because it only exists so the tests can undo the singleton decision, and any code could delete every account with it.
     * JUnit runs the tests one after another today. If it ran them in parallel, the tests would still share the one bank and reset() would delete the accounts of another running test.
     */
    public static void reset() {
        getInstance().accounts.clear();
    }

    /**
     * Erzeugt ein neues Sparkonto
     *
     * @return die neue Kontonummer
     */
    public String createSavingsAccount() {
        var account = accountFactory.createSavingsAccount();
        var id = account.getId();

        accounts.put(id, account);

        return id;
    }

    /**
     * Erzeugt ein neues Promo-Jugendsparkonto
     *
     * @return die neue Kontonummer
     */
    public String createPromoYouthSavingsAccount() {
        var account = accountFactory.createPromoYouthSavingsAccount();
        var id = account.getId();

        accounts.put(id, account);

        return id;
    }

    /**
     * Erzeugt ein neues Lohnkonto
     *
     * @param creditLimit Kreditlimite (negative Zahl)
     * @return String die neue Kontonummer
     */
    public String createSalaryAccount(long creditLimit) throws IllegalArgumentException {
        var account = accountFactory.createSalaryAccount(creditLimit);
        var id = account.getId();

        accounts.put(id, account);

        return id;

    }

    /**
     * Gibt den Kontostand der Bank zurück.
     *
     * @return long der Kontostand der Bank
     */
    public long getBalance() {
        return accounts
                .values()
                .stream()
                .mapToLong(Account::getBalance)
                .sum();
    }

    /**
     * Gibt den Kontostand des Kontos mit der gegebenen
     * Kontonummer zurück.
     *
     * @param id die Kontonummer
     * @return long der Kontostand des Kontos
     * @throws InvalidAmountException if no account with this id exists
     */
    public long getBalance(String id) throws InvalidAmountException {
        return getAccount(id).getBalance();
    }

    /**
     * Zahlt den gegebenen Betrag auf das Konto mit
     * der gegebenen Kontonummer ein.
     *
     * @param id     die Kontonummer
     * @param date   das Transaktionsdatum
     * @param amount der einzuzahlende Betrag
     * @param text der Transaktionstext
     * @throws InvalidAmountException if the account doesn't exist or the amount is not allowed
     * @throws InvalidDateException if the date is in the past
     */
    public void deposit(String id, Scheduled date,
                        long amount, String text) throws InvalidAmountException, InvalidAmountException, InvalidDateException {
        var account = getAccount(id);

        account.deposit(date, amount, text);
    }

    /**
     * Zahlt den gegebenen Betrag auf das Konto mit
     * der gegebenen Kontonummer ein.
     *
     * @param id     die Kontonummer
     * @param date   das Transaktionsdatum
     * @param amount der einzuzahlende Betrag
     * @throws InvalidAmountException if the account doesn't exist or the amount is not allowed
     * @throws InvalidDateException if the date is in the past
     */
    public void deposit(String id, Scheduled date,
                        long amount) throws InvalidAmountException, InvalidAmountException, InvalidDateException {
        var account = getAccount(id);

        account.deposit(date, amount);
    }

    /**
     * Hebt den gegebenen Betrag vom Konto mit
     * der gegebenen Kontonummer ab.
     *
     * @param id     die Kontonummer
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag
     * @param text der Transaktionstext
     * @return new balance
     * @throws InvalidAmountException if the account doesn't exist or the amount is not allowed
     * @throws InvalidDateException if the date is in the past
     */
    public long withdraw(String id, Scheduled date,
                            long amount, String text) throws InvalidAmountException, InvalidAmountException, InvalidDateException {
        var account = getAccount(id);

        return account.withdraw(date, amount, text);
    }

    /**
     * Hebt den gegebenen Betrag vom Konto mit
     * der gegebenen Kontonummer ab.
     *
     * @param id     die Kontonummer
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag
     * @return new balance
     * @throws InvalidAmountException if the account doesn't exist or the amount is not allowed
     * @throws InvalidDateException if the date is in the past
     */
    public long withdraw(String id, Scheduled date,
                         long amount) throws InvalidAmountException, InvalidAmountException, InvalidDateException {
        var account = getAccount(id);

        return account.withdraw(date, amount);
    }

    /**
     * Returns the 5 accounts with the highest balance, the highest first
     *
     * @return the accounts, fewer than 5 if the bank has fewer accounts
     */
    public List<Account> top5HighestBalances() {
        return accounts.values()
                .stream()
                .sorted(Comparator.comparingLong(Account::getBalance).reversed())
                .limit(5)
                .toList();
    }

    /**
     * Returns the 5 accounts with the lowest balance, the lowest first
     *
     * @return the accounts, fewer than 5 if the bank has fewer accounts
     */
    public List<Account> top5LowestBalances() {
        return accounts.values()
                .stream()
                .sorted(Comparator.comparingLong(Account::getBalance))
                .limit(5)
                .toList();
    }

    /**
     * Getter of the property <tt>account</tt>
     *
     * @param id the account id
     * @return Returns the account.
     * @throws InvalidAmountException if no account with this id exists
     */
    public Account getAccount(String id) throws InvalidAmountException {

        var account = Optional.ofNullable(accounts.get(id));

        if (account.isEmpty()) {
            throw new InvalidAmountException("Invalid bank id");
        }

        return account.get();
    }
}
