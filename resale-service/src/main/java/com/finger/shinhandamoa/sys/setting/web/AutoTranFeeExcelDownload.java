package com.finger.shinhandamoa.sys.setting.web;

import com.finger.shinhandamoa.sys.setting.dto.AutoDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
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

public class AutoTranFeeExcelDownload extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = sCurTime + "_자동이체출금신청목록.xlsx";
        Sheet worksheet = null;
        Row row = null;

		CellStyle styleEmptyRow = workbook.createCellStyle();
		styleEmptyRow.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("@"));

        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        // 컬럼 사이즈 설정
        int columnIndex = 0;
        while (columnIndex < 2) {
             
            if(columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 8000);
            }
            
            columnIndex++;
        }
        
        Row hRow = null;
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < 2; i++) {
	        hRow.createCell(i++).setCellValue("기관코드");
			hRow.createCell(i++).setCellValue("출금요청금액");
        }
        
        int rowIndex = 1;

		for (int rowCnt = 1; rowCnt < 10; rowCnt++) {
			row = worksheet.createRow(rowCnt);
			row.setRowStyle(styleEmptyRow);
		}
         
		try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		  } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		      }
		}

}
