package kr.co.finger.shinhandamoa.sys.service;

import kr.co.finger.msgio.msg.AutoWithdrawalHelper;
import kr.co.finger.msgio.msg.EB22;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.nio.charset.Charset;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/*.xml", "/spring/appServlet/*.xml"})
public class KftcWithdrawServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcWithdrawServiceTest.class);
    
    @Autowired
    private KftcWithdrawService kftcWithdrawService;

    @Test
    public void writeEB22() throws Exception {
        final File file = File.createTempFile("EB22", ".tmp");
        FileUtils.copyInputStreamToFile(new ClassPathResource("kftc/EB22").getInputStream(), file);

        AutoWithdrawalHelper.setCharset(Charset.forName("KSC5601"));
        final EB22 eb22 = AutoWithdrawalHelper.decodeEB22(file.getAbsolutePath());
        kftcWithdrawService.writeEB22(eb22);
    }
}
