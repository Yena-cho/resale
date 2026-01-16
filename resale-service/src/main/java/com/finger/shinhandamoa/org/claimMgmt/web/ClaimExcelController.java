package com.finger.shinhandamoa.org.claimMgmt.web;

import com.finger.shinhandamoa.common.CmmnUtils;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimExcelService;
import com.finger.shinhandamoa.org.claimMgmt.service.ClaimService;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author by puki
 * @date 2018. 4. 10.
 * @desc 최초생성
 */
@Controller
@RequestMapping("org/claimMgmt/**")
public class ClaimExcelController {
    private static final Logger logger = LoggerFactory.getLogger(ClaimExcelController.class);

    // 업로드 디렉토리
    @Value("${file.path.temp}")
    private String uploadPath;

    // 청구항목 Service
    @Inject
    private ClaimExcelService claimExcelService;

    @Inject
    private ClaimService claimService;

    @Inject
    private CodeService codeService;

    /*
     * 청구관리 > 청구등록 > 청구대량등록(excel upload)
     */
    @Transactional
    @SuppressWarnings("resource")
    @ResponseBody
    @RequestMapping("excelUpload")
    public HashMap<String, Object> excelUpload(MultipartHttpServletRequest request, boolean flag) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        MultipartFile excelFile = request.getFile("file");
        String fileName = excelFile.getOriginalFilename();
        int pos = fileName.toLowerCase().lastIndexOf(".");
        String ext = fileName.substring(pos + 1);

