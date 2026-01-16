package com.finger.shinhandamoa.sys.cashreceipt.service;

/**
 * 기관명 상세보기 팝업 DTO
 * 
 * @author jungbna@finger.co.kr
 */
public class DetailPopUpChaNameDTO {
    private String rcpreqYn;    //발행여부
    private String rcpReqty;    //가상계좌 자동발행 여부
    private String rcpReqsvety; //수기수납 발행여부
    private String mnlrcpReqty; // 수기수납 자동발행 여부
    private String notaxYn;     //면세여부

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
}
