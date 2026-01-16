package kr.co.finger.damoa.model.corp;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
@Deprecated
public class Head implements Serializable {
    private String kubun = "H";
    private String corpCode;
    private String dealDate; // 8
    private String filler;

    public String getKubun() {
        return kubun;
    }

    public void setKubun(String kubun) {
        this.kubun = kubun;
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