        InputStream inputDocument = null;
        Workbook workbook = null;
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        FormulaEvaluator evaluator = null;
        try {
            logger.debug("기관 {} 청구대량등록", chaCd);
            inputDocument = excelFile.getInputStream();
            if (!ext.equals("xls") && !ext.equals("xlsx")) {
                retMap.put("resCode", "0001");
                logger.debug("기관 {} 청구대량등록 오류 엑셀파일 아님", chaCd);
                return retMap;
            } else {
                if (fileName.toLowerCase().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputDocument);
                } else {
                    workbook = new HSSFWorkbook(inputDocument);
                }
            }
            evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        try {
            final List<CodeDTO> claimItemCd = codeService.claimItemCd(chaCd); // 청구항목코드
            final int cusGubnCdCnt = codeService.cusGubnCdCnt(chaCd);    // 고객구분항목카운트
            List<CodeDTO> cusGubnCd = codeService.cusGubnCd(chaCd); // 고객구분항목
            retMap.put("cusGubnCdCnt", cusGubnCdCnt);
            retMap.put("cusGubnCd", cusGubnCd);

            Sheet workSheet = workbook.getSheetAt(0); // 첫번째 Sheet

            int rowSize = workSheet.getPhysicalNumberOfRows(); // 행의 총 개수 (행은 0부터 시작함)
            int failCnt = 0;

            List<Map<String, Object>> list = new ArrayList<>();

            // 작업한 엑셀의 마지막 row를 확인하기 위함 - commit을 한번만  수행하기 위해...
            Row rCnt = workSheet.getRow(0);
            int cellLen = (int) rCnt.getLastCellNum();
            int rowNum = 0;
            for (int i = 2; i < rowSize; i++) {
                Row row = workSheet.getRow(i);
                for (int j = 0; j < cellLen; j++) {
                    Cell cell = row.getCell(j);
                    if (i == j && (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)) {
                        rowNum++;
                    }
                }
            }

            if (cellLen < ((claimItemCd.size() * 2) + 6 + cusGubnCdCnt)) {
                logger.debug("기관: {} 청구대량등록 양식 컬럼갯수 오류", chaCd);
                retMap.put("resCode", "0001");
                retMap.put("failCnt", failCnt);
                return retMap;
            }
            logger.debug("기관: {} 청구대량등록 총 {} 행", chaCd, rowSize);
            int a = 0;
            for (int i = 2; i < rowSize; i++) { // i를 2부터 시작해야 세번째 행부터 데이터가 입력된다.
                Map<String, Object> map = new HashMap<String, Object>();
                boolean insFlag = true; // 파일등록 flag

                if (workSheet.getRow(i) == null) {
                    logger.debug("기관: {} 청구대량등록 {} 행 없음", chaCd, i);
                    continue;
                }

                if (i == 2) {
                    claimExcelService.claimFailDelete(chaCd);     // 기존 Fail Data delete
                }

                Row rowCnt = workSheet.getRow(0); // 컬럼 수 확인을 위하여 header 수를 읽어온다.
                Row row = workSheet.getRow(i);

                int cellLength = (int) rowCnt.getLastCellNum(); // 열의 총 개수
                int idx = i + 1;

                // 셀서식을 텍스트로 초기화 한 엑셀 중 input값이 없는 row의 insert를 막기위함
                int cno = 0;
                for (int j = 0; j < cellLength; j++) {
                    Cell cell = row.getCell(j);

                    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        cno++;
                    }
                }

                if (((claimItemCd.size() * 2) + 9) == cno) {
                    logger.debug("기관: {} 청구대량등록 {} 행에 값 없음", chaCd, i);
                    continue;
                }

                final String[] columnValues = new String[cellLength + (Integer) retMap.get("cusGubnCdCnt")];

                for (int j = 0; j < cellLength + (Integer) retMap.get("cusGubnCdCnt"); j++) {
                    String valueStr = ""; // 엑셀에서 뽑아낸 데이터를 담아놓을 String 변수 선언 및 초기화

                    final Cell cell = row.getCell(j);
                    final CellValue cellValue = evaluator.evaluate(cell);
                    try {
                        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { // CELL_TYPE_BLANK로만 체크할 경우 비어있는  셀을 놓칠 수 있다.
                            continue;
                        }

                        switch (cellValue.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                valueStr = cellValue.getStringValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC: // 날짜 형식이든 숫자 형식이든 다 CELL_TYPE_NUMERIC으로 인식.
                                if (DateUtil.isCellDateFormatted(cell)) { // 날짜 유형의 데이터일 경우,
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                                    String formattedStr = dateFormat.format(cellValue.getNumberValue());
                                    valueStr = formattedStr;
                                    break;
                                } else { // 순수하게 숫자 데이터일 경우,
                                    valueStr = NumberToTextConverter.toText(cellValue.getNumberValue()); // String로  변형
                                    break;
                                }
                            case Cell.CELL_TYPE_BOOLEAN:
                                valueStr = Boolean.toString(cellValue.getBooleanValue());
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                valueStr = "#ERROR!";
                                map.put("result", "올바르지 않은 수식이 포함되어 있습니다.");
                                break;
                            default:
                                valueStr = cellValue.getStringValue();
                                break;
                        }
                    } finally {
                        columnValues[j] = StringUtils.trim(valueStr);
                    }

                }

                map.put("chaCd", chaCd);
                map.put("xRow", String.valueOf(i));
                map.put("xCount", claimItemCd.size());

                // 청구월
                if (StringUtils.isEmpty(columnValues[0])) {
                    map.put("result", "청구월 항목이 없음");
                    map.put("masMonth", "");
                } else if (!StringUtils.isEmpty(columnValues[0]) && !CmmnUtils.validateDateFormat(StringUtils.defaultString(columnValues[0]).replace(".", ""))) {
                    map.put("result", "숫자형식에 맞게 입력해주세요");
                    map.put("masMonth", "");
                } else {
                    map.put("masMonth", columnValues[0].replace(".", ""));
                }

                // 고객명
                if (StringUtils.isEmpty(columnValues[1])) {
                    map.put("result", "고객명이 없음");
                } else {
                    map.put("cusName", columnValues[1]);
                }

                // 고객구분
                for (int j = 1; j <= cusGubnCdCnt; j++) {
                    if(columnValues[j+1].getBytes("EUC-KR").length > 30) {
                        map.put("result", "고객구분" + j + " 30byte 초과");
                        map.put("cusGubn"+j, "");
                    } else {
                        map.put("cusGubn"+j, StringUtils.defaultString(columnValues[j+1]));
                    }
                }

                // 가상계좌
                if (StringUtils.isEmpty(columnValues[2 + cusGubnCdCnt])) {
                    map.put("result", "가상계좌 없음");
                    map.put("vano", "");
                } else if (!StringUtils.isEmpty(columnValues[2 + cusGubnCdCnt]) && !StringUtils.isNumeric(StringUtils.defaultString(columnValues[2 + cusGubnCdCnt]))) {
                    map.put("result", "숫자형식에 맞게 입력해주세요");
                    map.put("vano", "");
                } else {
                    map.put("vano", StringUtils.defaultString(columnValues[2 + cusGubnCdCnt]));
                }
                map.put("cusKey", StringUtils.defaultString(columnValues[2 + cusGubnCdCnt]));

                // 납부시작일
                if (StringUtils.isEmpty(columnValues[3 + cusGubnCdCnt])) {
                    map.put("result", "납부시작일이 없음");
                    map.put("startDate", "");
                } else if (!StringUtils.isEmpty(columnValues[3 + cusGubnCdCnt]) && !CmmnUtils.validateDateFormat2(StringUtils.defaultString(columnValues[3 + cusGubnCdCnt]).replace(".", ""))) {
                    map.put("result", "숫자형식에 맞게 입력해주세요");
                    map.put("startDate", "");
                } else {
                    map.put("startDate", StringUtils.defaultString(columnValues[3 + cusGubnCdCnt]).replace(".", ""));
                }

                // 납부마감일
                if (StringUtils.isEmpty(columnValues[4 + cusGubnCdCnt])) {
                    map.put("result", "납부마감일이 없음");
                    map.put("endDate", "");
                } else if (!StringUtils.isEmpty(columnValues[4 + cusGubnCdCnt]) && !CmmnUtils.validateDateFormat2(StringUtils.defaultString(columnValues[4 + cusGubnCdCnt]).replace(".", ""))) {
                    map.put("result", "숫자형식에 맞게 입력해주세요");
                    map.put("endDate", "");
                } else {
                    map.put("endDate", StringUtils.defaultString(columnValues[4 + cusGubnCdCnt]).replace(".", ""));
                }

                // 고지서용 표시마감일 (비어있으면 자동으로 enddate랑 같은일자로
                if (StringUtils.isEmpty(columnValues[5 + cusGubnCdCnt])) {
                    map.put("printDate", StringUtils.defaultString(columnValues[4 + cusGubnCdCnt]).replace(".", ""));
                } else if (!StringUtils.isEmpty(columnValues[5 + cusGubnCdCnt]) && !CmmnUtils.validateDateFormat2(StringUtils.defaultString(columnValues[5 + cusGubnCdCnt]).replace(".", ""))) {
                    map.put("result", "숫자형식에 맞게 입력해주세요");
                    map.put("printDate", "");
                } else {
                    map.put("printDate", StringUtils.defaultString(columnValues[5 + cusGubnCdCnt]).replace(".", ""));
                }

                /**
                 * 추가안내사항 60자씩 자르기
                 */
                final int claimItemStartColumnNo = 6 + cusGubnCdCnt;
                final int remarkStartColumnNo = claimItemStartColumnNo + claimItemCd.size() * 2;
                map.put("remark1", StringUtils.substring(columnValues[remarkStartColumnNo], 0, 60));
                map.put("remark2", StringUtils.substring(columnValues[remarkStartColumnNo + 1], 0, 60));
                map.put("remark3", StringUtils.substring(columnValues[remarkStartColumnNo + 2], 0, 60));

                logger.debug("청구항목 시작 행: {}", i);
                int itemCount = 0;
                boolean isNumeric = true;
                for (int j = 0; j < claimItemCd.size(); j++) {

                    // 금액
                    String amt = columnValues[claimItemStartColumnNo + j * 2];
                    amt = amt.replaceAll(",", "");
                    if (amt.contains(".") && !amt.startsWith(".")) {
                        amt = String.valueOf(Double.valueOf(NumberUtils.toDouble(amt, -1)).longValue());
                    }

                    // 비고
                    String remark = StringUtils.substring(columnValues[claimItemStartColumnNo + j * 2 + 1], 0, 50);

                    if (StringUtils.isBlank(amt) || "".equals(amt)) {
                        continue;
                    }

                    if (!StringUtils.isNumeric(amt) || NumberUtils.toLong(amt) < 0L) {
                        map.put("payItemCd", rowCnt.getCell(claimItemStartColumnNo + j * 2) + "");
                        isNumeric = false;
                        continue;
                    }

                    map.put("payItemCd", rowCnt.getCell(claimItemStartColumnNo + j * 2) + "");
                    map.put("xAmt", amt);

                    logger.warn("청구항목 행 amt: {}, {}", rowCnt.getCell(claimItemStartColumnNo + j * 2), amt);

                    map.put("remark", remark);
                    if (itemCount >= 9) {
                        map.put("result", "청구항목개수 초과");
                        failCnt += claimExcelService.bulkUploadExcel(Arrays.asList(map), a++, (rowSize - rowNum));
                    } else {
                        failCnt += claimExcelService.bulkUploadExcel(Arrays.asList(map), a++, (rowSize - rowNum));
                    }

                    itemCount++;
                }

                if (!isNumeric) {
                    map.put("result", "숫자형식에 맞게 입력해주세요");
                    map.put("xAmt", "0");
                    failCnt += claimExcelService.bulkUploadExcel(Arrays.asList(map), a++, (rowSize - rowNum));
                    continue;
                }

                if (itemCount == 0) {
                    map.put("result", "청구항목값이 없음");
                    failCnt += claimExcelService.bulkUploadExcel(Arrays.asList(map), a++, (rowSize - rowNum));
                    continue;
                }

            } // for loop(i) end (Rows)

