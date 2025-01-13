package com.skinversity.backend.Requests;


public class PaymentRequest {
    private String email;
    private int amount;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "email='" + email + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
