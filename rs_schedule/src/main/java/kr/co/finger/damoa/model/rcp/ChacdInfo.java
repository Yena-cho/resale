package kr.co.finger.damoa.model.rcp;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChacdInfo {
    private boolean doDateCheck;
    private boolean doAmountCheck;
    private String chast;
    private String chatrty;
    private String adjaccYn;
    private String chaName;

    //CUSNAMETY
    private String cusNameTy;

    public ChacdInfo(boolean doDateCheck, boolean doAmountCheck, String chast, String chatrty, String adjaccYn) {
        this.doDateCheck = doDateCheck;
        this.doAmountCheck = doAmountCheck;
        this.chast = chast;
        this.chatrty = chatrty;
        this.adjaccYn = adjaccYn;
    }

    public ChacdInfo(boolean doDateCheck, boolean doAmountCheck) {
        this.doDateCheck = doDateCheck;
        this.doAmountCheck = doAmountCheck;
    }

    public String getCusNameTy() {
        return cusNameTy;
    }

    public void setCusNameTy(String cusNameTy) {
        this.cusNameTy = cusNameTy;
    }

    public String getAdjaccYn() {
        return adjaccYn;
    }

    public void setAdjaccYn(String adjaccYn) {
        this.adjaccYn = adjaccYn;
    }

    public String getChaName() {
        return chaName;
    }

    public void setChaName(String chaName) {
        this.chaName = chaName;
    }

    public String getChast() {
        return chast;
    }

    public String getChatrty() {
        return chatrty;
    }

    public boolean isDoDateCheck() {
        return doDateCheck;
    }

    public boolean isDoAmountCheck() {
        return doAmountCheck;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("doDateCheck", doDateCheck)
                .append("doAmountCheck", doAmountCheck)
                .append("chast", chast)
                .append("chatrty", chatrty)
                .append("adjaccYn", adjaccYn)
                .append("chaName", chaName)
                .append("cusNameTy", cusNameTy)
                .toString();
    }
}
