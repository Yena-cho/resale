package kr.co.finger.damoa.scheduler.task;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.scheduler.service.SimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimpleTask extends DefaultTask {
    public static final String CRON_EXPRESSION = "0 0 0 * * ?";
    private String workId = "simple_per_ten";

    @Autowired
    private SimpleService simpleService;
    private boolean isFirst;
    private boolean isFinished;

    private Logger LOG = LoggerFactory.getLogger(getClass());
    public SimpleTask() {
        super(CRON_EXPRESSION);
    }

//    @Scheduled(cron = CRON_EXPRESSION)
    public void execute() {
        String startDate = DateUtils.to14NowString(scheduledFireDate());

        if (doExecution(simpleService,startDate, workId)) {
            try{
                LOG.info("EXECUTE " + startDate+ " "+workId);
            }finally {
                finishExecution(simpleService,startDate,workId,0);
            }
        } else {
            LOG.info("SKIP "+startDate+ " "+workId);
        }
    }

}
