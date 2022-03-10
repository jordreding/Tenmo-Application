package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.security.Principal;

@Component
public class JdbcTransactionDao implements TransactionDao {

    private JdbcTemplate jdbcTemplate;

    private Transaction transaction = new Transaction();

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createSentApprovedTransaction(Transaction transaction, String accountFromUsername) {
        int accountFrom = getAccountIdFromUsername(accountFromUsername);
        int accountTo = getAccountNumberFromUserId(transaction.getUserIdTo());
        subtractFromSendersAccount(transaction.getAmount(), accountFrom);
        addToRecipientAccount(transaction.getAmount(), accountTo);
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from," +
                " account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class, transaction.getTransfer_type_id(), transaction.getTransfer_status_id(),
                accountFrom, accountTo, transaction.getAmount());
        transaction.setTransferId(id);
        return transaction;
    }

    private int getAccountIdFromUsername(String username) {
        String sql =  "SELECT account_id from account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE username = ?";
        Integer accountFrom = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return accountFrom;
    }

    private int getAccountNumberFromUserId(int userId) {
        String sql =  "SELECT account.account_id from account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE tenmo_user.user_id = ?";
        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return accountId;
    }

    private void subtractFromSendersAccount(BigDecimal amountToSubtract, int senderAccountId) {
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE account.account_id = ?";

        jdbcTemplate.update(sql, amountToSubtract, senderAccountId);
    }

    private void addToRecipientAccount(BigDecimal amountToAdd, int recipientAccountId) {
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE account.account_id = ?";
        jdbcTemplate.update(sql, amountToAdd, recipientAccountId);
    }

}