package ch.bbw.web;

import ch.bbw.accounts.Account;
import ch.bbw.service.BankService;
import ch.bbw.web.dto.AccountResponse;
import ch.bbw.web.dto.BalanceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * REST endpoints for the bank staff: the balance of the bank and the top 5 lists
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
@RestController
@RequestMapping("/bank")
public class BankController {
    /**
     * The service that talks to the bank
     */
    private final BankService bankService;

    /**
     * Creates a new bank controller
     *
     * @param bankService the service that talks to the bank
     */
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    /**
     * Returns the balance of all accounts together
     *
     * @return the balance of the bank
     */
    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        return new BalanceResponse(bankService.getBalance());
    }

    /**
     * Returns the 5 accounts with the highest balance
     *
     * @return the accounts, the highest first
     */
    @GetMapping("/top5/highest")
    public List<AccountResponse> top5Highest() {
        return toResponses(bankService.top5HighestBalances());
    }

    /**
     * Returns the 5 accounts with the lowest balance
     *
     * @return the accounts, the lowest first
     */
    @GetMapping("/top5/lowest")
    public List<AccountResponse> top5Lowest() {
        return toResponses(bankService.top5LowestBalances());
    }

    /**
     * Converts accounts into responses
     *
     * @param accounts the accounts
     * @return the responses in the same order
     */
    private List<AccountResponse> toResponses(List<Account> accounts) {
        List<AccountResponse> responses = new ArrayList<>();

        for (Account account : accounts) {
            responses.add(AccountResponse.from(account));
        }

        return responses;
    }
}
