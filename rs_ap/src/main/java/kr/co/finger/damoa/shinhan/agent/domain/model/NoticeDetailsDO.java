package kr.co.finger.damoa.shinhan.agent.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xnotidet;
import kr.co.finger.shinhandamoa.data.table.model.Xrcpdet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 청구원장 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class NoticeDetailsDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeDetailsDO.class);
    
    private Xnotidet xnotidet;
    private PayItemDO payItem;
    private List<ReceiptDetailsDO> receiptDetailsList;

    public NoticeDetailsDO(Xnotidet xnotidet) {
        this.xnotidet = xnotidet;  
        this.receiptDetailsList = new ArrayList<>();
    }

    public void setPayItem(PayItemDO payItem) {
        this.payItem = payItem;
    }

    public PayItemDO getPayItem() {
        return payItem;
    }

    public List<ReceiptDetailsDO> getReceiptDetailsList() {
        return receiptDetailsList;
    }
    
    public void addReceiptDetails(ReceiptDetailsDO receiptDetails) {
        receiptDetailsList.add(receiptDetails);
    }

    public long getRemainAmount() {
        long noticeAmount = xnotidet.getPayitemamt();
        long paymentAmount = getPaymentAmount();
        
        return Math.max(noticeAmount-paymentAmount, 0);
    }

    public ReceiptDetailsDO createReceiptDetails(long transactionAmount) {
        if(this.getRemainAmount() == transactionAmount) {
            this.setStatus("PA03");
        } else if(transactionAmount > this.getRemainAmount()) {
            this.setStatus("PA05");
        } else {
            this.setStatus("PA04");
        }
        
        final Xrcpdet xrcpdet = new Xrcpdet();
        xrcpdet.setNotidetcd(xnotidet.getNotidetcd());
        xrcpdet.setDetkey(xnotidet.getDetkey());
        xrcpdet.setPayitemcd(xnotidet.getPayitemcd());
        xrcpdet.setAdjfiregkey(xnotidet.getAdjfiregkey());
        xrcpdet.setPayitemname(xnotidet.getPayitemname());
        xrcpdet.setPayitemamt(transactionAmount);
        xrcpdet.setRcpitemyn(xnotidet.getRcpitemyn());
        xrcpdet.setChaoffno(xnotidet.getChaoffno());
        xrcpdet.setCusoffno(xnotidet.getCusoffno());
        xrcpdet.setPtritemname(xnotidet.getPtritemname());
        xrcpdet.setPtritemremark(xnotidet.getPtritemremark());
        xrcpdet.setPtritemorder(xnotidet.getPtritemorder());
        xrcpdet.setRcpdetst("PA03");
        xrcpdet.setMakedt(new Date());
        xrcpdet.setMaker("DamoaShinhanAgent");
        xrcpdet.setRegdt(new Date());
        xrcpdet.setChatrty(xnotidet.getChatrty());

        try {
            xrcpdet.setAdjfiregkey(this.payItem.getAdjfiregkey());
        } catch (Exception e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }
            
        }

        final ReceiptDetailsDO receiptDetails = new ReceiptDetailsDO(xrcpdet);
        receiptDetails.setPayItem(this.payItem);
        
        return receiptDetails;
    }

    private void setStatus(String status) {
        this.xnotidet.setNotidetst(status);
    }

    public Xnotidet getXnotidet() {
        return xnotidet;
    }

    public long getNoticeAmount() {
        return xnotidet.getPayitemamt();
    }

    public long getPaymentAmount() {
        long paymentAmount = 0;
        for (ReceiptDetailsDO each : receiptDetailsList) {
            if(each.isCanceled()) {
                continue;
            }
            
            paymentAmount += each.getAmount();
        }
        
        return paymentAmount;
    }

    /**
     * 납부자가 선택한 부분납 여부
     * 
     * @return
     */
    public boolean isSelected() {
        return StringUtils.equals(this.xnotidet.getPickrcpyn(), "Y");
    }
}
