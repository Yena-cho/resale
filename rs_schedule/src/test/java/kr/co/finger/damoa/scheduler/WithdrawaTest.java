package kr.co.finger.damoa.scheduler;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.commons.io.HexUtil;
import kr.co.finger.damoa.scheduler.service.FileService;
import kr.co.finger.damoa.scheduler.service.WithdrawalService;
import kr.co.finger.msgio.layout.LayoutFactory;
import kr.co.finger.msgio.msg.*;
import kr.co.finger.shinhandamoa.test.TestConfig;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class WithdrawaTest {
    @Autowired
    private FileService fileService;
    @Autowired
    private LayoutFactory withdrawalLayoutFactory;
    @Autowired
    private Charset charset;

    @Autowired
    private WithdrawalService withdrawalService;

    @Value("${damoa.autoWithdrawal.corpCode}")
    private String corpCode;    // 핑거이용기관번호
    @Value("${damoa.autoWithdrawal.accountNo}")
    private String accountNo;   // 핑거계좌번호
    @Value("${damoa.autoWithdrawal.bankCode}")
    private String bankCode;    // 핑거계좌은행 (농협)
    @Value("${damoa.autoWithdrawal.shinhanCode}")
    private String shinhanCode; // 신한코드
    @Value("${damoa.autoWithdrawal.fileDir}")
    private String msgFileDir;  // 파일저장위치
    @Value("${damoa.autoWithdrawal.dueDay}")
    private String dueDay;      // 자동이체날짜.
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Before
    public void setup() throws Exception {
        AutoWithdrawalHelper.setLayoutFactory(withdrawalLayoutFactory);
        AutoWithdrawalHelper.setCharset(charset);
    }


    @Test
    public void test13() throws Exception {
        List<Map<String,Object>> maps = withdrawalService.findNewAutomaticWithdrawal();
    }


    @Test
    public void decodeEB14() throws IOException {
        String msg = read("F:\\tmp\\scheduler\\trunk\\data\\EB140817",charset);
        EB14 eb14 = (EB14)withdrawalLayoutFactory.decode(msg,"EB14");


        System.out.println("::::::::");
        System.out.println(eb14.getHeader());
        System.out.println(eb14.getTrailer());

        List<NewData> dataList = eb14.getDataList();
        for (NewData data : dataList) {
            System.out.println(data);
        }

        // 신청결과 처리해야 함.

    }


    @Test
    public void decodeEB11() throws IOException {
        String msg = read("F:\\tmp\\scheduler\\trunk\\data\\EB110817",charset);
        EB11 eb14 = (EB11)withdrawalLayoutFactory.decode(msg,"EB11");
        System.out.println("::::::::");
        System.out.println(eb14.getHeader());
        System.out.println(eb14.getTrailer());

        List<NewData> dataList = eb14.getDataList();
        for (NewData data : dataList) {
            System.out.println(data);
        }
        // 신청결과 처리해야 함.

    }
    @Test
    public void decodeEB12() throws IOException {
        String msg = read("F:\\tmp\\scheduler\\trunk\\data\\EB110817",charset);
        EB12 eb14 = (EB12)withdrawalLayoutFactory.decode(msg,"EB12");
        System.out.println("::::::::");
        System.out.println(eb14.getHeader());
        System.out.println(eb14.getTrailer());

        List<NewData> dataList = eb14.getDataList();
        for (NewData data : dataList) {
            System.out.println(data);
        }
        // 신청결과 처리해야 함.

    }
    @Test
    public void decodeEB22() throws IOException {
        String previousMonth = DateUtils.previousMonth();
        String msg = read("F:\\tmp\\scheduler\\trunk\\data\\EB220817",charset);
        EB22 eb22 = (EB22)withdrawalLayoutFactory.decode(msg,"EB22");

        System.out.println("::::::");
        System.out.println(eb22.getHeader());
        System.out.println(eb22.getTrailer());
        for (BatchData batchData:eb22.getDataList()) {
            System.out.println(batchData);
        }

        System.out.println(eb22.getDataList().size());
//        withdrawalService.updateEB22Result(eb22,previousMonth);

    }
    @Test
    public void decodeEB21() throws IOException {
        String previousMonth = DateUtils.previousMonth();
        String msg = read("C:\\CMS\\9964238166\\EB21\\EB210710",charset);
        EB21 eb22 = (EB21)withdrawalLayoutFactory.decode(msg,"EB21");
        System.out.println("::::::::");
        System.out.println(eb22.getHeader());
        System.out.println(eb22.getTrailer());
        long total = 0;
        for (BatchData batchData:eb22.getDataList()) {
            System.out.println(batchData);
            total += Long.valueOf(batchData.getWithdrawalAmout());
        }
        System.out.println("TOTAL "+total);
        System.out.println(eb22.getDataList().size());
//        withdrawalService.updateEB22Result(eb22,previousMonth);

    }
    private List<Map<String,Object>> slice(List<Map<String,Object>> maps) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        int index = 0;
        for (Map<String, Object> map : maps) {
            index++;
            if (index == 10) {
                break;
            }

            mapList.add(map);
        }

        return mapList;

    }

    private String thisYear() {
        return DateUtils.toString(new Date(), "yy");
    }
    @Test
    public void encodeEB21() throws Exception {
//        String month = "201804";
        String previousMonth = DateUtils.previousMonth();    //  이전달 구하는 로직 필요.
        String thisMonth = DateUtils.thisMonth();
        String thisYear = thisYear();
        System.out.println(thisYear+" thisYear");
        //TODO 쿼리 변경 필요. 처리하지 않았거나 에러나 난 것들.
        List<Map<String,Object>> maps = withdrawalService.findMonthlyAutomaticWithdrawal(previousMonth);
        if (maps == null || maps.size() == 0) {
            System.out.println("처리할 자동이체 데이터가 없음. "+previousMonth);
            return;
        }
        String fileName = fileNameEB21(thisMonth); // 이번달 자동이체 파일명 구하는 메소드 구현.
//        String fileName = "EB210516";
        EB21 eb21 = AutoWithdrawalHelper.convertEB21(maps, fileName, corpCode,bankCode,accountNo,shinhanCode,thisYear);
        String encoded = AutoWithdrawalHelper.encodeEB21(eb21);
        System.out.println(encoded);
        fileService.write(encoded,msgFileDir+"/"+fileName);
        withdrawalService.updateEB21Result(eb21,previousMonth);



//        System.out.println("count "+maps.size());
//        fileService.write(encoded,"./data/"+fileName);
//        EB21 _eb21 = (EB21)withdrawalLayoutFactory.decode(encoded, "EB21");
//
//        System.out.println(_eb21.getHeader());
//        System.out.println(_eb21.getTrailer());
//        List<BatchData> batchDataList = _eb21.getDataList();

//        for (BatchData batchData : batchDataList) {
//            System.out.println(batchData);
//        }
//
//        System.out.println(batchDataList.size());
    }



    private String read(String filePath,Charset charset) throws IOException {
        return FileUtils.readFileToString(new File(filePath),charset);
    }
    private byte[] readFile(String filePath) throws IOException {
        return FileUtils.readFileToByteArray(new File(filePath));
    }

    private String fileNameEB21(String thisMonth) {
        return "EB21" + thisMonth + dueDay;
    }


    @Test
    public void decodeEI13() throws Exception {
        AutoWithdrawalHelper.setLayoutFactory(withdrawalLayoutFactory);
        AutoWithdrawalHelper.setCharset(charset);
        byte[] bytes = FileUtils.readFileToByteArray(new File("F:\\tmp\\scheduler\\trunk\\data\\EI130817 (2)"));
        EI13 ei13 = AutoWithdrawalHelper.decodeEI13(bytes);

        System.out.println("|||||||||||||||||");
        System.out.println(ei13.getHeader());
        System.out.println(ei13.getTrailer());
        List<EvidenceData> list =  ei13.getDataList();
        int total = 0;
        for (EvidenceData evidenceData : list) {
            System.out.println(evidenceData);
            int sum = 119+evidenceData.getEvidenceData().length+evidenceData.getFiller2().length() ;
            if (sum % 1024 == 0) {
                total += sum/1024;
            } else {
                System.out.println("ERROR============");
            }
        }

        System.out.println(total);

        ei13.write("F:\\dev\\문서\\새 폴더");
    }

    @Test
    public void testEI13() throws Exception {
        Date _now = new Date();
        String now = DateUtils.toString(_now, "yyyyMMdd");
        String corpCode = "9964238166";
        String shinhanCode = "021";
        List<Map<String,Object>> maps = withdrawalService.findNewAutomaticWithdrawal();
        if (maps == null || maps.size() == 0) {
            LOG.info("자동이체 등록할 신규기관이 없음.");
            return;
        }
        for (Map<String, Object> map : maps) {
            System.out.println(map);
            String file = Maps.getValue(map, "CMS_FILE_NAME");
            map.put("CMS_FILE_NAME", "./data/" + file);

        }
        byte[] bytes= AutoWithdrawalHelper.encodeEI13(maps, corpCode, _now,shinhanCode);
        LOG.info(HexUtil.hexDump(bytes));
        String fullFilePath = msgFileDir+"/EI13"+DateUtils.toString(new Date(),"MMdd");
        System.out.println(fullFilePath);
        FileUtils.writeByteArrayToFile(new File(fullFilePath), bytes,false);


    }

    @Test
    public void decodeEB13() throws IOException {
        String msg = read("F:\\tmp\\scheduler\\trunk\\data\\EB130817 (1)",charset);
        EB13 eb13 = (EB13)withdrawalLayoutFactory.decode(msg,"EB13");
        System.out.println("|||||||||||||");
        System.out.println(eb13.getHeader());
        System.out.println(eb13.getTrailer());
        List<NewData> dataList = eb13.getDataList();
        for (NewData data : dataList) {
            System.out.println(data);
        }
    }

    /**
     * EB13과 EB12는 동일하게 처리할 수 있는 구조임..
     * @throws Exception
     */
    @Test
    public void testEB13() throws Exception {

        List<Map<String,Object>> maps = withdrawalService.findNewAutomaticWithdrawal();
        if (maps == null || maps.size() == 0) {
            LOG.info("자동이체 등록할 신규기관이 없음.");
            return;
        }
        Date now = new Date();
        String fileName = "EB13"+DateUtils.toString(now,"MMdd");
        EB13 eb13 = AutoWithdrawalHelper.convertEB13(maps,corpCode,shinhanCode,fileName,now);
        String msg = AutoWithdrawalHelper.encodeEB13(eb13);
        LOG.info(msg);
        String fullFilePath = msgFileDir+"/"+fileName;
        fileService.write(msg,fullFilePath);
//        withdrawalService.updateEBI13Result(eb13);

    }

}
