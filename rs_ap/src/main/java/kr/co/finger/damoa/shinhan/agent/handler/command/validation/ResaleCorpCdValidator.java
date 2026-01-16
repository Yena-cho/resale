package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 입금계좌번호..
 */
public class ResaleCorpCdValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String corpCode = (String)getValue(commonMessage,"getDepositCorpCode",LOG);
        String accountNo = (String)getValue(commonMessage,"getDepositAccountNo",LOG);
        Map<String, Object> map =  damoaContext.findCorpCdByVanoAndFgcd(corpCode.trim(),accountNo.trim());
        if (map == null) {
            LOG.error("해당가상계좌번호확인하세요 " + corpCode + " " + accountNo);
            return new ResultBean("V891", "해당가상계좌번호확인하세요");
        } else if(map.get("useyn").equals("N")) {
            LOG.error("해당가상계좌번호확인하세요 " + corpCode + " " + accountNo);
            return new ResultBean("V891", "해당가상계좌번호확인하세요");
        } else {
            LOG.debug("가상계좌번호 정상 " + corpCode + " " + accountNo);
        }

        Map<String, Object> map2 = damoaContext.findChaSimpleInfo(corpCode);
        if (map2 == null) {
            LOG.error("해당기관 없음");
            return new ResultBean("V818", "해당기관 없음");
        } else {
            String chast = Maps.getValue(map, "chast");
            if ("ST02".equalsIgnoreCase(chast)) {
                LOG.error("해지된 기관 ... " + corpCode);
                return new ResultBean("V841", "해지된 기관");
            } else if ("ST04".equals(chast) || "ST07".equalsIgnoreCase(chast) || "ST08".equalsIgnoreCase(chast)) {
                LOG.error("정지된 기관 ... " + corpCode);
                return new ResultBean("V818", "해당기관 없음");
            } else if ("ST05".equals(chast) || "ST01".equalsIgnoreCase(chast)) {
                LOG.error("대기중 기관 ... " + corpCode);
                return new ResultBean("V818", "해당기관 없음");
            } else {
                LOG.debug("정상 기관 ... " + corpCode);
                return new ResultBean();
            }
        }


    }
}
