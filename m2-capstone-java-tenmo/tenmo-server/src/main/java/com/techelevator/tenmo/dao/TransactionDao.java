package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDao {

    Transaction createSentApprovedTransaction(Transaction transaction, String accountFromName);
    Transaction getTransaction(int transferId);
    Transaction createRequestedPendingTransaction(Transaction transaction, String accountFromName);
    void approvePendingTransaction(Transaction transaction);
    void rejectPendingTransaction(Transaction transaction);
    List<Transaction> getAllApprovedTransactions(String username);
    List<Transaction> getAllPendingTransactions(String username);
}
