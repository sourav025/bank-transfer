package com.srv.transfer.service;

import com.srv.transfer.entity.Account;

public interface AccountService {
    Account createAccount(String accountNo, double initialBalance);

    int totalAccounts();

    Account getAccount(String accountNo);
}
