package com.sample.bankAccount.transaction;

import com.sample.bankAccount.StringConstants;
import com.sample.bankAccount.model.AccountDetails;
import com.sample.bankAccount.accounts.AccountServiceI;
import com.sample.bankAccount.accounts.AccountServiceImpl;
import com.sample.bankAccount.model.TransactionResposeDetails;

public class TransactionServiceImpl implements TransactionServiceI {

    AccountServiceI accountService = new AccountServiceImpl();

    @Override
    public TransactionResposeDetails transferAmount(String senderAccount, String receiverAccount, double amount) {
        if (amount <= 0) {
            return new TransactionResposeDetails(StringConstants.FAILED, "Invalid amount");
        }

        AccountDetails sender = accountService.getAccountDetails(senderAccount);
        if (null == sender) {
            return new TransactionResposeDetails(StringConstants.FAILED, "Invalid sender");
        }

        AccountDetails receiver = accountService.getAccountDetails(receiverAccount);
        if (null == receiver) {
            return new TransactionResposeDetails(StringConstants.FAILED, "Invalid receiver");
        }

        if (sender.getBalance() < amount) {
            return new TransactionResposeDetails(StringConstants.FAILED, "insufficient funds");
        }

        if (accountService.deductAmount(sender, amount)) {
            if (accountService.addAmount(receiver, amount)) {
                return new TransactionResposeDetails(StringConstants.SUCCESS, "Transaction Successful");
            } else {
                //in case receiver is not accessible, add the deducted amount back to sender
                accountService.addAmount(sender, amount);
                return new TransactionResposeDetails(StringConstants.FAILED, "Transaction failed - amount could not be added to receiver");
            }
        } else {
            return new TransactionResposeDetails(StringConstants.FAILED, "Transaction failed - amount could not be deducted from sender");
        }

    }

}
