package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.data.domain.mapper.NoticeMasterMapper;
import kr.co.finger.shinhandamoa.data.domain.model.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 청구 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class NoticeRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeRepository.class);
    
    @Autowired
    private NoticeMasterMapper noticeMasterMapper;
    
    public Statistics aggregate(String clientId, List<String> stateList, String fromDate, String toDate, String cancelYn) {
        return noticeMasterMapper.aggregate(clientId, stateList, fromDate, toDate, cancelYn);
    }
}
