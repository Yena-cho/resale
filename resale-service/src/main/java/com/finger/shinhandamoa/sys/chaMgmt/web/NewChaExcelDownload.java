package com.finger.shinhandamoa.sys.chaMgmt.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.common.StrUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaDTO;

public class NewChaExcelDownload extends AbstractExcelView2 {

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
        while (columnIndex < 12) {
             
            if(columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 2) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 3) {
                worksheet.setColumnWidth(columnIndex, 7000);
            } else if (columnIndex == 4) {
                worksheet.setColumnWidth(columnIndex, 7000);
            } else if (columnIndex == 5) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 6) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 7) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 8) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 9) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 10) {
                worksheet.setColumnWidth(columnIndex, 4000);
            }

            columnIndex++;
        }
        
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < 12; i++) {
        	hRow.createCell(i++).setCellValue("등록일자");
        	hRow.createCell(i++).setCellValue("승인일자");
        	hRow.createCell(i++).setCellValue("기관코드");
        	hRow.createCell(i++).setCellValue("기관명");
    		hRow.createCell(i++).setCellValue("담당자번호");
    		hRow.createCell(i++).setCellValue("발급요청좌수");
    		hRow.createCell(i++).setCellValue("이용방식");
    		hRow.createCell(i++).setCellValue("기관검증");
    		hRow.createCell(i++).setCellValue("고객분류");
    		hRow.createCell(i++).setCellValue("출금동의서");
    		hRow.createCell(i++).setCellValue("해피콜");
        }
    	
        int rowIndex = 1;
        for(SysChaDTO dto : list) {

        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
			row.createCell(i++).setCellValue(dto.getBnkRegiDt());
			row.createCell(i++).setCellValue(dto.getBnkAcptDt());
			row.createCell(i++).setCellValue(dto.getChaCd());
			row.createCell(i++).setCellValue(dto.getChaName());
			row.createCell(i++).setCellValue(dto.getChrHp());
			row.createCell(i++).setCellValue(dto.getAccNoCnt());
			if("Y".equals(dto.getAmtchkTy())) {
				row.createCell(i++).setCellValue("승인");
			} else {
				row.createCell(i++).setCellValue("통지");
			}
			if("Y".equals(dto.getChaCloseChk())) {
				row.createCell(i++).setCellValue("부적합");
			} else {
				row.createCell(i++).setCellValue("적합");
			}
			if("01".equals(dto.getChatrty())) {
				row.createCell(i++).setCellValue("WEB");
			} else {
				row.createCell(i++).setCellValue("API");
			}
			if("".equals(dto.getCmsFileName()) || dto.getCmsFileName() == null) {
				row.createCell(i++).setCellValue("미완료");
			} else {
				row.createCell(i++).setCellValue("완료");
			}
			if("10".equals(dto.getPreChast()) || dto.getPreChast() == null) {
				row.createCell(i++).setCellValue("미완료");
			} else {
				row.createCell(i++).setCellValue("완료");
			}

			rowIndex++;
		}
		 
		try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		  } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		      }
		}

}
