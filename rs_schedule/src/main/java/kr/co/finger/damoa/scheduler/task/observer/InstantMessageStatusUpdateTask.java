package kr.co.finger.damoa.scheduler.task.observer;

import kr.co.finger.damoa.scheduler.service.InstantMessageService;
import kr.co.finger.damoa.scheduler.service.model.InstantMessageDTO;
import kr.co.finger.shinhandamoa.adapter.fim.FingerIntegrateMessageAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 문자메시지 상태 업데이트 작업
 * <p>
 * 1초에 한 번씩 실행하자
 *
 * @author wisehouse@finger.co.kr
 * @author jungbna@finger.co.kr
 */
@Component
public class InstantMessageStatusUpdateTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstantMessageStatusUpdateTask.class);

    @Autowired
    private InstantMessageService instantMessageService;

    @Autowired
    private FingerIntegrateMessageAdapter fingerIntegrateMessageAdapter;

    /**
     * TODO 이것도 API gateway 사용 하도록 수정 예정
     * @throws Exception
     */
    @Scheduled(fixedDelayString = "${damoa.task.instant-message-status-update-task.schedule.delay}")
    private void trigger() throws Exception {
        LOGGER.debug("문자메시지 상태 업데이트 작업 시작");
        execute();
        LOGGER.debug("문자메시지 상태 업데이트 작업 종료");
    }

    /**
     * Task - Service - Mapper
     */
    public void execute() throws Exception {
        // TODO : 여기서 서비스 호출
        // 1. 발송 상태가 전송 중인 건 전체 조회
        String messageStatus = "0";
        long totalItemCount = instantMessageService.count(messageStatus);
        
        if(totalItemCount == 0) {
            LOGGER.trace("문자메시지 상태 업데이트 작업 대상 없음");
            return;
        }

        LOGGER.info("문자메시지 상태 업데이트 작업 대상: {}건", totalItemCount);

        // 2. 발송 상태가 전송 중인 건 100건 조회
        List<InstantMessageDTO> itemList = instantMessageService.getList(messageStatus, new RowBounds(0, 100));
        int count = 0;
        int success = 0;
        int fail = 0;
        int error = 0;

        for (InstantMessageDTO each : itemList) {
            // 3. 발송 상태 조회 및 상태 업데이트
            final String instantMessageId = each.getSmsReqCd();
            final String fimStatus = getFIMStatus(instantMessageId);

            count++;
            switch (fimStatus) {
                case StringUtils.EMPTY:
                    each.setStatus("0");
                    success++;
                    break;
                case "0":
                    each.setStatus("2");
                    success++;
                    break;
                case "1":
                    each.setStatus("3");
                    fail++;
                    break;
                case "4":
                    each.setStatus("4");
                    error++;
                    break;
            }

            final String smsReqCd = each.getSmsReqCd();
            final String status = each.getStatus();
            
            instantMessageService.updateStatus(smsReqCd, status);
        }

        // 4. 작업 통계 (작업 대상 건수, 성공/실패/조회실패)
        LOGGER.info("전체 : {}", count);
        LOGGER.info("성공 : {}", success);
        LOGGER.info("실패 : {}", fail);
        LOGGER.info("에러 : {}", error);
    }

    private String getFIMStatus(String messageId) {
        String messageCd = StringUtils.EMPTY;

        try {
            long smsReqCd = NumberUtils.toLong(messageId);
            FingerIntegrateMessageAdapter.Message message = fingerIntegrateMessageAdapter.getResult(smsReqCd);
            LOGGER.trace("문자 메시지 {} : {}", smsReqCd, message);

            if (message.getStatus() == null) {
                LOGGER.debug("문자 메시지 상태가 없음");
                return messageCd;
            }

            messageCd = message.getStatus().getCode();
        } catch (Exception e) {

            LOGGER.info("발송 상태 조회 및 상태 업데이트 없음");
            messageCd = "4";
        }

        return messageCd;
    }
}
