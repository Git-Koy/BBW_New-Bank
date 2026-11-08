package ch.bbw.accounts;


import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

/**
 * Sparkonto.
 *
 * @author Luigi Cavuoti, lro@gmx.ch
 * @version 1.1
 */
public class SavingsAccount extends Account
{
	/**
	 * Initialisiert ein Sparkonto
	 *
	 * @param id die Kontonummer
	 */
	public SavingsAccount(String id)
	{
		super(id);
	}

	/**
	 * Hebt den gegebenen Betrag vom Konto ab.
	 *
	 * @param date das Transaktionsdatum
	 * @param amount der abzuhebende Betrag
	 * @param text der Transaktionstext
	 *
	 * @return der neue Saldo
	 * @throws InvalidAmountException if the amount is 0, negative or greater than the balance
	 * @throws InvalidDateException if the date is in the past
	 */
	@Override
	public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
		if (getBalance() < amount)
			throw new InvalidAmountException("Amount is greater than balance");

		return super.withdraw(date, amount, text);
	}

	/**
	 * Returns the name of the account type.
	 *
	 * @return "Savings Account"
	 */
	@Override
	public String getAccountType() {
		return "Savings Account";
	}
}
