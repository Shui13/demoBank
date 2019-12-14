package com.sample.bankAccount.accounts;

import com.sample.bankAccount.model.AccountDetails;

//list of operations that can be performed on accounts
public interface AccountServiceI {

    public boolean createAccount(String accountId, double amount);

    public AccountDetails getAccountDetails(String accountId);

    public boolean addAmount(AccountDetails account, double amount);

    public boolean deductAmount(AccountDetails account, double amount);

}
