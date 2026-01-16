//package kr.co.finger.damoa.scheduler.task.daily;
//
//import kr.co.finger.damoa.commons.DateUtils;
//import kr.co.finger.damoa.commons.io.HttpClientHelper;
//import kr.co.finger.damoa.commons.model.CorpInfoBeans;
//import kr.co.finger.damoa.scheduler.ThreadSessionHolder;
//import kr.co.finger.damoa.scheduler.service.DamoaService;
//import kr.co.finger.msgio.corpinfo.CorpInfo;
//import kr.co.finger.msgio.corpinfo.Data;
//import kr.co.finger.msgio.layout.LayoutFactory;
//import kr.co.finger.shinhandamoa.common.Progress;
//import kr.co.finger.shinhandamoa.service.ClientInfomationPullHistoryDTO;
//import kr.co.finger.shinhandamoa.service.ClientInfomationService;
//import kr.co.finger.shinhandamoa.service.ClientService;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.apache.commons.lang3.time.StopWatch;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * 기관 정보 수신 작업 (일 배치)
// *
// * 신한은행에서 보낸 신한다모아 기관정보연동 파일의 내용을 데이터베이스에 입력한다.
// *
// * @author lloydkwon@gmail.com
// * @author wisehouse@finger.co.kr
// */
//@Component
//public class ClientInformationReceiveTask {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInformationReceiveTask.class);
//
//    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,##0");
//
//    /**
//     * 신한은행 펌뱅킹 API 서버의 기본 URL
//     */
//    @Value("${shinhanbank-firmbank.host}")
//    private String serverHost;
//
//    @Value("${shinhanbank-firmbank.port}")
//    private int serverPort;
//
//    private String serverFile = "/receive";
//
//    @Value("${client-information-receive-task.org-code}")
//    private String corpInfoOrgCode;
//
//    @Value("${client-information-receive-task.info-code}")
//    private String corpInfoInfoCode;
//
//    @Value("${client-information-receive-task.user}")
//    private String corpInfoUser;
//
//    @Autowired
//    private LayoutFactory corpInfoLayoutFactory;
//
//    @Autowired
//    private DamoaService damoaService;
//
//    @Autowired
//    private ClientService clientService;
//
//    @Autowired
//    private ClientInfomationService clientInfomationService;
//
////    @Scheduled(cron = "${client-information-receive-task.schedule.cron}")
//    public void trigger() {
//        try {
//            LOGGER.info("기관 정보 연동 작업 시작");
//            execute();
//        } catch (Exception e) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.error(e.getMessage(), e);
//            } else {
//                LOGGER.error(e.getMessage());
//            }
//        } finally {
//            LOGGER.info("기관 정보 연동 작업 종료");
//        }
//    }
//
//    @Transactional
//    public void execute() {
//        final String nowString = DateUtils.toString(new Date(), "yyyyMMdd");
//
//        execute(nowString);
//    }
//
//    protected void execute(String targetDt) {
//        final StopWatch stopWatch = StopWatch.createStarted();
//
//        Progress progress = new Progress(0);
//
//        try {
//            ThreadSessionHolder.getInstance().getSession("damoa-scheduler");
//
//            LOGGER.info("신한다모아 기관 정보 연동 작업 시작");
//
//            // 1. 파일 수신
//            LOGGER.info("수신 파일 확인 [{}]", targetDt);
//
//            final String url = "http://" + serverHost + ":" + serverPort + serverFile + "?fromDate=" + targetDt + "&toDate=" + targetDt + "&orgCode=" + corpInfoOrgCode + "&infoCode=" + corpInfoInfoCode + "&user=" + corpInfoUser;
//            LOGGER.debug("수신 파일 목록 조회 URL: {}", url);
//
//            final CorpInfoBeans corpInfoBeans = HttpClientHelper.callCorpInfo(url);
//            if (corpInfoBeans == null) {
//                LOGGER.warn("수신 파일 없음");
//                return;
//            }
//
//            LOGGER.info("수신 파일 건수: {}", corpInfoBeans.getItemCount());
//            final int count = NumberUtils.toInt(corpInfoBeans.getItemCount(), 0);
//            if (count == 0) {
//                return;
//            }
//
//            final String fileId = corpInfoBeans.getItemList().get(0).getId();
//            final String downloadURL = "http://" + serverHost + ":" + serverPort + serverFile + "/" + fileId + "/download";
//            LOGGER.debug("수신 파일 조회 URL: {}", downloadURL);
//
//            ByteArrayInputStream is = download(downloadURL);
//
//            final List<String> list = IOUtils.readLines(is, Charset.forName("EUC-KR"));
//            final CorpInfo corpInfo = corpInfoLayoutFactory.decodeCorpInfo(list);
//            LOGGER.debug(corpInfo.getHead().toString());
//            LOGGER.debug(corpInfo.getTrailer().toString());
//
//            final List<kr.co.finger.msgio.corpinfo.Data> dataList = corpInfo.getDataList();
//
//            LOGGER.debug("중복 데이터 삭제");
//            clientInfomationService.deleteByPullDt(targetDt);
//
//            LOGGER.info("데이터 저장");
//            progress = new Progress(dataList.size());
//            for (Data each : dataList) {
//                ClientInfomationPullHistoryDTO clientInfomationPullHistoryDTO = new ClientInfomationPullHistoryDTO();
//                clientInfomationPullHistoryDTO.setPullDt(targetDt);
//                clientInfomationPullHistoryDTO.setClientId(each.getCorpCode());
//                clientInfomationPullHistoryDTO.setClientName(each.getCorpName());
//                clientInfomationPullHistoryDTO.setClientDeleteYn(each.getDeleteYn());
//                clientInfomationPullHistoryDTO.setRelayClientId(each.getRelayCorpCode());
//                clientInfomationPullHistoryDTO.setClientCreateDt(each.getCreateDt());
//                clientInfomationPullHistoryDTO.setClientIdentityNo(each.getBizNo());
//                clientInfomationPullHistoryDTO.setClientStatus(each.getCorpStatus());
//
//                clientInfomationPullHistoryDTO.setAgencyCode01(each.getAgencyCode1());
//                clientInfomationPullHistoryDTO.setAgencyName01(each.getAgencyName1());
//                clientInfomationPullHistoryDTO.setAgencyStatus01(each.getAgencyStatus1());
//                clientInfomationPullHistoryDTO.setAgencyCode02(each.getAgencyCode2());
//                clientInfomationPullHistoryDTO.setAgencyName02(each.getAgencyName2());
//                clientInfomationPullHistoryDTO.setAgencyStatus02(each.getAgencyStatus2());
//                clientInfomationPullHistoryDTO.setAgencyCode03(each.getAgencyCode3());
//                clientInfomationPullHistoryDTO.setAgencyName03(each.getAgencyName3());
//                clientInfomationPullHistoryDTO.setAgencyStatus03(each.getAgencyStatus3());
//                clientInfomationPullHistoryDTO.setAgencyCode04(each.getAgencyCode4());
//                clientInfomationPullHistoryDTO.setAgencyName04(each.getAgencyName4());
//                clientInfomationPullHistoryDTO.setAgencyStatus04(each.getAgencyStatus4());
//                clientInfomationPullHistoryDTO.setAgencyCode05(each.getAgencyCode5());
//                clientInfomationPullHistoryDTO.setAgencyName05(each.getAgencyName5());
//                clientInfomationPullHistoryDTO.setAgencyStatus05(each.getAgencyStatus5());
//                clientInfomationPullHistoryDTO.setAgencyCode06(each.getAgencyCode6());
//                clientInfomationPullHistoryDTO.setAgencyName06(each.getAgencyName6());
//                clientInfomationPullHistoryDTO.setAgencyStatus06(each.getAgencyStatus6());
//                clientInfomationPullHistoryDTO.setAgencyCode07(each.getAgencyCode7());
//                clientInfomationPullHistoryDTO.setAgencyName07(each.getAgencyName7());
//                clientInfomationPullHistoryDTO.setAgencyStatus07(each.getAgencyStatus7());
//                clientInfomationPullHistoryDTO.setAgencyCode08(each.getAgencyCode8());
//                clientInfomationPullHistoryDTO.setAgencyName08(each.getAgencyName8());
//                clientInfomationPullHistoryDTO.setAgencyStatus08(each.getAgencyStatus8());
//                clientInfomationPullHistoryDTO.setAgencyCode09(each.getAgencyCode9());
//                clientInfomationPullHistoryDTO.setAgencyName09(each.getAgencyName9());
//                clientInfomationPullHistoryDTO.setAgencyStatus09(each.getAgencyStatus9());
//                clientInfomationPullHistoryDTO.setAgencyCode10(each.getAgencyCode10());
//                clientInfomationPullHistoryDTO.setAgencyName10(each.getAgencyName10());
//                clientInfomationPullHistoryDTO.setAgencyStatus10(each.getAgencyStatus10());
//
//                clientInfomationPullHistoryDTO.setPaymentFeeAmount(NumberUtils.toInt(each.getFeeAmount()));
//                clientInfomationPullHistoryDTO.setFingerFeeRate(NumberUtils.toInt(each.getFeeRate()));
//
//                clientInfomationPullHistoryDTO.setBranchCode(each.getBranchCode());
//
//                clientInfomationService.create(clientInfomationPullHistoryDTO);
//
//                progress.tick();
//                LOGGER.info("기관 정보 연동 데이터 저장 - {}% ({}/{}), {}s left", new DecimalFormat("##0.0").format(progress.getProgressRate() *100), progress.getProgressCount(), progress.getTotalCount(), new DecimalFormat("#,##0.0").format(progress.getTimeLeft()/1000F));
//            }
//        } catch (Exception e) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.error(e.getMessage(), e);
//            } else {
//                LOGGER.error(e.getMessage());
//            }
//        } finally {
//            ThreadSessionHolder.getInstance().removeSession();
//
//            LOGGER.info("========================================");
//            LOGGER.info(" 기관 정보 연동 작업");
//            LOGGER.info("----------------------------------------");
//            LOGGER.info(" 전체 건수 : {}", progress.getTotalCount());
//            LOGGER.info(" 처리 건수 : {}", progress.getProgressCount());
//            LOGGER.info("----------------------------------------");
//            LOGGER.info(" 시작 시각 : {}", DateUtils.format(stopWatch.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
//            LOGGER.info(" 종료 시각 : {}", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            LOGGER.info(" 실행 시간 : {}ms", NUMBER_FORMAT.format(stopWatch.getTime()));
//            LOGGER.info("========================================");
//        }
//    }
//
//    private ByteArrayInputStream download(String url) throws IOException {
//        final File file = File.createTempFile("DAMOA_", "");
//
//        HttpClientHelper.downloadCorpInfo(file.getAbsolutePath(), url);
//
//        return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
//    }
//
//
//}
