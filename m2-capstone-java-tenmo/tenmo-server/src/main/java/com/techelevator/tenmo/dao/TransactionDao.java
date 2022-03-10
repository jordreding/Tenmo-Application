package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface TransactionDao {

    void create(Integer recipientId, BigDecimal amount, String username);
}
