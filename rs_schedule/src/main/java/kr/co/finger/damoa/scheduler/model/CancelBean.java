package kr.co.finger.damoa.scheduler.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class CancelBean {
    private String dealSeqNo;
    private String dealDate;
    private Date maked;
    private String rcpMasCd;
    private String notiMasCd;
    public CancelBean(String dealSeqNo, String dealDate) {
        this.dealSeqNo = dealSeqNo;
        this.dealDate = dealDate;
        this.maked = new Date();
    }

    public String getNotiMasCd() {
        return notiMasCd;
    }

    public void setNotiMasCd(String notiMasCd) {
        this.notiMasCd = notiMasCd;
    }

    public String getRcpMasCd() {
        return rcpMasCd;
    }

    public void setRcpMasCd(String rcpMasCd) {
        this.rcpMasCd = rcpMasCd;
    }

    public String getDealSeqNo() {
        return dealSeqNo;
    }

    public String getDealDate() {
        return dealDate;
    }

    public Date getMaked() {
        return maked;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dealSeqNo", dealSeqNo)
                .append("dealDate", dealDate)
                .append("maked", maked)
                .append("rcpMasCd", rcpMasCd)
                .append("notiMasCd", notiMasCd)
                .toString();
    }
}
