package kr.co.finger.shinhandamoa.domain.model;

import com.finger.shinhandamoa.data.table.model.Cha;
import com.finger.shinhandamoa.data.table.model.Xdaysum;
import kr.co.finger.shinhandamoa.common.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 이용기관 일 정산 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class DailyClientSettleDO {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyClientSettleDO.class);
    
    private Cha cha;
    private Xdaysum xdaysum;

    public DailyClientSettleDO(Cha cha, Xdaysum xdaysum) {
        this.cha = cha;
        this.xdaysum = xdaysum;
    }

    public String getFgcd() { return cha.getFgcd(); }

    public String getClientId() {
        return cha.getChacd();
    }

    public Date getSettleDate() {
        return DateUtils.parseDate(xdaysum.getSettleDt(), "yyyyMMdd", null);
    }

    public int getNoticeCount() {
        return xdaysum.getNoticeCount();
    }

    public long getNoticeAmount() {
        return xdaysum.getNoticeAmount();
    }

    public long getNoticeFee() {
        return xdaysum.getNoticeFee();
    }

    public int getVaPaymentCount() {
        return xdaysum.getVasCount();
    }

    public long getVaPaymentAmount() {
        return xdaysum.getVasAmount();
    }

    public long getVaPaymentFee() {
        return xdaysum.getVasFee();
    }
    
    public long getVaBankFee() {
        return xdaysum.getVasBankFee();
    }

    public int getSmsCount() {
        return xdaysum.getSmsCount();
    }

    public long getSmsFee() {
        return xdaysum.getSmsFee();
    }

    public int getLmsCount() {
        return xdaysum.getLmsCount();
    }

    public long getPrnFee() {
        return xdaysum.getPrnFee();
    }

    public int getPrnCount() { return xdaysum.getPrnCount(); }

    public long getLmsFee() {
        return xdaysum.getLmsFee();
    }

    public static DailyClientSettleDO getInstance(Cha cha, Xdaysum xdaysum) {
        return new DailyClientSettleDO(cha, xdaysum);
    }

    public String getClientName() {
        return cha.getChaname();
    }

    public int getAtCount() {
        return xdaysum.getAtcount();
    }

    public long getAtFee() {
        return xdaysum.getAtfee();
    }
}
