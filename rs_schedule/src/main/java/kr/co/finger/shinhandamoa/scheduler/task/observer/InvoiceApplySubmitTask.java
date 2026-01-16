package kr.co.finger.shinhandamoa.scheduler.task.observer;

import kr.co.finger.shinhandamoa.constants.InvoiceApplyProcessStatus;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplySE;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceApplySF;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceApplySO;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 고지서 신청서 발송 작업
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class InvoiceApplySubmitTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceApplySubmitTask.class);

    @Autowired
    private InvoiceService invoiceService;

    //TODO
//    @Scheduled(fixedDelayString = "${invoice-apply-submit-task.schedule.delay}")
    public void start() {
        run();
    }

    protected void run() {
        final InvoiceApplySF filter = new InvoiceApplySF();
        filter.setProcessStatusCd(InvoiceApplyProcessStatus.STAND_BY);

        final InvoiceApplySE example = new InvoiceApplySE();
        example.setProcessStatusCd(InvoiceApplyProcessStatus.WIP);

        for (;;) {
            try {
                LOGGER.debug("고지서 신청서 발송 대상 조회");
                final InvoiceApplySO each = invoiceService.pollInvoiceApply(filter, example);
                if(each == null) {
                    LOGGER.debug("고지서 신청서 발송 대상 없음");
                    break;
                }

                final String invoiceApplyId = each.getId();
                LOGGER.info("고지서 신청서 처리 [{}]", invoiceApplyId);
                invoiceService.submitInvoiceApply(invoiceApplyId);
            } catch (Throwable e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
