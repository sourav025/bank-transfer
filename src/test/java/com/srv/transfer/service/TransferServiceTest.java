package com.srv.transfer.service;

import com.srv.transfer.entity.Status;
import com.srv.transfer.entity.TransactionEntity;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.srv.transfer.utils.AmountFormatter.format;
import static com.srv.transfer.utils.AmountFormatter.getDisplayAmount;
import static junit.framework.TestCase.assertEquals;

public class TransferServiceTest {
    private AccountService accountService;
    private final int NO_OF_ACCOUNTS = 2000;
    private final int TEST_TRANSACTIONS = 2000;
    private final double INITIAL_BALANCE = 1000.0;
    private TransferService tranferService;

    @Before
    public void setUp() {
        accountService = new DefaultAccountService();
        tranferService = new TransferService(accountService);
        createAccounts();
    }

    @Test
    public void should_complete_one_transaction() {
        TransactionEntity transfer = tranferService.transfer("ac1", "ac2", 10);
        assertEquals(transfer.getStatus(), Status.SUCCESS);
        assertEquals(BigDecimal.valueOf(990.0), accountService.getAccount("ac1").getBalance());
        assertEquals(BigDecimal.valueOf(1010.0), accountService.getAccount("ac2").getBalance());
    }

    @Test
    public void should_complete_two_transactions() {
        TransactionEntity transfer1 = tranferService.transfer("ac1", "ac2", 10);
        assertEquals(Status.SUCCESS, transfer1.getStatus());
        assertEquals(BigDecimal.valueOf(990.0), accountService.getAccount("ac1").getBalance());
        assertEquals(BigDecimal.valueOf(1010.0), accountService.getAccount("ac2").getBalance());

        TransactionEntity transfer2 = tranferService.transfer("ac2", "ac1", 10);
        assertEquals(Status.SUCCESS, transfer2.getStatus());
        assertEquals(BigDecimal.valueOf(1000.0), accountService.getAccount("ac1").getBalance());
        assertEquals(BigDecimal.valueOf(1000.0), accountService.getAccount("ac2").getBalance());
    }

    @Test
    public void should_complete_parallel_transaction() throws InterruptedException {
        Thread[] threads = new Thread[TEST_TRANSACTIONS];
        for (int i = 0; i < TEST_TRANSACTIONS; i++) {
            String fromAc = "ac" + (1 + ((int) (Math.random() * TEST_TRANSACTIONS)));
            String to = "ac" + (1 + (int) (Math.random() * TEST_TRANSACTIONS));
            threads[i] = new Thread((() -> {
                BigDecimal amount = getDisplayAmount(BigDecimal.valueOf(Math.random() * 500));
                tranferService.transfer(fromAc, to, Double.parseDouble(amount.toString()));
            }));
        }
        for (int i = 0; i < TEST_TRANSACTIONS; i++) threads[i].start();
        for (int i = 0; i < NO_OF_ACCOUNTS; i++) threads[i].join();

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 1; i <= NO_OF_ACCOUNTS; i++) {
            BigDecimal cur = accountService.getAccount("ac" + i).getBalance();
            sum = sum.add(cur);
        }
        assertEquals(format(BigDecimal.valueOf(NO_OF_ACCOUNTS * 1000)), format(sum));
    }

    private void createAccounts() {
        for (int i = 1; i <= NO_OF_ACCOUNTS; i++) accountService.createAccount("ac" + i, INITIAL_BALANCE);
    }

}