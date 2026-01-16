package kr.co.finger.damoa.scheduler.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NotiDetBean {

    private String notimasCd;
    private String notdetCd;
    private Long amount;

    private String beforeState;
    private String afterState;

    private String masState;

    public String getMasState() {
        return masState;
    }

    public void setMasState(String masState) {
        this.masState = masState;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }


    public String getBeforeState() {
        return beforeState;
    }

    public void setBeforeState(String beforeState) {
        this.beforeState = beforeState;
    }

    public String getAfterState() {
        return afterState;
    }

    public void setAfterState(String afterState) {
        this.afterState = afterState;
    }

    public NotiDetBean(String notimasCd, String notdetCd) {
        this.notimasCd = notimasCd;
        this.notdetCd = notdetCd;
    }

    public Long getAmount() {
        return amount;
    }

    public String getNotdetCd() {
        return notdetCd;
    }

    public String getNotimasCd() {
        return notimasCd;
    }


    public String notimasDetCd() {
        return notimasCd + "_" + notdetCd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("notimasCd", notimasCd)
                .append("notdetCd", notdetCd)
                .append("amount", amount)
                .toString();
    }
}
