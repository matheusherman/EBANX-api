package com.ebanx.springapi.model;

public class Event {
    private String type;
    private String destination;
    private float amount;

    public String getType() {
        return type;
    }


    public String getDestination() {
        return destination;
    }


    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
