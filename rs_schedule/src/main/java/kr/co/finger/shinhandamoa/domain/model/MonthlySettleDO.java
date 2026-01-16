package kr.co.finger.shinhandamoa.domain.model;

import kr.co.finger.shinhandamoa.data.domain.model.Statistics;
import kr.co.finger.shinhandamoa.data.table.model.Xmonthsum;
import kr.co.finger.shinhandamoa.domain.DomainException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 월 정산 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class MonthlySettleDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlySettleDO.class);
    
    private Xmonthsum xmonthsum;

    public MonthlySettleDO(Xmonthsum xmonthsum) {
        this.xmonthsum = xmonthsum;
    }
    
    public MonthlySettleDO() {
        this(new Xmonthsum());
        this.setClose(false);
        this.setWithdrawStatus("OST01");
    }
    
    public static final MonthlySettleDO newInstance(ClientDO clientDO, String month, Statistics notice, Statistics receipt, int smsCount, int lmsCount, int invoiceCount, int atCount) {
        final long noticeFee = clientDO.getNoticeFee();
        final long noticeBaseCharge = clientDO.getNoticeBaseCharge();
        final long noticeBaseCount = clientDO.getNoticeBaseCount();
        final long receiptFee = clientDO.getReceiptFee();
        final long receiptBankFee = clientDO.getReceiptBankFee();
        final long smsFee = clientDO.getSmsFee();
        final long lmsFee = clientDO.getLmsFee();
        final long invoiceFee = clientDO.getInvoiceFree() ? 0 : 50;
        // TODO 알림톡 가격 확인
        final long atFee = clientDO.getAtFee();
        
        final MonthlySettleDO monthlySettleDO = new MonthlySettleDO();
        
        monthlySettleDO.setClientId(clientDO.getId());
        monthlySettleDO.setMonth(month);
        monthlySettleDO.setNoticeCount(notice.getCount());
        monthlySettleDO.setNoticeAmount(notice.getAmount());
        if(notice.getCount() == 0) {
            monthlySettleDO.setNoticeFee(0);
        } else {
            monthlySettleDO.setNoticeFee(noticeBaseCharge + Math.max(0, notice.getCount()-noticeBaseCount) * noticeFee);
        }
        monthlySettleDO.setReceiptCount(receipt.getCount());
        monthlySettleDO.setReceiptAmount(receipt.getAmount());
        monthlySettleDO.setReceiptFee(receipt.getCount() *receiptFee);
        monthlySettleDO.setReceiptBankFee(receipt.getCount() *receiptBankFee);
        monthlySettleDO.setSmsCount(smsCount);
        monthlySettleDO.setSmsFee(smsCount * smsFee);
        monthlySettleDO.setLmsCount(lmsCount);
        monthlySettleDO.setLmsFee(lmsCount * lmsFee);
        monthlySettleDO.setInvoiceCount(invoiceCount);
        monthlySettleDO.setInvoiceFee(invoiceCount * invoiceFee);
        monthlySettleDO.setAtCount(atCount);
        monthlySettleDO.setAtFee(atCount * atFee);

        return monthlySettleDO;

    }

    private void setInvoiceCount(int invoiceCount) {
        this.xmonthsum.setPrncnt(invoiceCount);
    }

    private void setInvoiceFee(long invoiceFee) {
        this.xmonthsum.setPrnfee(invoiceFee);
    }

    private void setWithdrawStatus(String withdrawStatus) {
        this.xmonthsum.setRcpSt(withdrawStatus);
    }

    private void setLmsFee(long lmsFee) {
        this.xmonthsum.setLmsfee(lmsFee);
    }

    private void setLmsCount(int lmsCount) {
        this.xmonthsum.setLmscnt(lmsCount);
    }

    private void setSmsFee(long smsFee) {
        this.xmonthsum.setSmsfee(smsFee);
    }

    private void setSmsCount(int smsCount) {
        this.xmonthsum.setSmscnt(smsCount);
    }

    private void setAtFee(long atFee) {
        this.xmonthsum.setAtfee(atFee);
    }

    private void setAtCount(int atCount) {
        this.xmonthsum.setAtcnt(atCount);
    }

    private void setReceiptBankFee(long receiptBankFee) {
        this.xmonthsum.setRcpbnkfee(receiptBankFee);
    }

    private void setNoticeFee(long noticeFee) {
        this.xmonthsum.setNotifee(noticeFee);
    }

    private void setNoticeAmount(long noticeAmount) {
        this.xmonthsum.setNotiamt(noticeAmount);
    }

    private void setNoticeCount(int noticeCount) {
        this.xmonthsum.setNoticnt(noticeCount);
    }

    private void setClientId(String clientId) {
        this.xmonthsum.setChacd(clientId);
    }
    
    private void setMonth(String month) {
        this.xmonthsum.setMonth(month);
    }

    private void setReceiptCount(int receiptCount) {
        this.xmonthsum.setRcpcnt(receiptCount);
    }
    
    private void setReceiptAmount(long receiptAmount) {
        this.xmonthsum.setRcpamt(receiptAmount);
    }
    
    private void setReceiptFee(long receiptFee) {
        this.xmonthsum.setRcpfee(receiptFee);
    }
    
    private void setClose(boolean close) {
        this.xmonthsum.setFinishyn(close ? "Y" : "N");
    }
    
    private boolean getClose() {
        return StringUtils.equalsIgnoreCase(this.xmonthsum.getFinishyn(), "Y");
    }
    
    private void setCloseDate(Date date) {
        this.xmonthsum.setFinishDt(new SimpleDateFormat("yyyyMMdd").format(date));
    }

    public Xmonthsum getXmonthsum() {
        return xmonthsum;
    }
    
    public void close() throws DomainException {
        if(getClose()) {
            throw new DomainException();
        }
        setClose(true);
        setCloseDate(new Date());
    }
}
