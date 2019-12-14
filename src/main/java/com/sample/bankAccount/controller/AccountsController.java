package com.sample.bankAccount.controller;

import com.sample.bankAccount.StringConstants;
import com.sample.bankAccount.model.AccountDetails;
import com.sample.bankAccount.accounts.AccountServiceI;
import com.sample.bankAccount.accounts.AccountServiceImpl;
import com.sample.bankAccount.model.AccountRequestDetails;
import com.sample.bankAccount.model.TransactionResposeDetails;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("demoBank/account")
public class AccountsController {

    private AccountServiceI accountServiceI = new AccountServiceImpl();

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //creates new account
    public Response createAccount(AccountRequestDetails accountRequestDetails) {
        if (accountServiceI.createAccount(accountRequestDetails.getAccountId(), accountRequestDetails.getAmount())) {
            return Response.status(Response.Status.CREATED).entity(new TransactionResposeDetails(StringConstants.SUCCESS, "New user account created")).type(MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new TransactionResposeDetails(StringConstants.FAILED, "User creation failed")).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/get")
    @Consumes(MediaType.TEXT_PLAIN)
    //fetches account details for a given ID. If not found returns htts status 404
    public Response getAccountDetails(@QueryParam("accountId") String accountId) {
        AccountDetails accountDetails = accountServiceI.getAccountDetails(accountId);
        if (null == accountDetails) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(accountDetails).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositMoney(AccountRequestDetails account) {

        if (accountServiceI.addAmount(accountServiceI.getAccountDetails(account.getAccountId()), account.getAmount())) {
            return Response.status(Response.Status.OK).entity(new TransactionResposeDetails(StringConstants.SUCCESS, "deposit Successful")).type(MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(new TransactionResposeDetails(StringConstants.FAILED, "deposit Failed")).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @PUT
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdrawMoney(AccountRequestDetails account) {
        if (accountServiceI.deductAmount(accountServiceI.getAccountDetails(account.getAccountId()), account.getAmount())) {
            return Response.status(Response.Status.OK).entity(new TransactionResposeDetails(StringConstants.SUCCESS, "withdraw Successful")).type(MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(new TransactionResposeDetails(StringConstants.FAILED, "withdraw Failed")).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
