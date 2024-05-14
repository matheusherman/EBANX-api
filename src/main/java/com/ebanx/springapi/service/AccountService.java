package com.ebanx.springapi.service;

import com.ebanx.springapi.model.Account;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    private final Map<String, Account> accounts = new HashMap<>();

    /**
     * Resets all accounts by clearing the accounts map.
     */
    public void resetAccounts() {
        accounts.clear();
    }

    /**
     * Handles deposit events by updating the account balance.
     *
     * @param accountId the ID of the account
     * @param amount the amount to deposit
     * @return the updated Account
     */
    public Account deposit(String accountId, float amount) {
        Account account = getOrCreateAccount(accountId);
        account.setBalance(account.getBalance() + amount);
        accounts.put(accountId, account);
        return account;
    }

    /**
     * Handles withdraw events by updating the account balance if sufficient funds are available.
     *
     * @param accountId the ID of the account
     * @param amount the amount to withdraw
     * @return the updated Account or null if account not found
     */
    public Account withdraw(String accountId, float amount) {
        Account account = accounts.get(accountId);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accounts.put(accountId, account);
        }
        return account;
    }

    /**
     * Handles transfer events by updating the balances of the origin and destination accounts.
     *
     * @param originId the ID of the origin account
     * @param destinationId the ID of the destination account
     * @param amount the amount to transfer
     * @return a map containing the updated origin and destination accounts
     */
    public Map<String, Account> transfer(String originId, String destinationId, float amount) {
        Account origin = accounts.get(originId);
        if (origin == null || origin.getBalance() < amount) {
            return null;
        }
        Account destination = getOrCreateAccount(destinationId);
        origin.setBalance(origin.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
        accounts.put(originId, origin);
        accounts.put(destinationId, destination);

        Map<String, Account> result = new HashMap<>();
        result.put("origin", origin);
        result.put("destination", destination);
        return result;
    }

    /**
     * Retrieves an existing account or creates a new one if it does not exist.
     *
     * @param accountId the ID of the account to retrieve or create
     * @return the retrieved or newly created Account
     */
    private Account getOrCreateAccount(String accountId) {
        Account account = accounts.getOrDefault(accountId, new Account());
        if (account.getId() == null) {
            account.setId(accountId);
        }
        return account;
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param accountId the ID of the account
     * @return the Account or null if not found
     */
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }
}
