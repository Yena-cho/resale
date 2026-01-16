package kr.co.finger.damoa.shinhan.agent.handler.command;

import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Sha256;
import kr.co.finger.damoa.commons.concurrent.AggregateQueue;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.domain.repository.ChaRepository;
import kr.co.finger.damoa.shinhan.agent.domain.repository.ReceiptMasterRepository;
import kr.co.finger.damoa.shinhan.agent.domain.repository.TransApiRelayDataRepository;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

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
public class CancelNoticeMsgCommand extends BasicCommand {
    private static Logger LOGGER = LoggerFactory.getLogger(CancelNoticeMsgCommand.class);

    @Autowired
    private MessageFactory messageFactory;

    @Autowired
    private DamoaDao damoaDao;

    @Autowired
    private AggregateQueue<MessageBean> damoaQueue;

    @Autowired
    private DamoaContext damoaContext;

    @Autowired
    private XchalistMapper xchalistMapper;

    @Autowired
    private ReceiptMasterRepository receiptMasterRepository;

    @Autowired
    private XrcpmasMapper xrcpmasMapper;

    @Autowired
    private TransRcpNoticeCommand transRcpNoticeCommand;
    @Autowired
    private TransApiRelayDataRepository transApiRelayDataRepository;

    @Autowired
    private ChaRepository chaRepository;

    private Object MONITOR = new Object();

    private final NoticeMessageValidatorChain validator;

    private final CommunicateUtil communicateUtil;

    public CancelNoticeMsgCommand(CommunicateUtil communicateUtil) {
        this.communicateUtil = communicateUtil;
        validator = new NoticeMessageValidatorChain();
    }

    @Override
    public void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {
        throw new RuntimeException("폐기된 메소드 호출");
    }

