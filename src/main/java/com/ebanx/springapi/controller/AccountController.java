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

    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        accountService.resetAccounts();
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

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

    private ResponseEntity<Object> handleDeposit(Event event) {
        Account account = accountService.deposit(event.getDestination(), event.getAmount());
        Map<String, Object> response = new HashMap<>();
        response.put("destination", account);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ResponseEntity<Object> handleWithdraw(Event event) {
        Account account = accountService.withdraw(event.getOrigin(), event.getAmount());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("origin", account);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ResponseEntity<Object> handleTransfer(Event event) {
        Map<String, Account> accounts = accountService.transfer(event.getOrigin(), event.getDestination(), event.getAmount());
        if (accounts == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(0);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("origin", accounts.get("origin"));
        response.put("destination", accounts.get("destination"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/balance")
    public ResponseEntity<Object> getBalance(@RequestParam(value = "account_id") String accountId) {
        Account account = accountService.getAccount(accountId);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        return ResponseEntity.ok(account.getBalance());
    }
}
