package com.finger.shinhandamoa.sys.chaMgmt.web;

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

import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaDTO;

public class CloseChaExcelDownload extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = sCurTime + "_신규기관.xlsx";
        Sheet worksheet = null;
        Row row = null;
        Row hRow = null;
         
        List<SysChaDTO> list = (List<SysChaDTO>)model.get("list");
         
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        // 컬럼 사이즈 설정
        int columnIndex = 0;
        while (columnIndex < 11) {
             
            if(columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 2) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 3) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 4) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 5) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 6) {
                worksheet.setColumnWidth(columnIndex, 6000);
            } else if (columnIndex == 7) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 8) {
                worksheet.setColumnWidth(columnIndex, 15000);
            }
            
            columnIndex++;
        }
        
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < 9; i++) {
        	hRow.createCell(i++).setCellValue("검증일시");  		
        	hRow.createCell(i++).setCellValue("기관코드");
        	hRow.createCell(i++).setCellValue("기관명");
        	hRow.createCell(i++).setCellValue("업종");
    		hRow.createCell(i++).setCellValue("담당자명");
    		hRow.createCell(i++).setCellValue("담당자번호");
    		hRow.createCell(i++).setCellValue("기관상태");
    		hRow.createCell(i++).setCellValue("기관검증결과");
    		hRow.createCell(i++).setCellValue("부적합사유");
        }
    	
        int rowIndex = 1;
        for(SysChaDTO dto : list) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
			row.createCell(i++).setCellValue(dto.getChaCloseVarDt());       
			row.createCell(i++).setCellValue(dto.getChaCd());
			row.createCell(i++).setCellValue(dto.getChaName());
			row.createCell(i++).setCellValue(dto.getChaStatus());
			row.createCell(i++).setCellValue(dto.getChrName());
			row.createCell(i++).setCellValue(dto.getChrHp());
			row.createCell(i++).setCellValue(getChastNameDamoa(dto.getChast()));
			if("N".equals(dto.getChaCloseChk())) {
				row.createCell(i++).setCellValue("적합");
			} else {
				row.createCell(i++).setCellValue("부적합");
			}
			row.createCell(i++).setCellValue(dto.getChaCloseVarReson());
			
			rowIndex++;
		}
		 
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private String getChastNameDamoa(String chast) {
		String retVal = "";
		if("ST01".equals(chast)) {
			retVal = "이용대기[다모아]";
		} else if("ST06".equals(chast)) {
			retVal = "정상[다모아]";
		} else if("ST07".equals(chast)) {
			retVal= "거부[다모아]";
		} else if("ST08".equals(chast)) {
			retVal= "정지[다모아]";
		} else if("ST02".equals(chast)) {
			retVal= "해지[은행]";
		} else if("ST03".equals(chast)) {
			retVal= "승인거부[은행]";
		} else if("ST04".equals(chast)) {
			retVal= "정지[은행]";
		} else if("ST05".equals(chast)) {
			retVal= "승인대기[은행]";
		} 
		return retVal;
	}
}
