package kr.co.finger.damoa.shinhan.agent.domain.repository;

import kr.co.finger.damoa.shinhan.agent.domain.model.ReceiptDetailsDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.ReceiptMasterDO;
import kr.co.finger.shinhandamoa.data.table.mapper.TransApiRelayDataMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XrcpdetMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XrcpmasMapper;
import kr.co.finger.shinhandamoa.data.table.model.TransApiRelayData;
import kr.co.finger.shinhandamoa.data.table.model.Xrcpmas;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.TransNotiHistDTO;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.TransNotiMasDTO;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.XrcpmasDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 중계기관 송수신 데이터 저장소
 */
@Repository
public class TransApiRelayDataRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransApiRelayDataRepository.class);

    @Autowired
    private TransApiRelayDataMapper transApiRelayDataMapper;
    
//    @Autowired
//    private XrcpdetMapper xrcpdetMapper;
    
    public void createTransApiRelayData(TransApiRelayData transApiRelayData) {
        transApiRelayDataMapper.insert(transApiRelayData);
    }

    public void updateTransApiRelayData(TransApiRelayData transApiRelayData) {
        transApiRelayDataMapper.update(transApiRelayData);
    }

}
