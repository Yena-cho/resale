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

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysRcpStatDTO;

public class ExcelSaveSysRcpStatReg extends AbstractExcelView2 {

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
		List<SysRcpStatDTO> listExcel = (List<SysRcpStatDTO>)model.get("list"); // 목록
        @SuppressWarnings("unchecked")
		List<SysRcpStatDTO> tlistExcel = (List<SysRcpStatDTO>)model.get("tlist"); // 합계목록
        
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        int idx = 5; // 컬럼수
        
        // 컬럼 사이즈 설정
        for(int i = 0; i < idx; i++) {
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000); 
    		worksheet.setColumnWidth(i++, 4000); 
    		worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 4000);
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("청구월");
        	hRow.createCell(i++).setCellValue("납부방법");
        	hRow.createCell(i++).setCellValue("이용기관수");
    		hRow.createCell(i++).setCellValue("수납금액");
        	hRow.createCell(i++).setCellValue("입금건수");
        }
        
        int rowIndex = 1;
        
        for(SysRcpStatDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
        	if(i == 0 || i%2 == 0){
        	row.createCell(i++).setCellValue(dto.getMasMonth());
        	}else {
        	row.createCell(i++).setCellValue("");
        	}
        	row.createCell(i++).setCellValue(dto.getPayMethod());
    		row.createCell(i++).setCellValue(dto.getCustCount());
    		row.createCell(i++).setCellValue(dto.getRcpAmt());
    		row.createCell(i++).setCellValue(dto.getRcpCount());

    		rowIndex++;
        }
        
        for(SysRcpStatDTO dto : tlistExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
        	if(i == 0 || i%3 == 0 ){
        	row.createCell(i++).setCellValue("계");
        	}else {
            row.createCell(i++).setCellValue("");
        	}
        	row.createCell(i++).setCellValue(dto.getPayMethod());
    		row.createCell(i++).setCellValue(dto.getCcSum());
    		row.createCell(i++).setCellValue(dto.getRaSum());
    		row.createCell(i++).setCellValue(dto.getRcSum());
    		rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	}

}
