package kr.co.finger.msgio.corpinfo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Trailer {

    private String kubun= "T";	//Trailer 구분
    private String totalCount= "";	//총건수
    private String filler= "";	//filler

    public String getKubun() {
        return kubun;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public void setKubun(String kubun) {
        this.kubun = kubun;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("kubun", kubun)
                .append("totalCount", totalCount)
                .append("filler", filler)
                .toString();
    }
}
