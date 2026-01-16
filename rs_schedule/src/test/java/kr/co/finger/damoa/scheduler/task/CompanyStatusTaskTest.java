package kr.co.finger.damoa.scheduler.task;

import kr.co.finger.shinhandamoa.test.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@link CompanyStatusTaskTest} 테스트
 *
 * @author jungbna@finger.co.kr
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
public class CompanyStatusTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyStatusTaskTest.class);

    @Autowired
    private CompanyStatusTask companyStatusTask;

    @Test
    public void execute() throws Exception {
        companyStatusTask.execute();
    }

}