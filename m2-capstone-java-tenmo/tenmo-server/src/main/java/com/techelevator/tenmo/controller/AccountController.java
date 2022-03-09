package com.techelevator.tenmo.controller;

import ch.qos.logback.core.db.BindDataSourceToJNDIAction;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path="/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal principal) {
        String name = principal.getName();
        BigDecimal accountBalance = accountDao.getAccountBalance(name);
        return accountBalance;
    }

}
