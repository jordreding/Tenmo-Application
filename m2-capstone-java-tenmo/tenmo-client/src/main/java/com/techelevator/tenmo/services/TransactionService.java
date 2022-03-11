package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

public class TransactionService {
    private final RestTemplate restTemplate = new RestTemplate();
    private String baseUrl = "";
    private AuthenticatedUser user;

    public TransactionService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public Transaction addSendTransaction(Transaction sendTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transaction> entity = new HttpEntity<Transaction>(sendTransaction, headers);
        String username = user.getUser().getUsername();
        Transaction sentTransaction = restTemplate.exchange(baseUrl + "account/" + username + "/transaction/send", HttpMethod.POST, entity, Transaction.class).getBody();

        return sentTransaction;
    }

    public Transaction getTransactionByTransferId(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        Transaction transaction = restTemplate.exchange(baseUrl + "account/" + user.getUser().getUsername() + "/transaction/" + transferId, HttpMethod.GET, entity, Transaction.class).getBody();
        return transaction;
    }


}
