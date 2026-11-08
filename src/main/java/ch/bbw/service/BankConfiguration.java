package ch.bbw.service;

import ch.bbw.Bank;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the bank. This is the outermost entry point (composition root), so it is the only place that calls Bank.getInstance().
 * Every other class gets the bank injected through its constructor.
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
@Configuration
public class BankConfiguration {
    /**
     * Makes the only bank available as a Spring bean
     *
     * @return the bank
     */
    @Bean
    public Bank bank() {
        return Bank.getInstance();
    }
}
