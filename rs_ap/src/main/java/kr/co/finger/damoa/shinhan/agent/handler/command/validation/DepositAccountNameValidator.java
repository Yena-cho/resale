package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 입금계좌명..
 */
public class DepositAccountNameValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String vano = (String)getValue(commonMessage,"getDepositAccountNo",LOG);
        String corpInfo = (String)getValue(commonMessage,"getDepositCorpCode",LOG);
        String name = damoaContext.findCusName(vano);

        if (name == null) {
            Map<String,Object> map = damoaContext.findChaSimpleInfo(corpInfo.trim());
            String amountChk = Maps.getValue(map, "amtchkty");
            if ("N".equalsIgnoreCase(amountChk)) {
                return new ResultBean();
            } else {
                LOG.error("수취인 성명 오류.. "+vano);
                return new ResultBean("V405","수취인 성명 오류");

            }
        } else {
            return new ResultBean();
        }
    }
}
