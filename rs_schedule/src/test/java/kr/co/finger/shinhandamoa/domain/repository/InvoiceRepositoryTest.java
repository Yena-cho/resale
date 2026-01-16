package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.domain.model.InvoiceDF;
import kr.co.finger.shinhandamoa.domain.model.InvoiceDO;
import kr.co.finger.shinhandamoa.test.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class InvoiceRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceRepositoryTest.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void test() {
        final List<InvoiceDO> itemList = invoiceRepository.find(new InvoiceDF());
        LOGGER.info("{}", itemList.size());

        for (InvoiceDO each : itemList) {
            LOGGER.info("{}, {}", each.getMasterId(), each.getSeqNo());
        }
    }
}