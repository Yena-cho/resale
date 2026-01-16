package com.finger.shinhandamoa.sys.cashreceipt.rest;

/**
 * 이용기관별 현금영수증 발행 현황 조회 파라미터
 * 
 * @author wisehouse@finger.co.kr
 */
public class CashReceiptStatusByClientParam {
    private String startDate;
    private String endDate;
    private String clientId;
    private String clientName;
    private String status;
    private String enableCashReceipt;
    private String enableAutomaticCashReceipt;
    private String mandRcpYn;
    private String orderBy;

    public String getMandRcpYn() {
        return mandRcpYn;
    }

    public void setMandRcpYn(String mandRcpYn) {
        this.mandRcpYn = mandRcpYn;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnableCashReceipt() {
        return enableCashReceipt;
    }

    public void setEnableCashReceipt(String enableCashReceipt) {
        this.enableCashReceipt = enableCashReceipt;
    }

    public String getEnableAutomaticCashReceipt() {
        return enableAutomaticCashReceipt;
    }

    public void setEnableAutomaticCashReceipt(String enableAutomaticCashReceipt) {
        this.enableAutomaticCashReceipt = enableAutomaticCashReceipt;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
