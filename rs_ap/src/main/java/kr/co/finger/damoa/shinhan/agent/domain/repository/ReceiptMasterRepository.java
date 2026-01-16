package kr.co.finger.damoa.shinhan.agent.domain.repository;

import kr.co.finger.damoa.shinhan.agent.domain.model.ReceiptDetailsDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.ReceiptMasterDO;
import kr.co.finger.shinhandamoa.data.table.mapper.XrcpdetMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XrcpmasMapper;
import kr.co.finger.shinhandamoa.data.table.model.Xrcpmas;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.TransNotiHistDTO;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.TransNotiMasDTO;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.XrcpmasDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 수납원장 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class ReceiptMasterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptMasterRepository.class);

    @Autowired
    private XrcpmasMapper xrcpmasMapper;
    
    @Autowired
    private XrcpdetMapper xrcpdetMapper;
    
    public void createReceiptMaster(ReceiptMasterDO receiptMaster) {
        final Xrcpmas xrcpmas = receiptMaster.getXrcpmas();
        xrcpmasMapper.insertSelective(xrcpmas);

        final List<ReceiptDetailsDO> detailsList = receiptMaster.getDetailsList();
        for (ReceiptDetailsDO each : detailsList) {
            xrcpdetMapper.insertSelective(each.getXrcpdet());
        }
    }

    /**
     * 입금 통지할 수납정보 가져오기
     *
     */
//    public List<XrcpmasDTO> getXrcpmasInfo(String rcpmascd) {
//        return xrcpmasMapper.selectXrcpmasByRcpmascd(rcpmascd);
//    }
    public XrcpmasDTO getXrcpmasInfo(String rcpmascd) {
        return xrcpmasMapper.selectXrcpmasByRcpmascd(rcpmascd);
    }

    /**
     * 입금 통지 발송 결과 내역 테이블 적재
     *
     */
    public void createTransNotiMas(TransNotiMasDTO transNotiMas) {
        xrcpmasMapper.insertTransNotiMas(transNotiMas);
    }

    /**
     * 입금 통지 발송 결과 update
     *
     */
    public void updateTransNotiMas(TransNotiMasDTO transNotiMas) {
        xrcpmasMapper.updateTransNotiMas(transNotiMas);
    }

    /**
     * 입금 통지 발송 이력 테이블 적재
     *
     */
    public void createTransNotiHist(TransNotiHistDTO transNotiHist) {
        xrcpmasMapper.insertTransNotiHist(transNotiHist);
    }
}
