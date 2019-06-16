package com.srv.transfer.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.srv.transfer.utils.AmountFormatter.format;

public class Account {

    private final List<AccountTransaction> transactions = new ArrayList<>();

    private String accountNo;
    private BigDecimal balance;

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    public Account(String accountNo, BigDecimal amount) {
        this.accountNo = accountNo;
        this.balance = amount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public BigDecimal getBalance() {
        readWriteLock.readLock().lock();
        try {
            return this.balance;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public boolean deposit(BigDecimal amount, String transactionRef) {
        readWriteLock.writeLock().lock();
        try {
            this.balance = this.balance.add(amount);
            saveToTransaction(TransactionType.CREDIT, amount, transactionRef);
            return true;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public synchronized boolean withdrawl(BigDecimal amount, String transactionRef) {
        readWriteLock.writeLock().lock();
        try {
            if (this.balance.compareTo(amount) >= 0) {
                this.balance = this.balance.subtract(amount);
                saveToTransaction(TransactionType.DEBIT, amount, transactionRef);
                return true;
            }
            System.out.println("Insufficient balance " + format(amount) + ">" + format(this.balance) + "!!!");
            return false;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void saveToTransaction(TransactionType type, BigDecimal amount, String transactionRef) {
        AccountTransaction accountTransaction = new AccountTransaction(type, amount, transactionRef);
        transactions.add(accountTransaction);
    }

    public List<AccountTransaction> getTransactions() {
        return this.transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNo='" + accountNo + '\'' +
                ", balance=" + balance +
                '}';
    }
}
