package kr.co.finger.damoa.scheduler.util;

import kr.co.finger.damoa.commons.io.SftpClient;
import org.junit.Before;
import org.junit.Test;

public class SftpClientTest {

    private SftpClient sftpClient;
    @Before
    public void setup() {
        sftpClient = new SftpClient("dev.fchin.co.kr",22,"damoa","finger1113#");
    }

    @Test
    public void upload() throws Exception {
        sftpClient.upload("F:\\tmp\\newfolder\\simplejsondatagenerator\\src\\main\\java\\kr\\co\\finger\\sftp\\Sftp.java", "/home/damoa/FileServer/91 개인 자료실/권석훈");
    }

    @Test
    public void download() throws Exception {
        sftpClient.download("/home/damoa/FileServer/91 개인 자료실/권석훈/SqlLoggingPlugin.java","F:\\tmp");
    }
}
