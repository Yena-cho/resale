package kr.co.finger.damoa.scheduler.task.monthly;

import kr.co.finger.shinhandamoa.test.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class MonthlySettleTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlySettleTaskTest.class);
    
    @Autowired
    private MonthlySettleTask monthlySettleTask;
 
    public void execute() {
        monthlySettleTask.execute();
    }
}
