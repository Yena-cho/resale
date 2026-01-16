package com.finger.shinhandamoa.org.mypage.dto;

/**
 * 수납현황 DTO
 * 
 * @author wisehouse@finger.co.kr
 */
public class PaymentStatisticsDTO {
    private String payDay;
    private String noticeMonth;
    private String customerName;
    private String rcpName;
    private String kftcCode;
    private String bankName;
    private String payAmount;
    private String rcpAmount;

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public String getNoticeMonth() {
        return noticeMonth;
    }

    public void setNoticeMonth(String noticeMonth) {
        this.noticeMonth = noticeMonth;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRcpName() {
        return rcpName;
    }

    public void setRcpName(String rcpName) {
        this.rcpName = rcpName;
    }

    public String getKftcCode() {
        return kftcCode;
    }

    public void setKftcCode(String kftcCode) {
        this.kftcCode = kftcCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getRcpAmount() {
        return rcpAmount;
    }

    public void setRcpAmount(String rcpAmount) {
        this.rcpAmount = rcpAmount;
    }
}
