package kr.co.finger.shinhandamoa.domain.repository;

import com.finger.shinhandamoa.data.table.mapper.ChaMapper;
import com.finger.shinhandamoa.data.table.mapper.XdaysumMapper;
import com.finger.shinhandamoa.data.table.model.Cha;
import com.finger.shinhandamoa.data.table.model.Xdaysum;
import com.finger.shinhandamoa.data.table.model.XdaysumKey;
import kr.co.finger.shinhandamoa.data.domain.mapper.DailyClientSettleMapper;
import kr.co.finger.shinhandamoa.data.domain.model.DailyClientSettleExample;
import kr.co.finger.shinhandamoa.data.domain.model.DailyClientSettleKey;
import kr.co.finger.shinhandamoa.domain.model.DailyClientSettleDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 이용기관 일 정산 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class DailyClientSettleRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyClientSettleRepository.class);

    @Autowired
    private ChaMapper chaMapper;
    
    @Autowired
    private XdaysumMapper xdaysumMapper;
    
    @Autowired
    private DailyClientSettleMapper dailyClientSettleMapper;
    
    private DailyClientSettleDO get(String clientId, Date settleDate) {
        return get(clientId, new SimpleDateFormat("yyyyMMdd").format(settleDate));
    }

    private DailyClientSettleDO get(DailyClientSettleKey dailyClientSettleKey) {
        return get(dailyClientSettleKey.getClientId(), dailyClientSettleKey.getSettleDate());
    }

    private DailyClientSettleDO get(String clientId, String settleDate) {
        final Cha cha = chaMapper.selectByPrimaryKey(clientId);

        final XdaysumKey xdaysumKey = new XdaysumKey();
        xdaysumKey.setChacd(clientId);
        xdaysumKey.setSettleDt(settleDate);
        final Xdaysum xdaysum = xdaysumMapper.selectByPrimaryKey(xdaysumKey);

        return DailyClientSettleDO.getInstance(cha, xdaysum);
    }

    public List<DailyClientSettleDO> findWithRowBounds(String clientId, String clientName, Date fromDate, Date toDate, String sort, RowBounds rowBounds) {
        final DailyClientSettleExample example = new DailyClientSettleExample();
        if(StringUtils.isNotBlank(clientId)) {
            example.setClientId(clientId);
        }
        if(StringUtils.isNotBlank(clientName)) {
            example.setClientName(clientName);
        }
        example.setFromDate(fromDate);
        example.setToDate(toDate);
        
        switch(sort) {
            case "clientName_asc":
                example.setOrderByClause("A.CHANAME ASC, B.SETTLE_DT DESC");
                break;
            case "settleDate_desc":
                example.setOrderByClause("B.SETTLE_DT DESC, A.CHACD ASC");
                break;
            case "settleDate_asc":
                example.setOrderByClause("B.SETTLE_DT ASC, A.CHACD ASC");
                break;
            default:
            case "clientId_asc":
                example.setOrderByClause("A.CHACD ASC, B.SETTLE_DT DESC");
                break;
        }
        
        final List<DailyClientSettleDO> keyList = dailyClientSettleMapper.selectByExampleWithRowBounds(example, rowBounds);
        final ArrayList<DailyClientSettleDO> doList = new ArrayList<>();
        for (DailyClientSettleDO each : keyList) {
            doList.add(each);
        }

        return doList;
    }

    public long count(String clientId, String clientName, Date fromDate, Date toDate) {
        final DailyClientSettleExample example = new DailyClientSettleExample();
        if(StringUtils.isNotBlank(clientId)) {
            example.setClientId(clientId);
        }
        if(StringUtils.isNotBlank(clientName)) {
            example.setClientName(clientName);
        }
        example.setFromDate(fromDate);
        example.setToDate(toDate);

        return dailyClientSettleMapper.countByExample(example);
    }
}
