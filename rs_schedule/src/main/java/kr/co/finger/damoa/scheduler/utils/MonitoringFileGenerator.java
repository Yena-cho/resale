package kr.co.finger.damoa.scheduler.utils;

import kr.co.finger.damoa.scheduler.service.RsMonitoredDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MonitoringFileGenerator {

    @Autowired
    private RsMonitoredDataService rsMonitoredDataService;

    final static int RM_HEADER_FILLER_LENGTH = 178;
    final static int RA_HEADER_FILLER_LENGTH = 78;
    final static int RM_TRAILER_FILLER_LENGTH = 175;
    final static int RA_TRAILER_FILLER_LENGTH = 75;

    final static String newLine = "\r\n";

    @Value("${damoa.monitor.filePutDir}")
    private String filePath;

    public String padRight(String data, int totalBytes) {
        Charset charset = Charset.forName("EUC-KR");
        if (data == null) {
            return repeat(" ", totalBytes);
        }
        byte[] dataBytes = data.getBytes(charset);
        int dataLength = dataBytes.length;

        if (dataLength >= totalBytes) {
            return new String(dataBytes, 0, totalBytes, charset);
        }

        int paddingLength = totalBytes - dataLength;
        return data + repeat(" ", paddingLength);

//        return data == null ? repeat(" ", length) : String.format("%-" + length + "s", data).substring(0, length);
    }

    //헤더부 생성 함수
    private String generateHeaderPart(String fileCode, String fgCd, int fillerLength) {
        LocalDate now = LocalDate.now();
        YearMonth previousMonth = YearMonth.from(now.minusMonths(1));
        LocalDate lastDayOfPreviousMonth = previousMonth.atEndOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String jobDt = lastDayOfPreviousMonth.format(formatter);


        return "H" +
                padRight(fileCode.toUpperCase(), 2) +
                "088" +
                padRight(jobDt, 8) +
                padRight(fgCd, 8) +
                padRight(null, fillerLength) +
                newLine;
    }

    //트레일러부 생성 함수
    private String generateTrailerPart(int totalRecords, int fillerLength) {
        return "T" +
                String.format("%08d", totalRecords) +
                padRight(null, 16) +
                padRight(null, fillerLength);
    }

    //데이터부 생성 함수
    private String generateDataPart(Map<String, Object> data, boolean isRmFile) {
        StringBuilder result = new StringBuilder();
        result.append("D")
                .append(padRight(data.get("seq_no").toString(), 8))
                .append(data.get(isRmFile ? "trx_c" : "trx_g").toString());

        if (isRmFile) {
            result.append(generateRmFileData(data));
        } else {
            result.append(generateRaFileData(data));
        }

        result.append(padRight(data.get("dr_dt") != null ? data.get("dr_dt").toString() : "", 8))
                .append(padRight(data.get("hji_dt") != null ? data.get("hji_dt").toString() : "", 8))
                .append(repeat(" ", isRmFile ? 53 : 50));

        return result.append(newLine).toString();
    }

    private String generateRmFileData(Map<String, Object> data) {
        return padRight(data.get("hawi_mcht_bizno").toString(), 10) +
                padRight(data.get("hawi_mcht_nm").toString(), 50) +
                padRight(data.get("hawi_mcht_upjong_nm").toString(), 60) +
                data.get("epay_kj_age_yn").toString();
    }

    private String generateRaFileData(Map<String, Object> data) {
        return padRight(data.get("vrtl_acno").toString(), 14) +
                padRight(data.get("hawi_mcht_bizno").toString(), 10);
    }


    public void generateFile(String fileCode, List<Map<String, Object>> fgCdList) {
        String formattedyyyyMMdd = getFormattedStringDate("yyyyMMdd");
        String formattedMMdd = getFormattedStringDate("MMdd");

        boolean isRmFile = "rm".equals(fileCode);

        int headerFillerLength = isRmFile ? RM_HEADER_FILLER_LENGTH : RA_HEADER_FILLER_LENGTH;
        int trailerFillerLength = isRmFile ? RM_TRAILER_FILLER_LENGTH : RA_TRAILER_FILLER_LENGTH;

        for (Map<String, Object> data : fgCdList) {
            String fgCd = data.get("hdr_vac_org_cd_no").toString(); //은행에 등록 된 핑거 기관 코드
            String fgCdBack4 = fgCd.substring(fgCd.length() - 4);
            String fileName = getMonitoringFileName(fileCode, fgCdBack4, formattedMMdd);

            List<Map<String, Object>> orgMonitoringDataList = rsMonitoredDataService.getRsOrgMonitoredData(fgCd);
            List<Map<String, Object>> vaList = rsMonitoredDataService.getRsVaMonitoredData(fgCd);

            Path realFilePath = Paths.get(filePath + fileName);

            try {
                Files.createDirectories(realFilePath.getParent());

                try (BufferedWriter writer = Files.newBufferedWriter(realFilePath, Charset.forName("EUC-KR"))) {
                    writer.write(generateHeaderPart(fileCode, fgCd, headerFillerLength));
                    List<String> monitoringDataList = isRmFile ?
                            orgMonitoringDataList.stream()
                                    .map(dataItem -> generateDataPart(dataItem, true))
                                    .collect(Collectors.toList()) :
                            vaList.stream()
                                    .map(dataItem -> generateDataPart(dataItem, false))
                                    .collect(Collectors.toList());

                    for (String monitoringData : monitoringDataList) {
                        writer.write(monitoringData);
                    }

                    writer.write(generateTrailerPart(monitoringDataList.size(), trailerFillerLength));
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("fileName", fileName);
                    paramMap.put("filePath", filePath);
                    paramMap.put("tranDt", formattedyyyyMMdd);

                    rsMonitoredDataService.insertMonitoringFileHist(paramMap);  //파일생성히스토리 저장
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String repeat(String str, int times) {
        if (str == null) {
            throw new IllegalArgumentException("String to repeat cannot be null");
        }
        if (times <= 0) {
            return "";
        }
        StringBuilder repeated = new StringBuilder(str.length() * times);
        for (int i = 0; i < times; i++) {
            repeated.append(str);
        }
        return repeated.toString();
    }

    private String getMonitoringFileName(String fileType, String chaCd, String mmdd) {
        return "vas_" + fileType + chaCd + "." + mmdd;
    }

    private String getFormattedStringDate(String ofPattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(ofPattern));
    }

    private String getLastDayOfPreviousMonthByFormat(String format) {
        LocalDate now  = LocalDate.now();
        LocalDate lastDayOfPrevMon = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return lastDayOfPrevMon.format(formatter);
    }
}
