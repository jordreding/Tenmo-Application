package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransactionRecord;

import java.util.List;

public interface TransactionRecordDao {

    List<TransactionRecord> getAllApprovedTransactions(String username);
    List<TransactionRecord> getAllPendingTransactions(String username);
}
