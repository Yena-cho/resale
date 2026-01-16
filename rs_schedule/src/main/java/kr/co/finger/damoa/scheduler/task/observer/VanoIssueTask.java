//package kr.co.finger.damoa.scheduler.task.observer;
//
//import kr.co.finger.damoa.commons.Maps;
//import kr.co.finger.damoa.commons.io.HttpClientHelper;
//import kr.co.finger.damoa.commons.io.VaBean;
//import kr.co.finger.damoa.scheduler.service.DamoaService;
//import kr.co.finger.damoa.scheduler.service.SimpleService;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
///**
// * 가상계좌 발급 작업 (준실시간)
// *
// * @author wisehouse@finger.co.kr
// */
//@Component
//@Deprecated
//public class VanoIssueTask {
//    private Logger LOGGER = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private SimpleService simpleService;
//    private String workId = "VANO_ISSUE";
//    @Value("${damoa.vanoMsgServerUrl}")
//    private String vanoMsgServerUrl;
//
//    @Autowired
//    private DamoaService damoaService;
//
////    @Scheduled(fixedDelayString = "${damoa.vanoIssueTask.fixedDelay}")
//    public void trigger() {
//        try {
//            LOGGER.debug("가상계좌 발급 작업 시작");
//
//            final List<Map<String, Object>> mapList = damoaService.findVareq();
//            // VAREQ 조회
//            // 처리되지 않은게 있으면 가상계좌전문 서버에 요청처리.
//            // 응답이 제대로 되면 VAREQ 처리. VALIST insert DB 처리..
//            // 응답이 제대로 되지 않으면 VAREQ 에러 처리함.
//            if (mapList == null || mapList.size() == 0) {
//                LOGGER.debug("가상계좌 발급 요청이 없음");
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
//            LOGGER.debug("가상계좌 발급 작업 종료");
//        }
//    }
//
//    public void execute() {
//        try {
//            final List<Map<String, Object>> mapList = damoaService.findVareq();
//
//            LOGGER.info("가상계좌 발급");
//            for (Map<String, Object> map : mapList) {
//                String chacd = Maps.getValue(map, "CHACD");
//                String msgNo = Maps.getValue(map, "RULEMNGNO");
//                try {
//                    LOGGER.info("가상계좌발급시작 [기관]" + chacd + "[요청번호]" + msgNo);
//                    handleVanoIssue(map);
//                } catch (Exception e) {
//                    LOGGER.error("chacd 가상계좌발급 요청중 예외발생...[기관]" + chacd + "[요청번호]" + msgNo);
//                    LOGGER.error(e.getMessage(), e);
//                }
//            }
//            LOGGER.info("가상계좌 발급 요청이 있었음  " + mapList.size());
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            LOGGER.error("가상계좌 발급 스케쥴러 수행시 예외 발생 " + workId);
//        }
//    }
//
//    private void handleVanoIssue(Map<String, Object> map) throws IOException {
//        final String chacd = Maps.getValue(map, "CHACD");
//
//        String branchCode = findBranchCode(chacd);
//        String chatrty = findChatrty(chacd);
//        if (StringUtils.length(branchCode) != 4) {
//            LOGGER.error("관리점 코드 없음");
//            return;
//        }
//
//        branchCode = StringUtils.leftPad(branchCode, 5, '0');
//        branchCode = StringUtils.substring(branchCode, 0, 5);
//
//        String acccnt = Maps.getValue(map, "ACCCNT");
//        String msgNo = Maps.getValue(map, "RULEMNGNO");
//        String remark = Maps.getValue(map, "REMARK");
//        String fullUrl = vanoMsgServerUrl + "?type=1&corpCd=" + chacd + "&agencyCd=00000&branchCd=" + branchCode + "&count=" + String.valueOf(acccnt) + "&remark=" + remark;
//        LOGGER.trace("가상계좌전문 발급요청: {}", fullUrl);
//
//        // 아래코드는 굳이 실행할 필요는 없다. 단계가 많아져 오히려 문제를 더 발생시킬 수 있다.
//        final VaBean vaBean = callVanoReq(fullUrl);
//        if (vaBean == null) {
//            LOGGER.error("가상계좌전문 발급요청 예외 발생 [기관]" + chacd + "[요청번호]" + msgNo);
//            damoaService.finishVanoRequestFail(msgNo);
//            return;
//        }
//
//        LOGGER.info("가상계좌전문 발급요청 결과 [기관]" + chacd + "[요청번호]" + msgNo + " " + vaBean);
//        // 결과 코드
//        final String resultCode = vaBean.getResult();
//        // 시작 가상계좌번호
//        final String startAccount = StringUtils.trim(StringUtils.defaultString(vaBean.getStartAccount()));
//
//        // 전문서버 문제로 은행에서 응답을 못받은경우 재시도 (한글 result)
//        if (!StringUtils.isAlphanumeric(resultCode) || resultCode.length() != 4) {
//            damoaService.finishVanoRequestFailRetry(msgNo);
//            LOGGER.error("가상계좌 발급요청 실패 재시도 업데이트 [기관]" + chacd + "[요청번호]" + msgNo + "[가상계좌시작번호]" + startAccount + "[응답코드]" + resultCode);
//            return;
//        }
//
//        //은행에서 응답은 받았으나 오류코드인 경우 재시도 x
//        if (!StringUtils.equals(resultCode, "0000")) {
//            damoaService.finishVanoRequestFail(msgNo);
//            LOGGER.error("가상계좌 발급요청 실패 업데이트 [기관]" + chacd + "[요청번호]" + msgNo + "[가상계좌시작번호]" + startAccount + "[응답코드]" + resultCode);
//            return;
//        }
//
//        //은행에서 응답은 받았으나 시작 가상계좌 번호가 없는 경우 재시도x
//        if (StringUtils.isEmpty(startAccount)) {
//            damoaService.finishVanoRequestFail(msgNo);
//            LOGGER.error("가상계좌 발급요청 실패 업데이트 [기관]" + chacd + "[요청번호]" + msgNo + "[가상계좌시작번호]" + startAccount + "[응답코드]" + resultCode);
//            return;
//        }
//
//        // 전문 번호
//        final String fitxCd = msgNo;
//        boolean success = insertValist(startAccount, acccnt, fitxCd, chacd, remark, chatrty);
//
//        if(!success) {
//            damoaService.updateReqOkInsertFail(msgNo, startAccount);
//            LOGGER.error("가상계좌생성 실패 업데이트 [기관]" + chacd + "[요청번호]" + msgNo + "[가상계좌시작번호]" + startAccount);
//            return;
//        }
//
//            damoaService.finishVanoRequestOk(msgNo, startAccount);
//            LOGGER.info("가상계좌생성 성공 업데이트 [기관]" + chacd + "[요청번호]" + msgNo + "[가상계좌시작번호]" + startAccount);
//
//    }
//
//    private boolean insertValist(String startAccount, String acccnt, String fitxCd, String chacd, String remark, String chatrty) {
//        try {
//            // Web
//            if("01".equalsIgnoreCase(chatrty)){
//                damoaService.insertValist(startAccount, acccnt, "088", fitxCd, chacd, "N", "M", remark);
//            }else{
//                damoaService.insertValist(startAccount, acccnt, "088", fitxCd, chacd, "Y", "A", remark);
//            }
//            LOGGER.info("가상계좌생성 성공.. [시작가상계좌번호]" + startAccount + "[갯수]" + acccnt + "[기관]" + chacd + "[요청번호]" + fitxCd);
//            return true;
//        } catch (Exception e) {
//            LOGGER.error("가상계좌생성시 예외 발생.. [시작가상계좌번호]" + startAccount + "[갯수]" + acccnt + "[기관]" + chacd + "[요청번호]" + fitxCd);
//            LOGGER.error(e.getMessage(), e);
//            return false;
//        }
//
//    }
//
//    private VaBean callVanoReq(String fullUrl) {
//        try {
//            VaBean vaBean = HttpClientHelper.call(fullUrl);
//            return vaBean;
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//        return null;
//    }
//
//    private VaBean callVanoReq() {
//        VaBean vaBean = new VaBean();
//        vaBean.setResult("OK");
//        vaBean.setStartAccount("56210791725275");
//        return vaBean;
//    }
//
//    private String findBranchCode(String chacd) {
//        return damoaService.findBranchCode(chacd);
//    }
//
//    private String findChatrty(String chacd) {
//        return damoaService.findChatrty(chacd);
//    }
//}
