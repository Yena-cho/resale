package kr.co.finger.damoa.shinhan.agent.handler.command;

import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Sha256;
import kr.co.finger.damoa.commons.concurrent.AggregateQueue;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.domain.model.*;
import kr.co.finger.damoa.shinhan.agent.domain.repository.*;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaException;
import kr.co.finger.damoa.shinhan.agent.model.*;
import kr.co.finger.damoa.shinhan.agent.util.CommunicateUtil;
import kr.co.finger.damoa.shinhan.agent.validation.notice.NoticeMessageValidatorChain;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import kr.co.finger.shinhandamoa.data.table.mapper.XchalistMapper;
import kr.co.finger.shinhandamoa.data.table.mapper.XrcpmasMapper;
import kr.co.finger.shinhandamoa.data.table.model.TransApiRelayData;
import kr.co.finger.shinhandamoa.data.table.model.Xchalist;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.XrcpmasDTO;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import static kr.co.finger.damoa.shinhan.agent.util.DamoaBizUtil.isRetryMsg;


/**
 * 통지응답은 무시
 * 통지요청은 통지응답을 만들어 전송함.
 * 비동기적으로 파일저장, DB 처리함.
 * 나머지 전문은 응답전문 만드는게 별로 어렵지 않음.
 * <p>
 * 수납전문 테이블 필요
 * 수납일자,기관,가상계좌,수납금액,형태(수납,취소)
 */


@Component
public class NoticeMsgCommand extends BasicCommand {
    private static Logger LOGGER = LoggerFactory.getLogger(NoticeMsgCommand.class);
    
    private final NoticeMessageValidatorChain validator;
    
    @Autowired
    private MessageFactory messageFactory;
    
    @Autowired
    private DamoaDao damoaDao;
    
    @Autowired
    private AggregateQueue<MessageBean> damoaQueue;
    
    @Autowired
    private DamoaContext damoaContext;
    
    @Autowired
    private NoticeMasterRepository noticeMasterRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ChaRepository chaRepository;
    
    @Autowired
    private ReceiptMasterRepository receiptMasterRepository;

    @Autowired
    private XchalistMapper xchalistMapper;

    @Autowired
    private XrcpmasMapper xrcpmasMapper;

    @Autowired
    private TransRcpNoticeCommand transRcpNoticeCommand;

    @Autowired
    private TransApiRelayDataRepository transApiRelayDataRepository;

    private Object MONITOR = new Object();

    public NoticeMsgCommand(CommunicateUtil communicateUtil) {
        this.communicateUtil = communicateUtil;
        validator = new NoticeMessageValidatorChain();
    }
    private final CommunicateUtil communicateUtil;

    @Override
    public void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {
        throw new RuntimeException("폐기된 메소드 호출");
    }