            retMap.put("resCode", "0000");
            retMap.put("failCnt", failCnt);
            return retMap;
        } catch (Exception e) {
            logger.info("파일양식오류");
            logger.error(e.getMessage());
            throw e;
        } finally {
            inputDocument.close();
            logger.info(">>> 파일삭제완료 <<<");
        }
    }

    /*
     * 청구관리 > 청구등록 > 엑셀파일양식 다운로드(excel download)
     */
    @ResponseBody
    @RequestMapping("excelDownload")
    public View excelDownload(HttpServletRequest request, HttpServletResponse response, ClaimDTO dto, Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        Map<String, Object> map = new HashMap<String, Object>();

        if (dto.getMonthCheck() != null && dto.getMonthCheck().equals("true")) {
            map.put("masMonth", dto.getExcelClaimYear() + dto.getExcelClaimMonth());
        } else {
            map.put("masMonth", "");
        }
        if (dto.getInsPeriod() != null && dto.getInsPeriod().equals("true")) {
            map.put("startDate", dto.getExcelStartDate());
            map.put("endDate", dto.getExcelEndDate());
        } else {
            map.put("startDate", "");
            map.put("endDate", "");
        }
        if (dto.getPrintCheck() != null && dto.getPrintCheck().equals("true")) {
            map.put("printDate", dto.getPrintDate());
        } else {
            map.put("printDate", "");
        }

        map.put("chaCd", chaCd);
        map.put("selCusCk", dto.getSelCusCk());
        String selCusCk = dto.getSelCusCk();

        try {
            List<ClaimDTO> iList = null;
            List<ClaimDTO> cList = null;
            logger.debug("기관 {} 청구등록 > 엑셀파일양식다운", chaCd);
            if (selCusCk.equals("all")) {
                iList = claimExcelService.excelItemList(map);
                cList = claimExcelService.excelCusList(map);
            } else {
                iList = claimExcelService.excelItemList(map);
                cList = claimExcelService.selExcelBeforeMonth(map);
            }

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            model.addAttribute("iList", iList);
            model.addAttribute("cList", cList);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelDownload();
    }

    /*
     * 청구관리 > 청구등록 > 대량등록 > 실패내역
     */
    @ResponseBody
    @RequestMapping("claimFail")
    public HashMap<String, Object> selectClaimFail(@RequestParam(defaultValue = "1") int curPage, @RequestParam(defaultValue = "5") int PAGE_SCALE) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            logger.debug("기관 {} 청구등록 > 대량등록 > 실패내역", chaCd);
            // total count
            int count = claimExcelService.failTotalCount(chaCd);
            map.put("count", count);

            PageVO page = new PageVO(count, curPage, PAGE_SCALE);
            int start = page.getPageBegin();
            int end = page.getPageEnd();

            List<ClaimDTO> list = claimExcelService.failList(chaCd, start, end);
            map.put("PAGE_SCALE", PAGE_SCALE);
            map.put("failList", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return map;
    }

    /*
     * 청구관리 > 청구등록 > 대량등록 > 실패내역 엑셀 다운로드
     */
    @ResponseBody
    @RequestMapping("excelFailDownload")
    public View excelFailDownload(HttpServletRequest request, HttpServletResponse response, ClaimDTO dto, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chaCd", chaCd);

        try {
            logger.debug("기관 {} 청구등록 > 대량등록 > 실패내역 엑셀 다운로드", chaCd);
            List<ClaimDTO> iList = claimExcelService.excelItemList(map);
            model.addAttribute("iList", iList);

            List<ClaimDTO> itemMasterList = claimExcelService.selectClaimFailMasterExcel(chaCd);
            for (ClaimDTO each : itemMasterList) {
                Map<String, Object> itemMap = new HashMap<String, Object>();
                String xRow = each.getxRow();
                itemMap.put("xRow", xRow);
                itemMap.put("chaCd", chaCd);
                List<ClaimItemDTO> itemList = claimExcelService.selectFailDetailsForExcel(itemMap);
                each.setDetailsList(itemList);
            }
            model.addAttribute("list", itemMasterList);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

//        return new ExcelFailDownload();
        return new ExcelSaveClaimReg();
    }

    /*
     * 청구관리 > 청구조회 > 파일저장
     */
    @ResponseBody
    @RequestMapping("excelSave")
    public View excelSsaveDownload(HttpServletRequest request, HttpServletResponse response, ClaimDTO dto, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        try {
            logger.debug("기관 {} 청구조회 > 파일저장", chaCd);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("chaCd", chaCd);
            map.put("seachCusGubn", dto.getSeachCusGubn());
            if (dto.getCusGubnValue() != null && !"".equals(dto.getCusGubnValue())) {
                String value = dto.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            if (dto.getNotiMasSt().equals("PA00")) { //청구등록
                String selGb = "M";
                String masMonth = claimService.selectClaimMonth(chaCd, dto.getNotiMasSt());
                map.put("masMonth", masMonth);
                map.put("selGb", selGb);
                String view = "ins";
                model.addAttribute("view", view);
            } else {
                String selGb = dto.getSelGb() == null ? "M" : dto.getSelGb();
                map.put("selGb", selGb);
                if (selGb.equals("M")) { // 조회구분 - 청구월
                    map.put("masMonth", dto.getMasMonth());
                } else {                         // 조회구분 - 기간별
                    map.put("masStDt", dto.getMasStDt());
                    map.put("masEdDt", dto.getMasEdDt());
                }
                String view = "sel";
                model.addAttribute("view", view);
                map.put("cusGubn", dto.getCusGubn()); // 검색구분
                if (!"".equals(dto.getSearchValue())) {
                    String value = dto.getSearchValue().trim();
                    String[] valueList = value.split("\\s*,\\s*");
                    map.put("searchValue", valueList);
                }
                map.put("strList", dto.getStrList());
                map.put("payList", dto.getPayList()); // 수납상태
            }
            map.put("notiMasSt", dto.getNotiMasSt());
            map.put("search_orderBy", dto.getSearch_orderBy());

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            List<ClaimDTO> iList = claimExcelService.excelItemList(map);
            model.addAttribute("iList", iList);

            List<ClaimDTO> itemMasterList = claimExcelService.excelMasterList(map);
            for (ClaimDTO each : itemMasterList) {
                String notimascd = each.getNotiMasCd();
                List<ClaimItemDTO> itemList = claimExcelService.selectDetailsForExcel(notimascd);
                each.setDetailsList(itemList);
            }
            model.addAttribute("list", itemMasterList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveClaimReg();
    }
}
