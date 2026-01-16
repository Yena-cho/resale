package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 입금기관코드 체크..
 */
public class DepositCorpCodeValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        LOG.info("입금기관코드 호출됨....");
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String corpCode =  (String)getValue(commonMessage,"getDepositCorpCode",LOG);
        return validateCorpCode(damoaContext, corpCode, LOG);

    }
}
