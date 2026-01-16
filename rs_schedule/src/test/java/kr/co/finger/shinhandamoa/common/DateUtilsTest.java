package kr.co.finger.shinhandamoa.common;

import kr.co.finger.shinhandamoa.service.ClientSettleService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link DateUtils} 테스트
 * 
 * @author wisehouse@finger.co.kr
 */
public class DateUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtilsTest.class);
    @Autowired
    private ClientSettleService clientSettleService;
    
    @Test
    public void trundateHour() {
        Date date = DateUtils.trundateHour(new Date());
        LOGGER.info("date: {}", date);
    }
    
    @Test
    public void parseDate() {
        Date date = DateUtils.parseDate("201808", "yyyyMM");
        LOGGER.info("date: {}", date);
    }

    @Test
    public void execute() {
        clientSettleService.executeDailySettle(new SimpleDateFormat("yyyyMMdd").format(org.apache.commons.lang3.time.DateUtils.addDays(new Date(),-2)));
    }


}
