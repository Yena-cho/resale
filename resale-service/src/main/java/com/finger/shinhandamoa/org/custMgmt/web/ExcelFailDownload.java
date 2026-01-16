package com.finger.shinhandamoa.org.custMgmt.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.finger.shinhandamoa.org.custMgmt.dto.CustReg01DTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;

public class ExcelFailDownload extends AbstractExcelView2 {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        List<CustReg01DTO> listExcel = (List<CustReg01DTO>) model.get("list");
        List<CodeDTO> cusGbList = (List<CodeDTO>) model.get("cusGbList");
        int cusGbCnt = cusGbList.size();

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        String excelName = sCurTime + "_" + chaCd + "_고객등록실패목록.xlsx";
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
        cell.setCellValue("연락처 (휴대폰)");
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

        int rowIndex = 1;
        for (CustReg01DTO dto : listExcel) {
            row = worksheet.createRow(rowIndex);

            row.createCell(0).setCellValue(dto.getCusName());
            row.createCell(1).setCellValue(dto.getVano());
            row.createCell(2).setCellValue(dto.getCusKey());
            if ("Y".equals(dto.getRcpGubn())) {
                row.createCell(3).setCellValue("대상");
            } else {
                row.createCell(3).setCellValue("제외");
            }
            row.createCell(4).setCellValue(dto.getCusHp());
            row.createCell(5).setCellValue(dto.getCusMail());

            listCnt = 0;
            for (CodeDTO code : cusGbList) {
                String col = code.getCode();
                String colName = code.getCodeName();
                switch (col) {
                    case "CUSGUBN1":
                        row.createCell(6 + listCnt).setCellValue(dto.getCusGubn1());
                        listCnt++;
                        break;
                    case "CUSGUBN2":
                        row.createCell(6 + listCnt).setCellValue(dto.getCusGubn2());
                        listCnt++;
                        break;
                    case "CUSGUBN3":
                        row.createCell(6 + listCnt).setCellValue(dto.getCusGubn3());
                        listCnt++;
                        break;
                    case "CUSGUBN4":
                        row.createCell(6 + listCnt).setCellValue(dto.getCusGubn4());
                        listCnt++;
                        break;
                    default:
                        break;
                }
            }
            if ("1".equals(dto.getConfirm())) {
                row.createCell(6 + cusGbCnt).setCellValue("소득공제");
            } else if ("2".equals(dto.getConfirm())) {
                row.createCell(6 + cusGbCnt).setCellValue("지출증빙");
            } else {
                row.createCell(6 + cusGbCnt).setCellValue("");
            }
            if ("11".equals(dto.getConfirm())) {
                row.createCell(7 + cusGbCnt).setCellValue("휴대폰번호");
            } else if ("12".equals(dto.getConfirm())) {
                row.createCell(7 + cusGbCnt).setCellValue("현금영수증카드번호");
            } else if ("21".equals(dto.getConfirm())) {
                row.createCell(7 + cusGbCnt).setCellValue("사업자번호");
            } else {
                row.createCell(7 + cusGbCnt).setCellValue("");
            }
            row.createCell(8 + cusGbCnt).setCellValue(dto.getCusOffNo());
            row.createCell(9 + cusGbCnt).setCellValue(dto.getMemo());

            rowIndex++;
        }

        try {
            response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

}
