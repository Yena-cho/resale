package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 매체구분
 */
public class MediaGubunValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage)object;
        String mediaGubun = commonMessage.getMediaGubun();
        LOG.debug("매체구분 "+ mediaGubun);
        if ("1".equalsIgnoreCase(mediaGubun)
                || "2".equalsIgnoreCase(mediaGubun)
                || "3".equalsIgnoreCase(mediaGubun)
                || "4".equalsIgnoreCase(mediaGubun)
                || "5".equalsIgnoreCase(mediaGubun)
                || "6".equalsIgnoreCase(mediaGubun)
                || "7".equalsIgnoreCase(mediaGubun)
                || "8".equalsIgnoreCase(mediaGubun)
                || "9".equalsIgnoreCase(mediaGubun)
                || "0".equalsIgnoreCase(mediaGubun)
                || "M".equalsIgnoreCase(mediaGubun)
                ) {
            return new ResultBean();

        } else {
            LOG.error("비정상 매체구분 "+mediaGubun);
            return new ResultBean("F016");
        }
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
