package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 입금계좌번호..
 */
public class DepositAccountNoValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String corpCode = (String)getValue(commonMessage,"getDepositCorpCode",LOG);
        String accountNo = (String)getValue(commonMessage,"getDepositAccountNo",LOG);
        Map<String, Object> map =  damoaContext.findAccountNo(corpCode.trim(),accountNo.trim());
        if (map == null) {
            LOG.error("해당가상계좌번호확인하세요 " + corpCode + " " + accountNo);
            return new ResultBean("V891", "해당가상계좌번호확인하세요");
        } else {
            LOG.debug("가상계좌번호 정상 " + corpCode + " " + accountNo);
            return new ResultBean();
        }
    }
}
