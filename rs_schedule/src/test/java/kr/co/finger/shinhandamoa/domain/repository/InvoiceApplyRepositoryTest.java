package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.constants.InvoiceApplyProcessStatus;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDF;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDO;
import kr.co.finger.shinhandamoa.test.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = "kr.co.finger.shinhandamoa.domain")
public class InvoiceApplyRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceApplyRepositoryTest.class);

    @Autowired
    private InvoiceApplyRepository invoiceApplyRepository;

    @Test
    public void getFirst() {
        InvoiceApplyDF filter = new InvoiceApplyDF();
        InvoiceApplyDO item = invoiceApplyRepository.getFirst(filter);
        LOGGER.info("{}", item);
    }

    @Test
    public void get() {
        InvoiceApplyDO item = invoiceApplyRepository.get("10006365");
        LOGGER.info("{}", item);
    }

    @Test
    public void update() {
        InvoiceApplyDO origin = invoiceApplyRepository.get("10006365");
        LOGGER.info("{}", origin);

        origin.changeProcessStatusIntoWIP();
        invoiceApplyRepository.update(origin);

        InvoiceApplyDO result = invoiceApplyRepository.get("10006365");

        assertEquals(InvoiceApplyProcessStatus.WIP, result.getProcessStatusCd());

    }
}