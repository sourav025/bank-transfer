package com.srv.transfer.entity;

import com.google.common.base.Strings;
import com.srv.transfer.utils.AmountFormatter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TransactionEntity {
    private String transactionId;

    private String fromAccountNo;
    private String toAccountNo;
    private BigDecimal amount;
    private Status status;
    private String statusDetails;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;

    public TransactionEntity(String fromAccountNo, String toAccountNo, BigDecimal amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.fromAccountNo = fromAccountNo;
        this.toAccountNo = toAccountNo;
        this.amount = amount;
        this.status = Status.STARTED;
        createdOn = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getFromAccountNo() {
        return fromAccountNo;
    }

    public String getToAccountNo() {
        return toAccountNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "transactionId='" + transactionId + '\'' +
                ", fromAccountNo='" + fromAccountNo + '\'' +
                ", toAccountNo='" + toAccountNo + '\'' +
                ", amount=" + AmountFormatter.format(amount) +
                ", status=" + status +
                ", statusDetails=" + Strings.nullToEmpty(statusDetails) +
                ", createdOn=" + this.createdOn.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) +
                '}';
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    public void setStatus(boolean status) {
        setStatus(status ? Status.SUCCESS : Status.FAILED);
    }
}
