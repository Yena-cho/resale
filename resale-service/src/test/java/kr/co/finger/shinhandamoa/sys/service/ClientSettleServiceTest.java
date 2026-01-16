package kr.co.finger.shinhandamoa.sys.service;

import com.finger.shinhandamoa.common.ListResult;
import com.finger.shinhandamoa.common.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * {@link ClientSettleService} 테스트
 * 
 * @author wisehouse@finger.co.kr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/*.xml", "/spring/appServlet/*.xml"})
public class ClientSettleServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSettleServiceTest.class);

    @Autowired
    private ClientSettleService clientSettleService;
    
    @Test
    public void getDailySettleList() {
        final ListResult<DailyClientSettleDTO> listResult = clientSettleService.getDailySettleList(StringUtils.EMPTY, StringUtils.EMPTY, null, null,"clientId_asc", new PageBounds());
        LOGGER.info("listResult: {}", listResult);
    }
}
