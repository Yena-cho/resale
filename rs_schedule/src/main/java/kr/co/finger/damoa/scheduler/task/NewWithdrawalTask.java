//package kr.co.finger.damoa.scheduler.task;
//
//import kr.co.finger.damoa.commons.DateUtils;
//import kr.co.finger.damoa.commons.io.HexUtil;
//import kr.co.finger.damoa.scheduler.service.FileService;
//import kr.co.finger.damoa.scheduler.service.WithdrawalService;
//import kr.co.finger.msgio.layout.LayoutFactory;
//import kr.co.finger.msgio.msg.*;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * 출금이체 신청 전문파일 생성.
// *  출금동의 증빙자료내역 EI13
// *  출금이체 신청내역    EB13
// *  매일 오전 2시
// *  새로 신청한 기관을 대상으로 처리함.
// */
//public class NewWithdrawalTask {
//
//    @Autowired
//    private WithdrawalService withdrawalService;
//    @Autowired
//    private LayoutFactory withdrawalLayoutFactory;
//
//    @Autowired
//    private FileService fileService;
//    @Value("${damoa.autoWithdrawal.fileDir}")
//    private String msgFileDir;
//    @Value("${damoa.autoWithdrawal.corpCode}")
//    private String corpCode;
//    @Value("${damoa.autoWithdrawal.shinhanCode}")
//    private String shinhanCode;
//    private Logger LOG = LoggerFactory.getLogger(getClass());
//
//
//    private AutoWithdrawalHelper withdrawalHelper;
//
//    public void init() {
//        withdrawalHelper.setCharset(withdrawalLayoutFactory.getCharset());
//        withdrawalHelper.setLayoutFactory(withdrawalLayoutFactory);
//    }
//    public void send() {
//        try {
//            List<Map<String,Object>> maps = withdrawalService.findNewAutomaticWithdrawal();
//            if (maps == null || maps.size() == 0) {
//                LOG.info("등록한 신규기관이 없음.");
//                return;
//            }
//            executeEI13(maps);
//            executeEB13(maps);
//
//        } catch (Exception e) {
//            LOG.error(e.getMessage(),e);
//            withdrawalService.updateNewWithdrawal("",e.getMessage());
//        }
//    }
//
//    private void executeEI13(List<Map<String,Object>> maps) throws Exception {
//        EI13 ei13= createEI13(maps);
//        byte[] msg = withdrawalLayoutFactory.encodeToByteArray(ei13,"EI13");
//        LOG.info(HexUtil.hexDump(msg));
//        String fullFilePath = msgFileDir+"/EI13"+DateUtils.toString(new Date(),"MMdd");
//        fileService.write(msg,fullFilePath);
//    }
//
//    private void executeEB13(List<Map<String,Object>> maps) throws Exception {
//        // 테이블 조회
//        // 전문 생성.
//        // 파일로 저장.
//        Date now = new Date();
//        String fileName = "EB13"+DateUtils.toString(now,"MMdd");
////        BaseMsg eb13 = createEB13(maps,fileName);
////        String msg = withdrawalLayoutFactory.encode(eb13,"EB13");
//        EB13 eb13 = AutoWithdrawalHelper.convertEB13(maps,corpCode,shinhanCode,fileName,now);
//        String msg = AutoWithdrawalHelper.encodeEB13(eb13);
//        LOG.info(msg);
//        String fullFilePath = msgFileDir+"/"+fileName;
//        fileService.write(msg,fullFilePath);
//    }
//
//    private BaseMsg createEB13(List<Map<String,Object>> maps,String fileName) throws Exception {
//
//
////
////        EB13 eB13 = new EB13();
////        eB13.from(maps,corpCode,fileName,now);
//
////        String corpCd = "9876543210";
//
//        String now = DateUtils.toString(new Date(), "yyMMdd");
//
//        EB13 eB13 = new EB13();
//        NewHeader header = new NewHeader();
//        header.setCorpCode(corpCode);
//        header.setFileName(fileName);
//        header.setReqData(now);
//        eB13.setHeader(header);
//
//        NewData data = new NewData();
//
//        data.setSeqNo("00000001");
//        data.setCorpCode(corpCode);
//        data.setReqDate(now);
//        data.setReqType("1");
//        data.setPayerNo("20005363");
//        data.setBankCode(shinhanCode);
//        data.setWithdrawalAccountNo("100023863316");
//        data.setCompRegNo("2141134600");
//
//        eB13.addDataList(data);
//        data = new NewData();
//        data.setSeqNo("00000002");
//        data.setCorpCode(corpCode);
//        data.setReqDate(now);
//        data.setReqType("1");
//        data.setPayerNo("20005364");
//        data.setBankCode(shinhanCode);
//        data.setWithdrawalAccountNo("140008363410 ");
//        data.setCompRegNo("1329136249");
//        eB13.addDataList(data);
//
//        NewTrailer trailer = new NewTrailer();
//        trailer.setCorpCode(corpCode);
//        trailer.setFileName(fileName);
//        trailer.setTotalRecordCount("2");
//        trailer.setNewReqCount("2");
//        trailer.setModiReqCount("0");
//        trailer.setMoveReqCount("0");
//        trailer.setTempMoveReqCount("0");
//        eB13.setTrailer(trailer);
//
//        return eB13;
//    }
//
//    private EI13 createEI13(List<Map<String,Object>> maps) throws IOException {
//
////        EI13 ei13 = new EI13();
////        ei13.from(maps, new Date(), corpCode);
//
////        String corpCd = "9876543210";
//
//        String now = DateUtils.toString(new Date(), "yyyyMMdd");
//
//        EI13 ei13 = new EI13();
//        EvidenceHeader header = new EvidenceHeader();
//
//        header.setCorpCode(corpCode);
//        header.setReqData(now);
//        header.setCorpCode(corpCode);
//        header.setTotalEvidenceCount("2");
//        ei13.setHeader(header);
//
//        int totalSize = 0;
//
//        EvidenceData data = new EvidenceData();
//        data.setSeqNo("0000001");
//        data.setCorpCode(corpCode);
//        data.setPayerNo("20005363");
//        data.setBankCd(shinhanCode);
//        data.setAccountNo("100023863316");
//        data.setReqData(now);
//        data.setEvidenceType("1");
//        data.setEvidenceExt("jpg");
//        data.setupEvidenceData(readFile("F:\\dev\\damoa-workspace\\scheduler\\data\\books.jpg"),LOG);
//        ei13.addDataList(data);
//        totalSize += data.size();
//        data = new EvidenceData();
//        data.setSeqNo("0000002");
//        data.setCorpCode(corpCode);
//        data.setPayerNo("20005364");
//        data.setBankCd(shinhanCode);
//        data.setAccountNo("140008363410");
//        data.setReqData(now);
//        data.setEvidenceType("1");
//        data.setEvidenceExt("jpg");
//        // 이미지 파일을 처리할 때는 setupEvidenceData를 호출해야 함.
//        data.setupEvidenceData(readFile("F:\\dev\\damoa-workspace\\scheduler\\data\\netty_state.jpg"),LOG);
//        ei13.addDataList(data);
//        totalSize += data.size();
//
//        EvidenceTrailer trailer = new EvidenceTrailer();
//        trailer.setCorpCode(corpCode);
//        trailer.setTotalDataRecordCount("2");
//        trailer.setTotalDataBlockCount((totalSize/1024)+"");
//
//        ei13.setTrailer(trailer);
//
//        return ei13;
//    }
//
//    private byte[] readFile(String filePath) throws IOException {
//        return FileUtils.readFileToByteArray(new File(filePath));
//    }
//}
