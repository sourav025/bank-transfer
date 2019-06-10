package com.srv.transfer.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AmountFormatterTest {
    @Test
    public void should_provide_display_amount(){
        BigDecimal bigDecimal = new BigDecimal(45.56666);
        assertEquals(BigDecimal.valueOf(45.57),AmountFormatter.getDisplayAmount(bigDecimal));
    }

    @Test
    public void should_provide_display_amount2(){
        BigDecimal bigDecimal = new BigDecimal(45.509);
        assertEquals(BigDecimal.valueOf(45.51),AmountFormatter.getDisplayAmount(bigDecimal));
    }

    @Test
    public void should_provide_display_amount3(){
        BigDecimal bigDecimal = new BigDecimal(45);
        assertEquals("45.00",AmountFormatter.getDisplayAmount(bigDecimal).toString());
    }

    @Test
    public void should_format_with_currency(){
        BigDecimal bigDecimal = new BigDecimal(45.509);
        assertEquals("£45.51",AmountFormatter.format(bigDecimal));
    }

    @Test
    public void should_format_with_currency2(){
        BigDecimal bigDecimal = new BigDecimal(45.60);
        assertEquals("£45.60",AmountFormatter.format(bigDecimal));
    }

    @Test
    public void should_for_with_currentcy3(){
        BigDecimal bigDecimal = new BigDecimal(45);
        assertEquals("£45.00",AmountFormatter.format(bigDecimal));
    }

}