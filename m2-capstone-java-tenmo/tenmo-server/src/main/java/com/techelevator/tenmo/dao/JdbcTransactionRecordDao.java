package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransactionRecordDao implements TransactionRecordDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTransactionRecordDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TransactionRecord> getAllApprovedTransactions(String username){
        List<TransactionRecord> fullListOfTransaction = new ArrayList<>();
        List<TransactionRecord> transactionsSentToUser = getTransactionsSentToUser(username, 2);
        List<TransactionRecord> transactionsSentFromUser = getTransactionsSentFromUser(username,2);
        fullListOfTransaction.addAll(transactionsSentFromUser);
        fullListOfTransaction.addAll(transactionsSentToUser);
        return fullListOfTransaction;

     }

     @Override
     public List<TransactionRecord> getAllPendingTransactions(String username) {
         List<TransactionRecord> fullListOfTransaction = new ArrayList<>();
         List<TransactionRecord> transactionsSentFromUser = getTransactionsSentToUser(username, 1);
         fullListOfTransaction.addAll(transactionsSentFromUser);
         return fullListOfTransaction;
     }



     private List<TransactionRecord> getTransactionsSentToUser(String username, int statusId) {
        List<TransactionRecord> transactionsSentToUser = new ArrayList<>();
        int accountNumber = getAccountIdFromUsername(username);

        String sql = "SELECT transfer_id, transfer_type_id, username, amount FROM transfer " +
                "JOIN account ON account.account_id = transfer.account_to " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE account_from = ? AND transfer_status_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountNumber, statusId);
        while (rows.next()) {
            TransactionRecord record = mapRowToRecord(rows);
            record.setTransferType("TO");
            transactionsSentToUser.add(record);
        }
        return transactionsSentToUser;
     }

    private List<TransactionRecord> getTransactionsSentFromUser(String username, int statusId) {
        List<TransactionRecord> transactionsSentToUser = new ArrayList<>();
        int accountNumber = getAccountIdFromUsername(username);
        String sql = "SELECT transfer_id, transfer_type_id, username, amount FROM transfer " +
                "JOIN account ON account.account_id = transfer.account_from " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE account_to = ? AND transfer_status_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountNumber, statusId);
        while (rows.next()) {
            TransactionRecord record = mapRowToRecord(rows);
            record.setTransferType("FROM");
            transactionsSentToUser.add(record);
        }
        return transactionsSentToUser;
    }


    private TransactionRecord mapRowToRecord(SqlRowSet rows) {
        TransactionRecord record = new TransactionRecord();
        record.setTransferId(rows.getInt("transfer_id"));
        record.setUsername(rows.getString("username"));
        record.setAmount(rows.getDouble("amount"));
        return record;
    }

    private int getAccountIdFromUsername(String username) {
        String sql =  "SELECT account_id from account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE username = ?";
        Integer accountFrom = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return accountFrom;
    }
}
