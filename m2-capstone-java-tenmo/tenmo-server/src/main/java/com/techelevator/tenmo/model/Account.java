package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private BigDecimal balance;
    private int accountId;
    private int userId;

    public Account() {}

    public Account(BigDecimal balance, int userId) {
        this.balance = balance;
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
