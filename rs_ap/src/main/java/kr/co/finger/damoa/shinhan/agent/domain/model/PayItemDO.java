package kr.co.finger.damoa.shinhan.agent.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xpayitem;
import org.apache.commons.lang3.StringUtils;

/**
 * 청구항목구분 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class PayItemDO {
    private Xpayitem xpayitem;

    public PayItemDO(Xpayitem xpayitem) {
        this.xpayitem = xpayitem;
    }

    /**
     * 부분납 시 필수 항목 여부
     * 
     * @return
     */
    public boolean isRequired() {
        if(xpayitem == null) {
            return Boolean.TRUE;
        }
        
        return StringUtils.equals(xpayitem.getPayitemselyn(), "N");
    }
    
    public boolean isOptional() {
        return !isRequired();
    }

    public String getId() {
        if(xpayitem == null) {
            return StringUtils.EMPTY;
        }
        
        return xpayitem.getPayitemcd();
    }

    public String getAccountId() {
        if(xpayitem == null) { 
            return StringUtils.EMPTY;
        }
        
        return xpayitem.getAdjfiregkey();
    }

    public boolean isEnableCashReceipt() {
        if(xpayitem == null) {
            return Boolean.TRUE;
        }
        
        return StringUtils.equals(xpayitem.getRcpitemyn(), "Y");
    }

    public String getAdjfiregkey() {
        return xpayitem.getAdjfiregkey();
    }

    public String getCashReceiptIdentityNo() {
        if(xpayitem == null) {
            return StringUtils.EMPTY;
        }
        
        return xpayitem.getChaoffno();
    }
}
