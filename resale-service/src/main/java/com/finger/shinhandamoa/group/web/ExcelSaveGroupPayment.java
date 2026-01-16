package com.finger.shinhandamoa.group.web;

import com.finger.shinhandamoa.group.dto.GroupDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
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

/**
 * 그룹관리자 입금내역조회 엑셀 파일 저장
 */
public class ExcelSaveGroupPayment extends AbstractExcelView2 {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String groupId = authentication.getName();
        String status = "";

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        String excelName = "입금내역" + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row = null;
        List<GroupDTO> listExcel = (List<GroupDTO>) model.get("list");

        int idx = 8;
        int columnIndex = 0;
        worksheet = workbook.createSheet("sheet1");

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
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
            worksheet.setColumnWidth(i++, 4000);
        }
        hRow = worksheet.createRow(0);
        hRow.setHeight((short) 500);

        for (int i = 0; i < idx; i++) {
            hRow.createCell(i++).setCellValue("NO");
            hRow.createCell(i++).setCellValue("입금일자");
            hRow.createCell(i++).setCellValue("청구월");
            hRow.createCell(i++).setCellValue("기관코드");
            hRow.createCell(i++).setCellValue("기관명");
            hRow.createCell(i++).setCellValue("고객명");
            hRow.createCell(i++).setCellValue("고객번호");
            hRow.createCell(i++).setCellValue("고객구분1");
            hRow.createCell(i++).setCellValue("고객구분2");
            hRow.createCell(i++).setCellValue("고객구분3");
            hRow.createCell(i++).setCellValue("고객구분4");
            hRow.createCell(i++).setCellValue("가상계좌번호");
            hRow.createCell(i++).setCellValue("입금구분");
            hRow.createCell(i++).setCellValue("입금자명");
            hRow.createCell(i++).setCellValue("입금액(원)");
        }

        int rowIndex = 1;
        for (GroupDTO dto : listExcel) {
            int i = 0;
            row = worksheet.createRow(rowIndex);
            row.createCell(i++).setCellValue(rowIndex);
            row.createCell(i++).setCellValue(dto.getPayDay());
            row.createCell(i++).setCellValue(dto.getMasMonth());
            row.createCell(i++).setCellValue(dto.getChaCd());
            row.createCell(i++).setCellValue(dto.getChaName());
            row.createCell(i++).setCellValue(dto.getCusname());
            row.createCell(i++).setCellValue(dto.getCuskey());
            row.createCell(i++).setCellValue(dto.getCusgubn1());
            row.createCell(i++).setCellValue(dto.getCusgubn2());
            row.createCell(i++).setCellValue(dto.getCusgubn3());
            row.createCell(i++).setCellValue(dto.getCusgubn4());
            row.createCell(i++).setCellValue(dto.getVano());
            row.createCell(i++).setCellValue(dto.getSveCd());
            row.createCell(i++).setCellValue(dto.getRcpusrname());
            row.createCell(i++, CellType.NUMERIC).setCellValue( Long.parseLong(dto.getRcpamt()) );
            row.createCell(i++).setCellValue(status);
            rowIndex++;
        }

        try {
            response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
}
