package kr.co.finger.damoa.scheduler.task.observer;

import kr.co.finger.damoa.scheduler.service.CancelAPStatusService;
import kr.co.finger.shinhandamoa.adapter.fim.FingerIntegrateMessageAdapter;
import kr.co.finger.shinhandamoa.common.ListResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 입금취소 반영여부 작업
 * <p>
 * 1시간에 한번씩 체크 후 문제있을경우 문자
 *
 * @author wisehouse@finger.co.kr
 * @author denny91@finger.co.kr
 */
@Component
public class CancelAPStatusTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelAPStatusTask.class);

    @Autowired
    private CancelAPStatusService cancelAPStatusService;

    @Autowired
    private FingerIntegrateMessageAdapter fingerIntegrateMessageAdapter;

    @Value("${cancel-ap-status-check-task.destination-id}")
    private String destinationId;
    
    @Scheduled(fixedDelayString = "${cancel-ap-status-check-task.schedule.delay}")
    private void trigger() throws Exception {
        LOGGER.debug("입금취소 상태반영여부확인 작업 시작");
        execute();
        LOGGER.debug("입금취소 상태반영여부확인 작업 종료");
    }

    /**
     * Task - Service - Mapper
     */
    public void execute() throws Exception {
        // TODO : 여기서 서비스 호출
        // 1. 입금취소전문이 있으면서 취소상태가아닌 수납건수 확인
        final List<Map<String,Object>> itemCount = cancelAPStatusService.checkCancelAPStatus();
        
        if(itemCount == null || itemCount.size() == 0) {
            LOGGER.debug("입금취소 이상대상 없음");
            return;
        }

        // 2. 건수가 있을경우
        int count = itemCount.size();
        LOGGER.debug("입금취소 이상대상: {}건", count);
        LOGGER.debug("알림 문자발송 시작");
        sendSmsNotice(destinationId, count);
        LOGGER.debug("알림 문자발송 종료");

        LOGGER.info("입금 취소 이상 {}건에 대해서 알림 문자 발송", count);
    }

    private void sendSmsNotice(String destinationId, int count) throws Exception {
        if(StringUtils.isBlank(destinationId)) {
            return;
        }

        final String format = "입금취소 상태반영 실패건이 발생하였습니다.\n" +
                "%d :건";
        final String body = String.format(format, count);

        final ListResult<FingerIntegrateMessageAdapter.Message> listResult = fingerIntegrateMessageAdapter.createMessage(
                FingerIntegrateMessageAdapter.MessageType.MMS, "신한다모아", body, null, "15449350", destinationId, "신한다모아"); // sms 발송
    }
}
