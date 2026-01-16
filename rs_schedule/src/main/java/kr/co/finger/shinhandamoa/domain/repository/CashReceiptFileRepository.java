package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.io.SftpClient;
import kr.co.finger.shinhandamoa.data.table.mapper.CashFileTranHistMapper;
import kr.co.finger.shinhandamoa.data.table.model.CashFileTranHist;
import kr.co.finger.shinhandamoa.data.table.model.CashFileTranHistExample;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 현금영수증 파일 저장소
 *
 * @author wisehouse@finger.co.kr
 */
@Repository
public class CashReceiptFileRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptFileRepository.class);

    @Autowired
    private CashFileTranHistMapper cashFileTranHistMapper;

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

    /**
     * 파일 다운로드
     *
     * @param cashReceiptFileId
     */
    public void receiveResponseFile(final String cashReceiptFileId) throws Exception {
        final CashFileTranHist vo = cashFileTranHistMapper.selectByPrimaryKey(cashReceiptFileId);

        final String transferFileName = vo.getCashFileName();
        final String receiveFileName = StringUtils.replace(transferFileName, "S_FING_RS", "R_FING_RS");

        final SftpClient sftpClient = new SftpClient(sftpHost, sftpPort, sftpUser, sftpPassword);
        try {
            // 파일 다운로드
            sftpClient.connect();
            sftpClient.openChannel();

            final List<String> fileNameList = sftpClient.listFileNames(sftpGetRemoteDir + "/" + receiveFileName);
            if(fileNameList.isEmpty()) {
                LOGGER.warn("파일이 없습니다");
                return;
            }
            
            sftpClient.get(sftpGetRemoteDir + "/" + receiveFileName, sftpGetLocalDir);
            
            // 수신 정보 업데이트
            CashFileTranHist record = new CashFileTranHist();
            record.setRecDt(DateUtils.format(new Date(), "yyyyMMddHHmmss"));

            CashFileTranHistExample example = new CashFileTranHistExample();
            example.createCriteria().andCashFileNameEqualTo(transferFileName);
            
            cashFileTranHistMapper.updateByExampleSelective(record, example);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
            
            throw new Exception("파일 수신에 실패했습니다.", e);
        } finally {
            if (sftpClient != null) {
                sftpClient.disconnect();
            }
        }
    }

    public InputStream loadReceiveFile(String cashReceiptFileId) throws Exception {
        final CashFileTranHist vo = cashFileTranHistMapper.selectByPrimaryKey(cashReceiptFileId);

        final String transferFileName = vo.getCashFileName();
        final String receiveFileName = StringUtils.replace(transferFileName, "S_FING_RS", "R_FING_RS");
        
        final File file = new File(sftpGetLocalDir + File.separator + receiveFileName);
        
        return new FileInputStream(file);
    }
}
