package kr.co.finger.damoa.shinhan.agent.domain.model;

import kr.co.finger.shinhandamoa.data.table.model.Xcusmas;
import org.apache.commons.lang3.StringUtils;

/**
 * 고객 도메인
 * 
 * @author wisehouse@finger.co.kr
 */
public class CustomerDO {
    private Xcusmas xcusmas;

    public CustomerDO(Xcusmas xcusmas) {
        this.xcusmas = xcusmas;
    }

    public String getAccountNo() {
        return xcusmas.getVano();
    }

    public String getName() {
        return xcusmas.getCusname();
    }

    public String getKey() {
        return xcusmas.getCuskey();
    }

    public String getCategory1() {
        return xcusmas.getCusgubn1();
    }

    public String getCategory2() {
        return xcusmas.getCusgubn2();
    }

    public String getCategory3() {
        return xcusmas.getCusgubn3();
    }

    public String getCategory4() {
        return xcusmas.getCusgubn4();
    }

    public String getContactNo() {
        return xcusmas.getCushp();
    }
    
    public String getIdentityNo() {
        return xcusmas.getCusoffno();
    }

    public String getCashReceiptCustomerTypeCode() {
        return xcusmas.getCustype();
    }

    public String getCashReceiptMediaTypeCode() {
        return xcusmas.getConfirm();
    }

    public boolean disabled() {
        return StringUtils.equals(xcusmas.getDisabled(), "Y");
    }
}
