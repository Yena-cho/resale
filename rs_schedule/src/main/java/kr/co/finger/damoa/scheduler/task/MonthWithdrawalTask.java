//package kr.co.finger.damoa.scheduler.task;
//
//import kr.co.finger.damoa.commons.DateUtils;
//import kr.co.finger.damoa.scheduler.service.FileService;
//import kr.co.finger.damoa.scheduler.service.WithdrawalService;
//import kr.co.finger.msgio.layout.LayoutFactory;
//import kr.co.finger.msgio.msg.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * 매달 5일 주기적으로 출금이체(3일) 전문을 생성함.
// * 이전달 수수료를 받을 기관을 대상으로 출금의뢰내역(EB21)  전문 생성
// * 파일로 저장함.
// * 배치로 파일을 생성할 수 있지만 UI에서 요청도 가능함.
// * 기관에서 핑거로 이체하게 함.
// */
//public class MonthWithdrawalTask {
//
//    @Autowired
//    private LayoutFactory withdrawalLayoutFactory;
//
//    @Autowired
//    private WithdrawalService withdrawalService;
//
//    @Autowired
//    private FileService fileService;
//
//    @Value("${damoa.autoWithdrawal.corpCode}")
//    private String corpCode;
//    @Value("${damoa.autoWithdrawal.accountNo}")
//    private String accountNo;
//    @Value("${damoa.autoWithdrawal.bankCode}")
//    private String bankCode;
//    @Value("${damoa.autoWithdrawal.shinhanCode}")
//    private String shinhanCode;
//
//    /**
//     * 자동이체 파일 생성하여 금결원으로 전송함.
//     */
//    public void executeSending() {
//        try {
//            // DB 조회
//            String month = DateUtils.toString(new Date(), "yyyyMM");
//            List<Map<String,Object>> mapList = withdrawalService.findMonthlyAutomaticWithdrawal(month);
//            // 전문 생성.
//            String encoded = encode(mapList);
//            // 파일 저장.
//            fileService.write(encoded,"");
//            // 최종 DB 처리.
//            withdrawalService.updateMonthlyAutomaticWithdrawal("","OK");
//        } catch (Exception e) {
//            withdrawalService.updateMonthlyAutomaticWithdrawal("",e.getMessage());
//        }
//
//    }
//
//    /**
//     * 금결원으로부터 수신한 파일을 DB 처리함.
//     * @param filePath
//     */
//    public void receive(String filePath) {
//        try {
//
//            //파일 읽기
//            String msg = fileService.read(filePath);
//            EB22 _eb22 = (EB22)withdrawalLayoutFactory.decode(msg,"EB22");
//            withdrawalService.updateReceivedMonthlyAutomaticWithdrawal(_eb22);
//
//        } catch (Exception e) {
//
//            withdrawalService.updateReceivedMonthlyAutomaticWithdrawal(filePath,e.getMessage());
//        }
//    }
//
//    private String encode(List<Map<String, Object>> mapList) throws Exception {
//        String now = DateUtils.toString(new Date(), "yyMMdd");
//
//        String fileName = "EB21" + now;
//
//        EB21 eb21 = new EB21();
//        eb21.from(mapList, corpCode, bankCode, accountNo, fileName, now,shinhanCode);
//
//        return withdrawalLayoutFactory.encode(eb21,"EB21");
//    }
//}
