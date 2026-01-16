package kr.co.finger.damoa.scheduler.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AggregateBean {

    private int totalCount;
    private long totalAmount;
    private int cancelCount;
    private long cancelAmount;
    private int feeCount;
    private long feeAmount;


    public AggregateBean(int totalCount, long totalAmount, int cancelCount, long cancelAmount, int feeCount, long feeAmount) {
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
        this.cancelCount = cancelCount;
        this.cancelAmount = cancelAmount;
        this.feeCount = feeCount;
        this.feeAmount = feeAmount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public long getCancelAmount() {
        return cancelAmount;
    }

    public int getFeeCount() {
        return feeCount;
    }

    public long getFeeAmount() {
        return feeAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("totalCount", totalCount)
                .append("totalAmount", totalAmount)
                .append("cancelCount", cancelCount)
                .append("cancelAmount", cancelAmount)
                .append("feeCount", feeCount)
                .append("feeAmount", feeAmount)
                .toString();
    }
}
