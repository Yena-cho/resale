
package com.finger.shinhandamoa.org.custMgmt.web;

import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;
import com.finger.shinhandamoa.org.custMgmt.service.CustExcelService;
import com.finger.shinhandamoa.org.custMgmt.service.CustMgmtService;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import com.finger.shinhandamoa.util.service.CodeService;
import com.finger.shinhandamoa.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author by puki
 * @date 2018. 4. 10.
 * @desc 최초생성
 */
@Controller
@RequestMapping("org/custMgmt/**")
public class CustExcelController {

    private static final Logger logger = LoggerFactory.getLogger(CustExcelController.class);

    // 업로드 디렉토리 servlet-context.xml에 설정되어 있음
    @Value("${file.path.upload}")
    String uploadPath;

    // 청구항목 Service
    @Inject
    private CustExcelService custExcelService;

    @Inject
    private CodeService codeService;

    @Autowired
    private CustMgmtService custMgmtService;

    /*
     * 고객관리 > 고객등록 > 파일저장
     */
    @ResponseBody
    @RequestMapping("excelSaveCustReg")
    public View excelSaveCustReg(HttpServletRequest request, HttpServletResponse response, CustReg01DTO dto, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 고객등록 > 파일저장", chaCd);
            map.put("chaCd", chaCd);
            map.put("disabled", "I");
            map.put("searchOrderBy", dto.getSearchOrderBy());

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            List<CustReg01DTO> list = custExcelService.selectExcelSaveCustReg(map);
            model.addAttribute("map", dto.getCashShow());
            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return new ExcelSaveCustReg();
    }

