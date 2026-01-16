package kr.co.finger.shinhandamoa.adapter.bbtec;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.shinhandamoa.common.SamElement;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 고지서 출력 요청 파일
 *
 * @author wisehouse@finger.co.kr
 */
public class InvoiceSam {
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("0000000");

    public static final Charset DEFAULT_CHARSET = Charset.forName("EUC-KR");


    private InvoiceSamHeader header;
    private List<InvoiceSamBody> body;
    private InvoiceSamTailer tailer;

    public InvoiceSam() {
        this.header = new InvoiceSamHeader();

        this.body = new ArrayList<>();

        this.tailer = new InvoiceSamTailer();
    }

    public void addBody(InvoiceSamBody body) {
        this.body.add(body);
        this.tailer.setBodyCount(this.body.size());
    }

    public void writeTo(OutputStream os) throws IOException {
        header.writeTo(os);
        os.write('\n');
        for (InvoiceSamBody each : body) {
            each.writeTo(os);
            os.write('\n');
        }
        tailer.writeTo(os);
        os.write('\n');
    }
}
