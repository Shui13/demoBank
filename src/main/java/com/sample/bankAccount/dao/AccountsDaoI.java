package com.sample.bankAccount.dao;

import com.sample.bankAccount.model.AccountDetails;

//account storage layer
public interface AccountsDaoI {

    public AccountDetails put(String id, AccountDetails accountDetails);

    public AccountDetails get(String id);

    public boolean exists(String id);

}
