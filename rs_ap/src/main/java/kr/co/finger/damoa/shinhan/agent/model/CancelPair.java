package kr.co.finger.damoa.shinhan.agent.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CancelPair {
    private String rcpMasCd;
    private String notiMasCd;

    public CancelPair(String rcpMasCd, String notiMasCd) {
        this.rcpMasCd = rcpMasCd;
        this.notiMasCd = notiMasCd;
    }

    public String getRcpMasCd() {
        return rcpMasCd;
    }

    public String getNotiMasCd() {
        return notiMasCd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("rcpMasCd", rcpMasCd)
                .append("notiMasCd", notiMasCd)
                .toString();
    }
}
