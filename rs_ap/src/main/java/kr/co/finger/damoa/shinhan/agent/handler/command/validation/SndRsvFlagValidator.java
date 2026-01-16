package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SndRsvFlagValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage)object;
        String msgTypecode = commonMessage.getMsgTypeCode();
        if("0210".equalsIgnoreCase(msgTypecode)
                || "0710".equalsIgnoreCase(msgTypecode)
                || "0810".equalsIgnoreCase(msgTypecode)
                ){
            LOG.info("응답전문은 체크하지 않음.. "+msgTypecode);
            return new ResultBean();
        }else {
            String sndRsvFlag = commonMessage.getSndRcvFlag();
            LOG.debug("송수신 FLAG "+sndRsvFlag);
            if ("2".equalsIgnoreCase(sndRsvFlag)) {
                return new ResultBean();
            } else {
                LOG.error("비정상 송수신 FLAG "+sndRsvFlag);
                return new ResultBean("F004");
            }
        }

    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
