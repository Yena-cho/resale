package com.finger.shinhandamoa.org.custMgmt.web;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustExcelControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustExcelControllerTest.class);

    @Test
    public void checkPattern_1() {
        boolean result = CustExcelController.checkPattern("phone", "016-3011-8462");
        LOGGER.info("{}", result);
                
    }

    @Test
    public void checkPattern_2() {
        boolean result = CustExcelController.checkPattern("phone2", "01630118462");
        LOGGER.info("{}", result);
    }
}
