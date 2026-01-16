package kr.co.finger.shinhandamoa.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CashReceiptServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptServiceTest.class);

    @Test
    public void testListSort() {
        List<String> testList = Arrays.asList("3", "1", "2");

        testList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        LOGGER.info("{}", testList);

    }
}