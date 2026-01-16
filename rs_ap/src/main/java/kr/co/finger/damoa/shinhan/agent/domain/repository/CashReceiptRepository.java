package kr.co.finger.damoa.shinhan.agent.domain.repository;

import kr.co.finger.damoa.shinhan.agent.domain.model.CashReceiptDO;
import kr.co.finger.shinhandamoa.data.table.mapper.CashHstMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XcashmasMapper;
import kr.co.finger.shinhandamoa.data.table.model.CashHst;
import kr.co.finger.shinhandamoa.data.table.model.Xcashmas;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 현금영수증 저장소
 * 
 * @author wisehouse@finger.co.kr
 */
@Repository
public class CashReceiptRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashReceiptRepository.class);
    
    @Autowired
    private XcashmasMapper xcashmasMapper;

    @Autowired
    private CashHstMapper cashHstMapper;
    
    public void createCashReceipt(CashReceiptDO cashReceipt) {
        Xcashmas xcashmas = cashReceipt.getXcashmas();

        xcashmasMapper.insertSelective(xcashmas);

        //현금영수증 자동 발행요청시 이용내역에 등록
        if("ST05".equals(xcashmas.getCashmasst()) && !StringUtils.isBlank(xcashmas.getCashmascd())){
            CashHst cashHst = new CashHst();
            cashHst.setChacd(xcashmas.getChacd());
            cashHst.setCashmascd(xcashmas.getCashmascd());
            cashHst.setRequestUser("DamoaShinhanAgent");
            cashHst.setRequestDate(new Date());
            cashHst.setRequestTypeCd("I");
            cashHst.setResultCd("3");
            cashHst.setCusoffno(xcashmas.getCusoffno());
            cashHst.setRcpamt(xcashmas.getRcpamt());
            cashHst.setTax(xcashmas.getTax());
            cashHstMapper.insertSelective(cashHst);
        }

    }
}
