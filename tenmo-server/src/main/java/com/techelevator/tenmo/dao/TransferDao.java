package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getTransactionList(long id);
    void transfer(long id, Transfer transfer);
    void request(long userId, BigDecimal amount);
    String getStatus();
    boolean rejectOrApprove();
    Transfer getTransferById(long id);
}
