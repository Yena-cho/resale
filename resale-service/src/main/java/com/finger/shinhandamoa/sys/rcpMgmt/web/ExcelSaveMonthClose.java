package com.finger.shinhandamoa.sys.rcpMgmt.web;

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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAdminClosingDTO;

public class ExcelSaveMonthClose extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
	     
        String excelName = "월_"+ chaCd + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        
        @SuppressWarnings("unchecked")
		List<SysAdminClosingDTO> listExcel = (List<SysAdminClosingDTO>)model.get("list"); // 목록
        
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        int idx = 8; // 컬럼수
        
        // 컬럼 사이즈 설정
        for(int i = 0; i < idx; i++) {
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000); 
    		worksheet.setColumnWidth(i++, 7000); 
    		worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000); 
    		worksheet.setColumnWidth(i++, 7000); 
    		worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 7000);
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("No");
        	hRow.createCell(i++).setCellValue("마감월");
        	hRow.createCell(i++).setCellValue("기관명");
        	hRow.createCell(i++).setCellValue("청구금액");
    		hRow.createCell(i++).setCellValue("청구건수");
    		hRow.createCell(i++).setCellValue("청구수수료");
        	hRow.createCell(i++).setCellValue("수납금액");
        	hRow.createCell(i++).setCellValue("수납건수");
        	hRow.createCell(i++).setCellValue("수납수수료");
        	hRow.createCell(i++).setCellValue("은행수수료");
        	hRow.createCell(i++).setCellValue("SMS금액");
        	hRow.createCell(i++).setCellValue("SMS건수");
        	hRow.createCell(i++).setCellValue("LMS금액");
        	hRow.createCell(i++).setCellValue("LMS건수");
        	hRow.createCell(i++).setCellValue("마감일시");
        }
        
        int rowIndex = 1;
        
        for(SysAdminClosingDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(dto.getRn());
        	row.createCell(i++).setCellValue(dto.getCloseMonth());
    		row.createCell(i++).setCellValue(dto.getChaName());
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getNotiAmt()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getNotiCnt()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getNotiFee()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getRcpAmt()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getRcpCnt()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getRcpFee()));
        	row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getRcpBnkFee()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getSmsAmt()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getSmsCnt()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getLmsAmt()));
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getLmsCnt()));
    		row.createCell(i++).setCellValue(dto.getFinishDt());
    		rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	}

}
