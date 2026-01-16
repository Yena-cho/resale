package kr.co.finger.shinhandamoa.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyProcessStatus;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyStatus;
import kr.co.finger.shinhandamoa.data.InvoiceApplyDAO;
import kr.co.finger.shinhandamoa.data.InvoiceDAO;
import kr.co.finger.shinhandamoa.data.table.mapper.XnotimasreqMapper;
import kr.co.finger.shinhandamoa.data.table.model.Xnotimasreq;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDF;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplyDO;
import kr.co.finger.shinhandamoa.domain.model.InvoiceApplySE;
import kr.co.finger.shinhandamoa.domain.repository.InvoiceApplyRepository;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceApplySF;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceApplySO;
import kr.co.finger.shinhandamoa.service.invoice.InvoiceService;
import kr.co.finger.shinhandamoa.test.TestConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * {@link InvoiceService} 테스트
 *
 * @author wisehouse@finger.co.kr
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"kr.co.finger.shinhandamoa.domain", "kr.co.finger.shinhandamoa.adapter", "kr.co.finger.shinhandamoa.service.invoice"})
@Import(InvoiceService.class)
@Rollback
@Transactional
public class InvoiceServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceTest.class);

    private static final String[] REQST_LIST = new String[]{"BR01", "BR02", "BR03", "BR04", "BR09"};

    private static final String[] CODE_LIST = new String[]{"N20001", "N20002"};

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceApplyRepository invoiceApplyRepository;

    @Autowired
    private XnotimasreqMapper xnotimasreqMapper;

    @BeforeClass
    public static void beforeClass() {
        List<String> dateList = new ArrayList<>();
        Iterator<Calendar> iter = DateUtils.iterator(new Date(), DateUtils.RANGE_MONTH_MONDAY);
        while(iter.hasNext()) {
            dateList.add(DateFormatUtils.format(iter.next(), "yyyyMMdd"));
        }
        String[] dates = new String[dateList.size()];
        for(int i=0; i<dates.length; i++) {
            dates[i] = dateList.get(i);
        }

        Fixture.of(Xnotimasreq.class).addTemplate("valid", new Rule() {{
            add("notimasreqcd", regex("T[0-9]{7}"));
            add("chacd", regex("2000[0-9]{4}"));
            add("reqdate", random(dates));
            add("regdt", DateUtils.truncate(new Date(), Calendar.SECOND));
            add("billgubn", "01");
            add("resStsCd", random("N10000", "N11000", "N12000", "N13000", "N14000"));
            add("masmonth", regex("20190[1-9]"));
            add("filedt", random(dates));
            add("sendfileseq", regex("[0-9]{2}"));
            add("reqst", random(REQST_LIST));
            add("dlvrTypeCd", random(CODE_LIST));
        }});
    }

    @Before
    public void before() {
        LOGGER.debug("임시 데이터 생성");
        final List<Xnotimasreq> xnotimasreq = Fixture.from(Xnotimasreq.class).gimme(100, "valid");

        for (Xnotimasreq each : xnotimasreq) {
            xnotimasreqMapper.insertSelective(each);
        }
    }

    /**
     * 스케줄 작업 샘플
     *
     */
    @Test
    public void practice() {
        try {
            // 고지서 전송 대기 조회
            final InvoiceApplySF filter = new InvoiceApplySF();
            filter.setStatusCode(InvoiceApplyStatus.WIP);
            filter.setProcessStatusCd(InvoiceApplyProcessStatus.STAND_BY);

            final InvoiceApplySE example = new InvoiceApplySE();
            filter.setProcessStatusCd(InvoiceApplyProcessStatus.WIP);

            for (InvoiceApplySO each = null; (each = invoiceService.pollInvoiceApply(filter, example)) != null; ) {
                try {
                    String invoiceApplyId = each.getId();
                    // 파일 전송
                    invoiceService.submitInvoiceApply(invoiceApplyId);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@link InvoiceService#pollInvoiceApply(InvoiceApplySF, InvoiceApplySE)} 테스트
     *
     */
    @Test
    public void pollInvoiceApply() {
        LOGGER.debug("조회 조건 설정");
        final InvoiceApplySF filter = new InvoiceApplySF();
        filter.setStatusCode(InvoiceApplyStatus.WIP);
        filter.setProcessStatusCd(InvoiceApplyProcessStatus.STAND_BY);

        final InvoiceApplySE example = new InvoiceApplySE();
        example.setProcessStatusCd(InvoiceApplyProcessStatus.WIP);

        LOGGER.debug("조회 대상 확인");
        final List<InvoiceApplyDO> doList = invoiceApplyRepository.find(filter.toDomainFilter(), new RowBounds());

        LOGGER.debug("조회");
        while(invoiceService.pollInvoiceApply(filter, example) != null) {}

        LOGGER.debug("상태 변경 점검");
        for (InvoiceApplyDO each : doList) {
            InvoiceApplyDO invoiceApplyDO = invoiceApplyRepository.get(each.getId());
            assertEquals(InvoiceApplyProcessStatus.WIP, invoiceApplyDO.getProcessStatusCd());
        }
    }

    /**
     * {@link InvoiceService#submitInvoiceApply(String)} 테스트
     *
     */
    @Test
    public void submitInvoiceApply() {
        final InvoiceApplyDF filter = new InvoiceApplyDF();
        filter.setProcessStatusCd(InvoiceApplyProcessStatus.WIP);

        final List<InvoiceApplyDO> doList = invoiceApplyRepository.find(filter, new RowBounds());
        for (InvoiceApplyDO each : doList) {
            invoiceService.submitInvoiceApply(each.getId());
        }

        LOGGER.debug("상태 변경 점검");
        for (InvoiceApplyDO each : doList) {
            InvoiceApplyDO invoiceApplyDO = invoiceApplyRepository.get(each.getId());
            LOGGER.debug("invoiceApplyDO.getProcessStatusCd()= {}", invoiceApplyDO.getProcessStatusCd());
            assertTrue(StringUtils.equalsAny(invoiceApplyDO.getProcessStatusCd(), InvoiceApplyProcessStatus.SUCCESS, InvoiceApplyProcessStatus.FAIL, InvoiceApplyProcessStatus.FAIL_AS_NO_FILE_DT, InvoiceApplyProcessStatus.FAIL_AS_NO_FILE_NO, InvoiceApplyProcessStatus.FAIL_AT_BBTEC, InvoiceApplyProcessStatus.FAIL_AT_DAMOA, InvoiceApplyProcessStatus.FAIL_AS_NO_DATA));
        }
    }
}