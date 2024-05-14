package com.ebanx.springapi.controller;

import com.ebanx.springapi.model.Account;
import com.ebanx.springapi.model.Event;
import com.ebanx.springapi.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Resets the state of the application by clearing all accounts.
     *
     * @return a ResponseEntity with status OK and body "OK"
     */
    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        accountService.resetAccounts();
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    /**
     * Processes an incoming event by delegating to the appropriate handler based on event type.
     *
     * @param event the event to process
     * @return a ResponseEntity containing the result of the event processing
     */
    @PostMapping("/event")
    public ResponseEntity<Object> processEvent(@RequestBody Event event) {
        switch (event.getType()) {
            case "deposit":
                return handleDeposit(event);
            case "withdraw":
                return handleWithdraw(event);
            case "transfer":
                return handleTransfer(event);
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Entry Type");
        }
    }

    /**
     * Handles deposit events by updating the account balance and returning the updated account.
     *
     * @param event the deposit event containing account destination and amount
     * @return a ResponseEntity containing the updated account with status CREATED
     */
    private ResponseEntity<Object> handleDeposit(Event event) {
        Account account = accountService.deposit(event.getDestination(), event.getAmount());
        Map<String, Object> response = new HashMap<>();
        response.put("destination", account);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Handles withdraw events by updating the account balance if sufficient funds are available.
     *
     * @param event the withdraw event containing account origin and amount
     * @return a ResponseEntity containing the updated account with status CREATED, or an error status if the account does not exist or has insufficient funds
     */
    private ResponseEntity<Object> handleWithdraw(Event event) {
        Account account = accountService.withdraw(event.getOrigin(), event.getAmount());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        if (account.getBalance() < event.getAmount()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(0);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("origin", account);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Handles transfer events by updating the balances of the origin and destination accounts.
     *
     * @param event the transfer event containing account origin, account destination, and amount
     * @return a ResponseEntity containing the updated accounts with status CREATED, or an error status if the origin account does not exist or has insufficient funds
     */
    private ResponseEntity<Object> handleTransfer(Event event) {
        Account originAccount = accountService.getAccount(event.getOrigin());
        if (originAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }

        Map<String, Account> accounts = accountService.transfer(event.getOrigin(), event.getDestination(), event.getAmount());
        if (accounts == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(0);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("origin", accounts.get("origin"));
        response.put("destination", accounts.get("destination"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves the balance of a specified account.
     *
     * @param accountId the ID of the account whose balance is to be retrieved
     * @return a ResponseEntity containing the account balance or a status of NOT FOUND if the account does not exist
     */
    @GetMapping("/balance")
    public ResponseEntity<Object> getBalance(@RequestParam(value = "account_id") String accountId) {
        Account account = accountService.getAccount(accountId);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        return ResponseEntity.ok(account.getBalance());
    }
}
