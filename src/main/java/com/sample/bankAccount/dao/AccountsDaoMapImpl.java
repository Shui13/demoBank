package com.sample.bankAccount.dao;

import com.sample.bankAccount.model.AccountDetails;

import java.util.HashMap;
import java.util.Map;

//in memory storage using hashmaps
public class AccountsDaoMapImpl implements AccountsDaoI {

    //static to facilitate single instance of the storage object
    private static Map<String, AccountDetails> accounts = new HashMap<String, AccountDetails>();

    @Override
    public AccountDetails put(String id, AccountDetails accountDetails) {
        return accounts.put(id, accountDetails);
    }

    @Override
    public AccountDetails get(String id) {
        return accounts.get(id);
    }

    @Override
    public boolean exists(String id) {
        return accounts.containsKey(id);
    }
}
