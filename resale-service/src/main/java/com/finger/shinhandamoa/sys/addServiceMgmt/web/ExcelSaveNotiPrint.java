package com.finger.shinhandamoa.sys.addServiceMgmt.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XNotimasreqDTO;


public class ExcelSaveNotiPrint extends AbstractExcelView2 {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        String excelName = chaCd + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row = null;
        List<XNotimasreqDTO> listExcel = (List<XNotimasreqDTO>) model.get("list");

        int idx = 6;
        worksheet = workbook.createSheet("sheet1");
        int columnIndex = 0;

        for (int i = 0; i < idx; i++) {
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
        }
        hRow = worksheet.createRow(0);
        hRow.setHeight((short) 500);

        for (int i = 0; i < idx; i++) {
            hRow.createCell(i++).setCellValue("No");
            hRow.createCell(i++).setCellValue("의뢰일시");
            hRow.createCell(i++).setCellValue("구분");
            hRow.createCell(i++).setCellValue("기관코드");
            hRow.createCell(i++).setCellValue("기관명");
            hRow.createCell(i++).setCellValue("수취인명");
            hRow.createCell(i++).setCellValue("수취인전화번호");
            hRow.createCell(i++).setCellValue("출력건수");
            hRow.createCell(i++).setCellValue("처리일시");
            hRow.createCell(i++).setCellValue("상태");
        }

        int rowIndex = 1;
        for (XNotimasreqDTO dto : listExcel) {
            int i = 0;

            row = worksheet.createRow(rowIndex);

            row.createCell(i++).setCellValue(rowIndex);
            row.createCell(i++).setCellValue(dto.getRegdt());
            row.createCell(i++).setCellValue(dto.getDlvrTypeCd());
            row.createCell(i++).setCellValue(dto.getChacd());
            row.createCell(i++).setCellValue(dto.getChaname());
            row.createCell(i++).setCellValue(dto.getReqname());
            row.createCell(i++).setCellValue(dto.getReqhp());
            row.createCell(i++).setCellValue(dto.getSendcnt());
            row.createCell(i++).setCellValue(dto.getSenddt());
            if ("취소".equals(dto.getReqst())) {
                row.createCell(i++).setCellValue(dto.getMaker() + " " + dto.getReqst());
            } else {
                row.createCell(i++).setCellValue(dto.getReqst());
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
