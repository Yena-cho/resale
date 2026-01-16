package kr.co.finger.shinhandamoa.sys.service;

import kr.co.finger.msgio.msg.AutoWithdrawalHelper;
import kr.co.finger.msgio.msg.EB14;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/*.xml", "/spring/appServlet/*.xml"})
public class KftcPayerServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(KftcPayerServiceTest.class);

    @Autowired
    private KftcPayerService kftcPayerService;

    @Test
    public void writeEB14() throws Exception {
        final File file = File.createTempFile("EB14", ".tmp");
        FileUtils.copyInputStreamToFile(new ClassPathResource("kftc/EB141005").getInputStream(), file);

        final EB14 eb14 = AutoWithdrawalHelper.decodeEB14(file.getAbsolutePath());
        kftcPayerService.writeEB14(eb14);
    }
}
