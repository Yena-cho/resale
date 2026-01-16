package kr.co.finger.shinhandamoa.scheduler.task.daily;

import kr.co.finger.shinhandamoa.constants.InvoiceApplyDeliveryType;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyProcessStatus;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyStatus;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceApplySF;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 고지서 신청 : 긴급
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class NormalInvoiceApplyStartTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(NormalInvoiceApplyStartTask.class);

    @Autowired
    private InvoiceService invoiceService;

    //TODO
//    @Scheduled(cron = "${normal-invoice-apply-start-task.schedule.cron}")
    public void start() {
        run();
    }

    protected void run() {
        LOGGER.debug("시작 : 고지서 출력 의뢰 (택배) 시작 작업 ");
        final InvoiceApplySF invoiceApplySF = new InvoiceApplySF();
        invoiceApplySF.setDeliveryTypeCd(InvoiceApplyDeliveryType.NORMAL);
        invoiceApplySF.setStatusCode(InvoiceApplyStatus.WIP);
        invoiceApplySF.setProcessStatusCd(InvoiceApplyProcessStatus.CREATED);

        invoiceService.startInvoiceApply(invoiceApplySF);
        LOGGER.debug("종료 : 고지서 출력 의뢰 (택배) 시작 작업 ");
    }
}
