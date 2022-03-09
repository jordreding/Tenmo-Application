package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getAccountBalance(String username) {
        String sql = "SELECT balance " +
                "from account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE username = ?";

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
    }

    @Override
    public List<Account> getAllAccounts(String username) {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT account.user_id, username " +
                "from account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE username != ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, username);
        while (rows.next()) {
            Account account = mapRowToAccount(rows);
            accountList.add(account);
        }
        return accountList;
    }

    private Account mapRowToAccount(SqlRowSet rows) {
        Account account = new Account();
        account.setUserId(rows.getInt("user_id"));
        account.setBalance(rows.getBigDecimal("balance"));

        return account;
    }

}
