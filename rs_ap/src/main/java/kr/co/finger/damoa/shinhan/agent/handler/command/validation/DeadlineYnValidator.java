package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadlineYnValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage)object;
        String deadlineYn = commonMessage.getDeadlineYn();
        LOG.debug("deadlineYn(1|0) "+deadlineYn);
        if ("1".equalsIgnoreCase(deadlineYn) || "0".equalsIgnoreCase(deadlineYn)) {
            LOG.debug("마감전후 정상...");
            return new ResultBean();
        } else {
            LOG.error("비정상 마감전후 "+deadlineYn);
            return new ResultBean("F014");
        }
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
