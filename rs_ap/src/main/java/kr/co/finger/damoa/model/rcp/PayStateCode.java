package kr.co.finger.damoa.model.rcp;

import kr.co.finger.damoa.model.msg.Code;

public class PayStateCode {
    public static final Code NO_PAY = new Code("PA02","미납");
    public static final Code OK_PAY = new Code("PA03","수납");
    public static final Code PART_PAY = new Code("PA04","일부납");
    public static final Code OVER_PAY = new Code("PA05","초과납");
}
