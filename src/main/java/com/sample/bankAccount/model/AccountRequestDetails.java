package com.sample.bankAccount.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
public class AccountRequestDetails {

    @Getter
    @Setter
    @NotNull
    private String accountId;

    @Getter
    @Setter
    private volatile double amount;
}
