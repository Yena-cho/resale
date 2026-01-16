package kr.co.finger.damoa.shinhan.agent.validation.query;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.msg.QueryMessage;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.CustomerDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterDO;
import kr.co.finger.damoa.shinhan.agent.model.ValidationResult;
import kr.co.finger.damoa.shinhan.agent.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 고객 검증기
 *
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class QueryNameValidator implements Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryNameValidator.class);

    @Override
    public ValidationResult validate(List<NotiMas> notiMasList, ChacdInfo chacdInfo, MsgIF msgIF) {
        final QueryMessage noticeMessage = (QueryMessage) msgIF;
        final String value = notiMasList.get(0).getCusname();

        if (StringUtils.isBlank(value) && chacdInfo.isDoAmountCheck()) {
            LOGGER.error("가상계좌번호[" + noticeMessage.getDepositAccountNo() + "] 에 해당하는 사용자가 없음.");
            return new ValidationResult("V401", "가상계좌할당안됨");
        }

        if (StringUtils.isBlank(value) && !chacdInfo.isDoAmountCheck()) {
            LOGGER.info("금액체크하지 않는 기관이므로 사용자 확인은 SKIP...");
            return SUCCESS;
        }

        return SUCCESS;
    }

    @Override
    public ValidationResult validate(MsgIF requestInterface, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
        final QueryMessage request = (QueryMessage) requestInterface;

        if(customer != null && customer.disabled()) {
            LOGGER.debug("[가상계좌:{}] 삭제된 고객", request.getDepositAccountNo());
            return new ValidationResult("V401", "가상계좌할당안됨");
        }

        if(!cha.isEnableValidateAmount()) {
            LOGGER.debug("SET 방식. 계좌 할당 여부과 관계 없음.");
            return SUCCESS;
        }
        
        if(!noticeMasterList.isEmpty()) {
            LOGGER.debug("청원원장 있음");
            return SUCCESS;
        }
        
        if(customer != null) {
            // 위에서 청구 있으면 여기 어차피 안탐..?
            LOGGER.debug("무청구 입금");
            return SUCCESS;
        }
        
        LOGGER.info("가상계좌번호[" + request.getDepositAccountNo() + "] 에 해당하는 사용자가 없음.");
        return new ValidationResult("V401", "가상계좌할당안됨");
    }
}
