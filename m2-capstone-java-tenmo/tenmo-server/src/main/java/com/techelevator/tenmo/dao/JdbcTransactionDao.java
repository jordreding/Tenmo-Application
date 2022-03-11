package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createRequestedPendingTransaction(Transaction transaction, String accountFromName) {
        int accountFrom = getAccountIdFromUsername(accountFromName);
        int accountTo = getAccountNumberFromUserId(transaction.getUserIdTo());
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from," +
                " account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class, transaction.getTransfer_type_id(), transaction.getTransfer_status_id(),
                accountFrom, accountTo, transaction.getAmount());
        transaction.setTransferId(id);
        return transaction;
    }

    @Override
    public Transaction getTransaction(int transferId) throws UsernameNotFoundException {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, transferId);
        if (row.next()) {
            Transaction transactionIncomplete = mapRowToTransaction(row);
            return getNamesForTransactionByAccountId(transactionIncomplete);
        }
        throw new UsernameNotFoundException("TransferId: " + transferId + " was not found.");
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

    private Transaction getNamesForTransactionByAccountId(Transaction transaction) {
        String fromName = getNameByAccountId(transaction.getAccountFrom());
        transaction.setFromName(fromName);
        String toName = getNameByAccountId(transaction.getUserIdTo());
        transaction.setToName(toName);
        return transaction;
    }

    private String getNameByAccountId(int userId) {
        String sql = "SELECT username from tenmo_user " +
                "JOIN account ON tenmo_user.user_id = account.user_id " +
                "WHERE account_id = ?";

        String username = jdbcTemplate.queryForObject(sql, String.class, userId);
        return username;
    }

    private Transaction mapRowToTransaction(SqlRowSet row) {
        Transaction transaction = new Transaction();
        transaction.setTransferId(row.getInt("transfer_id"));
        transaction.setTransfer_type_id(row.getInt("transfer_type_id"));
        transaction.setTransfer_status_id(row.getInt(("transfer_status_id")));
        transaction.setAccountFrom(row.getInt("account_from"));
        transaction.setUserIdTo(row.getInt("account_to"));
        BigDecimal amount = new BigDecimal(row.getDouble("amount"));
        transaction.setAmount(amount);

        return transaction;
    }

}
