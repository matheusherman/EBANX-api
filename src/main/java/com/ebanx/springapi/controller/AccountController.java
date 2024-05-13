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

    private Map<String, Account> accounts = new HashMap<>();

    @PostMapping("/reset")
    public ResponseEntity<Object> reset() {
        accounts.clear();
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @PostMapping("/event")
    public Object processEvent(@RequestBody Event event) {
        switch (event.getType()) {
            case "deposit":
                Account account = accounts.getOrDefault(event.getDestination(), new Account());
                if (account.getId() == null) {
                    account.setId(event.getDestination());
                }
                account.setBalance(account.getBalance() + event.getAmount());
                accounts.put(event.getDestination(), account);
                Map<String, Object> depositResponse = new HashMap<>();
                depositResponse.put("destination", account);
                return ResponseEntity.status(HttpStatus.CREATED).body(depositResponse);

            case "withdraw":
                Account accountWithdraw = accounts.get(event.getOrigin());
                if (accountWithdraw == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
                }
                if (accountWithdraw.getBalance() < event.getAmount()) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(0);
                }
                accountWithdraw.setBalance(accountWithdraw.getBalance() - event.getAmount());
                accounts.put(event.getOrigin(), accountWithdraw);
                return ResponseEntity.status(HttpStatus.CREATED).body(accountWithdraw);
            case "transfer":
                Account accountOrigin = accounts.get(event.getOrigin());
                if (accountOrigin == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
                }
                Account accountDestination = accounts.getOrDefault(event.getDestination(), new Account());
                if (accountOrigin == null || accountOrigin.getBalance() < event.getAmount()) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(0);
                }
                if (accountDestination.getId() == null) {
                    accountDestination.setId(event.getDestination());
                }
                accountOrigin.setBalance(accountOrigin.getBalance() - event.getAmount());
                accountDestination.setBalance(accountDestination.getBalance() + event.getAmount());
                accounts.put(event.getOrigin(), accountOrigin);
                accounts.put(event.getDestination(), accountDestination);
                return ResponseEntity.status(HttpStatus.CREATED).body(accountOrigin);
            default:
                return null;
        }
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
