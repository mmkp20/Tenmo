package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private long accountId;
    private BigDecimal balance;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
