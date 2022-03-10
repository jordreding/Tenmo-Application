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
    public void create(Integer recipientId, BigDecimal amount, String username) {
         String sqlToGetName = "SELECT account_id from account " +
                 "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                 "WHERE username = ?";

         Integer accountFromId = jdbcTemplate.queryForObject(sqlToGetName, Integer.class, username);

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from," +
                " account_to, amount) VALUES (2, 2, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, accountFromId, recipientId, amount);
        } catch (DataAccessException e) {
            e.getMessage();
        }

    }

}
