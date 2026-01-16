package kr.co.finger.damoa.shinhan.agent.handler.command;

import kr.co.finger.damoa.commons.Constants;
import kr.co.finger.damoa.commons.Sha256;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.msg.QueryMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.dao.DamoaDao;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.CustomerDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterUtils;
import kr.co.finger.damoa.shinhan.agent.domain.repository.ChaRepository;
import kr.co.finger.damoa.shinhan.agent.domain.repository.CustomerRepository;
import kr.co.finger.damoa.shinhan.agent.domain.repository.NoticeMasterRepository;
import kr.co.finger.damoa.shinhan.agent.domain.repository.TransApiRelayDataRepository;
import kr.co.finger.damoa.shinhan.agent.handler.DamoaException;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import kr.co.finger.damoa.shinhan.agent.model.ValidationResult;
import kr.co.finger.damoa.shinhan.agent.util.CommunicateUtil;
import kr.co.finger.damoa.shinhan.agent.validation.query.QueryMessageValidatorChain;
import kr.co.finger.msgagent.client.NettyProducer;
import kr.co.finger.msgagent.layout.MessageFactory;
import kr.co.finger.shinhandamoa.data.table.model.TransApiRelayData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

/**
 * 계좌수취조회 처리. 컨트롤러 역할.
 *
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
@Component
public class QueryMsgCommand extends BasicCommand {
    private static Logger LOGGER = LoggerFactory.getLogger(QueryMsgCommand.class);

    @Autowired
    private MessageFactory messageFactory;

    @Autowired
    private DamoaDao damoaDao;

    @Autowired
    private DamoaContext damoaContext;

    @Autowired
    private NoticeMasterRepository noticeMasterRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ChaRepository chaRepository;

    private QueryMessageValidatorChain validator;

    @Autowired
    private TransApiRelayDataRepository transApiRelayDataRepository;

    public QueryMsgCommand() {
        validator = new QueryMessageValidatorChain();
    }

    @Autowired
    private CommunicateUtil communicateUtil;

    @Override
    @Deprecated
    public void execute(MsgIF msgIF, ConcurrentMap<String, CompletableFuture<MsgIF>> concurrentMap, NettyProducer nettyProducer) throws Exception {
        throw new RuntimeException("폐기된 메소드 호출");
    }

    @Override
    public boolean executeWithPush(MsgIF requestInterface, MsgIF responseInterface) throws Exception {
        try {

            final QueryMessage request = (QueryMessage) requestInterface;
            final QueryMessage response = (QueryMessage) responseInterface;
            //QueryMessage response = (QueryMessage) responseInterface;

            LOGGER.info("<<< [{}]{}", request.getDesc(), request);

            final String messageType = request.getMsgTypeCode() + request.getDealTypeCode();
            TransApiRelayData transApiRelayData = new TransApiRelayData();
            ValidationResult result = new ValidationResult();  // 거래검증결과

            // 1. 전문 형식 검사
            try {
                if (StringUtils.equals(request.getMsgTypeCode(), "0210")) {
                    LOGGER.warn("응답전문은 처리하지 않음");
                    return true;
                }
                ResultBean _result = validateCommons(request, messageFactory, damoaContext, LOGGER); // 전문의
                if ("0".equalsIgnoreCase(_result.getCode()) == false) {
                    throw new DamoaException(_result);
                }
                // 계좌번호 사용여부 확인
                _result = validate(request, messageFactory, damoaContext, LOGGER);
                if ("0".equalsIgnoreCase(_result.getCode()) == false) {
                    throw new DamoaException(_result);
                }
            } finally {
                // 2. 전문 저장
                // damoaDao.insertTransDataAcct(messageType, request.getDealSeqNo(), Constants.RECEIVE_DESTINATION, request.getOriginMessage());
                transApiRelayData = damoaDao.insertTransDataAcct(messageType, request.getDealSeqNo(), Constants.RECEIVE_DESTINATION, request.getOriginMessage());
            }

            // 3. 관련 데이터 조회
            final String fgCd = request.getDepositCorpCode();
            final String accountNo = StringUtils.trim(request.getDepositAccountNo());
            // 가상계좌 + 핑거코드(20008153, 20008155) 로 해당 기관코드 검색
            final String chaCd = chaRepository.findChacdByAccountNoAndFingerCd(fgCd, accountNo);
            LOGGER.debug("재판매 기관코드 {}" , chaCd);
            // 3.1. 기관 조회
            final ChaDO cha = chaRepository.findByChaCd(chaCd);

            // 2024.08.23 - 추가
            // 중계방식 기관일 경우 중계 서버에 수취조회 요청정보 송신
            if(cha.getAccessTypeCode().equals("07")) { // 기관 접속형태 [01:웹(WEB), 03:전문(API), 05:수납통지(SET), 07:중계(RELAY)]

                // 2024.09.23
                // 기관이 데이터 수신시, 데이터의 위변조여부를 체크하기 위해 사용한다.
                request.setChkValue(Sha256.getEncrypt(request.getDepositAccountNo(), request.getTransactionAmount()));

                transApiRelayData.setChacd(chaCd);  // 중계방식 기관인 경우 전송정보기록을 위해 사용
                transApiRelayDataRepository.createTransApiRelayData(transApiRelayData);

                // 기관의 수취조회 API url 가져오기
                String queryUrl = cha.getChaApiUrlQuery();
                LOGGER.info("수취조회 요청을 전송할 기관의 url  ======>  {}", queryUrl);

                // 수취조회 응답메세지
                QueryMessage queryResMsg = communicateUtil.sendQueryRequest(queryUrl, request);
                response.setResCode(queryResMsg.getResCode());
                response.setResMsg(queryResMsg.getResMsg());

                result = new ValidationResult();
                composeResponseToRelayCha(response, result, queryResMsg.getDepositAccountName());

            }
            else {
                // 3.3. 고객 조회
                final CustomerDO customer = customerRepository.findByAccountNo(chaCd, accountNo);
                // 3.3. 청구 조회
                final List<NoticeMasterDO> noticeMasterList = noticeMasterRepository.findActiveByAccountNo(chaCd, accountNo);
                // 3.4 한도방식인 경우 가상계좌별 일 한도 조회
                final Map<String, Object> limitMap = damoaDao.getLimitPerVano(accountNo);
                // 4. 거래 검증
                //final ValidationResult result = validate(request, cha, customer, noticeMasterList, limitMap);
                result = validate(request, cha, customer, noticeMasterList, limitMap);
                // 5. 결과 생성
                composeResponse(response, result, cha, customer, noticeMasterList);
            }
            return true;
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
    }

    private void composeResponse(QueryMessage response, ValidationResult result, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList) {
        if (!result.isValid()) {
            String msg = result.getMsg();
            LOGGER.info("불능: {}", msg);
        } else {
            LOGGER.info("정상");
            // 수취인명
            handleAccountName(response, cha, customer, noticeMasterList);
        }

        response.setResCode(result.getCode());
        response.setResMsg(result.getMsg());
    }

    private void composeResponseToRelayCha(QueryMessage response, ValidationResult result, String depositAccountName) {
        if(!response.getResCode().equals("0000") ) {// 응답코드 - (0000 : 정상거래)
            String msg = result.getMsg();
            LOGGER.info("불능: {}", msg);
        }
        else {
            LOGGER.info("정상");
            // 수취인명
            response.setDepositAccountName(depositAccountName);
        }

        response.setResCode(result.getCode());
        response.setResMsg(result.getMsg());
    }

    private ValidationResult validate(QueryMessage request, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
        return validator.validate(request, cha, customer, noticeMasterList, limitMap);
    }

    private void handleAccountName(QueryMessage response, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList) {
        if (StringUtils.equals(cha.getAccountDisplayNameType(), "C")) {
            response.setDepositAccountName(split(cha.getName()));
            return;
        }

        final String noticeCustomerName = NoticeMasterUtils.aggregateNoticeCustomerName(noticeMasterList);
        
        if (StringUtils.isNotBlank(noticeCustomerName)) {
            response.setDepositAccountName(split(noticeCustomerName));
            return;
        }

        final String customerName;
        if(customer == null) {
            customerName = null;
        } else {
            customerName = customer.getName();
        }
        final String chaName = cha.getName();
        final String actualName = StringUtils.defaultIfBlank(customerName, chaName);
        response.setDepositAccountName(split(actualName));
    }
}
