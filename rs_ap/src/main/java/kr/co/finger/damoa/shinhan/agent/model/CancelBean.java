package kr.co.finger.damoa.shinhan.agent.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CancelBean {
    private String dealSeqNo;
    private String dealDate;
    private Date maked;

    private List<CancelPair> cancelPairs = new ArrayList<>();
    public CancelBean(String dealSeqNo, String dealDate) {
        this.dealSeqNo = dealSeqNo;
        this.dealDate = dealDate;
        this.maked = new Date();
    }

    public List<CancelPair> getCancelPairs() {
        return cancelPairs;
    }

    public void add(String rcpMasCd,String notiMasCd) {
        cancelPairs.add(new CancelPair(rcpMasCd, notiMasCd));
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
                .append("cancelPairs", cancelPairs)
                .toString();
    }
}
