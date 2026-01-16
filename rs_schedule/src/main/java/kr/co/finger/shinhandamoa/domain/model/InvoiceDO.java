package kr.co.finger.shinhandamoa.domain.model;

import kr.co.finger.shinhandamoa.data.InvoiceVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 고지서 DO
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceDO.class);

    private final InvoiceVO vo;

    public InvoiceDO(InvoiceVO vo) {
        this.vo = vo;
    }

    public String getMasterId() {
        return StringUtils.trim(vo.getMasterId());
    }

    public String getSeqNo() {
        return StringUtils.trim(vo.getSeqNo());
    }

    public String getTitle() {
        return StringUtils.trim(vo.getTitle());
    }

    public String getVano() {
        return StringUtils.trim(vo.getVano());
    }

    public String getCustomerName() {
        return StringUtils.trim(vo.getCustomerName());
    }

    public String getContent1() {
        return StringUtils.trim(vo.getContent1());
    }

    public String getContent2() {
        return StringUtils.trim(vo.getContent2());
    }

    public String getContent3() {
        return StringUtils.trim(vo.getContent3());
    }

    public String getContent4() {
        return StringUtils.trim(vo.getContent4());
    }

    public String getContent5() {
        return StringUtils.trim(vo.getContent5());
    }

    public String getContent6() {
        return StringUtils.trim(vo.getContent6());
    }

    public String getContent7() {
        return StringUtils.trim(vo.getContent7());
    }

    public String getContent8() {
        return StringUtils.trim(vo.getContent8());
    }

    public String getContent9() {
        return StringUtils.trim(vo.getContent9());
    }

    public String getContent10() {
        return StringUtils.trim(vo.getContent10());
    }

    public String getInvoiceChaName() {
        return StringUtils.trim(vo.getInvoiceChaName());
    }

    public String getInvoiceChaContactNo() {
        return StringUtils.trim(vo.getInvoiceChaContactNo());
    }

    public String getDueDt() {
        return StringUtils.trim(vo.getDueDt());
    }

    public String getCustomerProperty1() {
        return StringUtils.trim(vo.getCustomerProperty1());
    }

    public String getCustomerProperty2() {
        return StringUtils.trim(vo.getCustomerProperty2());
    }

    public String getCustomerProperty3() {
        return StringUtils.trim(vo.getCustomerProperty3());
    }

    public String getCustomerProperty4() {
        return StringUtils.trim(vo.getCustomerProperty4());
    }

    public String getItem1Name() {
        return StringUtils.trim(vo.getItem1Name());
    }

    public long getItem1Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem1Amount()));
    }

    public String getItem1Remark() {
        return StringUtils.trim(vo.getItem1Remark());
    }

    public String getItem2Name() {
        return StringUtils.trim(vo.getItem2Name());
    }

    public long getItem2Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem2Amount()));
    }

    public String getItem2Remark() {
        return StringUtils.trim(vo.getItem2Remark());
    }

    public String getItem3Name() {
        return StringUtils.trim(vo.getItem3Name());
    }

    public long getItem3Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem3Amount()));
    }

    public String getItem3Remark() {
        return StringUtils.trim(vo.getItem3Remark());
    }

    public String getItem4Name() {
        return StringUtils.trim(vo.getItem4Name());
    }

    public long getItem4Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem4Amount()));
    }

    public String getItem4Remark() {
        return StringUtils.trim(vo.getItem4Remark());
    }

    public String getItem5Name() {
        return StringUtils.trim(vo.getItem5Name());
    }

    public long getItem5Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem5Amount()));
    }

    public String getItem5Remark() {
        return StringUtils.trim(vo.getItem5Remark());
    }

    public String getItem6Name() {
        return StringUtils.trim(vo.getItem6Name());
    }

    public long getItem6Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem6Amount()));
    }

    public String getItem6Remark() {
        return StringUtils.trim(vo.getItem6Remark());
    }

    public String getItem7Name() {
        return StringUtils.trim(vo.getItem7Name());
    }

    public long getItem7Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem7Amount()));
    }

    public String getItem7Remark() {
        return StringUtils.trim(vo.getItem7Remark());
    }

    public String getItem8Name() {
        return StringUtils.trim(vo.getItem8Name());
    }

    public long getItem8Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem8Amount()));
    }

    public String getItem8Remark() {
        return StringUtils.trim(vo.getItem8Remark());
    }

    public String getItem9Name() {
        return StringUtils.trim(vo.getItem9Name());
    }

    public long getItem9Amount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getItem9Amount()));
    }

    public String getItem9Remark() {
        return StringUtils.trim(vo.getItem9Remark());
    }

    public long getTotalNoticeAmount() {
        return NumberUtils.toLong(StringUtils.trim(vo.getTotalNoticeAmount()));
    }

    public String getOptionalMessage() {
        return StringUtils.trim(vo.getOptionalMessage());
    }

    public String getFooter1() {
        return StringUtils.trim(vo.getFooter1());
    }

    public String getFooter2() {
        return StringUtils.trim(vo.getFooter2());
    }

    public String getFooter3() {
        return StringUtils.trim(vo.getFooter3());
    }

    public String getChaContactNo2() {
        return StringUtils.trim(vo.getChaContactNo2());
    }

    public String getChaAddress() {
        return StringUtils.trim(vo.getChaAddress());
    }

    public String getRemark() {
        return StringUtils.trim(vo.getRemark());
    }

    public String getCustomerProperty1Name() {
        return StringUtils.trim(vo.getCustomerProperty1Name());
    }

    public String getCustomerProperty2Name() {
        return StringUtils.trim(vo.getCustomerProperty2Name());
    }

    public String getCustomerProperty3Name() {
        return StringUtils.trim(vo.getCustomerProperty3Name());
    }

    public String getCustomerProperty4Name() {
        return StringUtils.trim(vo.getCustomerProperty4Name());
    }

    public String getCustomerTitle() {
        return StringUtils.trim(vo.getCustomerTitle());
    }
}
