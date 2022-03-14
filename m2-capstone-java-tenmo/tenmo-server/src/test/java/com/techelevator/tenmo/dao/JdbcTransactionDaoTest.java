package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdbcTransactionDaoTest extends BaseDaoTests {

    private JdbcTemplate jdbcTemplate;
    private JdbcTransactionDao dao;
    private Transaction transaction;
    private JdbcUserDao user1;
    private JdbcUserDao user2;

    @Before
    public void setup() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.user1 = new JdbcUserDao(jdbcTemplate);
        this.user2 = new JdbcUserDao(jdbcTemplate);
        this.transaction = new Transaction();
        this.dao = new JdbcTransactionDao(jdbcTemplate);
        transaction.setAccountFrom(2001);
        transaction.setTransferType("TO");
        transaction.setUserIdTo(1002);
        transaction.setAmount(new BigDecimal(100));
        transaction.setTransfer_status_id(2);
        transaction.setTransfer_type_id(2);
        transaction.setFromName("bob");
        transaction.setToName("mark");
        user1.create("bob", "bobby");
        user2.create("mark", "markus");
    }

    @Test
    public void get_all_approved_transactions_returns_0_if_none() {
        List<Transaction> approved = dao.getAllApprovedTransactions("bob");
        assertEquals(0, approved.size());
    }

    @Test
    public void get_all_approved_transactions_returns_2() {
        dao.createSentApprovedTransaction(transaction, transaction.getFromName());
        dao.createSentApprovedTransaction(transaction, transaction.getFromName());
        List<Transaction> approved = dao.getAllApprovedTransactions("bob");
        assertEquals(2, approved.size());
    }

    @Test
    public void get_all_pending_transactions_returns_0() {
        List<Transaction> pending = dao.getAllPendingTransactions("bob");
        assertEquals(0, pending.size());
    }



    @Test
    public void createSentApprovedTransaction_creates_transaction() {
        dao.createSentApprovedTransaction(transaction, transaction.getFromName());
        List<Transaction> approvedTransactions = dao.getAllApprovedTransactions("bob");
        assertEquals(1, approvedTransactions.size());
    }

    @Test
    public void getTransaction_returns_correct_transaction() {
        dao.createSentApprovedTransaction(transaction, transaction.getFromName());
        Transaction transaction = new Transaction();
        List<Transaction> approved = dao.getAllApprovedTransactions("bob");
        transaction = dao.getTransaction(3004);

        assertEquals("bob", transaction.getFromName());
        assertEquals(new BigDecimal(100), transaction.getAmount());
        assertEquals(2, transaction.getTransfer_status_id());
    }

}
