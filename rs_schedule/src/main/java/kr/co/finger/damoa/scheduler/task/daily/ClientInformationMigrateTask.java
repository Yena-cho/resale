//package kr.co.finger.damoa.scheduler.task.daily;
//
//import kr.co.finger.damoa.commons.DateUtils;
//import kr.co.finger.damoa.commons.io.HttpClientHelper;
//import kr.co.finger.damoa.scheduler.ThreadSessionHolder;
//import kr.co.finger.shinhandamoa.common.Progress;
//import kr.co.finger.shinhandamoa.service.ClientInfomationPullHistoryDTO;
//import kr.co.finger.shinhandamoa.service.ClientInfomationService;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.time.StopWatch;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * 기관 정보 연동 작업
// *
// * @author lloydkwon@gmail.com
// * @author wisehouse@finger.co.kr
// */
//@Component
//public class ClientInformationMigrateTask {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInformationMigrateTask.class);
//
//    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,##0");
//
//    @Autowired
//    private ClientInfomationService clientInfomationService;
//
//    @Scheduled(fixedDelayString = "${client-information-migrate-task.schedule.delay}")
//    public void trigger() {
//        try {
//            LOGGER.debug("기관 정보 연동 작업 시작");
//
//            final List<ClientInfomationPullHistoryDTO> dtoList = clientInfomationService.getStandbyList();
//            if(dtoList.isEmpty()) {
//                LOGGER.debug("기관 정보 연동 대상 없음");
//                return;
//            }
//
//            execute();
//        } catch (Exception e) {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.error(e.getMessage(), e);
//            } else {
//                LOGGER.error(e.getMessage());
//            }
//        } finally {
//            LOGGER.debug("기관 정보 연동 작업 종료");
//        }
//    }
//
//    @Transactional
//    public void execute() {
//        final StopWatch stopWatch = StopWatch.createStarted();
//
//        int createCount = 0;
//        int modifyCount = 0;
//        int terminateCount = 0;
//        int holdCount = 0;
//        int successCount = 0;
//
//        try {
//            ThreadSessionHolder.getInstance().getSession("damoa-scheduler");
//
//            LOGGER.info("신한다모아 기관 정보 연동 작업 시작");
//            final List<ClientInfomationPullHistoryDTO> dtoList = clientInfomationService.getStandbyList();
//
//
//            LOGGER.info("대상 기관 수: {}", dtoList.size());
//            Progress progress2 = new Progress(dtoList.size());
//            for (ClientInfomationPullHistoryDTO each : dtoList) {
//                LOGGER.debug("[{}] {}", each.getClientId(), each.getClientName());
//
//                switch (each.getClientStatus()) {
//                    case "1":
//                        createCount++;
//                        break;
//                    case "2":
//                        modifyCount++;
//                        break;
//                    case "3":
//                        terminateCount++;
//                        break;
//                    case "4":
//                        holdCount++;
//                        break;
//                }
//
//                try {
//                    boolean flag = clientInfomationService.execute(each.getPullDt(), each.getClientId());
//                    if(flag) {
//                        successCount++;
//                    }
//                } catch (Exception e) {
//                    if (LOGGER.isDebugEnabled()) {
//                        LOGGER.error(e.getMessage(), e);
//                    } else {
//                        LOGGER.error(e.getMessage());
//                    }
//                } finally {
//                    progress2.tick();
//                    LOGGER.info("기관 정보 연동 [{}] - {}% ({}/{}), {}s left", each.getClientId(), new DecimalFormat("##0.0").format(progress2.getProgressRate()), progress2.getProgressCount(), progress2.getTotalCount(), new DecimalFormat("#,##0.0").format(progress2.getTimeLeft()/1000F));
//                }
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
//            final int totalCount = createCount + modifyCount + terminateCount + holdCount;
//
//            LOGGER.info("========================================");
//            LOGGER.info(" 기관 정보 연동 작업");
//            LOGGER.info("----------------------------------------");
//            LOGGER.info(" 전체 건수 : {}", totalCount);
//            LOGGER.info("----------------------------------------");
//            LOGGER.info(" 신규 건수 : {}", createCount);
//            LOGGER.info(" 변경 건수 : {}", modifyCount);
//            LOGGER.info(" 해지 건수 : {}", terminateCount);
//            LOGGER.info(" 정지 건수 : {}", holdCount);
//            LOGGER.info("----------------------------------------");
//            LOGGER.info(" 성공 건수 : {}", successCount);
//            LOGGER.info(" 실패 건수 : {}", totalCount - successCount);
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
