package kr.co.finger.shinhandamoa.service.invoice;

import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDF;

/**
 * 고지서 신청서 SE
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceApplySF {
    private InvoiceApplyDF invoiceApplyDF;
    private String statusCode;
    private String processStatusCd;

    public InvoiceApplySF() {
        invoiceApplyDF = new InvoiceApplyDF();
    }

    public InvoiceApplyDF toDomainFilter() {
        return invoiceApplyDF;
    }

    public void setStatusCode(String statusCode) {
        this.invoiceApplyDF.setStatusCode(statusCode);
    }

    public String getStatusCode() {
        return this.invoiceApplyDF.getStatusCode();
    }

    public void setProcessStatusCd(String processStatusCd) {
        this.invoiceApplyDF.setProcessStatusCd(processStatusCd);
    }

    public String getProcessStatusCd() {
        return this.invoiceApplyDF.getProcessStatusCd();
    }

    public void setDeliveryTypeCd(String emergency) {
        this.invoiceApplyDF.setDeliveryTypeCd(emergency);
    }
}
