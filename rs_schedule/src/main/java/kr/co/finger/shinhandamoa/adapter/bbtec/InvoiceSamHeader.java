package kr.co.finger.shinhandamoa.adapter.bbtec;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.shinhandamoa.common.SamElement;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class InvoiceSamHeader extends SamElement {
    public InvoiceSamHeader() {
        super(16, '0');
        this.setString(0, 1, "H");
        this.setString(1, 7, "0000000");
        this.setString(8, 8, DateUtils.toString(new Date(), "yyyyMMdd"));
    }
}
