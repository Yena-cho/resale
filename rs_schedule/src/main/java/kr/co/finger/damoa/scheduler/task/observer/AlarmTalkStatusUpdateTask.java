package kr.co.finger.damoa.scheduler.task.observer;

import kr.co.finger.damoa.scheduler.service.AlarmTalkService;
import kr.co.finger.damoa.scheduler.service.model.AlarmTalkDTO;
import kr.co.finger.shinhandamoa.adapter.fim.FingerAlarmTalkAdapter;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 알림톡 상태 업데이트 작업
 * <p>
 * 60초에 한번
 *
 * @author mljeong@finger.co.kr
 */
@Component
public class AlarmTalkStatusUpdateTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmTalkStatusUpdateTask.class);

    @Autowired
    private AlarmTalkService alarmTalkService;

    @Autowired
    private FingerAlarmTalkAdapter fingerAlarmTalkAdapter;

    /**
     * TODO API gateway 사용하도록 수정 예정
     * @throws Exception
     */
//    @Scheduled(fixedDelayString = "${damoa.task.alarm-talk-status-update-task.schedule.delay}")
    private void trigger() throws Exception {
        LOGGER.debug("알림톡 상태 업데이트 작업 시작");
        execute();
        LOGGER.debug("알림톡 상태 업데이트 작업 종료");
    }

    /**
     * Task - Service - Mapper
     */
    public void execute() throws Exception {
        // TODO : 여기서 서비스 호출
        // 1. 발송 상태가 전송 중인 건 전체 조회 ( ATREQ.sendStatusCd | 발송중 0, 발송완료1, 발송실패2)
        String sendStatusCd = "0";
        long totalItemCount = alarmTalkService.count(sendStatusCd);

        if (totalItemCount == 0) {
            LOGGER.trace("알림톡 상태 업데이트 작업 대상 없음");
            return;
        }

        LOGGER.info("알림톡 상태 업데이트 작업 대상: {}건", totalItemCount);

        // 2. 발송 상태가 전송 중인 건 100건 조회
        List<AlarmTalkDTO> itemList = alarmTalkService.getList(sendStatusCd, new RowBounds(0, 100));
        int count = 0;
        int success = 0;
        int wait = 0;
        int fail = 0;
        int error = 0;

        for (AlarmTalkDTO each : itemList) {
            LOGGER.info("알림톡 상태 조회 CMID : {}", each.getCmid());

            // 3. 발송 상태 조회 (cmid 이용) 및 상태 업데이트
            final String cmid = each.getCmid();
            FingerAlarmTalkAdapter.Message message = getAtSendStatus(cmid);    // cmid를 이용하여 알림톡 발송 결과 확인

            // atStatus | 성공 : 7000
            String atStatus = message.getResultObject().getCallStatus() == null ? "" : message.getResultObject().getCallStatus();

            count++;
            switch (atStatus) {
                case "7000":
                    each.setSendStatusCd("1");
                    success++;
                    break;

                case "0":
                    each.setSendStatusCd("0");
                    wait++;
                    break;

                case "9999":
                    each.setSendStatusCd("2");
                    error++;
                    break;

                default :
                    each.setSendStatusCd("2");
                    fail++;
                    break;
            }

            LOGGER.info("알림톡 상태 업데이트 시작 발송 상태 확인 : {}", atStatus);
            alarmTalkService.updateStatus(cmid, each.getSendStatusCd(), atStatus);
        }

        // 4. 작업 통계 (작업 대상 건수, 성공/실패/조회실패)
        LOGGER.info("전체 : {}", count);
        LOGGER.info("성공 : {}", success);
        LOGGER.info("대기 : {}", wait);
        LOGGER.info("실패 : {}", fail);
        LOGGER.info("에러 : {}", error);
    }

    private FingerAlarmTalkAdapter.Message getAtSendStatus(String cmid) {
        FingerAlarmTalkAdapter.Message message = new FingerAlarmTalkAdapter.Message();

        try {
            message = fingerAlarmTalkAdapter.getResult(cmid);  // 알림톡 발송 조회
            String reportTime = message.getResultObject().getReportTime();

            LOGGER.trace("알림톡 {} : {}", cmid, message.getResultObject());

            if (reportTime == null) {
                LOGGER.debug("알림톡 상태가 없음 / reportTime is NULL");
                message.getResultObject().setCallStatus("0");
                return message;
            }

        } catch (Exception e) {
            LOGGER.info("알림톡 발송 상태 조회 및 상태 업데이트 없음");
            message = new FingerAlarmTalkAdapter.Message();
            message.getResultObject().setCallStatus("9999");
        }

        return message;
    }
}
