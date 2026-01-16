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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysSmsUsageDTO;

public class ExcelSaveSysSmsUsageReg extends AbstractExcelView2 {

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
		List<SysSmsUsageDTO> listExcel = (List<SysSmsUsageDTO>)model.get("list"); // 목록
        
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        int idx = 10; // 컬럼수
        
        // 컬럼 사이즈 설정
        for(int i = 0; i < idx; i++) {
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000); 
    		worksheet.setColumnWidth(i++, 4000); 
    		worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 7000);
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("No");
        	hRow.createCell(i++).setCellValue("발송일시");
        	hRow.createCell(i++).setCellValue("기관코드");
    		hRow.createCell(i++).setCellValue("기관명");
    		hRow.createCell(i++).setCellValue("담당자명");
        	hRow.createCell(i++).setCellValue("발송유형");
        	hRow.createCell(i++).setCellValue("발송건수");
        	hRow.createCell(i++).setCellValue("실패건수");
        	hRow.createCell(i++).setCellValue("이용금액(원)");
        }
        
        int rowIndex = 1;
        
        for(SysSmsUsageDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(dto.getRn());
        	row.createCell(i++).setCellValue(dto.getDay());
    		row.createCell(i++).setCellValue(dto.getComCd());
    		row.createCell(i++).setCellValue(dto.getComNm());
    		row.createCell(i++).setCellValue(dto.getName());
    		row.createCell(i++).setCellValue(dto.getSendSt());
    		row.createCell(i++).setCellValue(dto.getSendcount());
    		row.createCell(i++).setCellValue(dto.getFailcount());
    		row.createCell(i++).setCellValue(dto.getCharge());
    		rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	}

}
