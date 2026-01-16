package kr.co.finger.msgio.corpinfo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Head {
    private String kubun= "H";	//Head구분 H
    private String corpCode= "";	//가상계좌 기관코드
    private String dealDate= "";	//거래일자
    private String filler= "";	//filler

    public String getKubun() {
        return kubun;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getFiller() {
        return filler;
    }

    public void setKubun(String kubun) {
        this.kubun = kubun;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("kubun", kubun)
                .append("corpCode", corpCode)
                .append("dealDate", dealDate)
                .append("filler", filler)
                .toString();
    }
}
