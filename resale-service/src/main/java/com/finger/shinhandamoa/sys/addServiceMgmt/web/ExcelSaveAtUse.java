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

public class ExcelSaveAtUse extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		String status = "";
		String msgType = "";
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
	     
        String excelName = "알림톡이용내역" + "_" + sCurTime + ".xlsx";
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
		}
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("No");
        	hRow.createCell(i++).setCellValue("발송일시");
        	hRow.createCell(i++).setCellValue("기관코드");
        	hRow.createCell(i++).setCellValue("로그인아이디");
			hRow.createCell(i++).setCellValue("기관명");
        	hRow.createCell(i++).setCellValue("유형");
			hRow.createCell(i++).setCellValue("고객명");
			hRow.createCell(i++).setCellValue("수신번호");
        	hRow.createCell(i++).setCellValue("발송결과");
        	hRow.createCell(i++).setCellValue("실패사유");
        }
        
        int rowIndex = 1;
        for(AddServiceMgmtDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
        	row.createCell(i++).setCellValue(rowIndex);
        	row.createCell(i++).setCellValue(dto.getSendDate());
        	row.createCell(i++).setCellValue(dto.getChaCd());
        	row.createCell(i++).setCellValue(dto.getLoginId());
			row.createCell(i++).setCellValue(dto.getChaName());
        	row.createCell(i++).setCellValue(dto.getMsgType());
			row.createCell(i++).setCellValue(dto.getCusName());
			row.createCell(i++).setCellValue(dto.getCusTelNo());
			row.createCell(i++).setCellValue(dto.getSendStatusCd());
			if ("발송성공".equals(dto.getSendStatusCd())) {
				row.createCell(i++).setCellValue("");
			} else {
				row.createCell(i++).setCellValue(dto.getDescription());
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
