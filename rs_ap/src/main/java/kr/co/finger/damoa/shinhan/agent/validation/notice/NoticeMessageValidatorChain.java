package kr.co.finger.damoa.shinhan.agent.validation.notice;

import kr.co.finger.damoa.shinhan.agent.validation.ValidatorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 입금 전문 검증
 * 
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class NoticeMessageValidatorChain extends ValidatorChain {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeMessageValidatorChain.class);

    public NoticeMessageValidatorChain() {
        this.addValidator(new NoticeChaValidator());
        this.addValidator(new NoticeNullValidator());
        this.addValidator(new NoticeNameValidator());
        this.addValidator(new NoticeTransactionAmountDateValidator());
    }
}
