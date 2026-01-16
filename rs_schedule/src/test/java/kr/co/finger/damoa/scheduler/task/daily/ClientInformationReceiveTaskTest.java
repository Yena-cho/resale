//package kr.co.finger.damoa.scheduler.task.daily;
//
//import kr.co.finger.shinhandamoa.test.TestConfig;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * {@link ClientInformationReceiveTask} 테스트
// *
// * @author wisehouse@finger.co.kr
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes= TestConfig.class)
//public class ClientInformationReceiveTaskTest {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInformationReceiveTaskTest.class);
//
//    @Autowired
//    private ClientInformationReceiveTask clientInformationReceiveTask;
//
//    @Test
//    @Transactional
////    @Rollback
//    @Commit
//    public void execute() {
//        clientInformationReceiveTask.execute("20181211");
//    }
//}
