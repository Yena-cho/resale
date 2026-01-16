package kr.co.finger.shinhandamoa.data.domain.mapper;

import kr.co.finger.shinhandamoa.data.domain.model.DailyClientSettleExample;
import kr.co.finger.shinhandamoa.domain.model.DailyClientSettleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 일 정산 도메인 매퍼
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
@Mapper
public interface DailyClientSettleMapper {

    List<DailyClientSettleDO> selectByExampleWithRowBounds(DailyClientSettleExample example, RowBounds rowBounds);

    long countByExample(DailyClientSettleExample example);
    
}
