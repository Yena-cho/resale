package com.finger.shinhandamoa.sys.cashreceipt.rest;

/**
 * 현금영수증 발행 현황 조회 파라미터
 * 
 * @author jungbna@finger.co.kr
 */
public class CashReceiptStatusByIssueParam {
    private String chaCd; //기관코드
    private String chaName; //기관명
    private String startDate; //수납일자
    private String endDate; //수납일자
    private String startDate2; //거래일자
    private String endDate2; //거래일자
    private String startDate3; //요청일자
    private String endDate3; //요청일자
    private String sveCd; //수납방법
    private String createErrorCashReceipt; //현금영수증 발행 데이터 오류 여부
    private String notIssuedErrorCashReceipt; //현금영수증 미발급 오류 여부
    private String duplicationIssuedErrorCashReceipt; //현금영수증 중복발행 오류 여부
    private String orderBy;

    public String getStartDate3() {
        return startDate3;
    }

    public void setStartDate3(String startDate3) {
        this.startDate3 = startDate3;
    }

    public String getEndDate3() {
        return endDate3;
    }

    public void setEndDate3(String endDate3) {
        this.endDate3 = endDate3;
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

    public String getCreateErrorCashReceipt() {
        return createErrorCashReceipt;
    }

    public void setCreateErrorCashReceipt(String createErrorCashReceipt) {
        this.createErrorCashReceipt = createErrorCashReceipt;
    }

    public String getDuplicationIssuedErrorCashReceipt() {
        return duplicationIssuedErrorCashReceipt;
    }

    public void setDuplicationIssuedErrorCashReceipt(String duplicationIssuedErrorCashReceipt) {
        this.duplicationIssuedErrorCashReceipt = duplicationIssuedErrorCashReceipt;
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
