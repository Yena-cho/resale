package com.finger.shinhandamoa.sys.cashreceipt.service;

import java.util.Date;

/**
 * 현금영수증번호 상세보기 팝업 DTO
 * 
 * @author jungbna@finger.co.kr
 */
public class DetailPopUpCashmasCdDTO {
    private String cusoffNo;    //발급번호
    private String confirm;     //발급번호구분
    private String cusType;     //발급자구분
    private long rcpAmt;        //매출금액
    private long tax;           //부과세액
    private long tip;           //서비스 금액
    private String appDay;      //승일일자
    private String appNo;       //승인번호
    private String cashMasst;   //상태
    private String maker;       //마지막 조작자
    private Date makeDt;        //마지막 조작일시

    public String getCusoffNo() {
        return cusoffNo;
    }

    public void setCusoffNo(String cusoffNo) {
        this.cusoffNo = cusoffNo;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public long getRcpAmt() {
        return rcpAmt;
    }

    public void setRcpAmt(long rcpAmt) {
        this.rcpAmt = rcpAmt;
    }

    public long getTax() {
        return tax;
    }

    public void setTax(long tax) {
        this.tax = tax;
    }

    public long getTip() {
        return tip;
    }

    public void setTip(long tip) {
        this.tip = tip;
    }

    public String getAppDay() {
        return appDay;
    }

    public void setAppDay(String appDay) {
        this.appDay = appDay;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getCashMasst() {
        return cashMasst;
    }

    public void setCashMasst(String cashMasst) {
        this.cashMasst = cashMasst;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public Date getMakeDt() {
        return makeDt;
    }

    public void setMakeDt(Date makeDt) {
        this.makeDt = makeDt;
    }
}
