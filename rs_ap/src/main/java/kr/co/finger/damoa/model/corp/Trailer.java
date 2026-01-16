package kr.co.finger.damoa.model.corp;

import org.apache.commons.lang3.builder.ToStringBuilder;
@Deprecated
public class Trailer {
    private String kubun = "T";
    private String totalCount;
    private String filler;

    public String getKubun() {
        return kubun;
    }

    public void setKubun(String kubun) {
        this.kubun = kubun;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("kubun", kubun)
                .append("totalCount", totalCount)
                .append("filler", filler)
                .toString();
    }
}
