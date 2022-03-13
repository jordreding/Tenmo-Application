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
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        dao = new JdbcAccountDao(dataSource);        }
        }

    @Test
    public void getAllAccounts_Should_return_all_accounts() {
        List<Account> accounts = dao.getAllAccounts("testUserOne");

        assertEquals(3, accounts.size());
        assertEquals("1002", accounts.get(0).getUserId());
        assertEquals("1003", accounts.get(1).getUserId());
        assertEquals("1004", accounts.get(2).getUserId());
    }
}
