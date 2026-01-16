package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.model.msg.CommonMessage;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import kr.co.finger.damoa.shinhan.agent.util.DamoaBizUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DealSeqNoValidator implements Validator {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public ResultBean validate(Object object, DamoaContext damoaContext) {
        CommonMessage commonMessage = (CommonMessage) object;
        String dealSeqNo = commonMessage.getDealSeqNo().trim();
        LOG.debug("거래일련번호 " + dealSeqNo);
        String msgTypeCode = commonMessage.getMsgTypeCode();
        String userWorkArea2 = commonMessage.getUsrWorkArea2().trim();
        if ("0400".equalsIgnoreCase(msgTypeCode)) {
            //취소전문일 때에는 여기에서 중복체크 하지 않음..
            return new ResultBean();
        } else {
            if (DamoaBizUtil.isRetryMsg(userWorkArea2)) {
                return new ResultBean();
            } else {
                if (damoaContext.containsKeyAtSeqNo(dealSeqNo)) {
                    LOG.error("거래일련번호 중복됨 ... " + dealSeqNo);
                    return new ResultBean("F008","거래일련번호 중복");
                } else {
                    return new ResultBean();
                }
            }

        }

    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        return null;
    }
}
