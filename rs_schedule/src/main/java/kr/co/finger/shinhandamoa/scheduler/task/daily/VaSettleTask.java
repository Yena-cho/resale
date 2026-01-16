package kr.co.finger.shinhandamoa.scheduler.task.daily;

import kr.co.finger.shinhandamoa.service.ClientSettleService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 일 정산 작업
 * 
 * @author wisehouse@finger.co.kr
 */
@Component
public class VaSettleTask {
    @Autowired
    private ClientSettleService clientSettleService;

    @Scheduled(cron="${damoa.daily-va-settle-task.cron}")
    public void trigger() {
        execute();
    }

    public void execute() {
        clientSettleService.executeVaSettle(new SimpleDateFormat("yyyyMMdd").format(DateUtils.addDays(new Date(),-1)));
    }
}
