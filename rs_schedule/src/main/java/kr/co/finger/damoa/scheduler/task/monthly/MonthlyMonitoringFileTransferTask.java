package kr.co.finger.damoa.scheduler.task.monthly;

import kr.co.finger.damoa.scheduler.service.HolidayService;
import kr.co.finger.damoa.scheduler.utils.MonitoringFileGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MonthlyMonitoringFileTransferTask {

    @Autowired
    private MonitoringFileGenerator monitoringFileGenerator;


    private final HolidayService holidayService;

    public MonthlyMonitoringFileTransferTask(HolidayService holidayService) {
        this.holidayService = holidayService;
    }


    static final String RM_FILE_CD = "rm";
    static final String RA_FILE_CD = "ra";

    static final String HIGHWAY_FG_CD = "20008153";
    static final String COMMON_FG_CD = "20008155";

    private final Logger LOG = LoggerFactory.getLogger(getClass());


    @Scheduled(cron = "${damoa.MonthlyMonitoringFileTransferTask.cron}")
    public void trigger() {
        LocalDate today = LocalDate.now();
//        LocalDate firstBusinessDay = holidayService.getFirstBusinessDay(today.getYear(), today.getMonthValue());
        LocalDate firstBusinessDay = holidayService.getFirstBusinessDay(today.getYear(), today.getMonthValue());
        LOG.info("MonthlyMonitoringFileTransferTask START", firstBusinessDay);
        if (today.equals(firstBusinessDay)) {
            LOG.info("오늘은 첫 영업일입니다. 모니터링 파일 생성 작업 실행");
            execute();
        } else {
            LOG.info("첫 영업일 아닙니다.");
        }
    }

    public void execute() {
        try {
            LOG.info("모니터링 파일 생성 시작");
            //은행에 등록 된 핑거 기관 코드 리스트
            Map<String, Object> hgWayFgCd = createFgCd(HIGHWAY_FG_CD);
            Map<String, Object> commFgCd = createFgCd(COMMON_FG_CD);

            List<Map<String, Object>> rsFgCdList = Arrays.asList(hgWayFgCd, commFgCd);

            LOG.info("{}", rsFgCdList);
            monitoringFileGenerator.generateFile(RM_FILE_CD, rsFgCdList);
            monitoringFileGenerator.generateFile(RA_FILE_CD, rsFgCdList);


        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private Map<String, Object> createFgCd(String fgCd) {
        Map<String, Object> map = new HashMap<>();
        map.put("hdr_vac_org_cd_no", fgCd);
        return map;
    }
}

