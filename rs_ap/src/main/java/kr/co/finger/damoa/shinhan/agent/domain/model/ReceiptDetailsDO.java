package kr.co.finger.damoa.shinhan.agent.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xrcpdet;
import org.apache.commons.lang3.StringUtils;

/**
 * 수납원장 상세 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class ReceiptDetailsDO {
    private Xrcpdet xrcpdet;
    private PayItemDO payItem;

    public ReceiptDetailsDO(Xrcpdet xrcpdet) {
        this.xrcpdet = xrcpdet;
    }

    public boolean isCanceled() {
        return StringUtils.equals(xrcpdet.getRcpdetst(), "ST09");
    }

    public long getAmount() {
        return xrcpdet.getPayitemamt();
    }

    public void setMasterId(String masterId) {
        xrcpdet.setRcpmascd(masterId);
    }

    public Xrcpdet getXrcpdet() {
        return xrcpdet;
    }

    public void setPayItem(PayItemDO payItem) {
        this.payItem = payItem;
    }

    public PayItemDO getPayItem() {
        return payItem;
    }
}
