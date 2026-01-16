package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 거래일자...
 * 통신망전문 처리.. 오늘 날짜이어야 함.
 */
public class DealDateValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String dealDate = (String)getValue(commonMessage,"getDealDate",LOG);
        String now = DateUtils.toDateString(new Date());
        if (dealDate.equalsIgnoreCase(now)) {
            return new ResultBean();
        }else {
            LOG.error("당일일자가 아님 "+dealDate);
            return new ResultBean("F021","거래일자가 당일일자가 아님");
        }
    }

}
