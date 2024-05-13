package com.ebanx.springapi.model;

public class Account {
    private String account_id;
    private float balance;

    public String getId() {
        return account_id;
    }

    public void setId(String account_id) {
        this.account_id = account_id;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
