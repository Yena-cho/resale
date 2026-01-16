package com.finger.shinhandamoa.sys.bbs.web;

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

import com.finger.shinhandamoa.sys.bbs.dto.SysDTO;

public class ExcelSaveQna extends AbstractExcelView2 {

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
        Row row  = null;
        List<SysDTO> listExcel = (List<SysDTO>)model.get("list");
        
        int idx = 8;
        worksheet = workbook.createSheet("sheet1");
        int columnIndex = 0;
        
        for(int i = 0; i < idx; i++) {
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
        hRow.setHeight((short)500);
        
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("No");
        	hRow.createCell(i++).setCellValue("기관코드");
        	hRow.createCell(i++).setCellValue("카테고리");
        	hRow.createCell(i++).setCellValue("제목");
        	hRow.createCell(i++).setCellValue("작성자");
        	hRow.createCell(i++).setCellValue("담당자번호");
        	hRow.createCell(i++).setCellValue("등록일");
        	hRow.createCell(i++).setCellValue("답변상태");
        }
        
        int rowIndex = 1;
        for(SysDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);

        	row.createCell(i++).setCellValue(rowIndex);
        	row.createCell(i++).setCellValue(dto.getCode());
        	row.createCell(i++).setCellValue(dto.getData7());
        	row.createCell(i++).setCellValue(dto.getTitle());
        	row.createCell(i++).setCellValue(dto.getWriter());
        	row.createCell(i++).setCellValue(dto.getData1());
        	row.createCell(i++).setCellValue(dto.getDay());
        	if(dto.getDatcnt() == 0) {
        		row.createCell(i++).setCellValue("답변대기");
        	}else {
        		row.createCell(i++).setCellValue("답변완료");
			}
        	rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
        
	}

}
