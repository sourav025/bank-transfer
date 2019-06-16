package com.srv.transfer.service;

import com.srv.transfer.entity.Account;
import com.srv.transfer.entity.AccountTransaction;
import com.srv.transfer.entity.Status;
import com.srv.transfer.entity.TransactionEntity;
import com.srv.transfer.exceptions.NotFoundException;
import com.srv.transfer.locks.LockHolder;
import com.srv.transfer.locks.StripLockHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransferService {

    private final List<TransactionEntity> transactionEntityList = new ArrayList<>();

    private final LockHolder stripLockHolder = new StripLockHolder();
    private final AccountService accountService;

    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public TransactionEntity transfer(String fromAccountNo, String toAccountNo, double amount) {
        String[] ids = {fromAccountNo, toAccountNo};
        TransactionEntity transactionEntity = new TransactionEntity(fromAccountNo, toAccountNo, BigDecimal.valueOf(amount));
        stripLockHolder.lock(ids);
        boolean success = false;
        try {
            Account from = accountService.getAccount(fromAccountNo);
            Account to = accountService.getAccount(toAccountNo);
            if (Objects.isNull(from)) throw new NotFoundException(fromAccountNo);
            if (Objects.isNull(to)) throw new NotFoundException(toAccountNo);
            BigDecimal transferAmount = BigDecimal.valueOf(amount);
            String transDescription = getTransactionRef(transactionEntity);
            success = from.withdrawl(transferAmount, transDescription) && to.deposit(transferAmount, transDescription);
        } catch (NotFoundException notFoundException) {
            transactionEntity.setStatusDetails("[ERROR] Account Not Found: " + notFoundException.getMessage());
        } catch (Exception exc) {
            transactionEntity.setStatusDetails("[UNKNOWN_ERROR] " + exc.getMessage());
        } finally {
            stripLockHolder.unlock(ids);
            saveTransaction(transactionEntity, success);
        }
        return transactionEntity;
    }

    public List<TransactionEntity> getTransactions() {
        return this.transactionEntityList;
    }

    public List<AccountTransaction> getTransactions(String accountNo) {
        Account account = accountService.getAccount(accountNo);
        if (account == null) {
            throw new NotFoundException("Account NOT FOUND: " + accountNo);
        }
        return account.getTransactions();
    }

    private String getTransactionRef(TransactionEntity transactionEntity) {
        return String.format("Transaction ref: %s", transactionEntity.getTransactionId());
    }

    private synchronized void saveTransaction(TransactionEntity transactionEntity, boolean isSuccess) {
        transactionEntity.setStatus(isSuccess ? Status.SUCCESS : Status.FAILED);
        transactionEntityList.add(transactionEntity);
    }
}
