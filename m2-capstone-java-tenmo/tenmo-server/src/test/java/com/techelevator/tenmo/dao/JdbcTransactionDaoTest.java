package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdbcTransactionDaoTest extends BaseDaoTests {

    private TransactionDao dao;
    private Transaction transaction;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTransactionDao(jdbcTemplate);
        transaction.setAccountFrom(2001);
        transaction.setTransferId(123);
        transaction.setTransferType("TO");
        transaction.setUserIdTo(1002);
        transaction.setAmount(new BigDecimal(100));
        transaction.setTransfer_status_id(2);
        transaction.setTransfer_type_id(2);
        transaction.setFromName("bob");
        transaction.setToName("mark");
    }

    @Test
    public void createSentApprovedTransaction_creates_transaction() {
        dao.createSentApprovedTransaction(transaction, transaction.getFromName());

        assertEquals("mark", dao.getTransaction(123).getToName());
        assertEquals(2, dao.getTransaction(123).getTransfer_type_id());
    }

    @Test
    public void getTransaction_returns_correct_transaction() {
        Transaction transaction = new Transaction();
        transaction = dao.getTransaction(123);

        assertEquals("bob", transaction.getFromName());
        assertEquals(2001, transaction.getAccountFrom());
    }

}
