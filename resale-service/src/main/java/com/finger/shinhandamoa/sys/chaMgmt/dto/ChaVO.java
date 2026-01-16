package com.finger.shinhandamoa.sys.chaMgmt.dto;

/**
 * 이용기관
 * 
 * @author wisehouse@finger.co.kr
 */
public class ChaVO {
    private String chaCd;
    private String name;
    private String owner;
    private String ownerTel;
    private String chaOffNo;
    private String regDt;

    public String getChaCd() {
        return chaCd;
    }

    public void setChaCd(String chaCd) {
        this.chaCd = chaCd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getChaOffNo() {
        return chaOffNo;
    }

    public void setChaOffNo(String chaOffNo) {
        this.chaOffNo = chaOffNo;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }
}
