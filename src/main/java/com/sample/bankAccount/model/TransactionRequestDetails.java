package com.sample.bankAccount.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class TransactionRequestDetails {

    @Getter
    @Setter
    @NotNull
    private String sender;

    @Getter
    @Setter
    @NotNull
    private String receiver;

    @Getter
    @Setter
    @NotNull
    private double amount;

    public TransactionRequestDetails() {

    }

}
