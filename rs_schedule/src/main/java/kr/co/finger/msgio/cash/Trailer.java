package kr.co.finger.msgio.cash;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Trailer {

    private String trailerType= "30";	//Trailer 구분
    private String totalCount= "";	//총건수 = 승인 + 취소
    private String totalAmount= "";	//총금액 = 승인 + 취소
    private String filler= "";	//filler

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("trailerType", trailerType)
                .append("totalCount", totalCount)
                .append("totalAmount", totalAmount)
                .append("filler", filler)
                .toString();
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }
}
