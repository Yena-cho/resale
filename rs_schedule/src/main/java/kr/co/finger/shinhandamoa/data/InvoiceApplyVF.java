package kr.co.finger.shinhandamoa.data;

/**
 * 고지서 신청서 VF
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceApplyVF {
    private String deliveryTypeCd;
    private String statusCode;
    private String processStatusCd;
    private String fileDt;
    private String fileDtFrom;
    private String fileDtTo;
    private String clientId;

    private String orderBy;

    public String getDeliveryTypeCd() {
        return deliveryTypeCd;
    }

    public void setDeliveryTypeCd(String deliveryTypeCd) {
        this.deliveryTypeCd = deliveryTypeCd;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getProcessStatusCd() {
        return processStatusCd;
    }

    public void setProcessStatusCd(String processStatusCd) {
        this.processStatusCd = processStatusCd;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setFileDt(String fileDt) {
        this.fileDt = fileDt;
    }

    public String getFileDt() {
        return fileDt;
    }

    public void setFileDtFrom(String fileDtFrom) {
        this.fileDtFrom = fileDtFrom;
    }

    public String getFileDtFrom() {
        return fileDtFrom;
    }

    public void setFileDtTo(String fileDtTo) {
        this.fileDtTo = fileDtTo;
    }

    public String getFileDtTo() {
        return fileDtTo;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}
