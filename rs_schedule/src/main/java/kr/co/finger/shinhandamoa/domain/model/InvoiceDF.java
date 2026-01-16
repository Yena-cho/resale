package kr.co.finger.shinhandamoa.domain.model;

import kr.co.finger.shinhandamoa.data.InvoiceVF;

/**
 * 고지서 DF
 */
public class InvoiceDF {
    private InvoiceVF invoiceVF;

    public InvoiceDF() {
        invoiceVF = new InvoiceVF();
    }

    public InvoiceVF toValueFilter() {
        return invoiceVF;
    }

    public void setMasterId(String masterId) {
        invoiceVF.setMasterId(masterId);
    }

    public String getMasterId() {
        return invoiceVF.getMasterId();
    }

    public void setOrderBy(String seqNo) {
        invoiceVF.setSeqNo(seqNo);
    }

    public String getOrderBy() {
        return invoiceVF.getSeqNo();
    }
}
