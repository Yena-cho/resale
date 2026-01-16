package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 발생구분
 */
public class OccurGubunValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage)object;
        String occurGubun = commonMessage.getOccurGubun();
        LOG.debug("발생구분 "+occurGubun);
        if ("1".equalsIgnoreCase(occurGubun)
                || "2".equalsIgnoreCase(occurGubun)
                || "3".equalsIgnoreCase(occurGubun)
                || "0".equalsIgnoreCase(occurGubun)) {
            return new ResultBean();

        } else {
            LOG.error("비정상 발생구분 "+occurGubun);
            return new ResultBean("F015");
        }
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
