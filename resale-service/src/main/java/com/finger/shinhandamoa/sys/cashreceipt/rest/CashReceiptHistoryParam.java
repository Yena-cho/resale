package com.finger.shinhandamoa.sys.cashreceipt.rest;

/**
 * 현금영수증 이용내역 조회 파람
 * 
 * @author denny91@finger.co.kr
 */
public class CashReceiptHistoryParam {
    private String startDate;
    private String endDate;
    private String chaCd;
    private String chaName;
    private String dateTy;
    private String resultTy;
    private String searchGb;
    private String searchValue;
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

    public String getDateTy() {
        return dateTy;
    }

    public void setDateTy(String dateTy) {
        this.dateTy = dateTy;
    }

    public String getResultTy() {
        return resultTy;
    }

    public void setResultTy(String resultTy) {
        this.resultTy = resultTy;
    }

    public String getSearchGb() {
        return searchGb;
    }

    public void setSearchGb(String searchGb) {
        this.searchGb = searchGb;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
