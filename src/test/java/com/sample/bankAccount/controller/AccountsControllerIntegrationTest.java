package com.sample.bankAccount.controller;

import com.sample.bankAccount.StringConstants;
import com.sample.bankAccount.model.AccountDetails;
import com.sample.bankAccount.model.AccountRequestDetails;
import com.sample.bankAccount.model.TransactionResposeDetails;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AccountsControllerIntegrationTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(AccountsController.class);
    }

    @Test
    public void shouldReturnNotFoundIfAccountIdNull() {
        Response response = target("demoBank/account/get").request()
                .get();

        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
    }

    @Test
    public void shouldReturnNotFoundIfAccountIdNotPresent() {
        Response response = target("demoBank/account/get")
                .queryParam("accountId", UUID.randomUUID())
                .request()
                .get();

        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
    }

    @Test
    public void shouldCreateAndGetAccount() {
        String accountId = createAndVerifyAccountRequest(0);

        verifyAccountBalance(accountId, 0);
    }

    @Test
    public void shouldCreateAndGetAccountWithAmount() {
        String accountId = createAndVerifyAccountRequest(100.50);

        verifyAccountBalance(accountId, 100.50);
    }

    @Test
    public void shouldDepositAmount() {
        String accountId = createAndVerifyAccountRequest(100);

        AccountRequestDetails depositRequest = getAccountRequest(accountId, 50);
        Response depositResponse = target("demoBank/account/deposit")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(depositRequest, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(HttpStatus.OK_200, depositResponse.getStatus());
        TransactionResposeDetails transactionResposeDetails = depositResponse.readEntity(TransactionResposeDetails.class);
        Assert.assertEquals(StringConstants.SUCCESS, transactionResposeDetails.getStatus());

        verifyAccountBalance(accountId, 150);
    }

    @Test
    public void shouldFailIfDepositAmountIncorrect() {
        String accountId = createAndVerifyAccountRequest(100);

        AccountRequestDetails depositRequest = getAccountRequest(accountId, -100);
        Response depositResponse = target("demoBank/account/deposit")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(depositRequest, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(HttpStatus.BAD_REQUEST_400, depositResponse.getStatus());
        TransactionResposeDetails transactionResposeDetails = depositResponse.readEntity(TransactionResposeDetails.class);
        assertEquals(StringConstants.FAILED, transactionResposeDetails.getStatus());

        verifyAccountBalance(accountId, 100);
    }

    @Test
    public void shouldWithdrawAmount() {
        String accountId = createAndVerifyAccountRequest(100);

        AccountRequestDetails depositRequest = getAccountRequest(accountId, 50);
        Response depositResponse = target("demoBank/account/withdraw")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(depositRequest, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(HttpStatus.OK_200, depositResponse.getStatus());
        TransactionResposeDetails transactionResposeDetails = depositResponse.readEntity(TransactionResposeDetails.class);
        assertEquals(StringConstants.SUCCESS, transactionResposeDetails.getStatus());

        verifyAccountBalance(accountId, 50);
    }

    @Test
    public void shouldFailIfWithdrawAmountIncorrect() {
        String accountId = createAndVerifyAccountRequest(100);

        AccountRequestDetails depositRequest = getAccountRequest(accountId, -50);
        Response depositResponse = target("demoBank/account/withdraw")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(depositRequest, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(HttpStatus.BAD_REQUEST_400, depositResponse.getStatus());
        TransactionResposeDetails transactionResposeDetails = depositResponse.readEntity(TransactionResposeDetails.class);
        assertEquals(StringConstants.FAILED, transactionResposeDetails.getStatus());

        verifyAccountBalance(accountId, 100);
    }

    @Test
    public void shouldFailIfWithdrawAmountMoreThanBalance() {
        String accountId = createAndVerifyAccountRequest(100);

        AccountRequestDetails depositRequest = getAccountRequest(accountId, 250);
        Response depositResponse = target("demoBank/account/withdraw")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(depositRequest, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(HttpStatus.BAD_REQUEST_400, depositResponse.getStatus());
        TransactionResposeDetails transactionResposeDetails = depositResponse.readEntity(TransactionResposeDetails.class);
        assertEquals(StringConstants.FAILED, transactionResposeDetails.getStatus());

        verifyAccountBalance(accountId, 100);
    }

    private String createAndVerifyAccountRequest(final double amount) {
        String accountId = UUID.randomUUID().toString();
        AccountRequestDetails request = getAccountRequest(accountId, amount);
        Response response = target("demoBank/account/create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(HttpStatus.CREATED_201, response.getStatus());
        TransactionResposeDetails transactionResposeDetails = response.readEntity(TransactionResposeDetails.class);
        assertEquals(StringConstants.SUCCESS, transactionResposeDetails.getStatus());
        return request.getAccountId();
    }

    private AccountRequestDetails getAccountRequest(final String accountId, final double amount) {
        AccountRequestDetails request = new AccountRequestDetails();
        request.setAccountId(accountId);
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
}
