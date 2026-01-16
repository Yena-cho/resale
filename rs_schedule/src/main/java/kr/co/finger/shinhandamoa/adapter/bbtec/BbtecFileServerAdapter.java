package kr.co.finger.shinhandamoa.adapter.bbtec;

import kr.co.finger.damoa.commons.io.FTPHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 비더빌텍 파일 서버
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class BbtecFileServerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BbtecFileServerAdapter.class);

    @Value("${bbtec.ftp.host}")
    private String host;

    @Value("${bbtec.ftp.username}")
    private String username;

    @Value("${bbtec.ftp.password}")
    private String password;

    @Value("${bbtec.ftp.workspace}")
    private String workspace;

    public void store(File file) {
        // 고지서 출력 전송
        try {
            FTPHelper ftpHelper = new FTPHelper(host, username, password);
            ftpHelper.setBinaryTransfer(false);
            ftpHelper.setCharset("EUC-KR");

            ftpHelper.send(file.getAbsolutePath(), workspace + File.separator + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}