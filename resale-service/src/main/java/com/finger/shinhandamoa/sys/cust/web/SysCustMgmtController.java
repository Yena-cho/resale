package com.finger.shinhandamoa.sys.cust.web;

import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;
import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;
import com.finger.shinhandamoa.sys.cust.service.SysCustService;
import com.finger.shinhandamoa.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("sys/**")
public class SysCustMgmtController {

    private static final Logger logger = LoggerFactory.getLogger(SysCustMgmtController.class);

    @Inject
    private SysCustService sysCustService;


    @RequestMapping("sys/counselSetting")
    public ModelAndView counselSetting() throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("sys/cust/custMgmt/counselSetting");
        return mav;
    }

    @RequestMapping("sys/ajaxCustList")
    @ResponseBody
    public HashMap<String, Object> AjaxCustList(@RequestBody SysCustDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("searchOrderBy", body.getSearchOrderBy());
        reqMap.put("searchOption", body.getSearchOption());
        reqMap.put("keyword", body.getKeyword());
        reqMap.put("startday", body.getStartday());
        reqMap.put("endday", body.getEndday());
        reqMap.put("id", body.getId());
        reqMap.put("userClass", body.getUserClass());//신청구분
        reqMap.put("data4", body.getData4());//상태구분

        // 대기건수
        int waitValue = sysCustService.custWaitingCount(reqMap);
        logger.info("waitValue : " + waitValue);
        // total count
        int totValue = sysCustService.custTotalCount(reqMap);
        logger.info("totValue : " + totValue);

        PageVO page = new PageVO(totValue, body.getCurPage(), body.getPageScale());
        int start = page.getPageBegin();
        int end = page.getPageEnd();
        reqMap.put("start", start);
        reqMap.put("end", end);

        List<SysCustDTO> list = sysCustService.custListAll(reqMap);
        ModelAndView mav = new ModelAndView();
        map.put("list", list);
        map.put("count", totValue);
        map.put("wcount", waitValue);
        map.put("pager", page);    // 페이징 처리를 위한 변수
        map.put("curPage", body.getCurPage());
        map.put("keyword", body.getKeyword());
        map.put("PAGE_SCALE", body.getPageScale());
        map.put("pageScale", body.getPageScale());
        map.put("searchOrderBy", body.getSearchOrderBy());
        map.put("data4", body.getData4());// 상태구분
        map.put("userClass", body.getUserClass());//신청구분
        map.put("retCode", "0000");
        mav.addObject("map", map);

        return map;

    }

    @RequestMapping("sys/ajaxModalCustList")
    @ResponseBody
    public HashMap<String, Object> AjaxModalCustList(@RequestBody SysCustDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("no", body.getNo());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String today = sdf.format(new Date());

        SysCustDTO list = sysCustService.custListModal(reqMap);
        map.put("list", list);
        map.put("today", today);
        map.put("retCode", "0000");

        return map;

    }

    @RequestMapping("sys/updateCust")
    @ResponseBody
    public HashMap<String, Object> UpdateCust(@RequestBody SysCustDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("no", body.getNo());
        reqMap.put("data2", body.getData2());
        reqMap.put("data3", body.getData3());
        reqMap.put("data5", body.getData5());
        reqMap.put("day1", body.getDay1());
        reqMap.put("id", body.getId());
        reqMap.put("data4", body.getData4());
        reqMap.put("writer", body.getWriter());
        reqMap.put("data1", body.getData1());
        reqMap.put("title", body.getTitle());
        reqMap.put("contents", body.getContents());

        int list = sysCustService.updateCust(reqMap);
        map.put("list", list);
        map.put("retCode", "0000");

        return map;
    }

    @RequestMapping("sys/insertCust")
    @ResponseBody
    public HashMap<String, Object> insertCust(@RequestBody SysCustDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("data2", body.getData2());
        reqMap.put("data3", body.getData3());
        reqMap.put("data5", body.getData5());
        reqMap.put("chaCd", body.getChaCd());
        reqMap.put("data4", body.getData4());
        reqMap.put("writer", body.getWriter());
        reqMap.put("data1", body.getData1());
        reqMap.put("title", body.getTitle());
        reqMap.put("contents", body.getContents());
        reqMap.put("id", body.getId());

        sysCustService.insertCust(reqMap);
        map.put("retCode", "0000");

        return map;
    }

    @RequestMapping("sys/deleteCust")
    @ResponseBody
    public HashMap<String, Object> DeleteCust(@RequestBody SysCustDTO body, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> reqMap = new HashMap<String, Object>();

        reqMap.put("no", body.getNo());

        int list = sysCustService.deleteCust(reqMap);
        ModelAndView mav = new ModelAndView();
        map.put("list", list);
        map.put("retCode", "0000");
        mav.addObject("map", map);

        return map;
    }

    @RequestMapping("SysCustMgmt/excelDown")
    @ResponseBody
    public View payExcelDown(HttpServletRequest request, HttpServletResponse response, SysCustDTO body, Model model) throws Exception {

        try {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("searchOrderBy", body.getFsearchOrderBy());
            reqMap.put("searchOption", body.getFsearchOption());
            reqMap.put("keyword", body.getFkeyword());
            reqMap.put("startday", body.getfStartDate());
            reqMap.put("endday", body.getfEndDate());
            reqMap.put("id", body.getFid());
            reqMap.put("userClass", body.getFuserClass());//신청구분
            reqMap.put("data4", body.getFdata4());//상태구분

            int totValue = sysCustService.custTotalCount(reqMap);

            reqMap.put("start", 1);
            reqMap.put("end", totValue);

            List<SysCustDTO> list = sysCustService.custListAll(reqMap);
            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }

        return new ExcelSaveSysCounselReg();
    }


    /*
     * 고객상담관리 > 상담내역관리 > 엑셀등록(excel upload)
     */
    @Transactional
    @SuppressWarnings("resource")
    @ResponseBody
    @RequestMapping("sys/uploadExcelCounsel")
    public HashMap<String, Object> excelUpload(MultipartHttpServletRequest request, boolean flag) throws Exception {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        logger.debug("상담내역관리 > 엑셀등록");
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
            int failCnt = 0;
            int successCnt = 0;
            Sheet workSheet = workbook.getSheetAt(0);
            int rowSize = workSheet.getPhysicalNumberOfRows();

            Row rCnt = workSheet.getRow(0);
            int cellLen = (int) rCnt.getLastCellNum();
            for (int i = 1; i < rowSize; i++) {
                Row row = workSheet.getRow(i);
                    Cell cell = row.getCell(0);
                    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        rowSize = i;
                        break;
                    }
            }

            for (int i = 1; i < rowSize; i++) { // i를 1부터 시작해야 두번째 행부터 데이터가 입력된다.
                boolean insFlag = true; // 파일등록 flag

                Row headerRow = workSheet.getRow(0); // 컬럼 수 확인을 위하여 header 수를 읽어온다.
                Row row = workSheet.getRow(i);

                int cellLength = (int) headerRow.getLastCellNum(); // 열의 총 개수

                // 셀서식을 텍스트로 초기화 한 엑셀 중 input값이 없는 row의 insert를 막기위함
                int no = 0;
                for (int j = 0; j < cellLength; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        no++;
                    }
                }

                final String[] columnValues = new String[cellLength];
                String valueStr = ""; // 엑셀에서 뽑아낸 데이터를 담아놓을 String 변수 선언 및 초기화
                CustReg01DTO dto = new CustReg01DTO(); // DB에 Insert하기 위해 valueStr 데이터를 옮겨담을 객체
                for (int j = 0; j < cellLength; j++) {
                    Cell cell = row.getCell(j);
                    try {
                        // 셀에 있는 데이터들을 타입별로 분류해서 valueStr 변수에 담는다.
                        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { // CELL_TYPE_BLANK로만 체크할 경우 비어있는  셀을 놓칠 수 있다.
                            valueStr = "";
                        } else {
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_STRING:
                                    valueStr = cell.getStringCellValue().trim();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC: // 날짜 형식이든 숫자 형식이든 다 CELL_TYPE_NUMERIC으로 인식.

                                    if (DateUtil.isCellDateFormatted(cell)) { // 날짜 유형의 데이터일 경우,
                                        if(j == 1) {    //상담시간
                                            Time time = new Time(cell.getDateCellValue().getTime());
                                            valueStr = time.toString();
                                            break;
                                        }
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
                HashMap<String, Object> reqMap = new HashMap<String, Object>();

                //예약코드 & 사유
                reqMap.put("data2", "01");
                reqMap.put("data3", "사이트이용");

                //상담일자 + 시간
                String date = columnValues[0] + " " + columnValues[1];
                reqMap.put("day", date);

                // 신청자 연락처
                String phone = columnValues[2];
                reqMap.put("data1", columnValues[2]);

                //문의내용
                if (columnValues[3].getBytes("EUC-KR").length > 500) {
                    insFlag = false;
                    reqMap.put("msg", "상담내용 500byte 초과");
                } else {
                    reqMap.put("title", columnValues[3]);
                }

                //답변내용
                if (columnValues[4].getBytes("EUC-KR").length > 4000) {
                    insFlag = false;
                    reqMap.put("msg", "조치내용 4000byte 초과");
               } else {
                    reqMap.put("contents", columnValues[4]);
                }

                // 기관코드
                String chaCd = "";
                Pattern pat = Pattern.compile("\\d{8}");
                Matcher match = pat.matcher(columnValues[6]);
                if(match.find()){
                    chaCd = match.group();
                }
                reqMap.put("chaCd", chaCd);

                //신청자명
                String writer = columnValues[7];
                if (writer.getBytes("EUC-KR").length > 100) {
                    insFlag = false;
                    reqMap.put("msg", "신청자명 100byte 초과");
                } else {
                    if(StringUtils.isEmpty(writer)) {
                        writer = "-";
                    }
                    reqMap.put("writer", writer);
                }

                //상담직원
                if (columnValues[8].getBytes("EUC-KR").length > 100) {
                    insFlag = false;
                    reqMap.put("msg", "상담직원 100byte 초과");
                } else {
                    reqMap.put("data5", columnValues[8]);
                }

                // 상태
                reqMap.put("data4", "3");
                reqMap.put("id", "guest");

                if (insFlag) {
                    sysCustService.insertExcelCust(reqMap);
                    successCnt++;
                    logger.debug(">>>>>>>>>> :: 고객 상담 엑셀 등록 저장 완료");
                } else {
                    failCnt++;
                    logger.debug(">>>>>>>>>> :: 고객 상담 엑셀 등록 {}행 저장 실패", i);
                }
            }

            retMap.put("successCnt", successCnt);
            retMap.put("failCnt", failCnt);
            retMap.put("resCode", "0000");
            retMap.put("rMsg", "0000");

            return retMap;
        } catch (Exception e) {
            logger.info("파일양식오류");
            logger.error(e.getMessage());
            throw e;
        } finally {
            inputDocument.close();
            logger.debug(">>>>>>>>>> :: 파일 삭제 완료");
            file.delete();
        }
    }

}
