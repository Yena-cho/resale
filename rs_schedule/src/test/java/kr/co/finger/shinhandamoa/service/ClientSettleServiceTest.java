package kr.co.finger.shinhandamoa.service;

import kr.co.finger.shinhandamoa.data.table.mapper.XmonthsumMapper;
import kr.co.finger.shinhandamoa.data.table.model.Xmonthsum;
import kr.co.finger.shinhandamoa.data.table.model.XmonthsumKey;
import kr.co.finger.shinhandamoa.test.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

/**
 * {@link ClientSettleService} 테스트
 *
 * @author wisehouse@finger.co.kr
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"kr.co.finger.shinhandamoa.domain"})
@Import(ClientSettleService.class)
@Transactional
@Rollback
public class ClientSettleServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSettleServiceTest.class);

    @Autowired
    private ClientSettleService clientSettleService;

    @Autowired
    private XmonthsumMapper xmonthsumMapper;

//    @Test
//    public void executeMonthlySettle_1() {
//        clientSettleService.executeMonthlySettle("201808");
//    }
//
//    @Test
//    public void executeMonthlySettle_2() {
//        clientSettleService.executeMonthlySettle("201906");
//    }

    /**
     * 월 마감 테스트
     *
     */
    @Test
    public void executeMonthlySettleForClient() {
        final XmonthsumKey key = new XmonthsumKey();
        key.setChacd("20007389");
        key.setMonth("201808");
        xmonthsumMapper.deleteByPrimaryKey(key);

        clientSettleService.executeMonthlySettleForClient("20007389", "201808");

        Xmonthsum xmonthsum = xmonthsumMapper.selectByPrimaryKey(key);
        assertNotNull(xmonthsum);
    }

    @Test
    public void executeDailySettleForClient_1() {
        clientSettleService.executeDailySettleForClient("20007389", "20181105");
    }

    /**
     * 일 정산 시 출력의뢰 포함 여부 확인
     */
    @Test
    public void executeDailySettleForClient_2() {
        clientSettleService.executeDailySettleForClient("20001577", "20190704");
    }
}
