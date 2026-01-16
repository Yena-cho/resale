package com.finger.shinhandamoa.sys.setting.web;

import com.finger.shinhandamoa.sys.setting.dto.AutoDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ATAgreementExcelDownload extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = sCurTime + "_자동이체동의관리목록.xlsx";
        Sheet worksheet = null;
        Row row = null;
         
        List<AutoDTO> listExcel = (List<AutoDTO>)model.get("list");
         
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        // 컬럼 사이즈 설정
        int columnIndex = 0;
        while (columnIndex < 11) {
             
            if(columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 2000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 2) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 3) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 4) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 5) {
                worksheet.setColumnWidth(columnIndex, 7000);
            } else if (columnIndex == 6) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 7) {
                worksheet.setColumnWidth(columnIndex, 7000);
            } else if (columnIndex == 8) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 9) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 10) {
				worksheet.setColumnWidth(columnIndex, 7000);
			}
            
            columnIndex++;
        }
        
        Row hRow = null;
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < 11; i++) {
	        hRow.createCell(i++).setCellValue("NO");
	        hRow.createCell(i++).setCellValue("기관상태");
	        hRow.createCell(i++).setCellValue("고객분류");
	        hRow.createCell(i++).setCellValue("기관코드");
	        hRow.createCell(i++).setCellValue("기관명");
	        hRow.createCell(i++).setCellValue("사업자번호");
	        hRow.createCell(i++).setCellValue("은행코드");
	        hRow.createCell(i++).setCellValue("출금계좌");
	        hRow.createCell(i++).setCellValue("CMS 신청상태");
	        hRow.createCell(i++).setCellValue("동의상태");
	        hRow.createCell(i++).setCellValue("동의일자");
        }
        
        int rowIndex = 1;
        for(AutoDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
    		row.createCell(i++).setCellValue(dto.getRn());
			if(dto.getChaSt() != null && dto.getChaSt().equals("ST06")) {
				row.createCell(i++).setCellValue("정상");
			} else if(dto.getChaSt() != null && dto.getChaSt().equals("ST04")) {
				row.createCell(i++).setCellValue("정지");
			} else if(dto.getChaSt() != null && dto.getChaSt().equals("ST02")){
				row.createCell(i++).setCellValue("해지");
			} else{
				row.createCell(i++).setCellValue("");
			}
			if(dto.getChatrty() != null && dto.getChatrty().equals("03")) {
				row.createCell(i++).setCellValue("API");
			} else if(dto.getChatrty() != null && dto.getChatrty().equals("01")) {
				row.createCell(i++).setCellValue("WEB");
			} else{
				row.createCell(i++).setCellValue("");
			}
			row.createCell(i++).setCellValue(dto.getChaCd());
			row.createCell(i++).setCellValue(dto.getChaName());
			row.createCell(i++).setCellValue(dto.getChaOffNo());
			row.createCell(i++).setCellValue(dto.getBnkCd());
			row.createCell(i++).setCellValue(dto.getFeeAccNo());
			if(dto.getCmsReqSt().equals("CST02")) {
				row.createCell(i++).setCellValue("등록대기");
			} else if(dto.getCmsReqSt().equals("CST03")) {
				row.createCell(i++).setCellValue("등록중");
			} else if(dto.getCmsReqSt().equals("CST04")) {
				row.createCell(i++).setCellValue("등록");
			} else if(dto.getCmsReqSt().equals("CST01") && dto.getChkCms().equals("동의")) {
				row.createCell(i++).setCellValue("등록가능");
			}else {
				row.createCell(i++).setCellValue("미등록");
			}
			row.createCell(i++).setCellValue(dto.getChkCms());
			row.createCell(i++).setCellValue(dto.getCmsReqDt());

			rowIndex++;
		}
         
		try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		  } catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		      }
		}

}