    @Override
    public boolean executeWithPush(MsgIF requestInterface, MsgIF responseInterface) throws Exception {
        try {
            final NoticeMessage request = (NoticeMessage) requestInterface;
            final NoticeMessage response = (NoticeMessage) responseInterface;
            LOGGER.info("<<< [{}]{}", request.getDesc(), request);

            final String messageType = request.getMsgTypeCode() + request.getDealTypeCode();
            TransApiRelayData transApiRelayData = new TransApiRelayData();

            // 1. 전문 형식 검사
            // 1.1. 공통부
            try {
                ResultBean _result = validateCommons(request, messageFactory, damoaContext, LOGGER);
                if ("0".equalsIgnoreCase(_result.getCode()) == false) {
                    throw new DamoaException(_result);
                }
                // 2.2. 기관코드, 계좌번호
                _result = validate(request, messageFactory, damoaContext, LOGGER);
                if ("0".equalsIgnoreCase(_result.getCode()) == false) {
                    throw new DamoaException(_result);
                }
            } finally {
                // 2. 전문 저장
                transApiRelayData = damoaDao.insertTransDataAcct(messageType, request.getDealSeqNo(), Constants.RECEIVE_DESTINATION, request.getOriginMessage());
            }

            // 3. 재처리 여부 확인
            // 재처리일 경우 기존 응답을 그대로 돌려준다
            LOGGER.info("재처리 점검");
            if (damoaContext.hasCancelKey(request)) {
                LOGGER.debug("기 취소된 거래");
                if (isRetryMsg(request.getUsrWorkArea2().trim())) {
                    LOGGER.info("재처리");
                    
                    response.setResCode("0000");
                    // 다계좌 정보를 기존 요청에서 가져온다
                    handleManyAccountNoWithPreviousInfo(response, request.getDealSeqNo(), "0210" + request.getDealTypeCode());
                    LOGGER.info("기존 응답 전송");
                } else {
                    LOGGER.info("재처리 아님");
                    
                    response.setResCode("V848");
                    response.setResMsg("이미 취소되었음..");
                    LOGGER.warn("[V848] 이미 취소되었음.. " + request.getDealSeqNo());
                }
                
                return true;
            }

            // 4. 관련데이터 조회
            // 수납원장 조회

            // 5. 검증
            final ValidationResult result = validate(request, response, request.getDealSeqNo(), request.getDealTypeCode());
            
            if(!result.isValid()) {
                response.setResCode(result.getCode());
                response.setResMsg(result.getMsg());
                return true;
            }

            // 6. 취소 처리
            //processCancel(request, response, request.getDealSeqNo(), request.getDealTypeCode(), result);
            List<String> rcpmasCdList = processCancel(request, response, request.getDealSeqNo(), request.getDealTypeCode(), result);

            // 6.1 수납통지 기관일 경우 중계 서버에 수납 정보 송신
            Map<String, Object> corpInfoMap = damoaDao.findAccountInfo(request.getDepositCorpCode().trim(), StringUtils.trim(request.getDepositAccountNo()));
            String chaCd = corpInfoMap.get("chacd").toString().trim();
            Xchalist xchalist = (Xchalist) xchalistMapper.selectByPrimaryKey(chaCd);
            String rcpmasCd = rcpmasCdList.get(0);

            if(xchalist.getChatrty().equals("05")) {  // 기관 접속형태 [01:웹(WEB), 03:전문(API), 05:수납통지(SET) ]

              //  String rcpmasCd = rcpmasCdList.get(0);

                // 수납통지 기관의 url 가져오기
                String reqUri = xchalist.getRcpNoticeUrl();

                XrcpmasDTO xrcpmasDTO = receiptMasterRepository.getXrcpmasInfo(rcpmasCd);

                transRcpNoticeCommand.transRcpnNotice(reqUri, xrcpmasDTO);
            } else if (xchalist.getChatrty().equals("07")) {
                // 2024.09.23
                // 기관이 데이터 수신시, 데이터의 위변조여부를 체크하기 위해 사용한다.
                request.setChkValue(Sha256.getEncrypt(request.getDepositAccountNo(), request.getTransactionAmount()));

                transApiRelayData.setChacd(chaCd);  // 중계방식 기관인 경우 전송정보기록을 위해 사용
                transApiRelayDataRepository.createTransApiRelayData(transApiRelayData);

                // 기관의 수취조회 API url 가져오기
                final ChaDO cha = chaRepository.findByChaCd(chaCd);
                String cancelUrl = cha.getChaApiUrlNotice();
                LOGGER.info("입금통지 요청을 전송할 기관의 url  ======>  {}", cancelUrl);

                // 입금통지 응답메세지
                NoticeMessage noticeCancelResMsg = communicateUtil.sendCancelRequest(cancelUrl, request);
                response.setResCode(noticeCancelResMsg.getResCode());
                response.setResMsg(noticeCancelResMsg.getResMsg());
                response.setDepositAccountName(noticeCancelResMsg.getDepositAccountName());

//                if (response.getResCode().equals("0000")) {// 응답코드 - (0000 : 정상거래)
//                    result = new ValidationResult();
//                }
//                else {
//                    LOGGER.info("불능: {}", response.getResMsg());
//                    result = new ValidationResult( response.getResCode(), response.getResMsg());
//                }

                LOGGER.debug("== 입금통지 응답코드 : {}", result.getCode());
                LOGGER.debug("== 입금통지 응답메세지 : {}", result.getMsg());
                LOGGER.debug("== 입금통지 응답 검증결과 : {}", result.isValid());

                LOGGER.debug("== 입금통지 응답메세지 : {}", response.toString());

            }

            // 7. 응답 구성
        } catch (DamoaException e) {
            throw e;
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(e.getMessage(), e);
            } else {
                LOGGER.error(e.getMessage());
            }

            throw e;
        }

