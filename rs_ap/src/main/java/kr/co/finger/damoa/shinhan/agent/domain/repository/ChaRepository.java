package kr.co.finger.damoa.shinhan.agent.domain.repository;

import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.shinhandamoa.data.table.mapper.ValistMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XchalistMapper;
import kr.co.finger.shinhandamoa.data.table.model.Valist;
import kr.co.finger.shinhandamoa.data.table.model.ValistExample;
import kr.co.finger.shinhandamoa.data.table.model.Xchalist;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 기관 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class ChaRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChaRepository.class);
    
    @Autowired
    private XchalistMapper xchalistMapper;

    @Autowired
    private ValistMapper valistMapper;
    
    public ChaDO findByChaCd(String chaCd) {
        final Xchalist xchalist = xchalistMapper.selectByPrimaryKey(chaCd);
        
        if(xchalist == null) {
            return null;
        }

        return new ChaDO(xchalist);
    }

    public String findChacdByAccountNoAndFingerCd(String fgCd, String accountNo) {
        RowBounds rowBounds = new RowBounds(0, 1);
        ValistExample valistExample = new ValistExample();
        valistExample.createCriteria()
                .andFgcdEqualTo(fgCd)
                .andVanoEqualTo(accountNo);
        final List<Valist> valist = valistMapper.selectByExampleWithRowbounds(valistExample, rowBounds);

        if(valist == null || valist.isEmpty()) {
            return null;
        }
        if(valist.get(0).getChacd() == null) {
            return null;
        }

        return valist.get(0).getChacd();
    }
}
