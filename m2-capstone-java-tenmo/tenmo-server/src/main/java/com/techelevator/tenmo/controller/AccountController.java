package com.techelevator.tenmo.controller;

import ch.qos.logback.core.db.BindDataSourceToJNDIAction;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;
    private TransactionDao transactionDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transactionDao = transactionDao;
    }

    @RequestMapping(path="/account/{username}/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(@PathVariable String username, Principal principal) {
        return accountDao.getAccountBalance(principal.getName());
    }

    @RequestMapping(path="/account", method = RequestMethod.GET)
    public List<User> getAllUsersNotCurrentUser(Principal principal) {
        return userDao.getAllUsersNotCurrentUser(principal.getName());
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/account/{username}/transaction/send", method = RequestMethod.POST)
    public Transaction createTransaction(@RequestBody Transaction transaction, @PathVariable String username, Principal principal) {
        return transactionDao.createSentApprovedTransaction(transaction, principal.getName());
    }




}
