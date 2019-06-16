package com.srv.transfer.service;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class DefaultAccountServiceTest {

    DefaultAccountService accountService = new DefaultAccountService();

    @Test
    public void accountServiceTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 1; i < 10001; i++) {
            String accountNo = "ac-" + i;
            Runnable task = () -> accountService.createAccount(accountNo, 1000.0);
            executorService.submit(task);
            executorService.submit(task);
        }
        while (!executorService.isTerminated()) executorService.shutdown();
        assertEquals(10000, accountService.totalAccounts());
    }

}