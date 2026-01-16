package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.common.DateUtils;
import kr.co.finger.shinhandamoa.data.table.mapper.AtreqMapper;
import kr.co.finger.shinhandamoa.data.table.model.AtreqExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 알림톡 저장소
 *
 * @author mljeong@finger.co.kr
 */
@Repository
public class AlarmTalkRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmTalkRepository.class);

    @Autowired
    private AtreqMapper atreqMapper;

    public int count(String clientId, String fromDateString, String toDateString) {
        final Date fromDate = DateUtils.parseDate(fromDateString, "yyyyMMdd");
        final Date toDate = DateUtils.addDays(DateUtils.parseDate(toDateString, "yyyyMMdd"), 1);

        final AtreqExample example = new AtreqExample();
        final AtreqExample.Criteria criteria = example.createCriteria();
        criteria.andSendstatuscdEqualTo("1");
        criteria.andChacdEqualTo(clientId);
        criteria.andSenddateGreaterThanOrEqualTo(fromDate);
        criteria.andSenddateLessThan(toDate);

        return (int) atreqMapper.countByExample(example);
    }
}
