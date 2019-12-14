package com.sample.bankAccount.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class AccountDetails {

    @Getter
    @Setter
    //account id is String because Iban is alpha numeric
    private String accountId;

    @Getter
    @Setter
    private volatile double balance;

}
