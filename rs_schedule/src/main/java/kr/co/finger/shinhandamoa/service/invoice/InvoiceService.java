package kr.co.finger.shinhandamoa.service.invoice;

import kr.co.finger.shinhandamoa.adapter.bbtec.BbtecFileServerAdapter;
import kr.co.finger.shinhandamoa.adapter.bbtec.InvoiceSam;
import kr.co.finger.shinhandamoa.adapter.bbtec.InvoiceSamBody;
import kr.co.finger.shinhandamoa.adapter.fim.FingerIntegrateMessageAdapter;
import kr.co.finger.shinhandamoa.common.Progress;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyProcessStatus;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyStatus;
import kr.co.finger.shinhandamoa.constants.InvoiceOrder;
import kr.co.finger.shinhandamoa.domain.model.*;
import kr.co.finger.shinhandamoa.domain.repository.InvoiceApplyRepository;
import kr.co.finger.shinhandamoa.domain.repository.InvoiceRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 고지서 서비스
 *
 * @author wisehouse@finger.co.kr
 */
@Service
public class InvoiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private InvoiceApplyRepository invoiceApplyRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceHelper invoiceHelper;

    @Autowired
    private BbtecFileServerAdapter bbtecFileServerAdapter;

    @Autowired
    private FingerIntegrateMessageAdapter fingerIntegrateMessageAdapter;

    @Value("${damoa.contact-no}")
    private String damoaContactNo;

    /**
     * 고지서 신청서 1건을 가져온다
     *
     * @param filter
     * @param example
     * @return
     */
    public InvoiceApplySO pollInvoiceApply(InvoiceApplySF filter, InvoiceApplySE example) {
        final InvoiceApplyDF invoiceApplyDF = filter.toDomainFilter();
        final InvoiceApplyDO invoiceApplyDO = invoiceApplyRepository.getFirst(invoiceApplyDF);
        if (invoiceApplyDO == null) {
            return null;
        }

        final InvoiceApplyDE invoiceApplyDE = example.toDomainExample();
        invoiceApplyDO.modify(invoiceApplyDE);
        invoiceApplyRepository.update(invoiceApplyDO);

        return new InvoiceApplySO(invoiceApplyDO);
    }

    /**
     * 고지서 신청서 처리
     * <p>
     * 고지서 신청서 상태가 처리 중이고, 고지서 신청서 처리 상태가 처리 중인 고지서 신청서를 처리한다.
     * <p>
     * 파일을 생성하고, 비더빌텍에 파일을 전송한다.
     *
     * @param invoiceApplyId
     */
    // FIXME wisehouse | 흐름을 알기 어렵다
    public void submitInvoiceApply(String invoiceApplyId) {
        if (StringUtils.isBlank(invoiceApplyId)) {
            LOGGER.error("고지서 신청서 처리 [{}] : 고지서 신청서 번호 없음", invoiceApplyId);
            return;
        }

        final InvoiceApplyDO item = invoiceApplyRepository.get(invoiceApplyId);
        if (item == null) {
            LOGGER.error("고지서 신청서 처리 [{}] : 고지서 신청서 없음", invoiceApplyId);
            return;
        }

        if (!StringUtils.equals(InvoiceApplyProcessStatus.WIP, item.getProcessStatusCd())) {
            LOGGER.error("고지서 신청서 처리 [{}] : 고지서 신청서 처리 상태가 처리 중이 아님", invoiceApplyId);
            return;
        }

        try {
            if (StringUtils.isBlank(item.getFileDt())) {
                LOGGER.error("고지서 신청서 처리 [{}] : 파일 날짜 없음", invoiceApplyId);
                item.fail(InvoiceApplyProcessStatus.FAIL_AS_NO_FILE_DT);
                return;
            }

            if (StringUtils.isBlank(item.getFileNo())) {
                LOGGER.error("고지서 신청서 처리 [{}] : 파일 번호 없음", invoiceApplyId);
                item.fail(InvoiceApplyProcessStatus.FAIL_AS_NO_FILE_NO);
                return;
            }

            // 파일 생성
            try {
                LOGGER.debug("고지서 신청서 처리 [{}] : 데이터 조회", invoiceApplyId);
                final InvoiceDF filter = new InvoiceDF();
                filter.setMasterId(invoiceApplyId);
                filter.setOrderBy(InvoiceOrder.SEQ_NO);

                final List<InvoiceDO> itemList = invoiceRepository.find(filter);
                if (itemList.isEmpty()) {
                    LOGGER.warn("고지서 신청서 처리 [{}] : 데이터 없음", invoiceApplyId);
                    item.failAsNoData();
                    invoiceApplyRepository.update(item);
                    return;
                }

                LOGGER.debug("고지서 신청서 처리 [{}] : 파일 생성", invoiceApplyId);
                final InvoiceSam invoiceSam = new InvoiceSam();
                for (InvoiceDO each : itemList) {
                    InvoiceSamBody body = invoiceHelper.castToInvoiceSamBody(item, each);
                    invoiceSam.addBody(body);
                }

                final String filePath = invoiceHelper.getInvoiceFilePath(item);
                File file = new File(filePath);
                invoiceSam.writeTo(new FileOutputStream(file));
            } catch (Exception e) {
                LOGGER.error("고지서 신청서 처리 [{}] : 파일 생성 실패", invoiceApplyId);
                item.failAtDamoa();
                invoiceApplyRepository.update(item);

                throw new Exception(e);
            }

            try {
                LOGGER.debug("고지서 신청서 처리 [{}] : 파일 전송", invoiceApplyId);
                final String filePath = invoiceHelper.getInvoiceFilePath(item);
                File file = new File(filePath);
                bbtecFileServerAdapter.store(file);
            } catch (Exception e) {
                LOGGER.error("고지서 신청서 처리 [{}] : 파일 전송 실패", invoiceApplyId);
                item.failAtBbtec();
                invoiceApplyRepository.update(item);

                throw new Exception(e);
            }

            // FIXME wisehouse | 블록이 너무 많다
            try {
                if (StringUtils.equalsAny(item.getStatusCode(), InvoiceApplyStatus.WIP, InvoiceApplyStatus.FAILED)) {
                    final String destinationContactNo = item.getDestinationContactNo();
                    if (StringUtils.startsWithAny(destinationContactNo, "010", "011", "016", "018", "019")) {
                        LOGGER.debug("고지서 신청서 처리 [{}] : 알림 문자 발송", invoiceApplyId);
                        final String title = "[신한 다모아] 고지서 출력 의뢰 처리 알림";
                        final String contentTemplate = "[신한다모아] 고객님께서 %s월%s일 요청하신 고지서출력의뢰건이 출력준비중입니다. 처리완료 후, 고객님께서 선택하신 방법으로 배송될 예정입니다.  택배배송시 주말, 공휴일 제외 평균 3일 소요, 퀵배송시  당일 또는 익영업일 내 배송됩니다.";
                        final String content = String.format(contentTemplate, new SimpleDateFormat("M").format(item.getCreateDate()), new SimpleDateFormat("d").format(item.getCreateDate()));
                        fingerIntegrateMessageAdapter.createMessage(FingerIntegrateMessageAdapter.MessageType.MMS, title, content, new Date(), damoaContactNo, destinationContactNo, item.getDestinationManager());
                    } else {
                        LOGGER.debug("고지서 신청서 처리 [{}] : 알림 문자 발송 안함 (휴대폰 번호 아님)", invoiceApplyId);
                    }
                } else {
                    LOGGER.debug("고지서 신청서 처리 [{}] : 알림 문자 발송 안함 (재요청)", invoiceApplyId);
                }
            } catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
                LOGGER.debug("고지서 신청서 처리 [{}] : 알림 문자 발송 실패", invoiceApplyId);
            }

            LOGGER.debug("고지서 신청서 처리 [{}] : 상태 변경", invoiceApplyId);
            item.complete();

            LOGGER.debug("고지서 신청서 처리 [{}] : 처리 완료", invoiceApplyId);
        } catch (IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }

            item.fail();
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }

            item.fail();
        } finally {
            invoiceApplyRepository.update(item);
        }
    }

    /**
     *
     * @param filter
     */
    public void startInvoiceApply(InvoiceApplySF filter) {
        final InvoiceApplyDF invoiceApplyDF = filter.toDomainFilter();
        final List<InvoiceApplyDO> doList = invoiceApplyRepository.findForUpdate(invoiceApplyDF, new RowBounds());

        final Progress progress = new Progress(doList.size());
        for (InvoiceApplyDO each : doList) {
            try {
                final String fileDt = new SimpleDateFormat("yyyyMMdd").format(new Date());

                final InvoiceApplyDF fileNoFilter = new InvoiceApplyDF();
                fileNoFilter.setFileDt(fileDt);

                final int fileNo = invoiceApplyRepository.findForUpdate(fileNoFilter, new RowBounds()).size() + 1;

                each.start(fileDt, fileNo);
                invoiceApplyRepository.update(each);

                progress.tick();
                LOGGER.info("정상 : 고지서 출력 의뢰 시작 [{}] - {}% ({}/{}), {}s left", each.getId(), new DecimalFormat("##0.0").format(progress.getProgressRate()), progress.getProgressCount(), progress.getTotalCount(), new DecimalFormat("#,##0.0").format(progress.getTimeLeft()/1000F));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);

                progress.tick();
                LOGGER.info("시작 : 고지서 출력 의뢰 준비 [{}]- {}% ({}/{}), {}s left", each.getId(), new DecimalFormat("##0.0").format(progress.getProgressRate()), progress.getProgressCount(), progress.getTotalCount(), new DecimalFormat("#,##0.0").format(progress.getTimeLeft()/1000F));
            }
        }
    }

    public void readyInvoiceApply(InvoiceApplySF filter) {
        final InvoiceApplyDF invoiceApplyDF = filter.toDomainFilter();
        final List<InvoiceApplyDO> doList = invoiceApplyRepository.findForUpdate(invoiceApplyDF, new RowBounds());

        final Progress progress = new Progress(doList.size());
        for (InvoiceApplyDO each : doList) {
            try {
                each.ready();
                invoiceApplyRepository.update(each);
                progress.tick();
                LOGGER.info("정상 : 고지서 출력 의뢰 준비 [{}] - {}% ({}/{}), {}s left", each.getId(), new DecimalFormat("##0.0").format(progress.getProgressRate()), progress.getProgressCount(), progress.getTotalCount(), new DecimalFormat("#,##0.0").format(progress.getTimeLeft()/1000F));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                progress.tick();
                LOGGER.info("오류 : 고지서 출력 의뢰 준비 [{}]- {}% ({}/{}), {}s left", each.getId(), new DecimalFormat("##0.0").format(progress.getProgressRate()), progress.getProgressCount(), progress.getTotalCount(), new DecimalFormat("#,##0.0").format(progress.getTimeLeft()/1000F));
            }
        }
    }
}
