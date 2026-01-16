package kr.co.finger.shinhandamoa.service;

import kr.co.finger.shinhandamoa.common.DateUtils;
import kr.co.finger.shinhandamoa.constants.InvoiceApplyStatus;
import kr.co.finger.shinhandamoa.data.domain.model.Statistics;
import kr.co.finger.shinhandamoa.data.domain.model.XrcpdetVO;
import kr.co.finger.shinhandamoa.data.domain.model.XrcpmasVO;
import kr.co.finger.shinhandamoa.data.table.model.Xadjgroup;
import kr.co.finger.shinhandamoa.data.table.model.Xchalist;
import kr.co.finger.shinhandamoa.domain.model.*;
import kr.co.finger.shinhandamoa.domain.repository.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 기관 정산 서비스
 *
 * @author wisehouse@finger.co.kr
 */
@Service
public class ClientSettleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSettleService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private InstantMessageRepository instantMessageRepository;

    @Autowired
    private MonthlySettleRepository monthlySettleRepository;

    @Autowired
    private DailySettleRepository dailySettleRepository;

    @Autowired
    private InvoiceApplyRepository invoiceApplyRepository;

    @Autowired
    private AlarmTalkRepository alarmTalkRepository;

    /**
     * 전체 기관에 대해서 월 정산을 실행한다
     *
     * @param settleMonth
     */
    public void executeMonthlySettle(String settleMonth) {
        LOGGER.info("정산월: {}", settleMonth);
        final List<ClientDO> clientList = clientRepository.findAll(new RowBounds());
        for (ClientDO each : clientList) {
            executeMonthlySettleForClient(each.getId(), settleMonth);
        }
    }

    /**
     * 특정 기관에 대해서 월 정산을 실행한다.
     *
     * @param clientId
     * @param settleMonth
     */
    public void executeMonthlySettleForClient(String clientId, String settleMonth) {
        LOGGER.info("정산기관: {}, 정산월: {}", clientId, settleMonth);
        final String fromDateString = DateUtils.formatDate(DateUtils.parseDate(settleMonth, "yyyyMM"), "yyyyMMdd");
        final String toDateString = DateUtils.formatDate(DateUtils.getLastMonthDate(DateUtils.parseDate(settleMonth, "yyyyMM")), "yyyyMMdd");

        final ClientDO client = clientRepository.get(clientId);

        final Statistics noticeStatistics = noticeRepository.aggregate(clientId, Arrays.asList("PA02", "PA03", "PA04", "PA06"), fromDateString, toDateString, "N");

        final Statistics receiptStatistics = receiptRepository.aggregate(clientId, Arrays.asList("VAS"), Arrays.asList("PA03"), fromDateString, toDateString);

        // SMS는 정상 건만 조회
        final List<String> statusList = Arrays.asList("2");

        final int smsCount = instantMessageRepository.count(clientId, "SMS", statusList, fromDateString, toDateString);

        final int lmsCount = instantMessageRepository.count(clientId, "LMS", statusList, fromDateString, toDateString);

        final int atCount = alarmTalkRepository.count(clientId, fromDateString, toDateString);

        final InvoiceApplyDF invoiceApplyDF = new InvoiceApplyDF();
        invoiceApplyDF.setStatusCode(InvoiceApplyStatus.COMPLETE);
        invoiceApplyDF.setClientId(clientId);
        invoiceApplyDF.setFileDtFrom(fromDateString);
        invoiceApplyDF.setFileDtTo(toDateString);

        final List<InvoiceApplyDO> itemList = invoiceApplyRepository.find(invoiceApplyDF, new RowBounds());
        final int invoiceCount = itemList.stream()
                .mapToInt(value -> value.getInvoiceCount())
                .sum();

        if ((noticeStatistics.getCount() + receiptStatistics.getCount() + smsCount + lmsCount + invoiceCount + atCount) <= 0) {
            LOGGER.info("이용 내역 없음");
            return;
        }

        final MonthlySettleDO monthlySettleDO = MonthlySettleDO.newInstance(client, settleMonth, noticeStatistics, receiptStatistics, smsCount, lmsCount, invoiceCount, atCount);

        if (monthlySettleRepository.exists(clientId, settleMonth)) {
            monthlySettleRepository.update(monthlySettleDO);
        } else {
            monthlySettleRepository.create(monthlySettleDO);
        }
    }

    public void executeDailySettle(String settleDate) {
        LOGGER.info("일정산[{}]", settleDate);
        final List<ClientDO> clientList = clientRepository.findAll(new RowBounds());
        for (ClientDO each : clientList) {
            executeDailySettleForClient(each.getId(), settleDate);
        }
    }

    /**
     * 특정 기관에 대해서 일 정산을 실행한다.
     *
     * @param clientId
     * @param settleDateString
     */
    public void executeDailySettleForClient(String clientId, String settleDateString) {
        LOGGER.info("일정산[정산기관: {}, 정산일: {}]", clientId, settleDateString);
        final String fromDateString = settleDateString;
        final String toDateString = settleDateString;

        final ClientDO client = clientRepository.get(clientId);

        final Statistics noticeStatistics = noticeRepository.aggregate(clientId, Arrays.asList("PA02", "PA03", "PA04", "PA06"), fromDateString, toDateString, null);

        final Statistics receiptStatistics = receiptRepository.aggregate(clientId, Arrays.asList("VAS"), Arrays.asList("PA03"), fromDateString, toDateString);

        // SMS는 정상 건만 조회
        final List<String> statusList = Arrays.asList("2");

        final int smsCount = instantMessageRepository.count(clientId, "SMS", statusList, fromDateString, toDateString);

        final int lmsCount = instantMessageRepository.count(clientId, "LMS", statusList, fromDateString, toDateString);

        final int atCount = alarmTalkRepository.count(clientId, fromDateString, toDateString);

        final InvoiceApplyDF invoiceApplyDF = new InvoiceApplyDF();
        invoiceApplyDF.setStatusCode(InvoiceApplyStatus.COMPLETE);
        invoiceApplyDF.setClientId(clientId);
        invoiceApplyDF.setFileDt(settleDateString);
        final List<InvoiceApplyDO> itemList = invoiceApplyRepository.find(invoiceApplyDF, new RowBounds());
        final int invoiceCount = itemList.stream()
                .mapToInt(value -> value.getInvoiceCount())
                .sum();

        if ((noticeStatistics.getCount() + receiptStatistics.getCount() + smsCount + lmsCount + invoiceCount + atCount) <= 0) {
            LOGGER.info("이용 내역 없음");
            return;
        }

        final DailySettleDO dailySettleDO = DailySettleDO.newInstance(client, settleDateString, noticeStatistics, receiptStatistics, smsCount, lmsCount, invoiceCount, atCount);
        dailySettleRepository.createOrReplace(dailySettleDO);

    }

    public void executeVaSettle(String settleDate) {
        LOGGER.info("일별 가상계좌 정산 [{}]", settleDate);
        // 모든 기관 정보 다 가져옴
        final List<ClientDO> clientList = clientRepository.findAll(new RowBounds());
        for (ClientDO each : clientList) {
            executeVaSettleForClient(each.getId(), settleDate);
        }
    }

    /**
     * 가상계좌 입금건에 대해 기관별, 각기관 계좌별로 정산
     *
     * @param clientId
     * @param settleDateString
     */
    @Transactional
    public void executeVaSettleForClient(String clientId, String settleDateString) {
        LOGGER.info("일별 가상계좌 정산[정산기관: {}, 정산일: {}]", clientId, settleDateString);
        final String fromDateString = settleDateString;
        final String toDateString = settleDateString;

        // 해당 기관 상세 정보
        // 이 안에 기관 다계좌 정보도 포함
        final ClientDO client = clientRepository.get(clientId);
        Xchalist xchalist = client.getXchalist();

        List<Xadjgroup> xadjgroupList = client.getXadjgroupList();
        if (xadjgroupList == null || xadjgroupList.isEmpty()) {
            LOGGER.error("기관 계좌정보 없음");
            return;
        }

        final Statistics receiptStatistics = receiptRepository.aggregate(clientId, Arrays.asList("VAS"), Arrays.asList("PA03"), fromDateString, toDateString);

        if (receiptStatistics.getCount() <= 0) {
            LOGGER.info("이용 내역 없음");
            return;
        }

        // 일정 기간동안 입금된 모든 xrcpmas select
        // + 가져온 xrcpmas에 맞는 xrcpdet select
        List<XrcpmasVO> rcpmasList = receiptRepository.getRcpmasList(clientId, fromDateString, toDateString);
        List<String> rcpmascdList = new ArrayList<>();
        long fingerfee = 0L;
        LOGGER.debug("rcpmasList 정산입금건수 {}", rcpmasList.size());
        for (XrcpmasVO xrcpmasVO : rcpmasList) {
            rcpmascdList.add(xrcpmasVO.getRcpmascd());
            fingerfee += xchalist.getRcpcntfee();
        }

        //통지 방식일경우는 무조건 1번 계좌로 다 정산
        if (xchalist.getAmtchkty().equals("N")) {
            long rcpamt = 0L;
            for (XrcpmasVO xrcpmasVO : rcpmasList) {
                rcpamt += xrcpmasVO.getRcpamt();
            }
            if (xchalist.getFingerFeePayty().equals("PRE") && fingerfee != 0L) {
                LOGGER.debug("수수료 선취");
                LOGGER.debug("executeVaSettleForClient: 정산금액: {} || 수수료: {}", rcpamt, fingerfee);
                if (rcpamt >= fingerfee) {
                    rcpamt -= fingerfee;
                    fingerfee -= fingerfee;
                }
                if (fingerfee > rcpamt) {
                    fingerfee -= rcpamt;
//                    rcpamt = 0L;
                    rcpamt = fingerfee * -1L;
                }
            }
            try {
                LOGGER.info("일별 가상계좌 정산 통지방식 [정산기관: {}, 정산일: {}] 성공", clientId, settleDateString);
                receiptRepository.insertVaSettle(xchalist, xadjgroupList.get(0), rcpamt, settleDateString);
            } catch (ParseException e) {
                LOGGER.info("일별 가상계좌 정산 [정산기관: {}, 정산일: {}] 실패", clientId, settleDateString);
                LOGGER.debug(e.getMessage());
            }
        //승인 방식의 경우
        } else {
            // select distinct adjfiregkey from xrcpdet
            // 입금건들중 adjfiregkey distinct 리스트
            List<XrcpdetVO> rcpAdjfList = receiptRepository.getDistinctAdjfiregkey(rcpmascdList);
            LOGGER.debug("adjfiregkey distinct 갯수 {}", rcpAdjfList.size());
            List<Pair<String, List<XrcpdetVO>>> adjPair = new ArrayList<>();
            //모계좌번호별로 select rcpdet
            for (XrcpdetVO xrcpdetVO : rcpAdjfList) {
                List<XrcpdetVO> xrcpdetVOList = receiptRepository.getRcpdetList(rcpmascdList, xrcpdetVO.getAdjfiregkey());
                //pair에 <모계좌, rcpdetlist> 모계좌가 삭제되서 없는경우 00001이 디폴트
                if (xadjgroupList.stream().filter(r -> r.getAdjfiregkey().equals(xrcpdetVO.getAdjfiregkey())).count() > 0) {
                    adjPair.add(new ImmutablePair<>(xrcpdetVO.getAdjfiregkey(), xrcpdetVOList));
                } else {
                    adjPair.add(new ImmutablePair<>("00001", xrcpdetVOList));
                }
            }
            /**
             * 통지에서 승인으로 변경되는 경우 등
             * rcpmas 는 있는데 rcpdet가 없는경우 (청구가 없는경우)
             * 위에서 카운트 되지않음
             * 다시 불러와서 0001 에 합산하여 계산 (수수료는 rcpmas를 따라가기때문에 이미 합산되어있는 금액)
             */
            //rcpmaslist중 rcpdet가 null인 경우
            List<XrcpmasVO> rcpmasWithoutDetList = receiptRepository.getRcpmasWithoutDet(rcpmascdList);
            if(rcpmasWithoutDetList != null && !rcpmasWithoutDetList.isEmpty()){
                LOGGER.info("승인방식중 입금상세가 없는 건 {} 건 합산", rcpmasWithoutDetList.size());
                List<XrcpdetVO> xrcpdetVOList = new ArrayList<>();
                for (XrcpmasVO xrcpmasVO : rcpmasWithoutDetList) {
                    LOGGER.info("승인방식중 입금상세가 없는 건 {} 상세정보", xrcpmasVO.getRcpmascd());
                    xrcpdetVOList.add(xrcpmasVO.toDet());
                }
                adjPair.add(new ImmutablePair<>("00001", xrcpdetVOList));
            }
            //기관의 모계좌에 해당하는 rcpdetlist들만 sum 해서 총정산금액 계산
            for (Xadjgroup xadjgroup : xadjgroupList) {
                List<Pair<String, List<XrcpdetVO>>> list = adjPair.stream().filter(r -> r.getLeft().equals(xadjgroup.getAdjfiregkey())).collect(Collectors.toList());
                long rcpamt = 0L;
                for (Pair<String, List<XrcpdetVO>> pair : list) {
                    for (XrcpdetVO xrcpdetVO : pair.getRight()) {
                        rcpamt += xrcpdetVO.getPayitemamt();
                    }
                }
                /**
                 * PRE 선_공제 (기본 모계좌(00001)에서 공제
                 * CMS, CUR 후_cms, 후_직접
                 * xadjgroup.getAdjfiregkey().equals("00001")
                 */
                if (xchalist.getFingerFeePayty().equals("PRE") && fingerfee != 0L) {
                    LOGGER.debug("수수료 선취");
                    LOGGER.debug("executeVaSettleForClient: 정산금액: {} || 수수료: {}", rcpamt, fingerfee);
                    if (rcpamt >= fingerfee) {
                        rcpamt -= fingerfee;
                        fingerfee -= fingerfee;
                    }
                    if (fingerfee > rcpamt) {
                        fingerfee -= rcpamt;
//                        rcpamt = 0L;
//                        rcpamt = Math.abs(fingerfee);
                        rcpamt = fingerfee * -1L;
                    }
                }
                try {
                    LOGGER.info("일별 가상계좌 정산 승인방식 [정산기관: {}, 정산일: {}] 성공", clientId, settleDateString);
                    receiptRepository.insertVaSettle(xchalist, xadjgroup, rcpamt, settleDateString);
                } catch (ParseException e) {
                    LOGGER.info("일별 가상계좌 정산 [정산기관: {}, 정산일: {}] 실패", clientId, settleDateString);
                    LOGGER.debug(e.getMessage());
                }
            }
        }
//        rcpmasList.stream().filter(r -> r.getRcpamt().equals(0)).mapToInt(r -> Math.toIntExact(r.getRcpamt())).sum();
    }
}
