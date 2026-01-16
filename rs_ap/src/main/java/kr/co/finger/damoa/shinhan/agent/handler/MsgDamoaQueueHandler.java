package kr.co.finger.damoa.shinhan.agent.handler;

import kr.co.finger.damoa.commons.concurrent.AggregateQueue;
import kr.co.finger.damoa.shinhan.agent.model.CancelMessageBean;
import kr.co.finger.damoa.shinhan.agent.model.MessageBean;
import kr.co.finger.damoa.shinhan.agent.model.NoticeMessageBean;
import kr.co.finger.damoa.shinhan.agent.service.DamoaService;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * 통지 전문을 받아 비즈니스 로직을 처리한다.
 * 수납테이블 update XRCPMAS, XRCPDET
 * 전문송수신 테이블 insert TRANS_DATA_ACCT
 * 
 * @author lloydkwrn@gmail.com
 * @author wisehouse@finger.co.kr
 */
@Component
public class MsgDamoaQueueHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgDamoaQueueHandler.class);

    @Autowired
    private AggregateQueue<MessageBean> aggregateQueue;

    @Autowired
    private DamoaService damoaService;

    private volatile boolean doStop;

    @PostConstruct
    public void start() {
        new Thread(this).start();
    }

    @PreDestroy
    public void stop() {
        doStop = true;
    }

    @Override
    public void run() {
        LOGGER.info("MsgDamoaQueueHandler run");
        while (doStop == false) {
            try {
                List<MessageBean> messageList = aggregateQueue.take();

                if(ListUtils.emptyIfNull(messageList).isEmpty()) {
                    continue;
                }

                LOGGER.info("TAKE]" + messageList.size());
                handleMessage(messageList);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        }
    }

    private void handleMessage(List<MessageBean> messageList) {
        for (MessageBean each : messageList) {
            if(each instanceof NoticeMessageBean) {
                damoaService.handleDeposit((NoticeMessageBean) each);
                continue;
            }

            if(each instanceof CancelMessageBean) {
                damoaService.handleCancel((CancelMessageBean) each);
                continue;
            }

            LOGGER.warn("분류되지 않은 메시지: {}", each.getClass());
        }
    }
}
