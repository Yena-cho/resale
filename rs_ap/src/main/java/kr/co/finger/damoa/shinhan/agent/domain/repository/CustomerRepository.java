package kr.co.finger.damoa.shinhan.agent.domain.repository;

import kr.co.finger.damoa.shinhan.agent.domain.model.CustomerDO;
import kr.co.finger.shinhandamoa.data.table.mapper.XcusmasMapper;
import kr.co.finger.shinhandamoa.data.table.model.Xcusmas;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 고객 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class CustomerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepository.class);
    
    @Autowired
    private XcusmasMapper xcusmasMapper;

    /**
     * 고객 조회
     * 
     */
    public CustomerDO findByAccountNo(String chaCd, String accountNo) {
        LOGGER.debug("고객 조회[chaCd={}, accountNo:{}]", chaCd, accountNo);
        
        final Xcusmas xcusmas = xcusmasMapper.selectByPrimaryKey(accountNo);
        LOGGER.trace("xcusmas: {}", xcusmas);
        
        if(xcusmas == null) {
            LOGGER.warn("계좌에 할당된 고객 없음", accountNo);
            return null;
        }

        if(!StringUtils.equals(xcusmas.getChacd(), chaCd)) {
            LOGGER.warn("해당 기관 계좌 아님");
            return null;
        }
        
        return new CustomerDO(xcusmas);
    }
}
