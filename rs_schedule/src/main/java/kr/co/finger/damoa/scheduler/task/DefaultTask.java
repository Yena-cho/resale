package kr.co.finger.damoa.scheduler.task;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.scheduler.service.SimpleService;
import org.springframework.scheduling.support.CronSequenceGenerator;

import javax.annotation.PostConstruct;
import java.util.Date;

public abstract class DefaultTask implements Task {

    private final String cronExpress;
    private CronSequenceGenerator cronTrigger;
    public DefaultTask(String cronExpress) {
        this.cronExpress = cronExpress;
    }

    @PostConstruct
    public void init() {
        cronTrigger = new CronSequenceGenerator(cronExpress);
    }

    /**
     * 현재시간에 수행가능한지 확인함.
     * 여러서버로 구동될 수 있으므로 이런 과정이 필요함.
     * @param simpleService
     * @param startDate 시작시간 14자리
     * @param workId
     * @return
     */
    boolean doExecution(SimpleService simpleService, String startDate, String workId) {
        return simpleService.requestPermission(startDate, workId);
    }
    boolean doExecution(SimpleService simpleService, String workId) {
        String startDate = toString(scheduledFireDate());
        return doExecution(simpleService, startDate, workId);
    }

    /**
     * 스케쥴러 job이 끝이 나면 종료처리함.
     * @param simpleService
     * @param startDate
     * @param workId
     * @param count
     * @param isOk 처리결과 true 성공, false 실패..
     */
    void finishExecution(SimpleService simpleService,String startDate, String workId,int count,boolean isOk) {
        simpleService.finishExecution(startDate, workId, count,isOk);
    }
    void finishExecution(SimpleService simpleService,String startDate, String workId,int count) {
        finishExecution(simpleService, startDate, workId, count, true);
    }

    private String toString(Date date) {
        return DateUtils.to14NowString(date);
    }
    Date scheduledFireDate() {
        if (cronTrigger != null) {
            return cronTrigger.next(nowFireTime(new Date()));
        } else {
            return new Date();
        }
    }

    private Date nowFireTime(Date date) {
        return new Date(date.getTime() - 1000);
    }
}