    /*
     * 고객관리 > 고객등록 > 엑셀파일양식 다운로드(excel download)
     */
    @ResponseBody
    @RequestMapping("excelDownload")
    public View excelDownload(HttpServletRequest request, HttpServletResponse response, CustReg01DTO dto, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 고객등록 > 엑셀파일양식", user);
            map.put("chaCd", user);

            List<CustReg01DTO> list = custExcelService.excelList(map);
            List<CodeDTO> cusGbList = codeService.cusGubnCd(user);

            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());
            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return new ExcelDownload();
    }

    /*
     * 고객관리 > 고객등록 > 고객대량등록(excel upload)
     */
    @SuppressWarnings("resource")
    @ResponseBody
    @RequestMapping("excelUpload")
    public HashMap<String, Object> excelUpload(MultipartHttpServletRequest request, boolean flag) throws Exception {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        logger.debug("기관 {} 고객등록 > 고객대량등록", chaCd);
        MultipartFile excelFile = request.getFile("file");

        File file = null;
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        file = new File(rootPath + excelFile.getOriginalFilename());

        try {
            excelFile.transferTo(file);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        int pos = excelFile.getOriginalFilename().toLowerCase().lastIndexOf(".");
        String ext = excelFile.getOriginalFilename().substring(pos + 1);
        FileInputStream inputDocument = null;
        Workbook workbook = null;
        try {
            inputDocument = new FileInputStream(file);
            if (!ext.equals("xls") && !ext.equals("xlsx")) {
                retMap.put("resCode", "0001");
                return retMap;
            } else {
                if (file.getName().toLowerCase().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputDocument);
                } else {
                    workbook = new HSSFWorkbook(inputDocument);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
        try {
            int resCnt = 0;
            Sheet workSheet = workbook.getSheetAt(0);

            int rowSize = workSheet.getPhysicalNumberOfRows();
            int cusGubnCnt = codeService.cusGubnCdCnt(chaCd);

            Row rCnt = workSheet.getRow(0);
            int cellLen = (int) rCnt.getLastCellNum();
            if ((cusGubnCnt + 10) == cellLen) {
                for (int i = 1; i < rowSize; i++) { // i를 1부터 시작해야 두번째 행부터 데이터가 입력된다.
                    boolean insFlag = true; // 파일등록 flag

                    Row rowCnt = workSheet.getRow(0); // 컬럼 수 확인을 위하여 header 수를 읽어온다.
                    Row row = workSheet.getRow(i);

                    int cellLength = (int) rowCnt.getLastCellNum(); // 열의 총 개수

                    String valueStr = ""; // 엑셀에서 뽑아낸 데이터를 담아놓을 String 변수 선언 및 초기화
                    CustReg01DTO dto = new CustReg01DTO(); // DB에 Insert하기 위해 valueStr 데이터를 옮겨담을 객체

                    // 2행부터 50000 행까지의 셀서식을 텍스트로 초기화 한 엑셀 중 input값이 없는 row의 insert를 막기위함
                    int no = 0;
                    for (int j = 0; j < cellLength; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                            no++;
                        }
                    }
                    if ((cusGubnCnt + 10) == no) {
                        continue;
                    }

                    final String[] columnValues = new String[cellLength + cusGubnCnt];
                    for (int j = 0; j < cellLength; j++) {
                        Cell cell = row.getCell(j);
                        try {
                            // 셀에 있는 데이터들을 타입별로 분류해서 valueStr 변수에 담는다.
                            if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { // CELL_TYPE_BLANK로만 체크할 경우 비어있는  셀을 놓칠 수 있다.
                                valueStr = "";
                            } else {
                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_STRING:
                                        valueStr = cell.getStringCellValue();
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC: // 날짜 형식이든 숫자 형식이든 다 CELL_TYPE_NUMERIC으로 인식.

                                        if (DateUtil.isCellDateFormatted(cell)) { // 날짜 유형의 데이터일 경우,
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                                            String formattedStr = dateFormat.format(cell.getDateCellValue());
                                            valueStr = formattedStr;
                                            break;
                                        } else { // 순수하게 숫자 데이터일 경우,
                                            Double numericCellValue = cell.getNumericCellValue();
                                            if (Math.floor(numericCellValue) == numericCellValue) { // 소수점 이하를 버린 값이 원래의 값과 같다면,,
                                                valueStr = numericCellValue.intValue() + ""; // int형으로 소수점 이하 버리고 String으로 데이터 담는다.
                                            } else {
                                                valueStr = numericCellValue + "";
                                            }
                                            break;
                                        }
                                    case Cell.CELL_TYPE_BOOLEAN:
                                        valueStr = cell.getBooleanCellValue() + "";
                                        break;
                                }
                            }
                        } finally {
                            columnValues[j] = StringUtils.trim(valueStr);
                        }
                    }

                    // 고객명 (필수)
                    if (StringUtils.isEmpty(columnValues[0])) {
                        dto.setResult("고객명이 없음");
                    } else {
                        if (columnValues[0].length() > 25) {
                            insFlag = false;
                            dto.setResult("고객명이 너무 김");
                        } else {
                            dto.setCusName(columnValues[0]);
                        }
                    }

                    // 가상계좌번호
                    if (columnValues[1].length() > 14) {
                        insFlag = false;
                        dto.setResult("가상계좌번호 값이 너무 큼");
                    } else if (0 < columnValues[1].length() && columnValues[1].length() < 14) {
                        insFlag = false;
                        dto.setResult("올바른 가상계좌번호가 아님");
                    } else {
                        dto.setVano(columnValues[1]);
                    }

                    // 고객번호
                    if (columnValues[2].length() > 30) {
                        insFlag = false;
                        dto.setResult("고객번호 값이 너무 큼");
                    } else {
                        dto.setCusKey(columnValues[2]);
                    }

                    // 납부대상 (Y=대상 / N=제외)
                    if (columnValues[3].equals("")) {
                        dto.setRcpGubn("Y");
                    } else if ((!columnValues[3].toUpperCase().equals("Y") && !columnValues[3].toUpperCase().equals("N"))) {
                        dto.setRcpGubn("Y");
                    } else if (columnValues[3].length() > 1) {
                        insFlag = false;
                        dto.setResult("납부대상여부 값이 올바르지 않음");
                    } else {
                        dto.setRcpGubn(columnValues[3].toUpperCase());
                    }

                    // 연락처 (휴대폰 & 유선전화)
                    String _phNo =  columnValues[4];
                    String phNoStr = ""; 
                    if(!StringUtils.isEmpty(_phNo)) {
                    	phNoStr = _phNo.replaceAll("-", "");
//                    	logger.debug("-----> phNoStr = {}", phNoStr);
//                    	if(!checkPattern("tel2", phNoStr) || !checkPattern("phone2", phNoStr)) {
                    	if(!checkPattern("tel2", phNoStr) ) {
                    		insFlag = false;
                            dto.setResult("연락처 값이 올바르지 않음(Number).");
                    	}
                    	if(phNoStr.length() > 12 || phNoStr.length()<9) {
                    		insFlag = false;
                            dto.setResult("연락처 값이 올바르지 않음(Length).");
                    	}
                    } 
//                    if (!columnValues[4].equals("")) {                    	
//                        if ((!checkPattern("tel", columnValues[4]) && !checkPattern("tel2", columnValues[4])) || (!checkPattern("phone", columnValues[4]) && !checkPattern("phone2", columnValues[4]))) {
//                            insFlag = false;
//                            dto.setResult("연락처 값이 올바르지 않음");
//                        }
//                        if (columnValues[4].replaceAll("-", "").length() > 12) {
//                            insFlag = false;
//                            dto.setResult("연락처 값이 올바르지 않음");
//                        }
//                    }
                    dto.setCusHp(phNoStr);

                    // 이메일
                    if (!columnValues[5].equals("")) {
                        if (!checkPattern("email", columnValues[5])) {
                            insFlag = false;
                            dto.setResult("이메일 값이 올바르지 않음");
                        }
                    }
                    dto.setCusMail(columnValues[5]);

                    String cusMsg = "30byte 초과";
                    // 고객구분
                    switch (cusGubnCnt) {
                        case 4: {
                            if (columnValues[9].getBytes("EUC-KR").length > 30) {
                                insFlag = false;
                                dto.setResult("고객구분4 " + cusMsg);
                            } else {
                                dto.setCusGubn4(StringUtils.defaultString(columnValues[9]));
                            }
                        }
                        case 3: {
                            if (columnValues[8].getBytes("EUC-KR").length > 30) {
                                insFlag = false;
                                dto.setResult("고객구분3 " + cusMsg);
                            } else {
                                dto.setCusGubn3(StringUtils.defaultString(columnValues[8]));
                            }
                        }
                        case 2: {
                            if (columnValues[7].getBytes("EUC-KR").length > 30) {
                                insFlag = false;
                                dto.setResult("고객구분2 " + cusMsg);
                            } else {
                                dto.setCusGubn2(StringUtils.defaultString(columnValues[7]));
                            }
                        }
                        case 1: {
                            if (columnValues[6].getBytes("EUC-KR").length > 30) {
                                insFlag = false;
                                dto.setResult("고객구분1 " + cusMsg);
                            } else {
                                dto.setCusGubn1(StringUtils.defaultString(columnValues[6]));
                            }
                        }
                        case 0:
                    }


                    // 현금영수증발급방법 (11=휴대폰번호 / 12=현금영수증카드번호 / 21=사업자번호)
                    if (columnValues[6 + cusGubnCnt].length() > 0) {
                        // 발급용도 입력

                        if (columnValues[7 + cusGubnCnt].length() > 1) {
                            // 발급방법 입력

                            if ("1".equals(columnValues[6 + cusGubnCnt])) {
                                // 발급방법 1=소득공제 일 때
                                if ("11".equals(columnValues[7 + cusGubnCnt])) {
                                    dto.setConfirm(columnValues[7 + cusGubnCnt]);
                                } else if ("12".equals(columnValues[7 + cusGubnCnt])) {
                                    dto.setConfirm(columnValues[7 + cusGubnCnt]);
                                } else {
                                    insFlag = false;
                                    dto.setResult("발급용도에 따른 발급방법 값이 올바르지 않음");
                                }
                            } else if ("2".equals(columnValues[6 + cusGubnCnt])) {
                                // 발급방법 2=지출증빙 일 때
                                if ("11".equals(columnValues[7 + cusGubnCnt])) {
                                    insFlag = false;
                                    dto.setResult("발급용도에 따른 발급방법 값이 올바르지 않음");
                                } else if ("12".equals(columnValues[7 + cusGubnCnt])) {
                                    insFlag = false;
                                    dto.setResult("발급용도에 따른 발급방법 값이 올바르지 않음");
                                } else {
                                    dto.setConfirm(columnValues[7 + cusGubnCnt]);
                                }
                            } else {
                                // 발급방법이 1, 2 둘다 아닐 때
                                insFlag = false;
                                dto.setResult("발급방법 값이 올바르지 않음");
                            }

                        } else {
                            // 발급방법 미입력
                            insFlag = false;
                            dto.setResult("발급방법 값이 없음");
                        }

                    } else {
                        // 발급용도 미 입력
                        if (columnValues[7 + cusGubnCnt].length() > 1) {
                            // 발급방법 입력
                            insFlag = false;
                            dto.setResult("발급용도 값이 없음");
                        } else {
                            // 발급방법 미입력
                            dto.setConfirm(columnValues[7 + cusGubnCnt]);
                        }
                    }

                    // 발급용도 (1=소득공제 / 2=지출증빙)
                    if (columnValues[6 + cusGubnCnt].length() > 0) {
                        if ("1".equals(columnValues[6 + cusGubnCnt])) {
                            dto.setCusType(columnValues[6 + cusGubnCnt]);
                        } else if ("2".equals(columnValues[6 + cusGubnCnt])) {
                            dto.setCusType(columnValues[6 + cusGubnCnt]);
                        } else {
                            insFlag = false;
                            dto.setResult("발급용도 값이 올바르지 않음");
                        }
                    } else {
                        dto.setCusType(columnValues[6 + cusGubnCnt]);
                    }

                    // 현금영수증발급 번호
                    if (columnValues[8 + cusGubnCnt].replaceAll("-", "").length() > 20) {
                        insFlag = false;
                        dto.setResult("현금영수증납부번호 값이 너무 큼");
                    }
                    dto.setCusOffNo(columnValues[8 + cusGubnCnt].replaceAll("-", "").replaceAll(" ", ""));

                    // 메모 (고객 특이사항)
                    dto.setMemo(columnValues[9 + cusGubnCnt]);

                    dto.setChaCd(chaCd);
                    dto.setxRow(String.valueOf((i + 1)));
                    dto.setCusGubnCnt(cusGubnCnt);

                    if (i > 0) {
                        if (i == 1) {
                            logger.debug(">>>>>>>>>> :: 고객 대량 등록 기존 실패 내역 삭제 시작");
                            custExcelService.custFailDelete(chaCd);
                            logger.debug(">>>>>>>>>> :: 고객 대량 등록 기존 실패 내역 삭제 완료");
                        }
                        if (insFlag) {
                            logger.debug(">>>>>>>>>> :: 고객 대량 등록 정상 내역 저장 시작");
                            resCnt += custExcelService.custExcelInsert(dto);
                            logger.debug(">>>>>>>>>> :: 고객 대량 등록 정상 내역 저장 완료");
                        } else {
                            logger.debug("---------- :: 고객 대량 등록 실패 내역 저장 시작");
                            custExcelService.custExcelFailInsert(dto);
                            logger.debug("---------- :: 고객 대량 등록 실패 내역 저장 완료");
                            resCnt++;
                        }
                    }
                }
                retMap.put("failCnt", resCnt);
                retMap.put("resCode", "0000");
            } else {
                retMap.put("resCode", "0001");
            }

            return retMap;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            inputDocument.close();
            logger.debug(">>>>>>>>>> :: 파일 삭제 완료");
            file.delete();
        }
    }

    public static boolean checkPattern(String pattern, String str) {
        boolean okPattern = false;
        String regex = null;

        pattern = pattern.trim();

        //숫자 체크
        if (StringUtils.equals("num", pattern)) {
            regex = "^[0-9]*$";
        }

        //영문 체크

        //이메일 체크
        if (StringUtils.equals("email", pattern)) {
            regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:[\\w-.]+\\.)+\\w+$";
        }

        //전화번호 체크1
        if (StringUtils.equals("tel", pattern)) {
            regex = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
        }
        
        //전화번호 체크2
        if (StringUtils.equals("tel2", pattern)) {
            regex = "^\\d{2,3}\\d{3,4}\\d{4}$";
        }

        //휴대폰번호 체크1
        if (StringUtils.equals("phone", pattern)) {
            regex = "^01([016-9])-\\d{3,4}-\\d{4}$";
        }

        //휴대폰번호 체크2
        if (StringUtils.equals("phone2", pattern)) {
            regex = "^01([016-9])\\d{3,4}\\d{4}$";
        }

        okPattern = Pattern.matches(regex, str);
        return okPattern;
    }


    /*
     * 고객관리 > 고객등록 > 대량등록 > 실패내역
     */
    @ResponseBody
    @RequestMapping("selectCustFail")
    public HashMap selectCustFail(@RequestBody CustReg01DTO dto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();
        logger.debug("기관 {} 고객등록 > 대량등록 > 실패내역", chaCd);
        int curPage = dto.getCurPage();
        int PAGE_SCALE = 5;

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            // total count
            int count = custExcelService.failTotalCount(chaCd);
            map.put("count", count);

            PageVO page = new PageVO(count, curPage, PAGE_SCALE);
            int start = page.getPageBegin();
            int end = page.getPageEnd();
            map.put("modalPager", page);
            map.put("modalNo", 2);
            List<CustReg01DTO> list = custExcelService.failList(chaCd, start, end);
            map.put("PAGE_SCALE", PAGE_SCALE);
            map.put("failList", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return map;
    }

    /*
     * 청구관리 > 청구등록 > 대량등록 > 실패내역 엑셀 다운로드
     */
    @ResponseBody
    @RequestMapping("excelFailDownload")
    public View excelFailDownload(HttpServletRequest request, HttpServletResponse response, CustReg01DTO dto, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chaCd", chaCd);

        try {
            logger.debug("기관 {} 실패내역 엑셀 다운로드", chaCd);
            List<CustReg01DTO> list = custExcelService.failExcelList(map);

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelFailDownload();
    }

    /*
     * 고객관리 > 고객조회 > 파일저장
     */
    @ResponseBody
    @RequestMapping("excelSaveCustList")
    public View excelSaveCustList(HttpServletRequest request, HttpServletResponse response, CustReg01DTO dto, Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            logger.debug("기관 {} 고객조회 > 파일저장", chaCd);
            map.put("chaCd", chaCd);
            map.put("startDate", dto.getStartDate());
            map.put("endDate", dto.getEndDate());
            map.put("searchGb", dto.getSearchGb());

            if (!"".equals(dto.getSearchValue())) {
                String value = dto.getSearchValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("searchValue", valueList);
            }

            map.put("payList", dto.getPayList());
            map.put("cusList", dto.getCusList());
            map.put("monList", dto.getMonList());
            map.put("cusGubn", dto.getCusGubn());

            if (dto.getCusGubnValue() != null && !"".equals(dto.getCusGubnValue())) {
                String value = dto.getCusGubnValue().trim();
                String[] valueList = value.split("\\s*,\\s*");
                map.put("cusGubnValue", valueList);
            }

            map.put("searchOrderBy", dto.getSearchOrderBy());

            // 구분항목
            List<CodeDTO> cusGbList = codeService.cusGubnCd(chaCd);
            model.addAttribute("cusGbList", cusGbList);
            model.addAttribute("cusGbCnt", cusGbList.size());

            List<CustReg01DTO> list = custExcelService.selectExcelSaveCustReg(map);
            model.addAttribute("map", dto.getCashShow());
            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return new ExcelSaveCustReg();
    }
}
