package kr.co.finger.damoa.shinhan.agent.validation;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.CustomerDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterDO;
import kr.co.finger.damoa.shinhan.agent.model.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 기본 검증
 * 
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public abstract class ValidatorChain implements Validator {
    private List<Validator> validatorList;
    
    public ValidatorChain() {
        validatorList = new ArrayList<>();
    }
    
    protected void addValidator(Validator validator) {
        validatorList.add(validator);
    }

    @Override
    public ValidationResult validate(MsgIF requestInterface, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
        for (Validator validator : validatorList) {
            ValidationResult result = validator.validate(requestInterface, cha, customer, noticeMasterList, limitMap);
            if (result.isValid()) {
                continue;
            }

            return result;
        }

        return SUCCESS;
    }
    
    @Override
    @Deprecated
    public ValidationResult validate(List<NotiMas> noticeMasterList, ChacdInfo chacdInfo, MsgIF msgIF) {
        throw new RuntimeException("폐기된 메소드");
    }
}
