package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.math.BigDecimal;

public interface TransactionDao {

    Transaction createSentApprovedTransaction(Transaction transaction, String accountFromName);
    Transaction getTransaction(int transferId);
    Transaction createRequestedPendingTransaction(Transaction transaction, String accountFromName);
}