        return true;
    }

    private ValidationResult validate(NoticeMessage request, NoticeMessage response, String dealSeqNo, String dealTypeCode) {
        LOGGER.info("검증");
        // 취소할 전문이 있는지 확인
        if (damoaContext.containsSeqNoAtCancel(dealSeqNo, "0200" + dealTypeCode) == false) {
            LOGGER.error("[V601]취소할 원거래없음 " + dealSeqNo);
            return new ValidationResult("V601", "취소할 원거래없음.");
        }

        return new ValidationResult();
    }

    //private void processCancel(NoticeMessage request, NoticeMessage response, String dealSeqNo, String dealTypeCode, ValidationResult result) throws Exception {
    private List<String> processCancel(NoticeMessage request, NoticeMessage response, String dealSeqNo, String dealTypeCode, ValidationResult result) throws Exception {
        if (!result.isValid()) {
            return null;
        }

        LOGGER.info("취소 처리");

        final String nowDate = DateUtils.toDateString(new Date());
        final CancelBean cancelBean = damoaContext.findCancel(dealSeqNo, nowDate);
        if (cancelBean == null) {
            // 이경우 스케쥴러에서 취소처리를 할 필요가 없음..
            handleTranDataAcct(request);
            LOGGER.error("취소할 수 있는 수납 정보가 없음 .. [시간]" + nowDate + "[거래일련번호]" + dealSeqNo);
            // 여기에서 원거래 전문 취소시킴...
            damoaContext.updateMsgCancel(dealSeqNo, nowDate);

            handleManyAccountNoWithPreviousInfo(response, dealSeqNo, "0210" + dealTypeCode);

            return null;
        }

        /////////////////////////////////////////////////////
        // 아래 비즈니스로직은 스케쥴러로 이동시켜야 함.
        LOGGER.info("취소할 수 있는 정보 " + cancelBean.toString());
        final List<CancelPair> pairs = cancelBean.getCancelPairs();

        // 취소대상 수납원장코드 저장
        List<String> rcpmasCdList = new ArrayList<>();


        for (CancelPair pair : pairs) {
            try {
                LOGGER.info("취소처리 " + pair);
                String rcpMasCd = pair.getRcpMasCd();
                String notiMasCd = pair.getNotiMasCd();
                // 수납은 하나임..
                LOGGER.info("취소처리 수납건.. " + rcpMasCd);
                updateRcpCancel(rcpMasCd);

                // 청구의 수납 취소는 비동기로 처리
                // 현금영수증 삭제는 비동기로 처리
                final CancelMessageBean messageBean = new CancelMessageBean(rcpMasCd, notiMasCd);
                damoaQueue.put(messageBean);
                rcpmasCdList.add(rcpMasCd);

                LOGGER.info("취소처리완료 " + rcpMasCd);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                LOGGER.info("취소처리중 오류 발생.. " + pair);
            }
        }

        LOGGER.info("수납취소.. " + pairs.size());

        // 집계용 데이터 생성
        // 취소 사유 코드
        final String cancelCode = request.getCancelReasonCode().trim();
        Map<String, Object> corpInfoMap = damoaDao.findAccountInfo(request.getDepositCorpCode().trim(), StringUtils.trim(request.getDepositAccountNo()));
        if ("0000".equalsIgnoreCase(cancelCode) || "".equalsIgnoreCase(cancelCode)) {
            // 정상적 취소
            damoaDao.insertDepositMsgHistory(nowString(), corpInfoMap.get("chacd").toString().trim(), request.getDepositAccountNo().trim(), request.getTransactionAmount(), request.getAmountOfFee(), "CNCL", request.getDealSeqNo().trim());
        } else {
            // 강제 취소의 경우 집계에서 제외한다
            //TODO if조건 바꿔서 테스트 필요************************
            request.setDepositCorpCode(corpInfoMap.get("chacd").toString().trim());
            damoaDao.removeDepositMsgHistory(request);
        }

        // 취소전문의 원거래 응답을 찾아 설정함.
        handleManyAccountNoWithPreviousInfo(response, dealSeqNo, "0210" + dealTypeCode);

        return rcpmasCdList;
    }

    private void handleManyAccountNoWithPreviousInfo(NoticeMessage response, String dealSeqNo, String msgType) {
        NoticeMessage _notice = findNoticeMessage(dealSeqNo, msgType);
        handleAccountNameWithPreviousInfo(response, _notice);
        handleManyAccountNo(response, _notice);
    }

    private void handleAccountNameWithPreviousInfo(NoticeMessage response, NoticeMessage before) {
        response.setDepositAccountName(before.getDepositAccountName());

    }

    private void handleManyAccountNo(NoticeMessage target, NoticeMessage source) {
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


    private void updateRcpCancel(String rcpMasCd) {
        damoaDao.updateRcpCancel(rcpMasCd);
        damoaDao.updateRcpDetCancel(rcpMasCd);
        LOGGER.info("수납 취소 처리... " + rcpMasCd);
    }

    private void handleTranDataAcct(NoticeMessage noticeMessage) throws Exception {
        insertTransDataAcct(damoaDao, getMsgType(noticeMessage), noticeMessage.getDealSeqNo(), Constants.RECEIVE_DESTINATION, noticeMessage.getOriginMessage(), LOGGER);
    }

    private String nowString() {
        synchronized (MONITOR) {
            return DateUtils.toNowString();
        }
    }

}
