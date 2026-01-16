package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 입금계좌번호..
 */
public class TransactionAmountValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String transactionAmount = (String)getValue(commonMessage,"getTransactionAmount",LOG);
        long amount = Long.valueOf(transactionAmount);
        if (amount == 0) {
            LOG.error("거래금액이 0임");
            return new ResultBean("V713", "거래금액이 0임");
        } else {
            return new ResultBean();
        }
    }
}
