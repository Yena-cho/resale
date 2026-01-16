package com.finger.shinhandamoa.org.receiptmgmt.service;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptMgmtServiceImplTest {
    @Test
    public void test() throws ParseException {
        Date date = new SimpleDateFormat("yyyyMMddhhmm").parse("201810312158");
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
    }
    
    @Test
    public void test2() { 
        String[] a = org.springframework.util.StringUtils.commaDelimitedListToStringArray("PA02,PA04,PA03,PA05");
        for (String each : a) {
            System.out.println(each);
        }
    }
}
