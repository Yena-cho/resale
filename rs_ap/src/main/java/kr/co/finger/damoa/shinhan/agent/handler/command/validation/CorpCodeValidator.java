package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 집계대사...
 */
public class CorpCodeValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String corpCode =  (String)getValue(commonMessage,"getCorpCode",LOG);
        return validateCorpCode(damoaContext, corpCode, LOG);
    }
}
