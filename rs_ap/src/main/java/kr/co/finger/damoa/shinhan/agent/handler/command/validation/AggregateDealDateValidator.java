package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 집계대사 거래일자...
 * 미래날짜인지 확인...
 */
public class AggregateDealDateValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String dealDate = (String)getValue(commonMessage,"getDealDate",LOG);
        String now = DateUtils.toDateString(new Date());
        if (dealDate.compareTo(now)>0) {
            LOG.error("거래일자가 내일 이후 날짜임.. "+dealDate);
            return new ResultBean("F021");
        }else {
            return new ResultBean();
        }
    }
}
