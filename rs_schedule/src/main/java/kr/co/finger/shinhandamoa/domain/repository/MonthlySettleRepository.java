package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.data.table.mapper.XmonthsumMapper;
import kr.co.finger.shinhandamoa.data.table.model.Xmonthsum;
import kr.co.finger.shinhandamoa.data.table.model.XmonthsumExample;
import kr.co.finger.shinhandamoa.domain.model.MonthlySettleDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 월 정산 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class MonthlySettleRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlySettleRepository.class);

    @Autowired
    private XmonthsumMapper xmonthsumMapper;

    public void create(MonthlySettleDO monthlySettle) {
        Xmonthsum xmonthsum = monthlySettle.getXmonthsum();
        xmonthsumMapper.insert(xmonthsum);
    }
    
    public void update(MonthlySettleDO monthlySettle) {
        Xmonthsum xmonthsum = monthlySettle.getXmonthsum();
        xmonthsumMapper.updateByPrimaryKeySelective(xmonthsum);
    }

    public boolean exists(String clientId, String settleMonth) {
        final XmonthsumExample example = new XmonthsumExample();
        example.createCriteria().andChacdEqualTo(clientId).andMonthEqualTo(settleMonth);

        long count = xmonthsumMapper.countByExample(example);
        return count > 0;
    }
}
