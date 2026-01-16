package kr.co.finger.damoa.scheduler.task.daily;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.shinhandamoa.service.CashReceiptFileDTO;
import kr.co.finger.shinhandamoa.service.CashReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 현금영수증 수신.
 * 1. sftp로 파일 수신.
 * 2. 파일을 읽어 전문으로 변경
 * 3. cashLayoutFactory 로 자비객체로 변경
 * 4. XCASHMAS 테이블 업데이트 처리.
 * 5. CASHREQ 테이블에 이력 업데이트 수행.
 */
@Component
public class CashReceiptFileReceiveTask {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private CashReceiptService cashReceiptService;

    public CashReceiptFileReceiveTask() {

    }

    //TODO 2차 개발
    @Scheduled(cron = "${cash-receipt-file-receive-task.schedule.cron}")
    public void trigger() {
        execute();
    }

    public void execute() {
        final Date now = new Date();
        final String today = DateUtils.format(now, "yyyyMMddHHmmss");
        LOGGER.info("EXECUTE " + today);

        try {
            LOGGER.info("미수신 현금영수증 결과파일 조회");

            final List<CashReceiptFileDTO> fileList = cashReceiptService.getCashReceiptFileListForReceive();
            if (fileList == null || fileList.isEmpty()) {
                LOGGER.info("미수신 현금영수증 결과파일 없음");
                return;
            }
            LOGGER.info("미수신 현금영수증 결과파일: {}건", fileList.size());

            // 파일 수신
            for (CashReceiptFileDTO each : fileList) {
                try {
                    cashReceiptService.receiveCashReceiptResponseFile(each.getId());
                } catch (Exception e) {
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.error(e.getMessage(), e);
                    } else {
                        LOGGER.error(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
        }

    }
}
