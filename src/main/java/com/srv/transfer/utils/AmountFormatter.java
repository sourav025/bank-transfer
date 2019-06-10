package com.srv.transfer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class AmountFormatter {

    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

    public static String format(BigDecimal amount){
        BigDecimal displayAmount = getDisplayAmount(amount);
        NumberFormat usdCostFormat = NumberFormat.getCurrencyInstance(Locale.UK);
        usdCostFormat.setMinimumFractionDigits( 2 );
        usdCostFormat.setMaximumFractionDigits( 2 );
        return usdCostFormat.format(displayAmount.doubleValue());
    }

    public static BigDecimal getDisplayAmount(BigDecimal amount){
        return amount.setScale(2, DEFAULT_ROUNDING);
    }

}
