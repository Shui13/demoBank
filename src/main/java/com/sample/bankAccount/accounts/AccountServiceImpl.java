package com.sample.bankAccount.accounts;

import com.sample.bankAccount.dao.AccountsDaoI;
import com.sample.bankAccount.dao.AccountsDaoMapImpl;
import com.sample.bankAccount.model.AccountDetails;

public class AccountServiceImpl implements AccountServiceI {

    private AccountsDaoI accountsDaoI = new AccountsDaoMapImpl();

    @Override
    public boolean createAccount(String accountId, double amount) {
        //check if the account already exists
        if (null == accountId || accountsDaoI.exists(accountId.toLowerCase())) {
            return false;
        }
        AccountDetails accountDetails = new AccountDetails(accountId.toLowerCase(), amount);
        accountsDaoI.put(accountId.toLowerCase(), accountDetails);
        return true;
    }

    @Override
    public AccountDetails getAccountDetails(String accountId) {
        //account id case insensitive
        if (null == accountId || !accountsDaoI.exists(accountId.toLowerCase())) {
            return null;
        }
        return accountsDaoI.get(accountId.toLowerCase());
    }

    @Override
    public boolean addAmount(AccountDetails account, double amount) {
        //validating target account and amount
        if (null == account || amount <= 0) {
            return false;
        }
        //addition of amount should be a synchronous operation - thread safe block
        synchronized (account) {
            account.setBalance(account.getBalance() + amount);
        }
        return true;
    }

    @Override
    public boolean deductAmount(AccountDetails account, double amount) {
        //validating target account and amount
        if (null == account || amount <= 0) {
            return false;
        }
        //deduction of amount should be a synchronous operation - thread safe block
        synchronized (account) {
            if (account.getBalance() > amount) {
                account.setBalance(account.getBalance() - amount);
                return true;
            } else {
                return false;
            }
        }
    }
}
