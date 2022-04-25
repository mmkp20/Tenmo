package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransactionList(long id) {
        List<Transfer> list = new ArrayList<>();
        String sql = "select t.transfer_id as id, ur.username as receiver, us.username as sender, t.amount as amount " +
                "from transfer as t " +
                "join account as asend " +
                "on t.account_from = asend.account_id " +
                "join account as areceive " +
                "on t.account_to = areceive.account_id " +
                "join tenmo_user as us " +
                "on us.user_id = asend.user_id " +
                "join tenmo_user as ur " +
                "on ur.user_id = areceive.user_id " +
                "where ur.user_id = ? or us.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);

        while (results.next()) {
            list.add(mapToRowTransaction(results));
        }
        return list;
    }

    @Override
    @Transactional
    public void transfer(long id, Transfer transfer) {
        try {
            String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getToAccount());

            sql = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getFromAccount());

            String sql2 = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES " +
                    "((SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc = ?)," +
                    "(SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?), " +
                    "(SELECT account_id FROM account WHERE user_id = ?), (SELECT account_id FROM account WHERE user_id = ?), ?);";
            long transfer_id = jdbcTemplate.update(sql2, transfer.getType(), transfer.getStatus(), (int) id, (int) transfer.getToAccount(), transfer.getAmount());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Transfer getTransferById(long id) {
        Transfer transfer = null;
        String sql = "SELECT t.transfer_id AS id, us.username AS sender, ur.username AS receiver, ttype.transfer_type_desc AS type, tstatus.transfer_status_desc AS status, t.amount AS amount " +
                "FROM transfer AS t " +
                "JOIN account AS asend " +
                "ON t.account_from = asend.account_id " +
                "JOIN account AS areceive " +
                "ON t.account_to = areceive.account_id " +
                "JOIN tenmo_user AS us " +
                "ON us.user_id = asend.user_id " +
                "JOIN tenmo_user AS ur " +
                "ON ur.user_id = areceive.user_id " +
                "JOIN transfer_type AS ttype " +
                "ON ttype.transfer_type_id = t.transfer_type_id " +
                "JOIN transfer_status AS tstatus " +
                "ON tstatus.transfer_status_id = t.transfer_status_id " +
                "WHERE t.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            transfer = mapToRowSingleTransfer(results);
        }
        return transfer;
    }

    @Override
    public void request(long userId, BigDecimal amount) {

    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public boolean rejectOrApprove() {
        return false;
    }

    private Transfer mapToRowTransaction(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getLong("id"));
        transfer.setReceiver(results.getString("receiver"));
        transfer.setSender(results.getString("sender"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }

    private Transfer mapToRowSingleTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getLong("id"));
        transfer.setReceiver(results.getString("receiver"));
        transfer.setSender(results.getString("sender"));
        transfer.setType(results.getString("type"));
        transfer.setStatus(results.getString("status"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}