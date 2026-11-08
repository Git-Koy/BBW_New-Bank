package ch.bbw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the enum AccountType
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public class AccountTypeTests {
    /**
     * Every account type has the right prefix
     */
    @Test
    @DisplayName("Every account type having the right prefix should work")
    public void testPrefixes() {
        assertEquals("S", AccountType.SAVINGS.getPrefix());
        assertEquals("Y", AccountType.PROMO_YOUTH.getPrefix());
        assertEquals("P", AccountType.SALARY.getPrefix());
    }

    /**
     * No two account types share the same prefix
     */
    @Test
    @DisplayName("Every prefix being unique should work")
    public void testUniquePrefixes() {
        Set<String> prefixes = new HashSet<>();

        for (AccountType type : AccountType.values()) {
            prefixes.add(type.getPrefix());
        }

        assertEquals(AccountType.values().length, prefixes.size());
    }

    /**
     * The factory builds the ids with the prefix of the account type
     */
    @Test
    @DisplayName("The factory using the prefix of the account type should work")
    public void testFactoryUsesPrefix() {
        AccountFactory accountFactory = new AccountFactory();

        assertTrue(accountFactory.createSavingsAccount().getId().startsWith(AccountType.SAVINGS.getPrefix() + "-"));
        assertTrue(accountFactory.createPromoYouthSavingsAccount().getId().startsWith(AccountType.PROMO_YOUTH.getPrefix() + "-"));
        assertTrue(accountFactory.createSalaryAccount(-50_000).getId().startsWith(AccountType.SALARY.getPrefix() + "-"));
    }
}
