package kr.co.finger.damoa.shinhan.agent.validation.notice;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.CustomerDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterDO;
import kr.co.finger.damoa.shinhan.agent.model.ValidationResult;
import kr.co.finger.damoa.shinhan.agent.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 청구 검증
 * 
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class NoticeNullValidator implements Validator{
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeNullValidator.class);
    
    @Override
    @Deprecated
    public ValidationResult validate(List<NotiMas> notiMasList, ChacdInfo chacdInfo, MsgIF msgIF) {
        NoticeMessage noticeMessage = (NoticeMessage) msgIF;
        
        if (notiMasList == null || notiMasList.size()==0) {
            LOGGER.error("가상계좌번호[" + noticeMessage.getDepositAccountNo().trim() + "] 에 해당하는 청구가 없음.");
            return new ValidationResult("V814","거래가능원장없음");
        }

        return SUCCESS;
    }

    @Override
    public ValidationResult validate(MsgIF requestInterface, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
        if(!cha.isEnableValidateAmount()) {
            LOGGER.debug("SET 방식. 청구 유무 관계 없이 입금 가능");
            return SUCCESS;
        }

        if(noticeMasterList == null || noticeMasterList.isEmpty()) {
            LOGGER.info("가상계좌번호에 해당하는 청구가 없음.");
            return new ValidationResult("V814","거래가능원장없음");
        }

        return SUCCESS;
    }

}
