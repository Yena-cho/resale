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

import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XCashmasDTO;

public class ExcelSaveCashSysReq extends AbstractExcelView2 {

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
        List<XCashmasDTO> listExcel = (List<XCashmasDTO>)model.get("list");
        
        int idx = 11;
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
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000); 
        }
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("No");
        	hRow.createCell(i++).setCellValue("신청일자");
        	hRow.createCell(i++).setCellValue("기관코드");
        	hRow.createCell(i++).setCellValue("기관명");
        	hRow.createCell(i++).setCellValue("신청자명");
        	hRow.createCell(i++).setCellValue("납부방법");
        	hRow.createCell(i++).setCellValue("발급용도");
        	hRow.createCell(i++).setCellValue("발급방법");
        	hRow.createCell(i++).setCellValue("발급정보");
        	hRow.createCell(i++).setCellValue("상태");
        	hRow.createCell(i++).setCellValue("사유");
        }
        
        int rowIndex = 1;
        for(XCashmasDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(rowIndex);
        	row.createCell(i++).setCellValue(dto.getRegDt());
        	row.createCell(i++).setCellValue(dto.getChaCd());
        	row.createCell(i++).setCellValue(dto.getChaName());
        	row.createCell(i++).setCellValue(dto.getCusName());
        	row.createCell(i++).setCellValue(dto.getSveCdNm());
        	row.createCell(i++).setCellValue(dto.getCusTypeNm());
        	row.createCell(i++).setCellValue(dto.getConfirmNm());
        	row.createCell(i++).setCellValue(dto.getCusOffNo());
        	row.createCell(i++).setCellValue(dto.getCashMasNm());
        	row.createCell(i++).setCellValue(dto.getAppMsg());
        	rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
        
	}

}
