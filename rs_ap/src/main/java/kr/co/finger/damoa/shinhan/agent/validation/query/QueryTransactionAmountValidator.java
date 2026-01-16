package kr.co.finger.damoa.shinhan.agent.validation.query;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.msg.QueryMessage;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.CustomerDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeDetailsDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterDO;
import kr.co.finger.damoa.shinhan.agent.model.ValidationResult;
import kr.co.finger.damoa.shinhan.agent.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 거래금액 검증기
 *
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class QueryTransactionAmountValidator implements Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryTransactionAmountValidator.class);
    
    @Override
    public ValidationResult validate(List<NotiMas> notiMasList, ChacdInfo chacdInfo, MsgIF msgIF) {
        throw new RuntimeException("폐기된 인터페이스");
    }

    @Override
    public ValidationResult validate(MsgIF requestInterface, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
        final QueryMessage noticeMessage = (QueryMessage) requestInterface;

        // 기관코드
        final String corpCode = cha.getId();
        // 거래금액
        final long transactionAmount = NumberUtils.toLong(StringUtils.trim(noticeMessage.getTransactionAmount()));

        final long sumamt = NumberUtils.toLong(limitMap.get("sumamt").toString());
        final long sumcnt = NumberUtils.toLong(limitMap.get("sumcnt").toString());

        LOGGER.debug("거래금액: {}", transactionAmount);
        LOGGER.debug("기관정보[{}]: {}", corpCode, cha);

//        if (!cha.isEnableValidateAmount()) {
        if (cha.getAmtchkty().equals("N")) {
            // 금액, 체크하지 않을 때.
            LOGGER.info("입금방식 SET {}", cha.getAmtchkty());
            return SUCCESS;
        }

        // 한도방식일 경우
        if(cha.getAmtchkty().equals("L")) {
            // 1회 한도가 0이 아니고 거래금액보다 크거나 같은경우
            if(!cha.getLimitOnce().equals("0") && Long.parseLong(cha.getLimitOnce()) < transactionAmount){
                LOGGER.info("1회 한도 초과 {} < {}", cha.getLimitOnce(), transactionAmount);
                return new ValidationResult("V404", "수취계좌입금한도초과");
            }

            // 1일 한도가 0이 아니고 거래일 총금액+거래금액보다 크거나 같은경우
            if(!cha.getLimitDay().equals("0") && Long.parseLong(cha.getLimitDay()) < transactionAmount+sumamt){
                LOGGER.info("1일 한도 초과 {} < {}", cha.getLimitDay(), transactionAmount);
                return new ValidationResult("V404", "수취계좌입금한도초과");
            }

            // 1일 횟수가 0이 아니고 거래일 총횟수+1 보다 크거나 같은경우
            if(!cha.getLimitDayCnt().equals("0") && Long.parseLong(cha.getLimitDayCnt()) < sumcnt+1){
                LOGGER.info("1일 횟수 초과 {} < {}", cha.getLimitDayCnt(), sumcnt+1);
                return new ValidationResult("V404", "수취계좌입금한도초과");
            }
            return SUCCESS;
        }

        if (transactionAmount < 2) {
            LOGGER.debug("기관금액체크 이지만 체크금액이 0 또는 1이면 통과");
            return SUCCESS;
        }

        // 금액체크시
        LOGGER.info("거래금액: {}", transactionAmount);
        for (NoticeMasterDO each : noticeMasterList) {
            final List<Pair<Long, List<NoticeDetailsDO>>> remainList = each.generateRemainList(cha);

            // 입금 가능 여부 확인
            LOGGER.debug("[청구원장#{}] 납부가능금액: {}", each.getId(), remainList);
            for (Pair<Long, List<NoticeDetailsDO>> eachRemain : remainList) {
                if (transactionAmount != eachRemain.getLeft()) {
                    LOGGER.debug("납부금액 상이: {}", eachRemain.getLeft());
                    continue;
                }

                LOGGER.info("납부금액 일치: {}", eachRemain);
                return SUCCESS;
            }
        }

        LOGGER.info("납부금액 일치하는 청구원장 없음");
        return new ValidationResult("V407", "납부금액다름");
    }
}
