package com.finger.shinhandamoa.sys.cashreceipt.service;

/**
 * 거래일시 상세보기 팝업 DTO
 * 
 * @author jungbna@finger.co.kr
 */
public class DetailPopUpRegDtDTO {
    private String rcpmasCd;
    private String regDt;       //거래일시
    private String payDay;      //승일일자
    private long rcpAmt;        //매출금액

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

    public long getRcpAmt() {
        return rcpAmt;
    }

    public void setRcpAmt(long rcpAmt) {
        this.rcpAmt = rcpAmt;
    }
}
