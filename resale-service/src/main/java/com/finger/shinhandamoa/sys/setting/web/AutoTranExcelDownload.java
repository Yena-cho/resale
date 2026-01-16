package com.finger.shinhandamoa.sys.setting.web;

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
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.finger.shinhandamoa.sys.setting.dto.AutoDTO;

public class AutoTranExcelDownload extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = sCurTime + "_자동이체신청목록.xlsx";
        Sheet worksheet = null;
        Row row = null;
         
        List<AutoDTO> listExcel = (List<AutoDTO>)model.get("list");
         
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        // 컬럼 사이즈 설정
        int columnIndex = 0;
        while (columnIndex < 10) {
             
            if(columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 2000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 2) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 3) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 4) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 5) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 6) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 7) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 8) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 9) {
                worksheet.setColumnWidth(columnIndex, 10000);
            }
            
            columnIndex++;
        }
        
        Row hRow = null;
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < 9; i++) {
	        hRow.createCell(i++).setCellValue("NO");
	        hRow.createCell(i++).setCellValue("등록일자");
	        hRow.createCell(i++).setCellValue("신청구분");
	        hRow.createCell(i++).setCellValue("기관코드");
	        hRow.createCell(i++).setCellValue("기관명");
	        hRow.createCell(i++).setCellValue("담당자명");
	        hRow.createCell(i++).setCellValue("수수료출금계좌");
	        hRow.createCell(i++).setCellValue("CMS신청상태");
	        hRow.createCell(i++).setCellValue("승인완료일자"); 
	        hRow.createCell(i++).setCellValue("파일명"); 
        }
        
        int rowIndex = 1;
        for(AutoDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
    		row.createCell(i++).setCellValue(dto.getRn());				// no
			row.createCell(i++).setCellValue(dto.getRegDt());  	 		// 등록일자
			if(dto.getCmsAppGubn() != null && dto.getCmsAppGubn().equals("CM01")) {					// 신청구분
				row.createCell(i++).setCellValue("신규");
			} else if(dto.getCmsAppGubn() != null && dto.getCmsAppGubn().equals("CM02")) {
				row.createCell(i++).setCellValue("변경");
			} else if(dto.getCmsAppGubn() != null && dto.getCmsAppGubn().equals("CM03")) {
				row.createCell(i++).setCellValue("해지");
			} else {
				row.createCell(i++).setCellValue("");
			}
			row.createCell(i++).setCellValue(dto.getChaCd());  	 		// 기관코드
			row.createCell(i++).setCellValue(dto.getChaName());  	 	// 기관명
			row.createCell(i++).setCellValue(dto.getChrName());  	 	// 담당자명
			row.createCell(i++).setCellValue(dto.getFeeAccNo());  	 	// 수수료출금계좌
			if(dto.getCmsReqSt() == "CST02") { 							// CMS 신청
				row.createCell(i++).setCellValue("등록대기");
			} else if(dto.getCmsReqSt() == "CST03") {
				row.createCell(i++).setCellValue("등록중");
			} else if(dto.getCmsReqSt() == "CST04") {
				row.createCell(i++).setCellValue("등록");
			} else {
				row.createCell(i++).setCellValue("미등록");
			}
			row.createCell(i++).setCellValue(dto.getCmsReqDt());  	 	// 신청완료일자
			row.createCell(i++).setCellValue(dto.getCmsFileName());  	// 파일명
			
			rowIndex++;
		}
         
		try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		  } catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		      }
		}

}
