package com.sample.bankAccount.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class TransactionResposeDetails {

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private String message;
}
