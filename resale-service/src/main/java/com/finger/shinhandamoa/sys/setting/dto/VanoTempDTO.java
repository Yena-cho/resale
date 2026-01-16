package com.finger.shinhandamoa.sys.setting.dto;

public class VanoTempDTO {

    private String fgcd; //은행코드
    private String ficd;
    private String vano; //가상계좌번호
    private String fitxcd;
    private String chacd; //기관코드



    public String getFgcd() {
        return fgcd;
    }

    public void setFgcd(String fgcd) {
        this.fgcd = fgcd;
    }

    public String getFicd() {
        return ficd;
    }

    public void setFicd(String ficd) {
        this.ficd = ficd;
    }

    public String getVano() {
        return vano;
    }

    public void setVano(String vano) {
        this.vano = vano;
    }

    public String getFitxcd() {
        return fitxcd;
    }

    public void setFitxcd(String fitxcd) {
        this.fitxcd = fitxcd;
    }

    public String getChacd() {
        return chacd;
    }

    public void setChacd(String chacd) {
        this.chacd = chacd;
    }
}
