package com.srv.transfer.service;

import com.srv.transfer.entity.Account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultAccountService implements AccountService {

    private Map<String, Account> map = new HashMap<>();
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public Account createAccount(String accountNo, double initialBalance) {
        Account newAccount = new Account(accountNo, BigDecimal.valueOf(initialBalance));
        readWriteLock.writeLock().lock();
        try {
            Account oldAccount = map.putIfAbsent(accountNo, newAccount);
            if (oldAccount == null) {
                return newAccount;
            }
            return oldAccount;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public int totalAccounts() {
        return map.size();
    }

    @Override
    public Account getAccount(String accountNo) {
        readWriteLock.readLock().lock();
        try {
            return this.map.get(accountNo);
        } finally {
            readWriteLock.readLock().unlock();
        }

    }
}
