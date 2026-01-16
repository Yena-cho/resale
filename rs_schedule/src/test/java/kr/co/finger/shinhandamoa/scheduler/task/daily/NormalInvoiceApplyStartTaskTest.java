package kr.co.finger.shinhandamoa.scheduler.task.daily;

import kr.co.finger.shinhandamoa.test.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"kr.co.finger.shinhandamoa.domain", "kr.co.finger.shinhandamoa.adapter", "kr.co.finger.shinhandamoa.service.invoice"})
@Import({NormalInvoiceApplyStartTask.class})
public class NormalInvoiceApplyStartTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NormalInvoiceApplyStartTaskTest.class);

    @Autowired
    private NormalInvoiceApplyStartTask task;

    @Test
    public void run() {
        task.run();
    }
}