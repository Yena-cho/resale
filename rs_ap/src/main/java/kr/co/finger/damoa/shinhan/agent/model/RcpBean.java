package kr.co.finger.damoa.shinhan.agent.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RcpBean {

    private String notimasCd;
    private String notdetCd;
    private Long amount;
    private String sveCd;   //VAS (가상계좌 수납)인지 아닌지 체크
    private String rcpmasCd;
    private String rcpdetCd;

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public RcpBean(String notimasCd, String notdetCd) {
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

    public String getSveCd() {
        return sveCd;
    }

    public void setSveCd(String sveCd) {
        this.sveCd = sveCd;
    }

    public String getRcpmasCd() {
        return rcpmasCd;
    }

    public void setRcpmasCd(String rcpmasCd) {
        this.rcpmasCd = rcpmasCd;
    }

    public String getRcpdetCd() {
        return rcpdetCd;
    }

    public void setRcpdetCd(String rcpdetCd) {
        this.rcpdetCd = rcpdetCd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("notimasCd", notimasCd)
                .append("notdetCd", notdetCd)
                .append("amount", amount)
                .append("sveCd", sveCd)
                .append("rcpmasCd", rcpmasCd)
                .append("rcpdetCd", rcpdetCd)
                .toString();
    }
}
