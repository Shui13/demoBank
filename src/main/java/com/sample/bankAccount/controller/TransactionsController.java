package com.sample.bankAccount.controller;

import com.sample.bankAccount.StringConstants;
import com.sample.bankAccount.model.TransactionRequestDetails;
import com.sample.bankAccount.model.TransactionResposeDetails;
import com.sample.bankAccount.transaction.TransactionServiceI;
import com.sample.bankAccount.transaction.TransactionServiceImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("demoBank")
public class TransactionsController {

    private TransactionServiceI transactionServiceI = new TransactionServiceImpl();

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferMoney(TransactionRequestDetails requestDetails) {
        TransactionResposeDetails transactionResposeDetails = transactionServiceI.transferAmount(requestDetails.getSender(), requestDetails.getReceiver(), requestDetails.getAmount());
        if (transactionResposeDetails.getStatus().equals(StringConstants.SUCCESS)) {
            return Response.status(Response.Status.OK).entity(transactionResposeDetails).type(MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(transactionResposeDetails).type(MediaType.APPLICATION_JSON).build();
    }
}
