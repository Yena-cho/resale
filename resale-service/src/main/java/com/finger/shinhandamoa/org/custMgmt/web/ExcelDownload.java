package com.finger.shinhandamoa.org.custMgmt.web;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExcelDownload extends AbstractExcelView2 {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        List<CustReg01DTO> listExcel = (List<CustReg01DTO>) model.get("list");
        for (int i = 0 ; i<listExcel.size();i++){
            listExcel.get(i).setCusName(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusName()));
            listExcel.get(i).setCusGubn1(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn1()));
            listExcel.get(i).setCusGubn2(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn2()));
            listExcel.get(i).setCusGubn3(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn3()));
            listExcel.get(i).setCusGubn4(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn4()));
        }
        List<CodeDTO> cusGbList = (List<CodeDTO>) model.get("cusGbList");
        for (int i = 0 ; i<cusGbList.size();i++){
            cusGbList.get(i).setCodeName(StringEscapeUtils.unescapeHtml4(cusGbList.get(i).getCodeName()));
        }
        int cusGbCnt = cusGbList.size();

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        String excelName = sCurTime + "_" + chaCd + "_대량납부고객등록.xlsx";
        Sheet worksheet = null;
        Row row = null;
        Cell cell;

        CellStyle style0 = workbook.createCellStyle();
        style0.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
        style0.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style0.setAlignment(HorizontalAlignment.CENTER);

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        worksheet = workbook.createSheet("sheet1");

        worksheet.setColumnWidth(0, 4000);
        worksheet.setColumnWidth(1, 8000);
        worksheet.setColumnWidth(2, 8000);
        worksheet.setColumnWidth(3, 8000);
        worksheet.setColumnWidth(4, 5000);
        worksheet.setColumnWidth(5, 5000);

        int listCnt = 0;
        for (CodeDTO code : cusGbList) {
            String col = code.getCode();
            String colName = code.getCodeName();
            switch (col) {
                case "CUSGUBN1":
                    worksheet.setColumnWidth(6 + listCnt, 5000);
                    listCnt++;
                    break;
                case "CUSGUBN2":
                    worksheet.setColumnWidth(6 + listCnt, 5000);
                    listCnt++;
                    break;
                case "CUSGUBN3":
                    worksheet.setColumnWidth(6 + listCnt, 5000);
                    listCnt++;
                    break;
                case "CUSGUBN4":
                    worksheet.setColumnWidth(6 + listCnt, 5000);
                    listCnt++;
                    break;
                default:
                    break;
            }
        }
        worksheet.setColumnWidth(6 + cusGbCnt, 5000);
        worksheet.setColumnWidth(7 + cusGbCnt, 8000);
        worksheet.setColumnWidth(8 + cusGbCnt, 8000);
        worksheet.setColumnWidth(9 + cusGbCnt, 10000);

        row = worksheet.createRow(0);
        row.setHeight((short) 500);
        cell = row.createCell(0);
        cell.setCellValue("고객명(필수)");
        cell.setCellStyle(style0);

        cell = row.createCell(1);
        cell.setCellValue("가상계좌번호");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("고객번호");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("납부대상\n(Y=대상 / N=제외)");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("연락처");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("이메일");
        cell.setCellStyle(style);

        listCnt = 0;
        for (CodeDTO code : cusGbList) {
            String col = code.getCode();
            String colName = code.getCodeName();
            switch (col) {
                case "CUSGUBN1":
                    cell = row.createCell(6 + listCnt);
                    cell.setCellValue(cusGbList.get(0 + listCnt).getCodeName());
                    cell.setCellStyle(style);
                    listCnt++;
                    break;
                case "CUSGUBN2":
                    cell = row.createCell(6 + listCnt);
                    cell.setCellValue(cusGbList.get(0 + listCnt).getCodeName());
                    cell.setCellStyle(style);
                    listCnt++;
                    break;
                case "CUSGUBN3":
                    cell = row.createCell(6 + listCnt);
                    cell.setCellValue(cusGbList.get(0 + listCnt).getCodeName());
                    cell.setCellStyle(style);
                    listCnt++;
                    break;
                case "CUSGUBN4":
                    cell = row.createCell(6 + listCnt);
                    cell.setCellValue(cusGbList.get(0 + listCnt).getCodeName());
                    cell.setCellStyle(style);
                    listCnt++;
                    break;
                default:
                    break;
            }
        }

        cell = row.createCell(6 + cusGbCnt);
        cell.setCellValue("발급용도\n(1=소득공제 / 2=지출증빙)");
        cell.setCellStyle(style);

        cell = row.createCell(7 + cusGbCnt);
        cell.setCellValue("현금영수증발급방법\n(11=휴대폰번호 / 12=현금영수증카드번호 / 21=사업자번호)");
        cell.setCellStyle(style);

        cell = row.createCell(8 + cusGbCnt);
        cell.setCellValue("현금영수증발급 번호");
        cell.setCellStyle(style);

        cell = row.createCell(9 + cusGbCnt);
        cell.setCellValue("메모 (고객 특이사항)");
        cell.setCellStyle(style);

        CellStyle styleEmptyRow = workbook.createCellStyle();
        styleEmptyRow.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("@"));

        for (int rowCnt = 1; rowCnt < 10; rowCnt++) {
            row = worksheet.createRow(rowCnt);
            row.setRowStyle(styleEmptyRow);
        }

        try {
            response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

}
