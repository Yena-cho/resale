package kr.co.finger.damoa.shinhan.agent.handler.command.validation;

import kr.co.finger.damoa.shinhan.agent.config.DamoaContext;
import kr.co.finger.damoa.shinhan.agent.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AdminInfoValidator extends BasicValidator {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext) {
        return null;
    }

    @Override
    public ResultBean validate(Object commonMessage, DamoaContext damoaContext, String type) {
        String adminInfo = (String)getValue(commonMessage,"getAdminInfo",LOG);

        if ("001".equalsIgnoreCase(adminInfo)
                || "002".equalsIgnoreCase(adminInfo)
                || "003".equalsIgnoreCase(adminInfo)
                || "004".equalsIgnoreCase(adminInfo)
                || "005".equalsIgnoreCase(adminInfo)
                || "006".equalsIgnoreCase(adminInfo)
                || "007".equalsIgnoreCase(adminInfo)

                ) {
            LOG.debug("통신망 관리 관리정보 OK " +adminInfo);
            return new ResultBean("0","");
        } else {
            LOG.error("통신망 관리 관리정보 FAIL " +adminInfo);
            return new ResultBean("F022");
        }
    }
}
