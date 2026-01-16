package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class MsgSndDateValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage)object;
        String msgSndDate = commonMessage.getMsgSndDate();
        LOG.debug("전문전송일시 "+msgSndDate);
        String now = DateUtils.toDateString(new Date());
        if (msgSndDate.startsWith(now)) {
            return new ResultBean();
        }else {
            LOG.error("비정상(오늘이아님) 전문전송일시 "+msgSndDate);
            return new ResultBean("F009","비정상(오늘이아님) 전문전송일시");
        }

    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
