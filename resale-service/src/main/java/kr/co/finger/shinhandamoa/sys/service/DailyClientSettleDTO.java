package kr.co.finger.shinhandamoa.sys.service;

import kr.co.finger.shinhandamoa.domain.model.DailyClientSettleDO;

import java.io.Serializable;
import java.util.Date;

/**
 * 이용기관 일 정산 DTO
 * 
 * @author wisehouse@finger.co.kr
 */
public class DailyClientSettleDTO implements Serializable {
    private String clientId;
    private String clientName;
    private Date settleDate;
    private Integer noticeCount;
    private Long noticeAmount;
    private Long noticeFee;
    private Integer vaPaymentCount;
    private Long vaPaymentAmount;
    private Long vaPaymentFee;
    private Long vaBankFee;
    private Integer smsCount;
    private Long smsFee;
    private Integer lmsCount;
    private Long lmsFee;
    private Integer prnCount;
    private Long prnFee;
    private Integer atCount;
    private Long atFee;
    private String fgcd;

    /**
     * {@link DailyClientSettleDO}로 DTO 생성
     * 
     */
    public final static DailyClientSettleDTO newInstance(DailyClientSettleDO dailyClientSettleDO) {
        final DailyClientSettleDTO instance = new DailyClientSettleDTO();
        instance.setClientId(dailyClientSettleDO.getClientId());
        instance.setClientName(dailyClientSettleDO.getClientName());
        instance.setSettleDate(dailyClientSettleDO.getSettleDate());
        instance.setNoticeCount(dailyClientSettleDO.getNoticeCount());
        instance.setNoticeAmount(dailyClientSettleDO.getNoticeAmount());
        instance.setNoticeFee(dailyClientSettleDO.getNoticeFee());
        instance.setVaPaymentCount(dailyClientSettleDO.getVaPaymentCount());
        instance.setVaPaymentAmount(dailyClientSettleDO.getVaPaymentAmount());
        instance.setVaPaymentFee(dailyClientSettleDO.getVaPaymentFee());
        instance.setVaBankFee(dailyClientSettleDO.getVaBankFee());
        instance.setSmsCount(dailyClientSettleDO.getSmsCount());
        instance.setSmsFee(dailyClientSettleDO.getSmsFee());
        instance.setLmsCount(dailyClientSettleDO.getLmsCount());
        instance.setLmsFee(dailyClientSettleDO.getLmsFee());
        instance.setPrnCount(dailyClientSettleDO.getPrnCount());
        instance.setPrnFee(dailyClientSettleDO.getPrnFee());
        instance.setAtCount(dailyClientSettleDO.getAtCount());
        instance.setAtFee(dailyClientSettleDO.getAtFee());
        instance.setFgcd(dailyClientSettleDO.getFgcd());

        return instance;
    }

    public String getFgcd() {
        return fgcd;
    }

    public void setFgcd(String fgcd) {
        this.fgcd = fgcd;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    public Integer getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(Integer noticeCount) {
        this.noticeCount = noticeCount;
    }

    public Long getNoticeAmount() {
        return noticeAmount;
    }

    public void setNoticeAmount(Long noticeAmount) {
        this.noticeAmount = noticeAmount;
    }

    public Long getNoticeFee() {
        return noticeFee;
    }

    public void setNoticeFee(Long noticeFee) {
        this.noticeFee = noticeFee;
    }

    public Integer getVaPaymentCount() {
        return vaPaymentCount;
    }

    public void setVaPaymentCount(Integer vaPaymentCount) {
        this.vaPaymentCount = vaPaymentCount;
    }

    public Long getVaPaymentAmount() {
        return vaPaymentAmount;
    }

    public void setVaPaymentAmount(Long vaPaymentAmount) {
        this.vaPaymentAmount = vaPaymentAmount;
    }

    public Long getVaPaymentFee() {
        return vaPaymentFee;
    }

    public void setVaPaymentFee(Long vaPaymentFee) {
        this.vaPaymentFee = vaPaymentFee;
    }

    public Long getVaBankFee() {
        return vaBankFee;
    }

    public void setVaBankFee(Long vaBankFee) {
        this.vaBankFee = vaBankFee;
    }

    public Integer getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }

    public Long getSmsFee() {
        return smsFee;
    }

    public void setSmsFee(Long smsFee) {
        this.smsFee = smsFee;
    }

    public Integer getLmsCount() {
        return lmsCount;
    }

    public void setLmsCount(Integer lmsCount) {
        this.lmsCount = lmsCount;
    }

    public Long getLmsFee() {
        return lmsFee;
    }

    public void setLmsFee(Long lmsFee) {
        this.lmsFee = lmsFee;
    }

    public Integer getPrnCount() {
        return prnCount;
    }

    public void setPrnCount(Integer prnCount) {
        this.prnCount = prnCount;
    }

    public Long getPrnFee() {
        return prnFee;
    }

    public void setPrnFee(Long prnFee) {
        this.prnFee = prnFee;
    }

    public Integer getAtCount() {
        return atCount;
    }

    public void setAtCount(Integer atCount) {
        this.atCount = atCount;
    }

    public Long getAtFee() {
        return atFee;
    }

    public void setAtFee(Long atFee) {
        this.atFee = atFee;
    }
}
