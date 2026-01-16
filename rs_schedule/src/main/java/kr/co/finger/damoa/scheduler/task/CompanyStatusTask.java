package kr.co.finger.damoa.scheduler.task;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import kr.co.finger.damoa.commons.biz.CompanyStatusLookupAPI;
import kr.co.finger.damoa.scheduler.service.DamoaService;
import kr.co.finger.damoa.scheduler.service.SimpleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 한달에 한번 전 기관에 대해서 휴폐업 상태를 확인하여 업데이트 함
 * 1. 5일에 한번씩 시작하도록 함. (중간에 스케쥴러가 중단될 수도 있음. 해당월에 처리한 기관은 SKIP하게 함.)
 * 2. 대상은 전기관. (이번달에 휴폐업 검증을 하지 않은 기관 대상으로 함.)
 * 3. 한기관씩 상태 확인
 * 4. 업데이트 수행.
 * CHA_CLOSE_CHK        휴폐업상태 [ Y:휴폐업상태  N:정상 ]
 * CHA_CLOSE_ST         휴폐업상태코드 [ 11:정상, 31:휴업, 32:폐업 ]
 * CHA_CLOSE_VAR_DT     휴폐업 검증일시
 * 5. 이렇게 처리하면 됨..
 */
@Component
public class CompanyStatusTask {

    // 5일마다 10시에..
//    public static final String CRON_EXPRESSION = "0 0 10 1,6,11,16,21,26 * ?";

    private String workId = "COMPANY_STATUS_CHECKER";
    @Value("${damoa.companyStatusLookupAPI.ip:10.10.20.53}")
    private String apiIp;
    @Value("${damoa.companyStatusLookupAPI.port:8080}")
    private int apiPort;

    @Autowired
    private SimpleService simpleService;

    @Autowired
    private DamoaService damoaService;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    public CompanyStatusTask() {
    }

    /**
     * TODO API gateway 휴폐업조회 사용하도록 수정?
     */
    @Scheduled(cron = "${damoa.companyStatusTask.cron}")
    public void execute() {

        try {


            List<Map<String, Object>> mapList = damoaService.findAllCorpCode();
            int count = 0;
            int totalCount = mapList.size();
            if (mapList == null || mapList.size() == 0) {
                LOG.error("기관상태 처리할 기관이 없음...");
                return;
            }
            CompanyStatusLookupAPI api = new CompanyStatusLookupAPI(apiIp, apiPort);
            for (Map<String, Object> map : mapList) {
                handleCorpCode(map, (++count), totalCount, api);
                // 1건 당 10초 지연
                Thread.sleep(10000);
            }
            LOG.info("기관상태체크 완료 " + mapList.size());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {

        }
    }

    /**
     * CHA_CLOSE_CHK	휴폐업상태 [ Y:휴폐업상태  N:정상 ]
     * CHA_CLOSE_ST	휴폐업상태코드 [ 11:정상, 31:휴업, 32:폐업 ]
     * CHA_CLOSE_VAR_DT	휴폐업 검증일시
     * CHA_CLOSE_VAR_RESON	기관 검증 결과 부적합사유
     *
     * @param map
     * @param count
     * @param totalCount
     */
    private void handleCorpCode(Map<String, Object> map, int count, int totalCount, CompanyStatusLookupAPI api) {
        try {
            String chacd = Maps.getValue(map, "CHACD");
            String chkChaOffNo = Maps.getValue(map, "CHAOFFNO");
            String nowDate = DateUtils.toString(new Date(), "yyyyMM");

            String date = Maps.getValue(map, "CHA_CLOSE_VAR_DT").trim().substring(0,6);
            String companyStatus = Maps.getValue(map, "CHA_CLOSE_CHK");
            LOG.info("휴폐업 기관 확인..[현재월]" + nowDate + "[기관]" + chacd + "[체크월]" + date + "[ " + count + "/" + totalCount);
            if (nowDate.equalsIgnoreCase(date)) {
                LOG.info("이번달에 처리가 된 기관임..SKIP " + chacd + " " + date);
                return;
            }

            if (StringUtils.isEmpty(chkChaOffNo)) {
                LOG.info("사업자번호가 없는 기관임..SKIP " + chacd + " " + date);
                damoaService.updateNoChangeCompanyStatus(chacd);
                return;
            }

            CompanyStatusLookupAPI.IFX1002 result = api.lookup(chkChaOffNo);
            String code = result.getTaxGbn();
            String status = findStatus(code);
            LOG.info("휴폐업조회 결과 " + chacd + " " + code + " " + status);
            if (CompanyStatusLookupAPI.CODE_REST.equalsIgnoreCase(code) || CompanyStatusLookupAPI.CODE_CLOSE.equalsIgnoreCase(code)) {
                if ("N".equalsIgnoreCase(companyStatus)) {
                    LOG.info("정상에서 휴폐업으로 변경.. " + chacd);
                    damoaService.updateFailChangeCompanyStatus(chacd, code);
                } else {
                    LOG.info("휴폐업에서 휴폐업으로 변경.. " + chacd);
                    damoaService.updateNoChangeCompanyStatus(chacd);
                }

            } else {
                if (CompanyStatusLookupAPI.CODE_ERROR.equalsIgnoreCase(code)) {

                    LOG.info("[SKIP]미확인/오류.. " + chacd);
                    damoaService.updateNoChangeCompanyStatus(chacd);
                } else {
                    // 휴폐업, 미확인/오류가 아니면 모두 정상처리..
                    if ("N".equalsIgnoreCase(companyStatus)) {
                        LOG.info("정상에서 정상으로 변경.. " + chacd);
                        damoaService.updateNoChangeCompanyStatus(chacd);
                    } else {
                        // 휴폐업 상태에서 정상으로...
                        LOG.info("휴폐업상태에서 정상으로 변경.. " + chacd);
                        damoaService.updateOkChangeCompanyStatus(chacd, code);
                    }

                }
            }


        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * @param status
     * @return
     */
    private String findStatus(String status) {

        if (CompanyStatusLookupAPI.CODE_ERROR.equalsIgnoreCase(status)) {
            return "미확인/오류";
        } else if (CompanyStatusLookupAPI.CODE_NORMAL.equalsIgnoreCase(status)) {
            return "일반";
        } else if (CompanyStatusLookupAPI.CODE_SIMPLE.equalsIgnoreCase(status)) {
            return "간이";
        } else if (CompanyStatusLookupAPI.CODE_DUTYFREE.equalsIgnoreCase(status)) {
            return "면세";
        } else if (CompanyStatusLookupAPI.CODE_NGO.equalsIgnoreCase(status)) {
            return "비영리";
        } else if (CompanyStatusLookupAPI.CODE_REST.equalsIgnoreCase(status)) {
            return "휴업";
        } else if (CompanyStatusLookupAPI.CODE_CLOSE.equalsIgnoreCase(status)) {
            return "폐업";
        } else if (CompanyStatusLookupAPI.CODE_ETC.equalsIgnoreCase(status)) {
            return "기타";
        } else {
            return "";
        }

    }
}