    @Override
    public boolean executeWithPush(MsgIF requestInterface, MsgIF responseInterface) throws Exception {
        final StopWatch stopWatch = StopWatch.createStarted();

        final NoticeMessage request = (NoticeMessage) requestInterface;
        final NoticeMessage response = (NoticeMessage) responseInterface;
//        NoticeMessage response = (NoticeMessage) responseInterface;

        TransApiRelayData transApiRelayData = new TransApiRelayData();

        LOGGER.debug("[거래일자|{}]/[전문번호|{}] [전문구분코드|{}]/[거래구분코드|{}] [기관코드|{}] [입금계좌번호|{}] [거래금액|{}]",
                response.getMsgSndDate(),
                response.getDealSeqNo(),
                response.getMsgTypeCode(),
                response.getDealTypeCode(),
                response.getDepositCorpCode(),
                response.getDepositAccountNo(),
                response.getTransactionAmount());

        try {
            // 1. 전문 형식 검사
            final String messageType = request.getMsgTypeCode() + request.getDealTypeCode();
            try {
                // 1.1. 공통부
                ResultBean _result = validateCommons(request, messageFactory, damoaContext, LOGGER);
                if ("0".equalsIgnoreCase(_result.getCode()) == false) {
                    throw new DamoaException(_result);
                }
                // 1.2. 기관코드, 계좌번호
                // 계좌번호 사용여부 확인
                LOGGER.debug("계좌번호 사용여부 확인");
                _result = validate(request, messageFactory, damoaContext, LOGGER);
                if ("0".equalsIgnoreCase(_result.getCode()) == false) {
                    throw new DamoaException(_result);
                }
                // 1.3. 거래 금액
                _result = validateNotice(request, messageFactory, damoaContext, LOGGER);
                if ("0".equalsIgnoreCase(_result.getCode()) == false) {
                    throw new DamoaException(_result);
                }
            } finally {
                // 2. 전문 저장
                // damoaDao.insertTransDataAcct(messageType, request.getDealSeqNo(), Constants.RECEIVE_DESTINATION, request.getOriginMessage());
                transApiRelayData = damoaDao.insertTransDataAcct(messageType, request.getDealSeqNo(), Constants.RECEIVE_DESTINATION, request.getOriginMessage());
            }

            // 3. 관련데이터 조회
            final String dealSeqNo = StringUtils.trim(requestInterface.getDealSeqNo());
            final String typeCode = StringUtils.trim(getMsgType(requestInterface));
            final String accountNo = request.getDepositAccountNo().trim();
            final String fgCd = request.getDepositCorpCode().trim();

            // 가상계좌 + 핑거코드(20008153, 20008155) 로 해당 기관코드 검색
            final String chaCd = chaRepository.findChacdByAccountNoAndFingerCd(fgCd, accountNo);
            LOGGER.debug("재판매 기관코드 {}" , chaCd);

            // 3.1. 기관 조회
            final ChaDO cha = chaRepository.findByChaCd(chaCd);
            // 3.3. 고객 조회
            final CustomerDO customer = customerRepository.findByAccountNo(chaCd, accountNo);
            // 3.3. 청구 조회
            final List<NoticeMasterDO> noticeMasterList = noticeMasterRepository.findActiveByAccountNo(chaCd, accountNo);

            // 4. 재처리 확인
            LOGGER.debug("재처리 확인");
            if (isRetryMsg(request.getUsrWorkArea2()) && damoaContext.containsSeqNoAtCancel(dealSeqNo, typeCode)) {
                // 재처리이면서 기존 요청이 확인된 경우
                handleTranDataAcct(request);

                // 입금이체인 경우는 중복으로 들어올 수 있다.
                // 중복인 경우에는 정상처리
                handleManyAccountNoWithPreviousInfo(response, dealSeqNo, getMsgType(response));
                return true;
            }

            // 4.1 한도방식인 경우 가상계좌별 일 한도 조회
            final Map<String, Object> limitMap = damoaDao.getLimitPerVano(accountNo);

            // 5. 검증
            LOGGER.debug("입금 검증");
            //final ValidationResult result = validate(request, cha, customer, noticeMasterList, limitMap);
            ValidationResult result = validate(request, cha, customer, noticeMasterList, limitMap);

            Xchalist xchalist = (Xchalist) xchalistMapper.selectByPrimaryKey(chaCd);

            // 입금 통지 기관일 경우 중계 서버에 수납 정보 송신

            if(xchalist.getChatrty().equals("05")) { // 기관 접속형태 [01:웹(WEB), 03:전문(API), 05:수납통지(SET) ]
                // 6. 입금처리
                List<String> rcpmasCdList = process2(result, request, response, cha, customer, noticeMasterList);
                LOGGER.info("입금 처리 완료 후 rcpmasCdList {}", rcpmasCdList);

                // 수납통지 기관의 url 가져오기
                String reqUri = xchalist.getRcpNoticeUrl();
                LOGGER.info("수납통지 기관의 url  ======>  {}", reqUri);

                String rcpmasCd = rcpmasCdList.get(0);
                XrcpmasDTO xrcpmasDTO = receiptMasterRepository.getXrcpmasInfo(rcpmasCd);

                transRcpNoticeCommand.transRcpnNotice(reqUri, xrcpmasDTO);
            } else {

                // 2024.08.23 - 추가
                // 중계방식 기관일 경우 중계 서버에 수취조회 요청정보 송신
                if(cha.getAccessTypeCode().equals("07")) { // 기관 접속형태 [01:웹(WEB), 03:전문(API), 05:수납통지(SET), 07:중계(RELAY)]

                    // 2024.09.23
                    // 기관이 데이터 수신시, 데이터의 위변조여부를 체크하기 위해 사용한다.
                    request.setChkValue(Sha256.getEncrypt(request.getDepositAccountNo(), request.getTransactionAmount()));

                    transApiRelayData.setChacd(chaCd);  // 중계방식 기관인 경우 전송정보기록을 위해 사용
                    transApiRelayDataRepository.createTransApiRelayData(transApiRelayData);

                    // 기관의 수취조회 API url 가져오기
                    String noticeUrl = cha.getChaApiUrlNotice();
                    LOGGER.info("입금통지 요청을 전송할 기관의 url  ======>  {}", noticeUrl);

                    // 입금통지 응답메세지
                    NoticeMessage noticeResMsg = communicateUtil.sendNoticeRequest(noticeUrl, request);
                    response.setResCode(noticeResMsg.getResCode());
                    response.setResMsg(noticeResMsg.getResMsg());
                    response.setDepositAccountName(noticeResMsg.getDepositAccountName());


                    if (response.getResCode().equals("0000")) {// 응답코드 - (0000 : 정상거래)
                        result = new ValidationResult();
                    }
                    else {
                        LOGGER.info("불능: {}", response.getResMsg());
                        result = new ValidationResult( response.getResCode(), response.getResMsg());
                    }

//                    LOGGER.debug("== 입금통지 응답코드 : {}", result.getCode() );
//                    LOGGER.debug("== 입금통지 응답메세지 : {}", result.getMsg() );
//                    LOGGER.debug("== 입금통지 응답 검증결과 : {}", result.isValid() );
//
//                    LOGGER.debug("== 입금통지 응답메세지 : {}", response.toString());
                }

                // 6. 입금처리
                process(result, request, response, cha, customer, noticeMasterList);
            }

            // 7. 결과 생성
            composeResponse(response, result, cha, customer, noticeMasterList);

        } catch (DamoaException e) {
            throw e;
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }
            throw e;
        } finally {
            LOGGER.info("[거래일자|{}]/[전문번호|{}] [전문구분코드|{}]/[거래구분코드|{}] [기관코드|{}] [입금계좌번호|{}] [거래금액|{}] [응답코드|{}] [처리시간|{}]",
                    response.getMsgSndDate(),
                    response.getDealSeqNo(),
                    response.getMsgTypeCode(),
                    response.getDealTypeCode(),
                    response.getDepositCorpCode(),
                    response.getDepositAccountNo(),
                    response.getTransactionAmount(),
                    response.getResCode(),
                    stopWatch.getTime(TimeUnit.MILLISECONDS));
        }

