package ch.bbw;

import ch.bbw.accounts.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the class AccountFactory
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public class AccountFactoryTests {
    /**
     * The factory creates a savings account with the prefix S-
     */
    @Test
    @DisplayName("Creating a SavingsAccount with the prefix S- should work")
    public void testCreateSavingsAccount() {
        AccountFactory accountFactory = new AccountFactory();
        Account sav1 = accountFactory.createSavingsAccount();

        assertTrue(sav1 instanceof SavingsAccount);
        assertTrue(sav1.getId().startsWith("S-"));
    }

    /**
     * The factory creates a promo youth savings account with the prefix Y-
     */
    @Test
    @DisplayName("Creating a PromoYouthSavingsAccount with the prefix Y- should work")
    public void testCreatePromoYouthSavingsAccount() {
        AccountFactory accountFactory = new AccountFactory();
        Account pys1 = accountFactory.createPromoYouthSavingsAccount();

        assertTrue(pys1 instanceof PromoYouthSavingsAccount);
        assertTrue(pys1.getId().startsWith("Y-"));
    }

    /**
     * The factory creates a salary account with the prefix P-
     */
    @Test
    @DisplayName("Creating a SalaryAccount with the prefix P- should work")
    public void testCreateSalaryAccount() {
        AccountFactory accountFactory = new AccountFactory();
        Account sal1 = accountFactory.createSalaryAccount(-50_000);

        assertTrue(sal1 instanceof SalaryAccount);
        assertTrue(sal1.getId().startsWith("P-"));
    }

    /**
     * The credit limit is passed to the salary account and not discarded
     */
    @Test
    @DisplayName("The credit limit reaching the SalaryAccount should work")
    public void testCreditLimitReachesSalaryAccount() {
        AccountFactory accountFactory = new AccountFactory();
        Account sal1 = accountFactory.createSalaryAccount(-50_000);

        assertTrue(sal1 instanceof SalaryAccount);
        assertEquals(-50_000, ((SalaryAccount) sal1).getCreditLimit());
    }

    /**
     * A positive credit limit will be rejected by the factory
     */
    @Test
    @DisplayName("A positive credit limit will be rejected")
    public void testRejectPositiveCreditLimit() {
        AccountFactory accountFactory = new AccountFactory();

        assertThrows(IllegalArgumentException.class, () -> {accountFactory.createSalaryAccount(50_000);});
    }

    /**
     * The ids stay unique over 1000 creations of each account type
     */
    @Test
    @DisplayName("Ids being unique over many creations should work")
    public void testUniqueIds() {
        AccountFactory accountFactory = new AccountFactory();
        Set<String> ids = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            ids.add(accountFactory.createSavingsAccount().getId());
            ids.add(accountFactory.createPromoYouthSavingsAccount().getId());
            ids.add(accountFactory.createSalaryAccount(-50_000).getId());
        }

        assertEquals(3000, ids.size());
    }
}
