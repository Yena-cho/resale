package kr.co.finger.shinhandamoa.service.invoice;

import groovy.transform.ToString;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 고지서 신청서 SO
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceApplySO {
    private InvoiceApplyDO invoiceApplyDO;

    public InvoiceApplySO(InvoiceApplyDO invoiceApplyDO) {
        this.invoiceApplyDO = invoiceApplyDO;
    }

    public String getId() {
        return invoiceApplyDO.getId();
    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toStringBuilder.append("id", invoiceApplyDO.getId());

        return toStringBuilder.build();
    }
}
