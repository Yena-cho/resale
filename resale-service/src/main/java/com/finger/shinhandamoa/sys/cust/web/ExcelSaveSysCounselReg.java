package com.finger.shinhandamoa.sys.cust.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.finger.shinhandamoa.sys.cust.dto.SysCustDTO;

public class ExcelSaveSysCounselReg extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
	     
        String excelName = chaCd + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        
        @SuppressWarnings("unchecked")
		List<SysCustDTO> listExcel = (List<SysCustDTO>)model.get("list"); // 목록
        
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        int idx = 8; // 컬럼수
        
        for(int i = 0; i < idx; i++) {
        	worksheet.setColumnWidth(i++, 7000); 
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 7000); 
    		worksheet.setColumnWidth(i++, 7000); 
    		worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 7000);
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("신청일자");
        	hRow.createCell(i++).setCellValue("신청구분");
    		hRow.createCell(i++).setCellValue("기관코드");
        	hRow.createCell(i++).setCellValue("신청자");
        	hRow.createCell(i++).setCellValue("신청자 연락처");
        	hRow.createCell(i++).setCellValue("예약사유");
        	hRow.createCell(i++).setCellValue("상태");
        	hRow.createCell(i++).setCellValue("상담완료일자");
        }
        
        int rowIndex = 1;
        
        for(SysCustDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(dto.getDay());
        	if(dto.getUserClass() == null) {
        		row.createCell(i++).setCellValue("납부자/신규");
        	}else {
        		row.createCell(i++).setCellValue("이용기관");
        	}
    		row.createCell(i++).setCellValue(dto.getId());
    		row.createCell(i++).setCellValue(dto.getWriter());
    		row.createCell(i++).setCellValue(dto.getData1());
    		row.createCell(i++).setCellValue(dto.getData3());
    		if("1".equals(dto.getData4())) {
    			row.createCell(i++).setCellValue("대기");
    		} else if("2".equals(dto.getData4())) {
    			row.createCell(i++).setCellValue("진행중");
    		} else {
    			row.createCell(i++).setCellValue("완료");
    		}
    		row.createCell(i++).setCellValue(dto.getDay1());
    		rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	}

}
