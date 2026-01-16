package kr.co.finger.damoa.scheduler.task.observer;

import kr.co.finger.shinhandamoa.common.Progress;
import kr.co.finger.shinhandamoa.service.CashReceiptFileDTO;
import kr.co.finger.shinhandamoa.service.CashReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class CashReceiptFileImportTask {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * KIS 현금영수증 서버 주소
     */
    @Value("${kis-cash-receipt.host}")
    private String sftpHost;

    /**
     * KIS 현금영수증 서버 포트
     */
    @Value("${kis-cash-receipt.port}")
    private int sftpPort;

    /**
     * KIS 현금영수증 서버 사용자
     */
    @Value("${kis-cash-receipt.username}")
    private String sftpUser;

    /**
     * KIS 현금영수증 서버 비밀번호
     */
    @Value("${kis-cash-receipt.password}")
    private String sftpPassword;
    
    @Value("${damoa.cash.sftpGetRemoteDir}")
    private String sftpGetRemoteDir;
    
    @Value("${damoa.cash.sftpGetLocalDir}")
    private String sftpGetLocalDir;
    
    // 현금영수증에서 사용하는 기관명.
    @Value("${damoa.cash.corpCd}")
    private String corpCd;
    
    @Autowired
    private CashReceiptService cashReceiptService;


    public CashReceiptFileImportTask() {

    }

    //TODO 2차 개발 1초
    @Scheduled(fixedDelayString = "${cash-receipt-file-import-task.schedule.delay}")
    public void trigger() {
        LOGGER.debug("미처리 현금영수증 결과파일 조회");
        final List<CashReceiptFileDTO> fileList = cashReceiptService.getCashReceiptFileListForImport();
        if(fileList.isEmpty()) {
            LOGGER.debug("미처리 현금영수증 결과파일 없음");
            return;
        }
        
        execute();
    }

    public void execute() {
        LOGGER.debug("현금영수증 결과파일 임포트 작업 시작");
        
        Progress progress;
        try {
            LOGGER.info("미처리 현금영수증 결과파일 조회");
            final List<CashReceiptFileDTO> fileList = cashReceiptService.getCashReceiptFileListForImport();
            if(fileList.isEmpty()) {
                LOGGER.warn("미처리 현금영수증 결과파일 없음");
                return;
            }

            LOGGER.info("미처리 현금영수증 결과파일: {}건", fileList.size());
            
            progress = new Progress(fileList.size());

            for (CashReceiptFileDTO each : fileList) {
                try {
                    cashReceiptService.importCashReceiptFile(each.getId());
                    progress.tick();
                } finally {
                    LOGGER.info("현.금영수증 결과 임포트 작업 - {} - {}%({}/{}) - {}ms left", each.getReceiveFileName(), progress.getProgressRate(), progress.getProgressCount(), progress.getTotalCount(), progress.getTimeLeft());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("현금영수증 결과 임포트 작업 종료");
        }
    }
}