        return true;
    }

    private void process(ValidationResult result, NoticeMessage request, NoticeMessage response, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList) throws Exception {
        if (!result.isValid()) {
            return;
        }

        // 수납 데이터 생성
        List<ReceiptMasterDO> receiptMasterList = generateReceiptMasterList(request, cha, customer, noticeMasterList);
        for (ReceiptMasterDO each : receiptMasterList) {
            receiptMasterRepository.createReceiptMaster(each);
        }

        // 수취인명 입력
        if (cha.getAccessTypeCode().equals("07")) {
            handleAccountNameForRelayCha(response, cha, customer, receiptMasterList);
        } else {
            handleAccountName(response, cha, customer, receiptMasterList);
        }
        // 다계좌 배분 처리
        handleMultipleAccountNo(response, cha, receiptMasterList);
        // 집계 테이블 입력
        handleDepositMsgHistory(request);

        // 큐에 입력
        offerMsgBlockingQueue(damoaQueue, request, cha, customer, noticeMasterList, receiptMasterList);
    }

    /**
     * rcpmascd 반환하는 프로세스(입금통지용)
     *
     *
     */
    private List<String> process2(ValidationResult result, NoticeMessage request, NoticeMessage response, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList) throws Exception {
        if (!result.isValid()) {
            return null;
        }

        List<String> rcpmasCdList = new ArrayList<>();
        String rcpmasCd = "";

        // 수납 데이터 생성
        List<ReceiptMasterDO> receiptMasterList = generateReceiptMasterList(request, cha, customer, noticeMasterList);
        for (ReceiptMasterDO each : receiptMasterList) {
            receiptMasterRepository.createReceiptMaster(each);
            rcpmasCd = each.getXrcpmas().getRcpmascd();
            rcpmasCdList.add(rcpmasCd);
        }

        // 수취인명 입력
        handleAccountName(response, cha, customer, receiptMasterList);
        // 다계좌 배분 처리
        handleMultipleAccountNo(response, cha, receiptMasterList);
        // 집계 테이블 입력
        handleDepositMsgHistory(request);

        // 큐에 입력
        offerMsgBlockingQueue(damoaQueue, request, cha, customer, noticeMasterList, receiptMasterList);

        return rcpmasCdList;
    }
    /**
     * 수납원장 생성
     *
     * @param request
     * @param cha
     * @param customer
     * @param noticeMasterList
     * @return
     */
    private List<ReceiptMasterDO> generateReceiptMasterList(NoticeMessage request, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList) {
        final List<NoticeMasterDO> validNoticeMasterList = getValidNoticeMasterList(request, cha, customer, noticeMasterList);

        final List<ReceiptMasterDO> resultList = new ArrayList<>();
        long remainTransactionAmount = NumberUtils.toLong(request.getTransactionAmount());

        // 무청구 입금
        if (!cha.isEnableValidateAmount() && validNoticeMasterList.isEmpty()) {
            ReceiptMasterDO receiptMaster = ReceiptMasterDO.createWithoutNoticeMaster(request, cha, customer, remainTransactionAmount);
            resultList.add(receiptMaster);
            return resultList;
        }

        // 부분납
        if (cha.isEnableValidateAmount() && cha.isEnablePartialPayment()) {
            // 부분납이면 청구 1건 밖에 안된다
            NoticeMasterDO noticeMaster = validNoticeMasterList.get(0);
            NoticeMasterDO noticeMasterForPartialPayment = noticeMaster.getFilterForPartialPayment(cha, remainTransactionAmount);
            ReceiptMasterDO receiptMaster = ReceiptMasterDO.createWithNoticeMaster(request, cha, customer, noticeMasterForPartialPayment, remainTransactionAmount);

            resultList.add(receiptMaster);
            return resultList;
        }

        // 그외
        // 초과납까지 고려한 것이지만, 검증 단계에서 걸러지니까 차례대로 넣으면 됨
        final Iterator<NoticeMasterDO> noticeIterator = validNoticeMasterList.iterator();
        while (noticeIterator.hasNext() && remainTransactionAmount > 0) {
            final NoticeMasterDO each = noticeIterator.next();
            final long transactionAmount = noticeIterator.hasNext() ? Math.min(each.getRemainAmount(), remainTransactionAmount) : remainTransactionAmount;

            final ReceiptMasterDO receiptMaster = ReceiptMasterDO.createWithNoticeMaster(request, cha, customer, each, transactionAmount);
            resultList.add(receiptMaster);

            remainTransactionAmount -= transactionAmount;
        }

        return resultList;
    }

    private List<NoticeMasterDO> getValidNoticeMasterList(NoticeMessage request, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList) {
        final List<NoticeMasterDO> validNoticeMasterByAmount = filterByAmount(request, cha, noticeMasterList);

        final List<NoticeMasterDO> validNoticeMasterByAmountAndDate = filterByDate(request, cha, validNoticeMasterByAmount);

        if (cha.isEnableValidateAmount()) {
            return validNoticeMasterByAmountAndDate.subList(0, Math.min(1, validNoticeMasterByAmountAndDate.size()));
        } else {
            return validNoticeMasterByAmountAndDate;
        }
    }

    private List<NoticeMasterDO> filterByDate(NoticeMessage request, ChaDO cha, List<NoticeMasterDO> noticeMasterList) {
        final List<NoticeMasterDO> selectNoticeMasterList = ListUtils.select(noticeMasterList, each -> each.matchesByDate(cha, new Date()));

        return selectNoticeMasterList;
    }

    private List<NoticeMasterDO> filterByAmount(NoticeMessage noticeMessage, ChaDO cha, List<NoticeMasterDO> noticeMasterList) {
        // 기관코드
        final String corpCode = cha.getId();
        // 거래금액
        final long transactionAmount = NumberUtils.toLong(StringUtils.trim(noticeMessage.getTransactionAmount()));

        LOGGER.debug("거래금액: {}", transactionAmount);
        LOGGER.debug("기관정보[{}]: {}", corpCode, cha);

        if (!cha.isEnableValidateAmount()) {
            // 금액, 체크하지 않을 때.
            LOGGER.info("입금방식 SET");
            return Collections.unmodifiableList(ListUtils.select(noticeMasterList, each -> each.getRemainAmount() > 0));
        }

        // 금액체크시
        LOGGER.info("거래금액: {}", transactionAmount);
        final List<NoticeMasterDO> selectedNoticeMasterList = ListUtils.select(noticeMasterList, each -> each.matchesByAmount(cha, transactionAmount));

        return selectedNoticeMasterList;
    }

    private void composeResponse(NoticeMessage response, ValidationResult result, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList) {
        if (!result.isValid()) {
            String msg = result.getMsg();
            LOGGER.info("불능: {}", msg);
        } else {
            LOGGER.info("정상");
            LOGGER.info("[{}/{}] 입금통지[{}/{}] 기관코드:{}/계좌번호:{}/입금액:{}",
                    response.getMsgSndDate(),
                    response.getDealSeqNo(),
                    response.getMsgTypeCode(),
                    response.getDealTypeCode(),
                    response.getDepositCorpCode(),
                    response.getDepositAccountNo(),
                    response.getTransactionAmount());
        }

        response.setResCode(result.getCode());
        response.setResMsg(result.getMsg());
    }

    private void handleAccountName(NoticeMessage response, ChaDO cha, CustomerDO customer, List<ReceiptMasterDO> receiptMasterList) {
        if (StringUtils.equals(cha.getAccountDisplayNameType(), "C")) {
            response.setDepositAccountName(split(cha.getName()));
            return;
        }

        final String noticeCustomerName = ReceiptMasterUtils.aggregateNoticeCustomerName(receiptMasterList);

        if (StringUtils.isNotBlank(noticeCustomerName)) {
            response.setDepositAccountName(split(noticeCustomerName));
            return;
        }

        final String customerName;
        if (customer == null) {
            customerName = null;
        } else {
            customerName = customer.getName();
        }
        final String chaName = cha.getName();
        final String actualName = StringUtils.defaultIfBlank(customerName, chaName);
        response.setDepositAccountName(split(actualName));
    }

    private void handleAccountNameForRelayCha(NoticeMessage response, ChaDO cha, CustomerDO customer, List<ReceiptMasterDO> receiptMasterList) {

        String customerName = response.getDepositAccountName();

        final String chaName = cha.getName();
        final String actualName = StringUtils.defaultIfBlank(customerName, chaName);
        response.setDepositAccountName(split(actualName));
    }

    private ValidationResult validate(NoticeMessage request, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
        return validator.validate(request, cha, customer, noticeMasterList, limitMap);
    }

    private void handleManyAccountNoWithPreviousInfo(NoticeMessage response, String dealSeqNo, String msgType) {
        NoticeMessage _notice = findNoticeMessage(dealSeqNo, msgType);
        handleAccountNameWithPreviousInfo(response, _notice);
        handleMultipleAccountNo(response, _notice);
    }

    private void handleAccountNameWithPreviousInfo(NoticeMessage response, NoticeMessage before) {
        response.setDepositAccountName(before.getDepositAccountName());
    }

    private void handleMultipleAccountNo(NoticeMessage target, NoticeMessage source) {
        target.setVirtualAccountDivideCount(source.getVirtualAccountDivideCount());
        target.setupManyAccountNo(1, source.getVirtualAccountAmount01(), source.getVirtualAccountKey01());
        target.setupManyAccountNo(2, source.getVirtualAccountAmount02(), source.getVirtualAccountKey02());
        target.setupManyAccountNo(3, source.getVirtualAccountAmount03(), source.getVirtualAccountKey03());
        target.setupManyAccountNo(4, source.getVirtualAccountAmount04(), source.getVirtualAccountKey04());
        target.setupManyAccountNo(5, source.getVirtualAccountAmount05(), source.getVirtualAccountKey05());
        target.setupManyAccountNo(6, source.getVirtualAccountAmount06(), source.getVirtualAccountKey06());
        target.setupManyAccountNo(7, source.getVirtualAccountAmount07(), source.getVirtualAccountKey07());
        target.setupManyAccountNo(8, source.getVirtualAccountAmount08(), source.getVirtualAccountKey08());
        target.setupManyAccountNo(9, source.getVirtualAccountAmount09(), source.getVirtualAccountKey09());
    }

    private NoticeMessage findNoticeMessage(String dealSeqNo, String typeCode) {
        List<String> msgList = damoaDao.findOriginalMsg(dealSeqNo, typeCode);
        return (NoticeMessage) messageFactory.decode(msgList.get(0));
    }

    private void handleTranDataAcct(NoticeMessage noticeMessage) throws Exception {
        insertTransDataAcct(damoaDao, getMsgType(noticeMessage), noticeMessage.getDealSeqNo(), Constants.RECEIVE_DESTINATION, noticeMessage.getOriginMessage(), LOGGER);
    }

    private void handleDepositMsgHistory(NoticeMessage noticeMessage) {
        insertDepositMsgHistory(noticeMessage, LOGGER);
    }

    private void handleMultipleAccountNo(NoticeMessage response, ChaDO cha, List<ReceiptMasterDO> receiptMasterList) {
//        if (!cha.hasMultipleAccount()) {
//            LOGGER.info("다계좌 기관 아님" + response.getDepositCorpCode());
        LOGGER.info("다계좌 기관 아님" + cha.getId());
        setupNoManyAccountNo(response);
//            return;
//        }

//        LOGGER.info("다계좌 기관");
//        TreeMap<String, Long> _list = aggregateByAccountId(receiptMasterList);
//        handleManyAccountNo(_list, response);
    }

    private void handleManyAccountNo(TreeMap<String, Long> list, NoticeMessage response) {
        response.setVirtualAccountDivideCount(String.valueOf(list.size()));

        int i = 1;
        for (Map.Entry<String, Long> each : list.entrySet()) {
            response.setupManyAccountNo(i, String.valueOf(each.getValue()), each.getKey());

            i++;
        }
    }

    private void setupNoManyAccountNo(NoticeMessage response) {
        response.setupManyAccountNo(1, "", "");
        response.setupManyAccountNo(2, "", "");
        response.setupManyAccountNo(3, "", "");
        response.setupManyAccountNo(4, "", "");
        response.setupManyAccountNo(5, "", "");
        response.setupManyAccountNo(6, "", "");
        response.setupManyAccountNo(7, "", "");
        response.setupManyAccountNo(8, "", "");
        response.setupManyAccountNo(9, "", "");
        response.setVirtualAccountDivideCount("");
    }

    /**
     * @param msg
     */
    private void insertDepositMsgHistory(NoticeMessage msg, Logger LOG) {
        final String type = "RCP";
        Map<String, Object> corpInfoMap = damoaDao.findAccountInfo(msg.getDepositCorpCode().trim(), StringUtils.trim(msg.getDepositAccountNo()));
        // rcp history 에는 해당 재판매 기관코드로 입력
        damoaDao.insertDepositMsgHistory(nowString(), corpInfoMap.get("chacd").toString().trim(), msg.getDepositAccountNo().trim(), msg.getTransactionAmount(), msg.getAmountOfFee(), type, msg.getDealSeqNo().trim());

    }

    private String nowString() {
        synchronized (MONITOR) {
            return DateUtils.toNowString();
        }
    }

    private void offerMsgBlockingQueue(AggregateQueue<MessageBean> msgBlockingQueue, NoticeMessage noticeMessage, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, List<ReceiptMasterDO> receiptMasterList) throws InterruptedException {
        msgBlockingQueue.put(new NoticeMessageBean(noticeMessage, cha, customer, noticeMasterList, receiptMasterList));
    }

}
