package com.finger.shinhandamoa.sys.cashreceipt.rest;

/**
 * 수납별 현금영수증 발행 현황 조회 파라미터
 * 
 * @author jungbna@finger.co.kr
 */
public class CashReceiptStatusByReceiptParam {
    private String chaCd; //기관코드
    private String chaName; //기관명
    private String startDate; //수납일자
    private String endDate; //수납일자
    private String startDate2; //거래일자
    private String endDate2; //거래일자
    private String sveCd; //수납방법
    private String createCashReceipt; //현금영수증 데이터 생성 여부
    private String notIssuedErrorCashReceipt; //현금영수증 미발급 오류 여부
    private String orderBy;

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

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getSveCd() {
        return sveCd;
    }

    public void setSveCd(String sveCd) {
        this.sveCd = sveCd;
    }

    public String getStartDate2() {
        return startDate2;
    }

    public void setStartDate2(String startDate2) {
        this.startDate2 = startDate2;
    }

    public String getEndDate2() {
        return endDate2;
    }

    public void setEndDate2(String endDate2) {
        this.endDate2 = endDate2;
    }

    public String getCreateCashReceipt() {
        return createCashReceipt;
    }

    public void setCreateCashReceipt(String createCashReceipt) {
        this.createCashReceipt = createCashReceipt;
    }

    public String getNotIssuedErrorCashReceipt() {
        return notIssuedErrorCashReceipt;
    }

    public void setNotIssuedErrorCashReceipt(String notIssuedErrorCashReceipt) {
        this.notIssuedErrorCashReceipt = notIssuedErrorCashReceipt;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
