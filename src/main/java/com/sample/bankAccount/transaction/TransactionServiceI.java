package com.sample.bankAccount.transaction;

import com.sample.bankAccount.model.TransactionResposeDetails;

//handles transactional requests
public interface TransactionServiceI {

    public TransactionResposeDetails transferAmount(String senderAccount, String receiverAccount, double amount);

}
