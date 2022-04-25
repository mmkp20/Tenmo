package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/{id}/list", method = RequestMethod.GET)
    public List<Transfer> getTransactionList(@PathVariable long id) {
        return transferDao.getTransactionList(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void transfer(@PathVariable long id, @RequestBody Transfer transfer) {
        transferDao.transfer(id, transfer);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable long id) {
        return transferDao.getTransferById(id);
    }

    public void request(long userId, BigDecimal amount) {

    }

    //  check status is pending or different status
    public String getStatus() {
        return null;
    }

    public boolean rejectOrApprove() {
        return true;
    }
}
