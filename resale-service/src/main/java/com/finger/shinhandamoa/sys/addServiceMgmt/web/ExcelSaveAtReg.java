package com.finger.shinhandamoa.sys.addServiceMgmt.web;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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

public class ExcelSaveAtReg extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		String status = "";
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
	     
        String excelName = "알림톡신청관리" + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        List<AddServiceMgmtDTO> listExcel = (List<AddServiceMgmtDTO>)model.get("list");
        
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
			hRow.createCell(i++).setCellValue("발송기관명");
        	hRow.createCell(i++).setCellValue("업종");
        	hRow.createCell(i++).setCellValue("로그인아이디");
			hRow.createCell(i++).setCellValue("대표자명");
			hRow.createCell(i++).setCellValue("사업자번호");
        	hRow.createCell(i++).setCellValue("담당자명");
        	hRow.createCell(i++).setCellValue("담당자번호");
        	hRow.createCell(i++).setCellValue("상태");
			hRow.createCell(i++).setCellValue("승인(거절)일");
			hRow.createCell(i++).setCellValue("해지일");
        }
        
        int rowIndex = 1;
        for(AddServiceMgmtDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(rowIndex);
        	row.createCell(i++).setCellValue(dto.getAtRegDt());
        	row.createCell(i++).setCellValue(dto.getChaCd());
        	row.createCell(i++).setCellValue(dto.getChaName());
			row.createCell(i++).setCellValue(dto.getAtChaName());
        	row.createCell(i++).setCellValue(dto.getChaStatus());
        	row.createCell(i++).setCellValue(dto.getLoginId());
			row.createCell(i++).setCellValue(dto.getOwner());
			row.createCell(i++).setCellValue(dto.getChaOffNo());
        	row.createCell(i++).setCellValue(dto.getChrName());
        	row.createCell(i++).setCellValue(dto.getChrTelNo());
			if (dto.getAtYn().equals("Y")) {
				status = "승인완료";
			} else if (dto.getAtYn().equals("W")) {
				status = "승인대기";
			} else if (dto.getAtYn().equals("D")) {
				status = "해지";
			} else if (dto.getAtYn().equals("N")) {
				status = "거절";
			}
			row.createCell(i++).setCellValue(status);
			row.createCell(i++).setCellValue(dto.getAtAcptDt());
			row.createCell(i++).setCellValue(dto.getAtCnclDt());
        	rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
        
	}

}
