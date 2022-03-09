package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountService {

    private final RestTemplate restTemplate = new RestTemplate();
    private String baseUrl = "";
    private AuthenticatedUser user;

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        BigDecimal balance = restTemplate.exchange(baseUrl + "account/" + user.getUser().getUsername() + "/balance", HttpMethod.GET, entity, BigDecimal.class).getBody();
        return balance;
    }

    public List<User> getAllUsersNotCurrentUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        User[] userArray = restTemplate.exchange(baseUrl + "account", HttpMethod.GET, entity, User[].class).getBody();
        List<User> userList = Arrays.asList(userArray);

        return userList;
    }
    




}
