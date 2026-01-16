package com.finger.shinhandamoa.org.notimgmt.web;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.org.notimgmt.dto.NotiSendDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExcelSaveNoti extends AbstractExcelView2 {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        String excelName = chaCd + "_" + sCurTime + "_문자메시지고지_발송내역.xlsx";
        Sheet worksheet = null;
        Row row = null;
        Cell cell;

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        List<NotiSendDTO> listExcel = (List<NotiSendDTO>) model.get("list");
        for (int i = 0; i < listExcel.size(); i++) {
            listExcel.get(i).setReqDate(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getReqDate()));
            listExcel.get(i).setType(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getType()));
            listExcel.get(i).setCusName(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusName()));
            listExcel.get(i).setPhone(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getPhone()));
            listExcel.get(i).setMsg(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getMsg()));
            listExcel.get(i).setStatus(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getStatus()));
        }

        worksheet = workbook.createSheet("sheet1");

        worksheet.setColumnWidth(0, 6000);
        worksheet.setColumnWidth(1, 3000);
        worksheet.setColumnWidth(2, 8000);
        worksheet.setColumnWidth(3, 5000);
        worksheet.setColumnWidth(4, 12000);
        worksheet.setColumnWidth(5, 5000);

        row = worksheet.createRow(0);
        row.setHeight((short) 500);
        cell = row.createCell(0);
        cell.setCellValue("발송일시");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("유형");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("고객명");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("수신번호");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("내용");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("발송결과");
        cell.setCellStyle(style);

        int rowIndex = 1;
        for (NotiSendDTO dto : listExcel) {
            row = worksheet.createRow(rowIndex);

            row.createCell(0).setCellValue(dto.getReqDate());
            row.createCell(1).setCellValue(dto.getType());
            row.createCell(2).setCellValue(dto.getCusName());
            row.createCell(3).setCellValue(dto.getPhone());
            row.createCell(4).setCellValue(dto.getMsg());
            row.createCell(5).setCellValue(dto.getStatus());

            rowIndex++;
        }

        try {
            response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
}
