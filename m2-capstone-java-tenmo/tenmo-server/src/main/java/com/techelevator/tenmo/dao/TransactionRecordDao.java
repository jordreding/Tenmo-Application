package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransactionRecord;

import java.util.List;

public interface TransactionRecordDao {

    List<TransactionRecord> getAllTransactions(String username);
}
