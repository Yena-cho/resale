package com.finger.shinhandamoa.sys.bbs.web;

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

import com.finger.shinhandamoa.sys.bbs.dto.SysDTO;

public class ExcelSaveFaq extends AbstractExcelView2 {

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
        
        int idx = 6;
        worksheet = workbook.createSheet("sheet1");
        int columnIndex = 0;
        
        for(int i = 0; i < idx; i++) {
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
        	hRow.createCell(i++).setCellValue("등록일자");
        	hRow.createCell(i++).setCellValue("제목");
        	hRow.createCell(i++).setCellValue("읽기권한");
        	hRow.createCell(i++).setCellValue("카테고리");
        	hRow.createCell(i++).setCellValue("작성자");
        }
        
        int rowIndex = 1;
        for(SysDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(rowIndex);
        	row.createCell(i++).setCellValue(dto.getDay());
        	row.createCell(i++).setCellValue(dto.getTitle());
        	if(dto.getBbs()>50 && dto.getBbs()<=57) {
        		row.createCell(i++).setCellValue("로그인전");
        	}else if (dto.getBbs()>60 && dto.getBbs()<=67) {
        		row.createCell(i++).setCellValue("기관담당자");
			}else if (dto.getBbs()>70 && dto.getBbs()<=77) {
        		row.createCell(i++).setCellValue("기관담당자");
			}else {
				row.createCell(i++).setCellValue("");
			}
        	if(dto.getBbs()%10 == 1) {
        		row.createCell(i++).setCellValue("신청/해지");
        	}else if (dto.getBbs()%10 ==2) {
        		row.createCell(i++).setCellValue("납부");
			}else if (dto.getBbs()%10 ==3) {
        		row.createCell(i++).setCellValue("사이트이용");
			}else if (dto.getBbs()%10 ==4) {
        		row.createCell(i++).setCellValue("고지");
			}else if (dto.getBbs()%10 ==5) {
        		row.createCell(i++).setCellValue("수납");
			}else if (dto.getBbs()%10 ==6) {
        		row.createCell(i++).setCellValue("부가서비스");
			}else if (dto.getBbs()%10 ==7) {
        		row.createCell(i++).setCellValue("현금영수증");
			}else {
				row.createCell(i++).setCellValue("");
			}
        	row.createCell(i++).setCellValue(dto.getWriter());
        	rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
        
	}

}
