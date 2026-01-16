package kr.co.finger.damoa.scheduler.task.monthly;

import kr.co.finger.shinhandamoa.service.ClientSettleService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 월 정산 작업
 * 
 * @author wisehouse@finger.co.kr
 */
@Component
public class MonthlySettleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlySettleTask.class);
    
    @Autowired
    private ClientSettleService clientSettleService;

    @Scheduled(cron="${damoa.monthly-settle-task.cron}")
    public void trigger() {
        execute();
    }
    
    public void execute() {
        clientSettleService.executeMonthlySettle(new SimpleDateFormat("yyyyMM").format(DateUtils.addMonths(new Date(),-1 )));
    }
    
}
