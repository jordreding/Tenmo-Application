package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.Assert;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdbcAccountDaoTest extends BaseDaoTests {

    private AccountDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcAccountDao(jdbcTemplate);
        }

    @Test
    public void getAllAccounts_Should_return_all_accounts() {
        List<Account> accounts = dao.getAllAccounts("testUserOne");

        Assert.assertEquals(3, accounts.size());
        Assert.assertEquals(1002, accounts.get(0).getUserId());
        Assert.assertEquals(1003, accounts.get(1).getUserId());
        Assert.assertEquals(1004, accounts.get(2).getUserId());
    }

    @Test
    public void getAccountBalance_should_return_balance() {
        BigDecimal balance = dao.getAccountBalance("testUserOne");

        Assert.assertEquals(new BigDecimal("1000.00"), balance);
    }
}
