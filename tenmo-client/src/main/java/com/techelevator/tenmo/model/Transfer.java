package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private long id;
    private long fromAccount;
    private long toAccount;
    private String status;
    private String type;
    private BigDecimal amount;
    private String receiver;
    private String sender;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public long getToAccount() {
        return toAccount;
    }

    public void setToAccount(long toAccount) {
        this.toAccount = toAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Transfer(long id, long fromAccount, long toAccount, String status, String type, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.status = status;
        this.type = type;
        this.amount = amount;
    }

    public Transfer(long toAccount, BigDecimal amount, String status, String type) {
        this.toAccount = toAccount;
        this.amount = amount;
        this.status = status;
        this.type = type;
    }

    public Transfer(){}
}
