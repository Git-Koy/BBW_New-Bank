package ch.bbw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the singleton of the class Bank
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public class BankSingletonTests {
    /**
     * getInstance() always returns the same bank
     */
    @Test
    @DisplayName("getInstance() returning the same bank every time should work")
    public void testGetInstanceReturnsSameBank() {
        Bank bank1 = Bank.getInstance();
        Bank bank2 = Bank.getInstance();

        assertSame(bank1, bank2);
    }

    /**
     * The bank has no public constructor, so nobody can create a second bank
     */
    @Test
    @DisplayName("Bank having no public constructor should work")
    public void testNoPublicConstructor() {
        assertThrows(NoSuchMethodException.class, () -> {Bank.class.getConstructor();});
        assertEquals(0, Bank.class.getConstructors().length);
    }
}
