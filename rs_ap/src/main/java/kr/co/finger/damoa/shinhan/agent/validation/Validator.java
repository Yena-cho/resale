package kr.co.finger.damoa.shinhan.agent.validation;

import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.damoa.model.rcp.ChacdInfo;
import kr.co.finger.damoa.model.rcp.NotiMas;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.CustomerDO;
import kr.co.finger.damoa.shinhan.agent.domain.model.NoticeMasterDO;
import kr.co.finger.damoa.shinhan.agent.model.ValidationResult;

import java.util.List;
import java.util.Map;

public interface Validator {

    ValidationResult validate(List<NotiMas> notiMasList, ChacdInfo chacdInfo, MsgIF msgIF);
    ValidationResult validate(MsgIF request, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap);

    ValidationResult SUCCESS = new ValidationResult();
    Validator DUMMY = new Validator() {
        @Override
        public ValidationResult validate(List<NotiMas> notiMasList, ChacdInfo chacdInfo, MsgIF msgIF) {
            return new ValidationResult();
        }

        @Override
        public ValidationResult validate(MsgIF request, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
            // never occur
            throw new RuntimeException();
        }
    };
}
