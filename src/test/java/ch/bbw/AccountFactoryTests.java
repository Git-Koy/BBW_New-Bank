package ch.bbw;

import ch.bbw.accounts.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
     * The factory starts the ids at the given number
     */
    @Test
    @DisplayName("Starting the ids at a given number should work")
    public void testStartId() {
        AccountFactory accountFactory = new AccountFactory(5000);

        assertEquals("S-5000", accountFactory.createSavingsAccount().getId());
        assertEquals("Y-5001", accountFactory.createPromoYouthSavingsAccount().getId());
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

    /**
     * create() returns the right account type for every type in AccountType
     */
    @Test
    @DisplayName("Creating every account type through create() should work")
    public void testCreateEveryType() {
        AccountFactory accountFactory = new AccountFactory();

        assertTrue(accountFactory.create(AccountType.SAVINGS, 0) instanceof SavingsAccount);
        assertTrue(accountFactory.create(AccountType.PROMO_YOUTH, 0) instanceof PromoYouthSavingsAccount);
        assertTrue(accountFactory.create(AccountType.SALARY, -50_000) instanceof SalaryAccount);
    }

    /**
     * create() builds the id with the prefix of the account type
     */
    @Test
    @DisplayName("create() using the prefix of the account type should work")
    public void testCreateUsesPrefix() {
        AccountFactory accountFactory = new AccountFactory();

        for (AccountType type : AccountType.values()) {
            Account account = accountFactory.create(type, 0);

            assertTrue(account.getId().startsWith(type.getPrefix() + "-"));
        }
    }

    /**
     * create() passes the credit limit to the salary account
     */
    @Test
    @DisplayName("The credit limit reaching the SalaryAccount through create() should work")
    public void testCreateCreditLimitReachesSalaryAccount() {
        AccountFactory accountFactory = new AccountFactory();
        Account sal1 = accountFactory.create(AccountType.SALARY, -50_000);

        assertTrue(sal1 instanceof SalaryAccount);
        assertEquals(-50_000, ((SalaryAccount) sal1).getCreditLimit());
    }

    /**
     * create() rejects a positive credit limit for a salary account
     */
    @Test
    @DisplayName("A positive credit limit will be rejected by create()")
    public void testCreateRejectPositiveCreditLimit() {
        AccountFactory accountFactory = new AccountFactory();

        assertThrows(IllegalArgumentException.class, () -> {accountFactory.create(AccountType.SALARY, 50_000);});
    }

    /**
     * create() rejects a missing account type
     */
    @Test
    @DisplayName("A missing account type will be rejected by create()")
    public void testCreateRejectNullType() {
        AccountFactory accountFactory = new AccountFactory();

        assertThrows(IllegalArgumentException.class, () -> {accountFactory.create(null, 0);});
    }

    /**
     * The ids stay unique when many threads create accounts at the same time
     * @throws InterruptedException
     */
    @Test
    @DisplayName("Ids being unique when creating accounts from many threads should work")
    public void testUniqueIdsWithThreads() throws InterruptedException {
        AccountFactory accountFactory = new AccountFactory();
        Set<String> ids = ConcurrentHashMap.newKeySet();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 10_000; j++) {
                    ids.add(accountFactory.createSavingsAccount().getId());
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(100_000, ids.size());
    }
}
