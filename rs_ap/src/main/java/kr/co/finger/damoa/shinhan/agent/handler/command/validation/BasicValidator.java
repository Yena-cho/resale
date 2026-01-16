package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;

import java.util.Map;

public abstract class BasicValidator implements Validator {

    String getValue(Object obj, String key, Logger LOG) {
        try {
            String o = (String) MethodUtils.invokeMethod(obj, key);
            return o.trim();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }

    }

    ResultBean validateCorpCode(DamoaContext damoaContext, String corpCode, Logger LOG) {
        Map<String, Object> map = damoaContext.findChaSimpleInfo(corpCode);
        if (map == null) {
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
