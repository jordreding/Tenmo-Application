package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.TransactionRecord;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TransactionRecordService {
    private final RestTemplate restTemplate = new RestTemplate();
    private String baseUrl = "";
    private AuthenticatedUser user;

    public TransactionRecordService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public List<TransactionRecord> getAllTransactionRecords() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String username = user.getUser().getUsername();
        TransactionRecord[] transactionRecords = restTemplate.exchange(baseUrl + "account/" + username + "/transaction", HttpMethod.GET, entity, TransactionRecord[].class).getBody();
        return Arrays.asList(transactionRecords);
    }


}
