package kr.co.finger.shinhandamoa.data;

import kr.co.finger.shinhandamoa.data.table.model.Xnotimasreq;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 고지서 신청서 VO
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceApplyVO {
    private Xnotimasreq xnotimasreq;

    public InvoiceApplyVO() {
        this.xnotimasreq = new Xnotimasreq();
    }

    public Xnotimasreq getXnotimasreq() {
        return xnotimasreq;
    }

    public void setXnotimasreq(Xnotimasreq xnotimasreq) {
        this.xnotimasreq = xnotimasreq;
    }

    public String getId() {
        return xnotimasreq.getNotimasreqcd();
    }

    public String getStatusCode() {
        return xnotimasreq.getReqst();
    }

    public void setStatusCode(String statusCode) {
        xnotimasreq.setReqst(statusCode);
    }

    public void setProcessStatusCd(String processStatusCd) {
        xnotimasreq.setResStsCd(processStatusCd);
    }

    public String getProcessStatusCd() {
        return xnotimasreq.getResStsCd();
    }

    public String getChaCd() {
        return xnotimasreq.getChacd();
    }

    public String getMasMonth() {
        return xnotimasreq.getMasmonth();
    }

    public String getDestinationPostalCode() {
        return xnotimasreq.getZipcode();
    }

    public String getDestinationAddress() {
        return xnotimasreq.getAddress1() + " " + xnotimasreq.getAddress2();
    }

    public String getDestinationManager() {
        return xnotimasreq.getReqname();
    }

    public String getDestinationContactNo() {
        return xnotimasreq.getReqhp();
    }

    public String getFileNo() {
        return xnotimasreq.getSendfileseq();
    }

    public String getDeliveryTypeCd() {
        return xnotimasreq.getDlvrTypeCd();
    }

    public void setSendDate(Date date) {
        xnotimasreq.setSenddt(date);
    }

    public Date getSendDate() {
        return xnotimasreq.getSenddt();
    }

    public void setProcessStatusCode(String processStatusCd) {
        xnotimasreq.setResStsCd(processStatusCd);
    }

    public Date getCreateDate() {
        return xnotimasreq.getRegdt();
    }

    public String getFileDt() {
        return xnotimasreq.getFiledt();
    }

    public void setFileNo(int fileNo) {
        xnotimasreq.setSendfileseq(new DecimalFormat("00").format(fileNo));
    }

    public void setFileDt(String fileDt) {
        xnotimasreq.setFiledt(fileDt);
    }

    public int getInvoiceCount() {
        return xnotimasreq.getSendcnt();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
