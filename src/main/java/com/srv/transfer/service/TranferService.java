package com.srv.transfer.service;

import com.srv.transfer.entity.Account;
import com.srv.transfer.utils.DoubleLockholder;
import com.srv.transfer.utils.Lockholder;
import com.srv.transfer.utils.StripLockHolder;

import java.math.BigDecimal;

public class TranferService {
    private final Lockholder lockholder=new Lockholder();
    private final DoubleLockholder doubleLockholder=new DoubleLockholder();
    private final StripLockHolder stripLockHolder = new StripLockHolder();
    private final AccountService accountService;

    public TranferService(AccountService accountService){
        this.accountService=accountService;
    }

    public boolean transfer(String ac1, String ac2, double amount) throws InterruptedException {
        return forthApproach(ac1, ac2, amount);
    }

    public boolean forthApproach(String ac1, String ac2, double amount) {
        System.out.println(ac1 +" to "+ac2 +" "+ amount);
        String[] ids = {ac1, ac2};
        stripLockHolder.lock(ids);
        boolean success = false;
        try{
            Account from = accountService.getAccount(ac1);
            Account to = accountService.getAccount(ac2);
            BigDecimal transferAmount = BigDecimal.valueOf(amount);
            if(from.withdrawl(transferAmount) && to.deposit(transferAmount)){
                success=true;
            }
        }finally {
            stripLockHolder.unlock(ids);
        }
        return success;
    }

    public boolean thirdApproach(String ac1, String ac2, double amount) throws InterruptedException{
        String[] ids = {ac1, ac2};
        doubleLockholder.lock(ids);
        boolean success = false;
        try{
            System.out.println("Initializing transfer - "+ ac1 +" to "+ac2 + " - "+ amount);
            Account from = accountService.getAccount(ac1);
            Account to = accountService.getAccount(ac2);
            BigDecimal transferAmount = BigDecimal.valueOf(amount);
            if(from.withdrawl(transferAmount) && to.deposit(transferAmount)){
                success=true;
            }
        }finally {
            doubleLockholder.unlock(ids);
        }
        return success;
    }

    private boolean secondApproach(String ac1, String ac2, double amount) throws InterruptedException {
        boolean success = false;
        lockholder.aquireLock(ac1);
        try{
            Account account = accountService.getAccount(ac1);
            success=account.withdrawl(BigDecimal.valueOf(amount));
        }finally {
            lockholder.releaseLock(ac1);
        }

        if(success) {
            lockholder.aquireLock(ac2);
            try {
                Account account = accountService.getAccount(ac2);
                success= account.deposit(BigDecimal.valueOf(amount));
            } finally {
                lockholder.releaseLock(ac2);
            }
        }
        return success;
    }

    private boolean firstApproach(String ac1, String ac2, double amount) throws InterruptedException{
        String[] ids = {ac1, ac2};
        lockholder.lock(ids);
        boolean success = false;
        try{
            Account from = accountService.getAccount(ac1);
            Account to = accountService.getAccount(ac2);
            BigDecimal transferAmount = BigDecimal.valueOf(amount);
            if(from.withdrawl(transferAmount) && to.deposit(transferAmount) ){
                success=true;
            }
        }finally {
            lockholder.unlock(ids);
        }
        return success;
    }

}
