package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.data.table.mapper.*;
import kr.co.finger.shinhandamoa.data.table.model.Xdaysum;
import kr.co.finger.shinhandamoa.domain.model.DailySettleDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 일 정산 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class DailySettleRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailySettleRepository.class);
    
    @Autowired
    private XdaysumMapper xdaysumMapper;
    
    public void createOrReplace(DailySettleDO dailySettleDO) {
        Xdaysum xdaysum = dailySettleDO.getXdaysum();
        xdaysumMapper.deleteByPrimaryKey(xdaysum);
        xdaysumMapper.insertSelective(xdaysum);
    }
}
