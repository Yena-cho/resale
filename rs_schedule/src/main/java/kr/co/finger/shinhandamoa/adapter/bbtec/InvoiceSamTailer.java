package kr.co.finger.shinhandamoa.adapter.bbtec;

import kr.co.finger.shinhandamoa.common.SamElement;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 고지서 꼬리
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceSamTailer extends SamElement {
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("0000000");

    public InvoiceSamTailer() {
        super(8, ' ');
        this.setString(0, 1, "T");
        this.setString(1, 7, NUMBER_FORMAT.format(0));
    }

    public void setBodyCount(int bodyCount) {
        this.setString(1, 7, NUMBER_FORMAT.format(bodyCount));
    }
}
