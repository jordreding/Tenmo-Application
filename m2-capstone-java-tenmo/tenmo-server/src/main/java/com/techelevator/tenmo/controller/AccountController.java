package com.techelevator.tenmo.controller;

import ch.qos.logback.core.db.BindDataSourceToJNDIAction;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path="/account/{username}/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(@PathVariable String username, Principal principal) {
        return accountDao.getAccountBalance(principal.getName());
    }

    @RequestMapping(path="/account", method = RequestMethod.GET)
    public List<User> getAllUsersNotCurrentUser(Principal principal) {
        return userDao.getAllUsersNotCurrentUser(principal.getName());
    }



}
