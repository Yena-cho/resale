package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DealTypeCodeValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage)object;

        String dealTypeCode = commonMessage.getDealTypeCode();
        LOG.debug("거래구분코드 "+dealTypeCode);
        if ("1100".equalsIgnoreCase(dealTypeCode)
                || "3110".equalsIgnoreCase(dealTypeCode)
                || "4110".equalsIgnoreCase(dealTypeCode)
                || "6320".equalsIgnoreCase(dealTypeCode)
                || "1100".equalsIgnoreCase(dealTypeCode)
                ) {
            return new ResultBean();
        } else {
            LOG.error("비정상 거래구분코드 "+dealTypeCode);
            return new ResultBean("F006");
        }
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
