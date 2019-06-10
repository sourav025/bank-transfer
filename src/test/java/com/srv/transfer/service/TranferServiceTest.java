package com.srv.transfer.service;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.srv.transfer.utils.AmountFormatter.format;
import static com.srv.transfer.utils.AmountFormatter.getDisplayAmount;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TranferServiceTest {
    private AccountService accountService;
    private TranferService tranferService;

    private final double INITIAL_BALANCE = 1000.0;

    @Before
    public void setUp() {
        accountService = new AccountService();
        tranferService = new TranferService(accountService);
        create101Accounts();
    }

    @Test
    public void should_complete_one_transaction() throws InterruptedException {
        assertTrue(tranferService.transfer("ac1", "ac2", 10));
        assertEquals(BigDecimal.valueOf(990.0), accountService.getAccount("ac1").getBalance());
        assertEquals(BigDecimal.valueOf(1010.0), accountService.getAccount("ac2").getBalance());
    }

    @Test
    public void should_complete_two_transactions() throws InterruptedException {
        assertTrue(tranferService.transfer("ac1", "ac2", 10));
        assertEquals(BigDecimal.valueOf(990.0), accountService.getAccount("ac1").getBalance());
        assertEquals(BigDecimal.valueOf(1010.0), accountService.getAccount("ac2").getBalance());

        assertTrue(tranferService.transfer("ac2", "ac1", 10));
        assertEquals(BigDecimal.valueOf(1000.0), accountService.getAccount("ac1").getBalance());
        assertEquals(BigDecimal.valueOf(1000.0), accountService.getAccount("ac2").getBalance());
    }

    @Test
    public void should_complete_parallel_transaction() throws InterruptedException {
        int noOfTrn = 1800;
        Thread[] threads = new Thread[noOfTrn];
        for (int i = 0; i < noOfTrn; i++) {
            String fromAc = "ac" + (1 + ((int) (Math.random() * noOfTrn)));
            String to = "ac" + (1 + (int) (Math.random() * noOfTrn));
            threads[i] = new Thread((() -> {
                try {
                    BigDecimal amount = getDisplayAmount(BigDecimal.valueOf(Math.random()*500));
                    tranferService.transfer(fromAc, to, Double.parseDouble(amount.toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
        for (int i = 0; i < noOfTrn; i++) threads[i].start();
        for (int i = 0; i < noOfTrn; i++) threads[i].join();

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 1; i <= noOfTrn; i++) {
            BigDecimal cur = accountService.getAccount("ac" + i).getBalance();
            sum = sum.add(cur);
        }
        assertEquals(format(BigDecimal.valueOf(noOfTrn * 1000)), format(sum));
    }

    private void create101Accounts() {
        for (int i = 1; i < 2000; i++) accountService.createAccount("ac" + i, INITIAL_BALANCE);
    }

}