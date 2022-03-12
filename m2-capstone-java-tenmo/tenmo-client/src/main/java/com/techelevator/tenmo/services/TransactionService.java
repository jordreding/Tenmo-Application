package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;

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

    public Transaction addRequestTransaction(Transaction requestTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transaction> entity = new HttpEntity<Transaction>(requestTransaction, headers);
        String username = user.getUser().getUsername();
        Transaction requestedTransaction = restTemplate.exchange(baseUrl + "account/" + username + "/transaction/request", HttpMethod.POST, entity, Transaction.class).getBody();

        return requestedTransaction;
    }

    public Transaction getTransactionByTransferId(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        Transaction transaction = restTemplate.exchange(baseUrl + "account/" + user.getUser().getUsername() + "/transaction/" + transferId, HttpMethod.GET, entity, Transaction.class).getBody();
        return transaction;
    }

    public List<Transaction> getAllPendingTransactionRecords() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String username = user.getUser().getUsername();
        Transaction[] pendingTransactions = restTemplate.exchange(baseUrl + "account/" + username + "/transaction/pending", HttpMethod.GET, entity, Transaction[].class).getBody();
        return Arrays.asList(pendingTransactions);
    }

    public List<Transaction> getAllApprovedTransactionRecords() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String username = user.getUser().getUsername();
        Transaction[] transaction = restTemplate.exchange(baseUrl + "account/" + username + "/transaction/approved", HttpMethod.GET, entity, Transaction[].class).getBody();
        return Arrays.asList(transaction);
    }

    public void approveOrReject(Transaction transaction, int userDecision) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transaction> entity = new HttpEntity<Transaction>(transaction, headers);
        String username = user.getUser().getUsername();
        if (userDecision == 1) {
            restTemplate.exchange(baseUrl + "account/" + username + "/transaction/request/"+ transaction.getTransferId() +"/approved", HttpMethod.PUT, entity, Transaction.class);
        } else if (userDecision == 2) {
            restTemplate.exchange(baseUrl + "account/" + username + "/transaction/request/"+ transaction.getTransferId() +"/rejected", HttpMethod.PUT, entity, Transaction.class);
        }
    }

    public boolean transferIdExists(List<Transaction> records, int transferId) {
        for (Transaction record : records) {
            if (record.getTransferId() == transferId) {
                return true;
            }
        }
        return false;
    }


}
