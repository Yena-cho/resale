package kr.co.finger.shinhandamoa.domain.model;

/**
 *  고지서 신청서 SE
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceApplySE {
    private InvoiceApplyDE de;

    public InvoiceApplySE() {
        this.de = new InvoiceApplyDE();
    }

    public String getStatusCode() {
        return de.getStatusCode();
    }

    public void setStatusCode(String statusCode) {
        de.setStatusCode(statusCode);
    }

    public String getProcessStatusCd() {
        return de.getProcessStatusCd();
    }

    public void setProcessStatusCd(String processStatusCd) {
        de.setProcessStatusCd(processStatusCd);
    }

    public InvoiceApplyDE toDomainExample() {
        return de;
    }
}
