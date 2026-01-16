package com.finger.shinhandamoa.sys.chaMgmt.web;

import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChangeChaListExcel extends AbstractExcelView2 {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        String excelName = sCurTime + "_변경대기목록.xlsx";
        Sheet worksheet = null;
        Row row = null;
        Row hRow = null;

        List<SysChaDTO> list = (List<SysChaDTO>) model.get("list");

        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");

        // 컬럼 사이즈 설정
        int columnIndex = 0;
        while (columnIndex < 6) {
            if (columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 2) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 3) {
                worksheet.setColumnWidth(columnIndex, 3000);
            } else if (columnIndex == 4) {
                worksheet.setColumnWidth(columnIndex, 3000);
            } else if (columnIndex == 5) {
                worksheet.setColumnWidth(columnIndex, 5000);
            }
            columnIndex++;
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short) 500);
        for (int i = 0; i < 6; i++) {
            hRow.createCell(i++).setCellValue("수신일자");
            hRow.createCell(i++).setCellValue("기관코드");
            hRow.createCell(i++).setCellValue("기관명");
            hRow.createCell(i++).setCellValue("분류");
            hRow.createCell(i++).setCellValue("상태");
            hRow.createCell(i++).setCellValue("처리일자");
        }

        int rowIndex = 1;
        for (SysChaDTO dto : list) {
            int i = 0;
            row = worksheet.createRow(rowIndex);

            row.createCell(i++).setCellValue(dto.getPullDt());
            row.createCell(i++).setCellValue(dto.getChaCd());
            row.createCell(i++).setCellValue(dto.getChaName());
            row.createCell(i++).setCellValue(dto.getTypeName());
            row.createCell(i++).setCellValue(dto.getStatusName());

            if ("C10004".equals(dto.getStatusCd()) || "C10005".equals(dto.getStatusCd())) {
                row.createCell(i++).setCellValue(dto.getModifyDt());
            } else {
                row.createCell(i++).setCellValue("");
            }

            rowIndex++;
        }

        try {
            response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
