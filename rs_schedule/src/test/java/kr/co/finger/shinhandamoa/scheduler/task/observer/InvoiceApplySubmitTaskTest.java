package kr.co.finger.shinhandamoa.scheduler.task.observer;

import kr.co.finger.shinhandamoa.test.TestConfig;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@link InvoiceApplySubmitTask} 테스트
 *
 * @author wisehouse@finger.co.kr
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"kr.co.finger.shinhandamoa.domain", "kr.co.finger.shinhandamoa.adapter"})
@Import({InvoiceService.class, InvoiceApplySubmitTask.class})
public class InvoiceApplySubmitTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceApplySubmitTaskTest.class);

    @Autowired
    private InvoiceApplySubmitTask invoiceApplySubmitTask;

    @Test
    public void run() throws InterruptedException {
        invoiceApplySubmitTask.run();
    }
}