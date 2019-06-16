package com.srv.transfer.utils;

import com.srv.transfer.entity.Account;
import com.srv.transfer.entity.AccountTransaction;
import com.srv.transfer.entity.TransactionEntity;
import com.srv.transfer.service.AccountService;
import com.srv.transfer.service.TransferService;

import java.io.IOException;
import java.util.List;

import static com.srv.transfer.utils.InputUtils.takeInput;

public class CommandExecutionHelper {

    private AccountService accountService;
    private TransferService transferService;

    public CommandExecutionHelper(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    public void beginAccountCreation() throws IOException {
        String accountNo = takeInput("Account no : ");
        Double initAmount = Double.parseDouble(takeInput("Initial Ammount : "));
        Account newAccount = accountService.createAccount(accountNo, initAmount);
        System.out.println("Account created : " + newAccount);
    }

    public void beginTransaction() throws IOException {
        String fromAccount = takeInput("From Account Number: ");
        String toAccount = takeInput("To Account Number: ");
        double bigDecimal = Double.parseDouble(takeInput("Enter Amount: "));
        TransactionEntity transactionEntity = transferService.transfer(fromAccount, toAccount, bigDecimal);
        System.out.println(transactionEntity);
    }

    public void listTransactions() {
        System.out.println("\nAll transactions : ");
        List<TransactionEntity> transactions = transferService.getTransactions();
        for (TransactionEntity te : transactions) {
            System.out.println("\t" + te);
        }
        System.out.println(transactions.isEmpty() ? "<No transactions available>" : "");

    }

    public void listAccountTransactions() throws IOException {
        String acNo = takeInput("Enter Account no : ");
        System.out.println("\nAll transactions for account no( " + acNo + " ): ");
        List<AccountTransaction> transactions = transferService.getTransactions(acNo);
        for (AccountTransaction at : transactions) {
            System.out.println("\t" + at);
        }
        System.out.println(transactions.isEmpty() ? "<No available transactions>" : "");
    }
}
