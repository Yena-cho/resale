package kr.co.finger.shinhandamoa.domain.repository;

import kr.co.finger.shinhandamoa.data.domain.mapper.ReceiptMasterMapper;
import kr.co.finger.shinhandamoa.data.domain.model.*;
import kr.co.finger.shinhandamoa.data.table.mapper.VaSettleDetMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.VaSettleMasMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XrcpdetMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XrcpmasMapper;
import kr.co.finger.shinhandamoa.data.table.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 수납 저장소
 *
 * @author wisehouse@finger.co.kr
 * @author denny91@finger.co.kr
 */
@Repository
public class ReceiptRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptRepository.class);

    @Autowired
    private ReceiptMasterMapper receiptMasterMapper;

    @Autowired
    private XrcpmasMapper xrcpmasMapper;

    @Autowired
    private XrcpdetMapper xrcpdetMapper;

    @Autowired
    private VaSettleMasMapper vaSettleMasMapper;

    @Autowired
    private VaSettleDetMapper vaSettleDetMapper;

    public Statistics aggregate(String clientId, List<String> mediaTypeList, List<String> stateList, String fromDate, String toDate) {
        //mediaTypeList = sveCdList
        //stateList = rcpMasStList
        return receiptMasterMapper.aggregate(clientId, mediaTypeList, stateList, fromDate, toDate);
    }

    public List<XrcpmasVO> getRcpmasList(String clientId, String fromDateString, String toDateString) {
        XrcpmasExample xrcpmasExample = new XrcpmasExample();
        xrcpmasExample.createCriteria()
                .andChacdEqualTo(clientId)
                .andPaydayGreaterThanOrEqualTo(fromDateString)
                .andPaydayLessThanOrEqualTo(toDateString)
                .andSvecdEqualTo("VAS")
                .andRcpmasstEqualTo("PA03");
        List<Xrcpmas> xrcpmasList = xrcpmasMapper.selectByExample(xrcpmasExample);
        List<XrcpmasVO> xrcpmasVOList = new ArrayList<>();
        for (Xrcpmas xrcpmas : xrcpmasList) {
            xrcpmasVOList.add(XrcpmasVO.convert(xrcpmas));
        }

        return xrcpmasVOList;
    }

    public List<XrcpdetVO> getRcpdetList(List<String> rcpmascdList, String adjfiregkey) {
        XrcpdetExample xrcpdetExample = new XrcpdetExample();
        xrcpdetExample.createCriteria()
                .andAdjfiregkeyEqualTo(adjfiregkey)
                .andRcpmascdIn(rcpmascdList);

        List<Xrcpdet> xrcpdetList = xrcpdetMapper.selectByExample(xrcpdetExample);
        List<XrcpdetVO> xrcpdetVOList = new ArrayList<>();
        for (Xrcpdet xrcpdet : xrcpdetList) {
            xrcpdetVOList.add(XrcpdetVO.convert(xrcpdet));
        }

        return xrcpdetVOList;
    }

    public void insertVaSettle(Xchalist xchalist, Xadjgroup xadjgroup, long rcpamt, String settleDateString) throws ParseException {

        VaSettleMas vaSettleMas = VaSettleMasVO.create(xchalist, xadjgroup, rcpamt, settleDateString);
        vaSettleMasMapper.insertSelective(vaSettleMas);

        VaSettleDet vaSettleDet = VaSettleDetVO.create(vaSettleMas);
        vaSettleDetMapper.insertSelective(vaSettleDet);
    }

    public List<XrcpdetVO> getDistinctAdjfiregkey(List<String> rcpmascdList) {

        List<XrcpdetVO> xrcpdetVOList = receiptMasterMapper.getDistinctAdjfiregkey(rcpmascdList);

        return xrcpdetVOList;
    }

    public List<XrcpmasVO> getRcpmasWithoutDet(List<String> rcpmascdList) {
        return receiptMasterMapper.getRcpmasWithoutDet(rcpmascdList);
    }
}
