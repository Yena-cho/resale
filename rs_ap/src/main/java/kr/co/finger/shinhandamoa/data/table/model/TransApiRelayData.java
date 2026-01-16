package kr.co.finger.shinhandamoa.data.table.model;

import java.io.Serializable;
import java.util.Date;

public class TransApiRelayData implements Serializable {

    private String chacd;
    private String packetno;
    private String dealseqno;
    private String trcode;
    private String destination;
    private String transSuccessYn;
    private Date transdt;
    private Date updatedt;

    public String getChacd() {
        return chacd;
    }

    public void setChacd(String chacd) {
        this.chacd = chacd;
    }

    public String getPacketno() {
        return packetno;
    }

    public void setPacketno(String packetno) {
        this.packetno = packetno;
    }

    public String getDealseqno() {
        return dealseqno;
    }

    public void setDealseqno(String dealseqno) {
        this.dealseqno = dealseqno;
    }

    public String getTrcode() {
        return trcode;
    }

    public void setTrcode(String trcode) {
        this.trcode = trcode;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTransSuccessYn() {
        return transSuccessYn;
    }

    public void setTransSuccessYn(String transSuccessYn) {
        this.transSuccessYn = transSuccessYn;
    }

    public Date getTransdt() {
        return transdt;
    }

    public void setTransdt(Date transdt) {
        this.transdt = transdt;
    }

    public Date getUpdatedt() {
        return updatedt;
    }

    public void setUpdatedt(Date updatedt) {
        this.updatedt = updatedt;
    }
}