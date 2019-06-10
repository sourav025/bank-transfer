package com.srv.transfer.service;

import com.srv.transfer.entity.Account;
import com.srv.transfer.utils.StripLockHolder;

import java.math.BigDecimal;

public class TranferService {
    private final StripLockHolder stripLockHolder = new StripLockHolder();
    private final AccountService accountService;

    public TranferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean transfer(String ac1, String ac2, double amount) throws InterruptedException {
        return forthApproach(ac1, ac2, amount);
    }

    public boolean forthApproach(String ac1, String ac2, double amount) {
        String[] ids = {ac1, ac2};
        stripLockHolder.lock(ids);
        boolean success = false;
        try {
            Account from = accountService.getAccount(ac1);
            Account to = accountService.getAccount(ac2);
            BigDecimal transferAmount = BigDecimal.valueOf(amount);
            if (from.withdrawl(transferAmount) && to.deposit(transferAmount)) {
                success = true;
            }
        } finally {
            stripLockHolder.unlock(ids);
        }
        return success;
    }
}
