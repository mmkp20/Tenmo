package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class TransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public User[] list(AuthenticatedUser user) {
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(baseUrl + "transfer", HttpMethod.GET, createEntity(user), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public Transfer[] transferList(AuthenticatedUser user) {
        long userId = user.getUser().getId();

        Transfer[] transactions = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(baseUrl + "transfer/" + userId + "/list", HttpMethod.GET, createEntity(user), Transfer[].class);
            transactions = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transactions;
    }

    public void transfer(Transfer transfer, AuthenticatedUser user) {
        HttpEntity<Transfer> entity = createTransferEntity(transfer, user);

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "transfer/" + transfer.getFromAccount(), HttpMethod.PUT, entity, Transfer.class);
            response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public Transfer getTransferDetail(String transferId, AuthenticatedUser user) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "transfer/" + transferId, HttpMethod.GET, createEntity(user), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    private HttpEntity<AuthenticatedUser> createEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> createTransferEntity(Transfer transfer, AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }
}
