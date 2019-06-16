package com.srv.transfer.entity;

import com.srv.transfer.utils.AmountFormatter;

import java.math.BigDecimal;

public class AccountTransaction {
    TransactionType transactionType;
    private BigDecimal amount;
    private String description;

    public AccountTransaction(TransactionType transactionType, BigDecimal amount, String description) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "AccountTransaction{" +
                "transactionType=" + transactionType.name() +
                ", amount=" + AmountFormatter.format(amount) +
                ", description='" + description + '\'' +
                '}';
    }
}
