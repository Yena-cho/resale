package com.finger.shinhandamoa.org.receiptmgmt.dto;

/**
 * 납입증명서 상세
 *
 * @author wisehouse@finger.co.kr
 * @author mljeong@finger.co.kr
 */
public class PdfReceiptDetailsDTO {
    private String paymentMonth;
    private Long rcpAmount;

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public Long getRcpAmount() {
        return rcpAmount;
    }

    public void setRcpAmount(Long rcpAmount) {
        this.rcpAmount = rcpAmount;
    }
}
