package kr.co.finger.damoa.scheduler.task.observer;

import kr.co.finger.shinhandamoa.test.TestConfig;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link CashReceiptFileImportTask} 테스트
 *
 * @author wisehouse@finger.co.kr
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ComponentScan(
        basePackages = {
                "kr.co.finger.shinhandamoa.data",
                "kr.co.finger.shinhandamoa.domain.repository",
                "kr.co.finger.shinhandamoa.service",
//                "kr.co.finger.damoa.scheduler.config",
                "kr.co.finger.damoa.scheduler.dao",
                "kr.co.finger.damoa.scheduler.service"
        }, excludeFilters = @ComponentScan.Filter(type=FilterType.ANNOTATION, classes = Scheduled.class))
@Import(CashReceiptFileImportTask.class)
public class CashReceiptFileImportTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptFileImportTaskTest.class);

    @Autowired
    private CashReceiptFileImportTask cashReceiptFileImportTask;

    @Test
    @Transactional
    @Rollback
    public void execute() {
        // 테스트하기 어렵네
        cashReceiptFileImportTask.execute();
    }
    
    @Test
    public void streamAPI() {
        List<Pair<String, String>> itemList = new ArrayList<>();
        itemList.add(new MutablePair<>("A", "1"));
        itemList.add(new MutablePair<>("B", "1"));
        itemList.add(new MutablePair<>("C", "2"));
        itemList.add(new MutablePair<>("D", "2"));
        itemList.add(new MutablePair<>("E", "3"));
        
        LOGGER.debug("itemList.stream().collect(Collectors.groupingBy(Pair::getRight)): {}", new ArrayList<>(itemList.stream().collect(Collectors.groupingBy(Pair::getRight, Collectors.maxBy(Comparator.comparing(Pair::getLeft)))).values()));
        
//        List<Pair<String, String>> resultList = itemList.stream().collect(Collectors.groupingBy(Pair::getRight));
//        List<Pair<String, String>> resultList = itemList.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(Pair::getRight), each -> each.values().iterator().next()));
        
//        LOGGER.info("size: {}", resultList.size());
//        LOGGER.debug("resultList: {}", resultList);
    }

}
