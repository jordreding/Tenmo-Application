package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getAccountBalance(String username);
}
