package ch.bbw;

import ch.bbw.accounts.*;

public class AccountFactory {
    /**
     * Next account id.
     */
    private long nextAccountId;

    public AccountFactory() {
        this.nextAccountId = 1000;
    }

    public Account createPromoYouthSavingsAccount() {
        String id = AccountType.PROMO_YOUTH.getPrefix() + "-" + nextAccountId++;

        return new PromoYouthSavingsAccount(id);
    }

    public Account createSalaryAccount(long creditLimit) {
        String id = AccountType.SALARY.getPrefix() + "-" + nextAccountId++;

        return new SalaryAccount(id, creditLimit);
    }

    public Account createSavingsAccount(){
        String id = AccountType.SAVINGS.getPrefix() + "-" + nextAccountId++;

        return new SavingsAccount(id);
    }

}
