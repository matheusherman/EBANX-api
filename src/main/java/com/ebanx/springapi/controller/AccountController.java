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
    public void reset() {
        accounts.clear();
    }

    @PostMapping("/event")
    public Object processEvent(@RequestBody Event event) {
        switch (event.getType()) {
            case "deposit":
                Account account = accounts.getOrDefault(event.getDestination(), new Account());
                if (account.getId() == null) {
                    account.setId(event.getDestination());
                }
                float balance = account.getBalance();
                account.setBalance(balance + event.getAmount());
                accounts.put(event.getDestination(), account);
                return ResponseEntity.status(HttpStatus.CREATED).body(account);
            default:
                return null;
        }
    }

    @GetMapping("/balance")
    public void Balance(@RequestParam(value = "account_id") Long account_id) {
        // return pegar a conta com base no id e getBalance();
    }
}
