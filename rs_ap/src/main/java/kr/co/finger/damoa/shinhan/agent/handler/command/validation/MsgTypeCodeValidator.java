package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgTypeCodeValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage)object;
        String msgTypeCode = commonMessage.getMsgTypeCode();
        LOG.debug("전문구분코드 "+msgTypeCode);
        if ("0200".equalsIgnoreCase(msgTypeCode)
                || "0210".equalsIgnoreCase(msgTypeCode)
                || "0400".equalsIgnoreCase(msgTypeCode)
                || "0410".equalsIgnoreCase(msgTypeCode)
                || "0700".equalsIgnoreCase(msgTypeCode)
                || "0710".equalsIgnoreCase(msgTypeCode)
                || "0800".equalsIgnoreCase(msgTypeCode)
                || "0810".equalsIgnoreCase(msgTypeCode)
                ) {
            return new ResultBean();
        } else {
            LOG.error("비정상 전문구분코드 "+msgTypeCode);
            return new ResultBean("F005", "비정상 전문구분코드");
        }
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
