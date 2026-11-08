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
	 * @return boolean <code>true</code>, falls die
	 * Abhebung erfolgreich war, andernfalls (z.B.
	 * bei negativem Betrag, oder nicht gen�gend
	 * Saldo) <code>false</code>.
	 */
	@Override
	public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
		if (getBalance() < amount)
			throw new InvalidAmountException("Amount is greater than balance");

		return super.withdraw(date, amount, text);
	}

	@Override
	public String getAccountType() {
		return "Savings Account";
	}
}
