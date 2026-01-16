package kr.co.finger.damoa.scheduler;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.msgio.corpinfo.CorpInfo;
import kr.co.finger.msgio.corpinfo.Data;
import kr.co.finger.msgio.corpinfo.Head;
import kr.co.finger.msgio.corpinfo.Trailer;
import kr.co.finger.msgio.layout.LayoutFactory;
import kr.co.finger.shinhandamoa.test.TestConfig;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class CorpInfoTest {
    @Autowired
    private LayoutFactory corpInfoLayoutFactory;
    private Logger LOG = LoggerFactory.getLogger(getClass());
    @Before
    public void setup() throws Exception {
    }

    @Test
    public void testEncode() throws Exception {
        CorpInfo corpInfo = new CorpInfo();

        Head head = new Head();
        head.setCorpCode("20007481");
        head.setDealDate(DateUtils.toDateString(new Date()));

        corpInfo.setHead(head);


//        Data data = new Data();
//        data.setCorpCode("20001111");                       //CHACD
//        data.setCorpName("성지유치원");                      //CHANAME
//        data.setCorpNewDate("20140221");                    //REGDT
//        data.setBizNo("1298017600");                        //CHAOFFNO
//        data.setBizType("유치원");                             //CHASTATUS 특별한 값이 아님.
//        data.setCusNo("031-716-1122");                          //OWNERTEL
//        data.setPostNo("463801");                                //CHAZIPCODE
//        data.setDongGtAddr("경기 성남시 분당구 구미동");         //CHAADDRESS1
//        data.setDongltAddr("221");                          //CHAADDRESS2
//        data.setHoliday01("1");
//        data.setCorpState("2");                              // CHAST ( 1:신규 2:할당 3:해지 4:거래중지 )
        //기관상태 [ ST01:승인(신규기관)  ST02:해지  ST04:정지  ST05 승인대기, ST06:이용승인, ST07:이용거부, ST08:이용정지 ]
        //TODO 변환해야 함.

//        data.setFeeAccountNo1("140010847119");  //FEEACCNO
//
//        data.setAgencyCode1("00000");       //ADJFIREGKEY
//        data.setAgencyName1("739234");      //GRPADJNAME
//        data.setMoAccountNo1("140010847110");           //REAL_ACCNO
//
//        data.setAgencyCode2("00001");       //ADJFIREGKEY
//        data.setAgencyName2("739184");      //GRPADJNAME
//        data.setMoAccountNo2("140010847111");           //REAL_ACCNO
//
//        data.setAgencyCode3("00002");       //ADJFIREGKEY
//        data.setAgencyName3("739152");      //GRPADJNAME
//        data.setMoAccountNo3("140010847112");           //REAL_ACCNO
//
//        data.setFeeAmount("150");           //RCPCNTFEE(수납건당수수료)? RCPBNKFEE(수납건당은행수수료)?
//        data.setFeeRatio("50");               //수수료배분율은 도대체 무엇?
//        data.setFeeStartDate("20180815");       //BNK_FEE_HIST 테이블의 FEE_APP_DT
//        data.setFeeEndDate("20181015");         //BNK_FEE_HIST 테이블에 매핑할만한 필드가 없음.
//        data.setGrbrNo("1428");                 //지점코드 AGTCD
//        corpInfo.add(data);

//        data = new Data();
//        data.setCorpCode("20002201");                       //chacd
//        data.setCorpName("에듀프라임 달성지사");                      //chaname
//        data.setCorpNewDate("20090317");                    //REGDT
//        data.setBizNo("5149110704");                        //chaoffno
//        data.setBizType("");                             //chastatus 특별한 값이 아님.
//        data.setCusNo("053-290-3122");                          //ownertel
//        data.setPostNo("704370");                                //CHAZIPCODE
//        data.setDongGtAddr("대구 달서구 상인동");         //CHAADDRESS1
//        data.setDongltAddr("1546-8 3층");                          //CHAADDRESS2
//        data.setHoliday01("1");
//        data.setCorpState("2");                              // CHAST ( 1:신규 2:할당 3:해지 4:거래중지 )
//        //기관상태 [ ST01:승인(신규기관)  ST02:해지  ST04:정지  ST05 승인대기, ST06:이용승인, ST07:이용거부, ST08:이용정지 ]
//        //TODO 변환해야 함.
//
//        data.setFeeAccountNo1("110266613673");  //FEEACCNO
//
//        data.setAgencyCode1("");       //ADJFIREGKEY
//        data.setAgencyName1("");      //GRPADJNAME
//        data.setMoAccountNo1("");           //REAL_ACCNO
//
//
//        data.setFeeAmount("120");           //RCPCNTFEE(수납건당수수료)? RCPBNKFEE(수납건당은행수수료)?
//        data.setFeeRatio("30");               //수수료배분율은 도대체 무엇?
//        data.setFeeStartDate("20180817");       //BNK_FEE_HIST 테이블의 FEE_APP_DT
//        data.setFeeEndDate("20181017");         //BNK_FEE_HIST 테이블에 매핑할만한 필드가 없음.
//        data.setGrbrNo("1815");                 //지점코드 AGTCD
//        corpInfo.add(data);

        Trailer trailer = new Trailer();
        trailer.setTotalCount(corpInfo.getDataList().size()+"");

        corpInfo.setTrailer(trailer);

        String content = corpInfoLayoutFactory.encodeCorpInfo(corpInfo);
        System.out.println(content);

        FileUtils.write(new File("./data/20180816.corp"),content,Charset.forName("EUC-KR"));

    }
    @Test
    public void testDecode() throws Exception {
        List<String> strings = FileUtils.readLines(new File("./data/20180816.corp"), Charset.forName("EUC-KR"));
        CorpInfo corpInfo = corpInfoLayoutFactory.decodeCorpInfo(strings);

        System.out.println(corpInfo.getHead());
        System.out.println(corpInfo.getTrailer());

        List<Data> dataList = corpInfo.getDataList();
        for (Data data : dataList) {
            System.out.println(data);
        }
    }

}
