package com.srv.transfer.utils;

import com.google.common.base.Strings;
import com.srv.transfer.entity.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class InputUtils {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static String takeInput(String message) throws IOException {
        if (!Strings.isNullOrEmpty(message)) {
            System.out.print(message);
        }
        return br.readLine().trim();
    }

    public static Account inputAccountInfo() throws IOException {
        System.out.print("Account Number: ");
        String acNo = br.readLine().trim();
        System.out.print("Initial Amount: ");
        String amountString = br.readLine().trim();
        BigDecimal initBalance = new BigDecimal(amountString);
        return new Account(acNo, initBalance);
    }

}
