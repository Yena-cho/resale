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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 기관 검증
 * 
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class NoticeChaValidator implements Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeChaValidator.class);

    @Override
    @Deprecated
    public ValidationResult validate(List<NotiMas> notiMasList, ChacdInfo chacdInfo, MsgIF msgIF) {
        final NoticeMessage noticeMessage = (NoticeMessage) msgIF;
        final String corpCode = noticeMessage.getDepositCorpCode().trim();

        if (chacdInfo == null) {
            LOGGER.error("기관정보 없음 [{}]", corpCode);
            return new ValidationResult("V818", "해당기관이없음.");
        }

        final String chast = StringUtils.upperCase(chacdInfo.getChast());

        return validateChaStatus(corpCode, chast);
    }

    @Override
    public ValidationResult validate(MsgIF requestInterface, ChaDO cha, CustomerDO customer, List<NoticeMasterDO> noticeMasterList, Map<String, Object> limitMap) {
        final NoticeMessage request = (NoticeMessage) requestInterface;
//        final String chaCd = request.getDepositCorpCode();
        final String chaCd = cha.getId();
        LOGGER.debug("[기관#{}]", chaCd);

        if(cha == null) {
            LOGGER.error("기관정보 없음");
            return new ValidationResult("V818", "해당기관이없음.");
        }

        final String chaStatus = StringUtils.upperCase(cha.getStatus());
        LOGGER.debug("[기관#{}] chast={}", chaCd, chaStatus);

        return validateChaStatus(chaCd, chaStatus);
    }

    private ValidationResult validateChaStatus(String corpCode, String chaStatus) {
        final ValidationResult validationResult;
        switch(chaStatus) {
            case "ST02":
                LOGGER.warn("삭제된 기관");
                validationResult = new ValidationResult("V841", "삭제된기관");
                break;
            case "ST04":
                LOGGER.warn("정지된 기관");
                validationResult = new ValidationResult("V841", "해지기관");
                break;
            case "ST08":
                LOGGER.warn("정지된 기관");
                validationResult = new ValidationResult("V841", "정지기관");
                break;
            default:
                LOGGER.info("정상 기관");
                validationResult = SUCCESS;
        }

        return validationResult;
    }
}
