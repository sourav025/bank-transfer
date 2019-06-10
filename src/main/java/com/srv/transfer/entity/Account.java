package com.srv.transfer.entity;

import com.srv.transfer.utils.AmountFormatter;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.srv.transfer.utils.AmountFormatter.format;

public class Account {
    private String accountNo;
    private BigDecimal balance;

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);


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
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    public boolean deposit(BigDecimal amount){
        readWriteLock.writeLock().lock();
        try {
            this.balance = this.balance.add(amount);
            return true;
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public synchronized boolean withdrawl(BigDecimal amount){
        readWriteLock.writeLock().lock();
        try {
            if(this.balance.compareTo(amount)>=0){
                this.balance=this.balance.subtract(amount);
                return true;
            }
            System.out.println("Insufficiend balance, "+ format(amount)+">" +format(this.balance) +" . Transaction failed!!!");
            return false;
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

}
