package com.finger.shinhandamoa.sys.cashreceipt.service;

import java.util.Date;

/**
 * 수납별 현황 DTO
 * 
 * @author jungbna@finger.co.kr
 */
public class ReceiptWithCashReceiptStatusDTO {
    private String chaCd; //기관코드
    private String chaName; //기관명
    private String rcpmasCd; //거래번호
    private String regDt; //거래일시
    private String payDay; //수납일자
    private String rcpmasSt; //수납상태

    private String rcpreqYn; //발행여부
    private String rcpReqty; //가상계좌 자동발행 여부
    private String rcpReqsvety; //수기수납 발행여부
    private String mnlrcpReqty; // 수기수납 자동발행 여부
    private String notaxYn; //면세여부

    private String cashmasCd; //현금영수증번호
    private String cashMasst; //상태
    private String appDay; //승일일자
    private String appNo; //승인번호
    private long rcpAmt; //매출금액
    private long tax; //세금
    private long tip; //서비스
    private String cusoffNo; //발급번호
    private String confirm; //발급번호구분
    private String cusType; //발급자구분
    private String maker; //마지막 조작자
    private Date makeDt; //마지막 조작일시

    public String getRcpmasSt() {
        return rcpmasSt;
    }

    public void setRcpmasSt(String rcpmasSt) {
        this.rcpmasSt = rcpmasSt;
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

    public String getRcpmasCd() {
        return rcpmasCd;
    }

    public void setRcpmasCd(String rcpmasCd) {
        this.rcpmasCd = rcpmasCd;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public String getRcpreqYn() {
        return rcpreqYn;
    }

    public void setRcpreqYn(String rcpreqYn) {
        this.rcpreqYn = rcpreqYn;
    }

    public String getRcpReqty() {
        return rcpReqty;
    }

    public void setRcpReqty(String rcpReqty) {
        this.rcpReqty = rcpReqty;
    }

    public String getRcpReqsvety() {
        return rcpReqsvety;
    }

    public void setRcpReqsvety(String rcpReqsvety) {
        this.rcpReqsvety = rcpReqsvety;
    }

    public String getMnlrcpReqty() {
        return mnlrcpReqty;
    }

    public void setMnlrcpReqty(String mnlrcpReqty) {
        this.mnlrcpReqty = mnlrcpReqty;
    }

    public String getNotaxYn() {
        return notaxYn;
    }

    public void setNotaxYn(String notaxYn) {
        this.notaxYn = notaxYn;
    }

    public String getCashmasCd() {
        return cashmasCd;
    }

    public void setCashmasCd(String cashmasCd) {
        this.cashmasCd = cashmasCd;
    }

    public String getCashMasst() {
        return cashMasst;
    }

    public void setCashMasst(String cashMasst) {
        this.cashMasst = cashMasst;
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
