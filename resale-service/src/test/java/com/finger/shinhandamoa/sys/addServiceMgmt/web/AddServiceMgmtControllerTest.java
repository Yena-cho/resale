package com.finger.shinhandamoa.sys.addServiceMgmt.web;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * {@link AddServiceMgmtController} 테스트
 * 
 * @author wisehouse@finger.co.kr
 */
public class AddServiceMgmtControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddServiceMgmtControllerTest.class);

    
    @Test
    public void test() {
        String text = new AddServiceMgmtController().substringWithByteLength("고1-D유재복(대지,보정,상현,성복)", 30, Charset.forName("EUC-KR"));
        LOGGER.info("{}: {}", text, text.getBytes(Charset.forName("EUC-KR")).length);
    }

}
