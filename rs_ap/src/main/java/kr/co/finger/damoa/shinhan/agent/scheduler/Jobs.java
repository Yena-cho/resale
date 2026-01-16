package kr.co.finger.damoa.shinhan.agent.scheduler;

import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.fsm.Context;
import kr.co.finger.damoa.shinhan.agent.handler.command.NoticeMsgCommand;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Jobs {

    @Autowired
    private NettyProducer nettyProducer;
    @Autowired
    private MessageFactory messageFactory;
    private int index = 1;
    @Autowired
    private DamoaDao damoaDao;

    @Autowired
    private Context context;

    @Autowired
    private DamoaContext damoaContext;

    @Autowired
    private NoticeMsgCommand noticeMsgCommand;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    public void invokeLookupMsg() {
        try {
            long dealSeq = damoaDao.findDealSeq();
            LOG.info("dealSeq=" + dealSeq);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

//    /**
//     * 매일 0시 10분 0초에 DealSeq 초기화 실행.
//     * 전문에서 전문일련번호에 사용함.
//     */
////    @Scheduled(cron = "0 30 0 0/5 * ?")
//    public void resetSequence() {
//        try {
//            damoaDao.resetDealSeq();
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//        }
//    }

    public void changeFinishState() {
        context.changeState("FINISH");
    }

//    @Scheduled(cron = "0 * * * * ?")
    public void removeCancelHistory() {
        // 주기적으로 입금취소 전문에 대한 히스토리를 삭제해 주어야 한다.
//        LOG.info("NOTICE "+noticeMsgCommand);
//        damoaContext.makeupCancelHistory(1000 * 60 * 1);
    }

    @Scheduled(cron = "0 10 0 * * ?")
    public void removeSeqNoHistory() {
        // 주기적으로 일련번호에 대한 히스토리를 삭제해 주어야 한다. (10분이 지난 데이터)...
        damoaContext.cleanupCancelSeq();
    }


    private String toString(Date date) {
        return toString(date, "yyyyMMddHHmmss");
    }

    private String toString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
}
