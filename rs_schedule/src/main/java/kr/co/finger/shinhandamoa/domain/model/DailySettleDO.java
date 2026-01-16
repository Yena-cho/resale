package kr.co.finger.shinhandamoa.domain.model;

import kr.co.finger.shinhandamoa.data.domain.model.Statistics;
import kr.co.finger.shinhandamoa.data.table.model.*;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 일 정산 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class DailySettleDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailySettleDO.class);

    private final Xdaysum xdaysum;

    private DailySettleDO() {
        this(new Xdaysum());
    }

    public DailySettleDO(Xdaysum xdaysum) {
        this.xdaysum = xdaysum;
    }

    public Xdaysum getXdaysum() {
        return SerializationUtils.clone(xdaysum);
    }

    public static DailySettleDO newInstance(ClientDO client, String settleDateString, Statistics noticeStatistics, Statistics receiptStatistics, int smsCount, int lmsCount, int invoiceCount, int atCount) {
        int invoiceFee = client.getInvoiceFree() ? 0 : 50;

        final DailySettleDO instance = new DailySettleDO();
        instance.setClientId(client.getId());
        instance.setSettleDate(settleDateString);
        instance.setNoticeCount(noticeStatistics.getCount());
        instance.setNoticeAmount(noticeStatistics.getAmount());
        instance.setNoticeFee(noticeStatistics.getCount() * client.getNoticeFee());
        instance.setReceiptCount(receiptStatistics.getCount());
        instance.setReceiptAmount(receiptStatistics.getAmount());
        instance.setReceiptFee(receiptStatistics.getCount() * client.getReceiptFee());
        instance.setReceiptBankFee(receiptStatistics.getCount() * client.getReceiptBankFee());
        instance.setSmsCount(smsCount);
        instance.setSmsFee(smsCount * client.getSmsFee());
        instance.setLmsCount(lmsCount);
        instance.setLmsFee(lmsCount * client.getLmsFee());
        instance.setInvoiceCount(invoiceCount);
        instance.setInvoiceFee(invoiceCount * invoiceFee);
        instance.setCreateUser("damoa-scheduler");
        instance.setModifyUser("damoa-scheduler");
        instance.setAtCount(atCount);
        instance.setAtFee(atCount * client.getAtFee());

        return instance;
    }

    private void setInvoiceFee(long invoiceFee) {
        xdaysum.setPrnFee(invoiceFee);
    }

    private void setInvoiceCount(int invoiceCount) {
        xdaysum.setPrnCount(invoiceCount);
    }

    private void setModifyUser(String modifyUser) {
        xdaysum.setModifyUser(modifyUser);
    }

    private void setCreateUser(String createUser) {
        xdaysum.setCreateUser(createUser);
    }

    private void setLmsCount(int lmsCount) {
        xdaysum.setLmsCount(lmsCount);
    }

    private void setLmsFee(long lmsFee) {
        xdaysum.setLmsFee(lmsFee);
    }

    private void setSmsCount(int smsCount) {
        xdaysum.setSmsCount(smsCount);
    }

    private void setSmsFee(long smsFee) {
        xdaysum.setSmsFee(smsFee);
    }

    private void setReceiptAmount(long receiptAmount) {
        xdaysum.setVasAmount(receiptAmount);
    }

    private void setReceiptCount(int receiptCount) {
        xdaysum.setVasCount(receiptCount);
    }

    private void setReceiptFee(long receiptFee) {
        xdaysum.setVasFee(receiptFee);
    }

    private void setReceiptBankFee(long receiptBankFee) {
        xdaysum.setVasBankFee(receiptBankFee);
    }

    private void setNoticeAmount(long noticeAmount) {
        xdaysum.setNoticeAmount(noticeAmount);
    }

    private void setNoticeCount(int noticeCount) {
        xdaysum.setNoticeCount(noticeCount);
    }

    private void setNoticeFee(long noticeFee) {
        xdaysum.setNoticeFee(noticeFee);
    }

    private void setSettleDate(String settleDate) {
        xdaysum.setSettleDt(settleDate);
    }

    public void setClientId(String clientId) {
        xdaysum.setChacd(clientId);
    }

    private void setAtCount(int atCount) {
        xdaysum.setAtcount(atCount);
    }

    private void setAtFee(long atFee) {
        xdaysum.setAtfee(atFee);
    }
}
