package com.srv.transfer.entity;

import com.srv.transfer.exceptions.NotFoundException;

import java.util.Arrays;

public enum TransactionType {
    DEBIT("D"), CREDIT("C");
    private String literalType;

    TransactionType(String literalType) {
        this.literalType = literalType;
    }

    public String getLiteralType() {
        return this.literalType;
    }

    public TransactionType from(String literalType) {
        TransactionType transactionType1 = Arrays.stream(TransactionType.values())
                .filter(transactionType -> transactionType.getLiteralType().equals(literalType))
                .findAny().get();
        return transactionType1;
    }

}
