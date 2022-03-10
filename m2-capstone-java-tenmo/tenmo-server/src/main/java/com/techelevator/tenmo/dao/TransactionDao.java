package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.math.BigDecimal;

public interface TransactionDao {

    Transaction createSentApprovedTransaction(Transaction transaction, String accountFromName);
}
