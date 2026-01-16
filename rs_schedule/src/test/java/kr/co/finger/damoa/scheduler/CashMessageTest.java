package kr.co.finger.damoa.scheduler;

import kr.co.finger.msgio.cash.CashMessage;
import kr.co.finger.msgio.cash.Data;
import kr.co.finger.msgio.layout.LayoutFactory;
import kr.co.finger.shinhandamoa.service.ClientSettleService;
import kr.co.finger.shinhandamoa.test.TestConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class CashMessageTest {

    @Autowired
    private LayoutFactory cashLayoutFactory;
    @Autowired
    private ClientSettleService clientSettleService;

    @Test
    public void decodeCashMessage() throws Exception {

        List<String> strings= FileUtils.readLines(new File("./data/S_FING_DMA.20180815"), Charset.forName("KSC5601"));
        CashMessage cashMessage =cashLayoutFactory.decodeCash(strings);

        System.out.println(cashMessage.getHead());
        System.out.println(cashMessage.getTrailer());

        List<Data> dataList = cashMessage.getDataList();
        int index =638679995;
        for (Data data : dataList) {
            System.out.println(data);
//            index++;
//            data.setCashApprovalNo(index+"");
//            data.setReturnCd("0");

        }

//        String content = cashLayoutFactory.encodeCash(cashMessage);
//        FileUtils.write(new File("./data/R_FING_DMA.20180713_"),content,Charset.forName("KSC5601"));
    }

    private String makeCashApprovalNo(int index) {
        return "H" + StringUtils.leftPad(index + "", 8, "0");
    }


    @Test
    public void readCashMessag() throws Exception {
        List<String> strings= FileUtils.readLines(new File("./data/R_FING_DMA.20180627"), Charset.forName("KSC5601"));
        CashMessage cashMessage =cashLayoutFactory.decodeCash(strings);
        System.out.println(cashMessage);
    }
    @Test
    public void encodeCashMessage() throws Exception {
        /**
         *  CashReceiptsReceiverTask
         *  CashReceiptsSenderTask
         *  참고..
         */

    }

    @Test
    public void execute() {
        clientSettleService.executeVaSettle(new SimpleDateFormat("yyyyMMdd").format(org.apache.commons.lang3.time.DateUtils.addDays(new Date(),-2)));
    }

}
