package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.common.DateUtils;
import kr.co.finger.shinhandamoa.data.table.mapper.SmsreqMapper;
import kr.co.finger.shinhandamoa.data.table.model.SmsreqExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 문자 메시지 저장소
 *
 * @author wisehouse@finger.co.kr
 */
@Repository
public class InstantMessageRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstantMessageRepository.class);

    @Autowired
    private SmsreqMapper smsreqMapper;

    public int count(String clientId, String type, List<String> statusList, String fromDateString, String toDateString) {
        final Date fromDate = DateUtils.parseDate(fromDateString, "yyyyMMdd");
        final Date toDate = DateUtils.addDays(DateUtils.parseDate(toDateString, "yyyyMMdd"), 1);

        final SmsreqExample example = new SmsreqExample();
        final SmsreqExample.Criteria criteria = example.createCriteria();
        criteria.andChacdEqualTo(clientId);
        switch (type) {
            case "SMS":
                criteria.andTypeEqualTo("0");
                break;
            case "LMS":
                criteria.andTypeEqualTo("1");
                break;
        }
        if(!statusList.isEmpty()) {
            criteria.andStatusIn(statusList);
        }
        criteria.andRegdtGreaterThanOrEqualTo(fromDate);
        criteria.andRegdtLessThan(toDate);

        return (int) smsreqMapper.countByExample(example);
    }
}
