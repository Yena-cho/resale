package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaConstants;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgCorpCodeValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage)object;
        String corpCode = commonMessage.getMsgSndRcvCorpCode();
        LOG.debug("전문송수신기관코드 "+corpCode);
        if (DamoaConstants.FINGER.equalsIgnoreCase(corpCode) || DamoaConstants.NICE.equalsIgnoreCase(corpCode)) {
            return new ResultBean();
        } else {
            LOG.error("비정상 전문송수신기관코드 "+corpCode);
            return new ResultBean("V819","기관코드 오류");
        }
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
