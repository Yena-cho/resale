package kr.co.finger.shinhandamoa.domain.model;

import kr.co.finger.shinhandamoa.data.InvoiceApplyVF;

/**
 * 고지서 신청서 DE
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceApplyDF {
    private InvoiceApplyVF invoiceApplyVF;

    public InvoiceApplyDF() {
        this.invoiceApplyVF = new InvoiceApplyVF();
    }

    public String getDeliveryTypeCd() {
        return invoiceApplyVF.getDeliveryTypeCd();
    }

    public void setDeliveryTypeCd(String deliveryTypeCd) {
        this.invoiceApplyVF.setDeliveryTypeCd(deliveryTypeCd);
    }

    public String getStatusCode() {
        return this.invoiceApplyVF.getStatusCode();
    }

    public void setStatusCode(String statusCode) {
        this.invoiceApplyVF.setStatusCode(statusCode);
    }

    public String getProcessStatusCd() {
        return this.invoiceApplyVF.getProcessStatusCd();
    }

    public void setProcessStatusCd(String processStatusCd) {
        this.invoiceApplyVF.setProcessStatusCd(processStatusCd);
    }

    public String getOrderBy() {
        return this.invoiceApplyVF.getOrderBy();
    }

    public void setOrderBy(String orderBy) {
        this.invoiceApplyVF.setOrderBy(orderBy);
    }

    public InvoiceApplyVF toValueFilter() {
        return invoiceApplyVF;
    }

    public void setFileDt(String fileDt) {
        invoiceApplyVF.setFileDt(fileDt);
    }

    public void setFileDtFrom(String fileDtFrom) {
        this.invoiceApplyVF.setFileDtFrom(fileDtFrom);
    }

    public void setFileDtTo(String fileDtTo) {
        this.invoiceApplyVF.setFileDtTo(fileDtTo);
    }

    public void setClientId(String clientId) {
        this.invoiceApplyVF.setClientId(clientId);
    }
}
