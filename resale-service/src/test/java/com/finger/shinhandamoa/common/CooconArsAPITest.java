package com.finger.shinhandamoa.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CooconArsAPITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CooconArsAPITest.class);

    @Test
    public void test() {
        final String accessToken = "xk4JUVLHp1MaXhSvyrYr";
        final String corpCode = "20066";
        final String file = "http://dev.coocon.co.kr/sol/gateway/ars_wapi.jsp";

        CooconArsAPI.Output output = new CooconArsAPI(corpCode, accessToken, file).createArs("2300", "01099837415", "11", "신한다모아 테스트입니다", true, String.format("20180917%d.mp3", System.currentTimeMillis()));
        LOGGER.info("{}: {}", output.getRSLT_CD(), output.getRSLT_MSG());    
        LOGGER.debug("RESP_DATA: {}", output.getRESP_DATA());    
    }

}
