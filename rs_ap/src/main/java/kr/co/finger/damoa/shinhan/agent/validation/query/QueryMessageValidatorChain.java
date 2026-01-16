package kr.co.finger.damoa.shinhan.agent.validation.query;

import kr.co.finger.damoa.shinhan.agent.validation.ValidatorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 수취조회 검증기
 * 
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class QueryMessageValidatorChain extends ValidatorChain {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryMessageValidatorChain.class);

    public QueryMessageValidatorChain() {
        this.addValidator(new QueryChaValidator());
        this.addValidator(new QueryNullValidator());
        this.addValidator(new QueryNameValidator());
        this.addValidator(new QueryTransactionAmountValidator());
    }
}
