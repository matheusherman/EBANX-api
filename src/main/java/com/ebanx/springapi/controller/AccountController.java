package com.ebanx.springapi.controller;

import com.ebanx.springapi.model.Account;
import com.ebanx.springapi.model.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

    private final Map<String, Account> accounts = new HashMap<>();

    @PostMapping("/reset")
    public ResponseEntity<Object> reset() {
        accounts.clear();
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
        Account account = accounts.getOrDefault(event.getDestination(), new Account());
        if (account.getId() == null) {
            account.setId(event.getDestination());
        }
        account.setBalance(account.getBalance() + event.getAmount());
        accounts.put(event.getDestination(), account);

        Map<String, Object> response = new HashMap<>();
        response.put("destination", account);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ResponseEntity<Object> handleWithdraw(Event event) {
        Account account = accounts.get(event.getOrigin());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        if (account.getBalance() < event.getAmount()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(0);
        }
        account.setBalance(account.getBalance() - event.getAmount());
        accounts.put(event.getOrigin(), account);

        Map<String, Object> response = new HashMap<>();
        response.put("origin", account);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ResponseEntity<Object> handleTransfer(Event event) {
        Account accountOrigin = accounts.get(event.getOrigin());
        if (accountOrigin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        if (accountOrigin.getBalance() < event.getAmount()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(0);
        }
        Account accountDestination = accounts.getOrDefault(event.getDestination(), new Account());
        if (accountDestination.getId() == null) {
            accountDestination.setId(event.getDestination());
        }
        accountOrigin.setBalance(accountOrigin.getBalance() - event.getAmount());
        accountDestination.setBalance(accountDestination.getBalance() + event.getAmount());
        accounts.put(event.getOrigin(), accountOrigin);
        accounts.put(event.getDestination(), accountDestination);

        Map<String, Object> response = new HashMap<>();
        response.put("origin", accountOrigin);
        response.put("destination", accountDestination);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/balance")
    public ResponseEntity<Object> Balance(@RequestParam(value = "account_id") String account_id) {
        Account account = accounts.get(account_id);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        } else {
            return ResponseEntity.ok(account.getBalance());
        }
    }
}
