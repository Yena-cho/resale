package kr.finger.shinhandamoa.task;

import com.finger.shinhandamoa.common.FingerIntegrateMessaging;
import com.finger.shinhandamoa.common.ListResult;
import com.finger.shinhandamoa.sys.cust.service.SysNoticeService;
import com.finger.shinhandamoa.sys.cust.web.SysNoticeMgmtController;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/*.xml", "/spring/appServlet/*.xml"})
public class EmailTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailTask.class);

    @Autowired
    private SysNoticeMgmtController sysNoticeMgmtController;

    @Autowired
    private SysNoticeService sysNoticeService;

    @Test
    public void run() throws Exception {
        List<String[]> chaList = new ArrayList<>();
        chaList.add(new String[]{"20007389", "핑거", "010-8922-5008", "신재현"});
        chaList.add(new String[]{"20006683", "레드웨건", "01066438858", "강 지 석"});
        chaList.add(new String[]{"20006683", "레드웨건", "01066438858", "강 지 유"});
        chaList.add(new String[]{"20006683", "레드웨건", "01085906101", "고 윤 찬"});
        chaList.add(new String[]{"20006683", "레드웨건", "01095081807", "고 지 우"});
        chaList.add(new String[]{"20006683", "레드웨건", "01025854374", "권 민 하"});
        chaList.add(new String[]{"20006683", "레드웨건", "01092832647", "김 나 율"});
        chaList.add(new String[]{"20006683", "레드웨건", "01099306951", "김 민 건"});
        chaList.add(new String[]{"20006683", "레드웨건", "01064247599", "김 민 송"});
        chaList.add(new String[]{"20006683", "레드웨건", "01066033647", "김 연 준"});
        chaList.add(new String[]{"20006683", "레드웨건", "01027066991", "김 유 주"});
        chaList.add(new String[]{"20006683", "레드웨건", "01052325309", "김 윤 빈"});
        chaList.add(new String[]{"20006683", "레드웨건", "01032586372", "김 재 원"});
        chaList.add(new String[]{"20006683", "레드웨건", "01086542963", "김 제 이"});
        chaList.add(new String[]{"20006683", "레드웨건", "01097598090", "김 지 윤"});
        chaList.add(new String[]{"20006683", "레드웨건", "01032469316", "김 지 후"});
        chaList.add(new String[]{"20006683", "레드웨건", "01088501688", "김 하 은"});
        chaList.add(new String[]{"20006683", "레드웨건", "01055588219", "김 해 민"});
        chaList.add(new String[]{"20006683", "레드웨건", "01064273574", "김 현 중"});
        chaList.add(new String[]{"20006683", "레드웨건", "01034377649", "민 이 준"});
        chaList.add(new String[]{"20006683", "레드웨건", "01090857969", "반 재 민"});
        chaList.add(new String[]{"20006683", "레드웨건", "01030101555", "백 준 영"});
        chaList.add(new String[]{"20006683", "레드웨건", "01075529909", "설 예 인"});
        chaList.add(new String[]{"20006683", "레드웨건", "01041943125", "양 서 린"});
        chaList.add(new String[]{"20006683", "레드웨건", "01032406360", "오 연 수"});
        chaList.add(new String[]{"20006683", "레드웨건", "01090254601", "오 윤 찬"});
        chaList.add(new String[]{"20006683", "레드웨건", "01033421544", "온 예 린"});
        chaList.add(new String[]{"20006683", "레드웨건", "01028740817", "원 정 호"});
        chaList.add(new String[]{"20006683", "레드웨건", "01031874544", "육 라 희"});
        chaList.add(new String[]{"20006683", "레드웨건", "01097947985", "이 권 호"});
        chaList.add(new String[]{"20006683", "레드웨건", "01073275335", "이 서 희"});
        chaList.add(new String[]{"20006683", "레드웨건", "01076107657", "이 선 우"});
        chaList.add(new String[]{"20006683", "레드웨건", "01073334921", "이 윤 호"});
        chaList.add(new String[]{"20006683", "레드웨건", "01026332624", "이 재 용"});
        chaList.add(new String[]{"20006683", "레드웨건", "01024884633", "이 준 우"});
        chaList.add(new String[]{"20006683", "레드웨건", "01066477147", "이 지 효"});
        chaList.add(new String[]{"20006683", "레드웨건", "01092808569", "이 환"});
        chaList.add(new String[]{"20006683", "레드웨건", "01050600055", "장 수 빈"});
        chaList.add(new String[]{"20006683", "레드웨건", "01091503279", "정 서 준"});
        chaList.add(new String[]{"20006683", "레드웨건", "01098720800", "조 윤 성"});
        chaList.add(new String[]{"20006683", "레드웨건", "01034447081", "천 지 율"});
        chaList.add(new String[]{"20006683", "레드웨건", "01026211016", "최 우 주"});
        chaList.add(new String[]{"20006683", "레드웨건", "01063210454", "한 지 원"});
        chaList.add(new String[]{"20006683", "레드웨건", "01040364422", "한 채 원"});
        chaList.add(new String[]{"20006683", "레드웨건", "01033065155", "홍 유 나"});
        chaList.add(new String[]{"20006683", "레드웨건", "01026750779", "황 지 수"});


        for (String[] each : chaList) {
//            sendEmail(each);
//            sendSms(each);
        }

//        sendEmail(new String[]{"20007389", "핑거", "010-9983-7415", "wisehouse@finger.co.kr"});
//        sendSms(new String[]{"20007389", "핑거", "010-8922-5008", "신재현"});
//        sendSms(new String[]{"20007389", "핑거", "010-4343-1146", "윤난희"});
    }

    private void sendEmail(String[] strings) throws Exception {
        String chaCd = strings[0];
        String chaName = strings[1];
        String contactNo = strings[2];
        String email = strings[3];
        String emailFront = StringUtils.substringBefore(email, "@");
        String emailBack = StringUtils.substringAfter(email, "@");

        int smsseq = sysNoticeService.maxEmailSeq(new HashMap<>());
        String title = "[공지] 신한 다모아 ARS출금동의 예정 안내";
        String message = "안녕하세요. 신한 다모아 입니다. \n" +
                "다모아 관리수수료 출금관련 출금동의서 미접수 기관에 대해 ARS출금동의가 진행 될 예정입니다.\n" +
                "아래 내용 참고하시어 협조부탁드립니다.\n" +
                "감사합니다.\n" +
                "\n" +
                "\n" +
                "<strong>1.\t동의자료 재접수 배경</strong>\n" +
                "-\t신한다모아 운영사 변경에 따라 기존 다모아관리수수료를 출금하는 주체가 변경되었습니다. \n" +
                "당사에서 출금하는 부분에 대한 인지와 재동의가 선행되어야 수수료 출금을 위한 진행이 가능하며, 미동의 시 서비스이용이 중단될 수 있는 점 양지 부탁드립니다.\n" +
                "\n" +
                "<strong>2. ARS출금동의 전화예정안내</strong>\n" +
                "-  제휴대행사 : ㈜쿠콘 \n" +
                "-  ARS출금동의기간 : 9월17일~ 9월21일 \n" +
                "-  수신번호 : 당사전산에 등록된 휴대폰번호 \n" +
                "\n" +
                "<strong>3.  기관 별 요청사항</strong>\n" +
                "- 다모아관리자 로그인 -> 마이페이지-> 기본정보관리 -> 담당자 휴대폰정보 확인\n" +
                "- 휴대폰번호로 ARS수신 시 동의절차 진행요청\n" +
                "\n" +
                "☎ 신한다모아 고객센터 : 02-6929-1167\n" +
                "\n" +
                "\n" +
                "향후 좀 더 차별화된 서비스로 보답하겠습니다.\n" +
                "감사합니다.\n";
        sysNoticeMgmtController.sendNoticeEmail(smsseq, chaCd, chaName, emailFront, emailBack, "damoaadm", "damoa@finger.co.kr", title, message, "ST06", "신한다모아");
    }

    private void sendSms(String[] strings) throws Exception {
        String chaCd = strings[0];
        String chaName = strings[1];
        String contactNo = strings[2];
        String email = strings[3];
        String emailFront = StringUtils.substringBefore(email, "@");
        String emailBack = StringUtils.substringAfter(email, "@");
        
        if(StringUtils.isBlank(contactNo)) {
            return;
        }

        FingerIntegrateMessaging fingerIntegrateMessaging = new FingerIntegrateMessaging();

        fingerIntegrateMessaging.setHost("211.111.216.26");
        fingerIntegrateMessaging.setPort(80);
        fingerIntegrateMessaging.setAccessToken("DTpF1yuI2hMg8AuUGeU5HxiPYZtxY6gN");

        final ListResult<FingerIntegrateMessaging.Message> listResult = fingerIntegrateMessaging.createMessage(
                FingerIntegrateMessaging.MessageType.MMS, "신한다모아", "[신한다모아] 안녕하세요. "+chaName+" 의 가상계좌 수납서비스를 제공하는 ㈜핑거입니다. 당사프로그램 오류로 "+email+" 님의 입금 건(10월8일 ~ 10월13일)에 대해 중복으로 현금영수증이 발급되어 금일자로 정상거래를 제외하고 국세청에 발급취소 진행 예정이니 문자수신되는 경우 참고부탁드립니다. 불편을 드려 대단히 죄송합니다. 감사합니다.", null, "0269291167", contactNo, "신한다모아"); // sms 발송
    }

}
