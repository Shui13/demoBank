package com.sample.bankAccount.controller;

import com.sample.bankAccount.StringConstants;
import com.sample.bankAccount.model.AccountDetails;
import com.sample.bankAccount.model.AccountRequestDetails;
import com.sample.bankAccount.model.TransactionRequestDetails;
import com.sample.bankAccount.model.TransactionResposeDetails;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TransactionsControllerIntegrationTest extends JerseyTest {

    private String senderId;
    private String receiverId;

    @Override
    protected Application configure() {
        return new ResourceConfig(TransactionsController.class, AccountsController.class);
    }

    @Before
    public void setup() {
        senderId = createAndVerifyAccountRequest(0);
        receiverId = createAndVerifyAccountRequest(0);
    }

    @Test
    public void shouldTransferMoneyCorrectly() {
        depositAmount(senderId, 100);

        TransactionRequestDetails transactionRequest = getTransactionRequest(
                senderId,
                receiverId,
                80);
        Response transferResponse = target("demoBank/transfer")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transactionRequest, MediaType.APPLICATION_JSON_TYPE));

        verifySuccessResponse(transferResponse);
        verifyAccountBalance(senderId, 20);
        verifyAccountBalance(receiverId, 80);
    }

    @Test
    public void shouldNotTransferMoneyIfBalanceIsLow() {
        depositAmount(senderId, 100);

        TransactionRequestDetails transactionRequest = getTransactionRequest(
                senderId,
                receiverId,
                180);
        Response transferResponse = target("demoBank/transfer")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transactionRequest, MediaType.APPLICATION_JSON_TYPE));

        verifyFailedResponse(transferResponse);
        verifyAccountBalance(senderId, 100);
        verifyAccountBalance(receiverId, 0);
    }

    @Test
    public void shouldFailIfIncorrectSender() {
        TransactionRequestDetails transactionRequest = getTransactionRequest(
                UUID.randomUUID().toString(),
                receiverId,
                100);
        Response transferResponse = target("demoBank/transfer")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transactionRequest, MediaType.APPLICATION_JSON_TYPE));

        verifyFailedResponse(transferResponse);
    }

    @Test
    public void shouldFailIfIncorrectReceiver() {
        TransactionRequestDetails transactionRequest = getTransactionRequest(
                senderId,
                UUID.randomUUID().toString(),
                100);
        Response transferResponse = target("demoBank/transfer")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transactionRequest, MediaType.APPLICATION_JSON_TYPE));

        verifyFailedResponse(transferResponse);
    }

    @Test
    public void shouldFailIfIncorrectAmount() {
        TransactionRequestDetails transactionRequest = getTransactionRequest(
                senderId,
                receiverId,
                -100);
        Response transferResponse = target("demoBank/transfer")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(transactionRequest, MediaType.APPLICATION_JSON_TYPE));

        verifyFailedResponse(transferResponse);
    }

    private String createAndVerifyAccountRequest(final double amount) {
        String accountId = UUID.randomUUID().toString();
        AccountRequestDetails request = getAccountRequest(accountId, amount);
        Response response = target("demoBank/account/create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(HttpStatus.CREATED_201, response.getStatus());
        TransactionResposeDetails transactionResposeDetails = response.readEntity(TransactionResposeDetails.class);
        Assert.assertEquals(StringConstants.SUCCESS, transactionResposeDetails.getStatus());
        return request.getAccountId();
    }

    private AccountRequestDetails getAccountRequest(final String accountId, final double amount) {
        AccountRequestDetails request = new AccountRequestDetails();
        request.setAccountId(accountId);
        request.setAmount(amount);
        return request;
    }

    private TransactionRequestDetails getTransactionRequest(
            final String senderId,
            final String receiverId,
            final double amount) {
        TransactionRequestDetails request = new TransactionRequestDetails();
        request.setSender(senderId);
        request.setReceiver(receiverId);
        request.setAmount(amount);
        return request;
    }

    private void verifyAccountBalance(final String accountId, final double amount) {
        Response response = target("demoBank/account/get")
                .queryParam("accountId", accountId)
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        AccountDetails accountDetails = response.readEntity(AccountDetails.class);
        assertEquals(accountDetails.getAccountId(), accountId);
        assertEquals(accountDetails.getBalance(), amount, 0);
    }

    private void verifyFailedResponse(final Response response) {
        assertEquals(HttpStatus.BAD_REQUEST_400, response.getStatus());
        TransactionResposeDetails responseDetails = response.readEntity(TransactionResposeDetails.class);
        assertEquals(StringConstants.FAILED, responseDetails.getStatus());
    }

    private void verifySuccessResponse(final Response response) {
        assertEquals(HttpStatus.OK_200, response.getStatus());
        TransactionResposeDetails responseDetails = response.readEntity(TransactionResposeDetails.class);
        assertEquals(StringConstants.SUCCESS, responseDetails.getStatus());
    }

    private void depositAmount(String accountId, double amount) {
        AccountRequestDetails depositRequest = getAccountRequest(accountId, amount);
        Response depositResponse = target("demoBank/account/deposit")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(depositRequest, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(HttpStatus.OK_200, depositResponse.getStatus());
    }
}
