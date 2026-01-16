package kr.co.finger.damoa.scheduler.task.monthly;

import kr.co.finger.damoa.model.monitoring.RA;
import kr.co.finger.damoa.model.monitoring.RM;
import kr.co.finger.damoa.scheduler.service.RsMonitoredDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class MonthlyMonitoringFileReceiveTask {

    private static final Logger LOG = LoggerFactory.getLogger(MonthlyMonitoringFileReceiveTask.class);

    @Autowired
    private RsMonitoredDataService rsMonitoredDataService;

    @Value("${damoa.monitor.fileGetDir}")
    private String filePath;

    /*
     * 매일 아침 6시에 실행되고 파일 수신이 여부가 N 인것들만 수신 기록 갱신*/
    @Scheduled(cron = "${damoa.MonthlyMonitoringFileReceiveTask.cron}")
    public void trigger() {
        execute();
    }

    public void execute() {
        LOG.info("모니터링 응답 파일 조회 시작");
        List<Map<String, Object>> recvFileList = rsMonitoredDataService.selectMonitoringFileList();
        LOG.info("###1 파일 조회 파일 리스트 : {}", recvFileList);
        if (recvFileList.size() == 0) {
            LOG.info("모니터링 응답 파일이 없음");
        } else {
            for (Map<String, Object> recvFile : recvFileList) {
                if ("N".equals(recvFile.get("res_yn").toString().trim())) {
                    String fileNm = recvFile.get("file_nm").toString();
                    String histSeq = recvFile.get("hist_seq").toString();
                    Map<String, Object> fileMap = processFile(filePath + fileNm, fileNm, histSeq);
                    fileMap.put("tranDt", getLastDayOfLastMonth());
                    fileMap.put("fileName", fileNm);

                    // Update DB with the result
                    rsMonitoredDataService.updateMonitoringFileHist(fileMap);
                    LOG.info("모니터링 응답 파일 처리 완료");
                } else {
                    LOG.info("모니터링 응답 파일이 이미 생성 됨");
                }
            }
        }
    }

    private Map<String, Object> processFile(String filePath, String fileName, String histSeq) {
        Map<String, Object> retMap = new HashMap<>();

        String resCode = "";
        String resYn = "Y";

        List<byte[]> dividedDataList = new ArrayList<>();


        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(Paths.get(filePath)))) {

            int dataInitLength = fileName.contains("rm") ? 201 : 101;

            byte[] buffer = new byte[dataInitLength];
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
                if (bytesRead >= 200) {
                    byte[] actualData = Arrays.copyOf(buffer, bytesRead);
                    dividedDataList.add(actualData);
                }
            }

            String respC = "";
            for (byte[] data : dividedDataList) {
                if (fileName.contains("rm")) {
                    //모니터링 기관 정보 데이터 처리
                    if (data[0] == 68) {
                        RA.DataPart dataPart = RA.DataPart.builder().
                                dataRecG(getRangByString(data, 0, 1))
                                .seqNo(getRangByString(data, 1, 9))
                                .trxC(getRangByString(data, 9, 10))
                                .hawiMchtBizno(getRangByString(data, 10, 20))
                                .hawiMchtNm(getRangByString(data, 20, 70))
                                .hawiMchtUpjongNm(getRangByString(data, 70, 130))
                                .epayKjAgeYn(getRangByString(data, 130, 131))
                                .drDt(getRangByString(data, 131, 139))
                                .hjiDt(getRangByString(data, 139, 147))
                                .respC(getRangByString(data, 147, 151))
                                .build();

                        respC = dataPart.getRespC();
                        Map<String, Object> instMap = new HashMap<>();
                        Map<String, Object> paramMap = new HashMap<>();
                        paramMap.put("chaName", dataPart.getHawiMchtNm());
                        paramMap.put("chaOffNo", dataPart.getHawiMchtBizno());
                        String chacd = rsMonitoredDataService.getChacdByChaOffNoAndChaNameFromChaList(paramMap);
                        instMap.put("histSeq", histSeq);
                        instMap.put("chacd", chacd);
                        instMap.put("resCd", respC);
                        LOG.info("하위 기관 모니터링 결과 데이터 : {}", dataPart);
                        rsMonitoredDataService.insertMonitoringFileHistDet(instMap);
                    }
                } else {
                    //가상계좌 모니터링 데이터 처리
                    if (data[0] == 68) {
                        RM.DataPart dataPart = RM.DataPart.builder()
                                .dataRecG(getRangByString(data, 0, 1))
                                .seqNo(getRangByString(data, 1, 9))
                                .trxG(getRangByString(data, 9, 10))
                                .vrtlAcno(getRangByString(data, 10, 24))
                                .hawiMchtBizno(getRangByString(data, 24, 34))
                                .drDt(getRangByString(data, 34, 42))
                                .hjiDt(getRangByString(data, 42, 50))
                                .respC(getRangByString(data, 50, 54))
                                .build();
                        respC = dataPart.getRespC();
                        Map<String, Object> instMap = new HashMap<>();
                        String chacd = rsMonitoredDataService.getChacdByChaOffNoAndChaNameFromVaList(dataPart.getVrtlAcno());
                        instMap.put("histSeq", histSeq);
                        instMap.put("chacd", chacd);
                        instMap.put("resCd", respC);
                        LOG.info("가상계좌 모니터링 결과 데이터 : {}", dataPart);
                        rsMonitoredDataService.insertMonitoringFileHistDet(instMap);
                    }
                }

            }


        } catch (IOException e) {
            LOG.error("Error reading rs monitoring file: " + filePath, e);
            resYn = "N";
        }

        retMap.put("resCode", resCode);
        retMap.put("resYn", resYn);

        return retMap;
    }

    private String getLastDayOfLastMonth() {
        LocalDate now = LocalDate.now();
        YearMonth preMon = YearMonth.of(now.getYear(), now.getMonth().minus(1));
        LocalDate lastDayOfPreMon = preMon.atEndOfMonth();
        return lastDayOfPreMon.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }


    private String getRangByString(byte[] array, int start, int end) {
        int length = end - start;
        if (start < 0 || end > array.length || start > end) {
            throw new IllegalArgumentException("잘못된 범위 입니다.");
        }
        byte[] range = new byte[length];
        System.arraycopy(array, start, range, 0, length);

        String result = new String(range, Charset.forName("EUC-KR"));

        return result.trim();
    }
}
