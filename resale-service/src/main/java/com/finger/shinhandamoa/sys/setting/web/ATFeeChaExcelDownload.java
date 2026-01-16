package com.finger.shinhandamoa.sys.setting.web;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.sys.setting.dto.AutoDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ATFeeChaExcelDownload extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = sCurTime + "_자동이체대상관리목록.xlsx";
        Sheet worksheet = null;
        Row row = null;
         
        List<AutoDTO> listExcel = (List<AutoDTO>)model.get("list");
         
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        // 컬럼 사이즈 설정
        int columnIndex = 0;
        while (columnIndex < 12) {
             
            if(columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 2000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 2) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 3) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 4) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 5) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 6) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 7) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 8) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 9) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 10) {
				worksheet.setColumnWidth(columnIndex, 3000);
			}else if (columnIndex == 11) {
				worksheet.setColumnWidth(columnIndex, 3000);
			}
            
            columnIndex++;
        }
        
        Row hRow = null;
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < 12; i++) {
	        hRow.createCell(i++).setCellValue("NO");
	        hRow.createCell(i++).setCellValue("동의일자");
	        hRow.createCell(i++).setCellValue("기관코드");
	        hRow.createCell(i++).setCellValue("기관명");
	        hRow.createCell(i++).setCellValue("사업자번호");
	        hRow.createCell(i++).setCellValue("납부자번호");
	        hRow.createCell(i++).setCellValue("예금주");
	        hRow.createCell(i++).setCellValue("사업자번호(생년월일)");
	        hRow.createCell(i++).setCellValue("출금은행");
	        hRow.createCell(i++).setCellValue("출금계좌");
	        hRow.createCell(i++).setCellValue("신청상태");
	        hRow.createCell(i++).setCellValue("동의방법");
        }
        
        int rowIndex = 1;
        for(AutoDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
    		row.createCell(i++).setCellValue(dto.getRn());
    		row.createCell(i++).setCellValue(dto.getCmsReqDt());
			row.createCell(i++).setCellValue(dto.getChaCd());
			row.createCell(i++).setCellValue(dto.getChaName());
			row.createCell(i++).setCellValue(dto.getChaOffNo());
			row.createCell(i++).setCellValue(dto.getFinFeeOwnNo());
			row.createCell(i++).setCellValue(dto.getFeeAccName());
			row.createCell(i++).setCellValue(dto.getFinFeeAccIdent());
			row.createCell(i++).setCellValue(dto.getBnkCd());
			row.createCell(i++).setCellValue(dto.getFeeAccNo());
			if(dto.getCmsReqSt().equals("CST02")) {
				row.createCell(i++).setCellValue("등록대기");
			} else if(dto.getCmsReqSt().equals("CST03")) {
				row.createCell(i++).setCellValue("등록중");
			} else if(dto.getCmsReqSt().equals("CST04")) {
				row.createCell(i++).setCellValue("등록");
			} else {
				row.createCell(i++).setCellValue("미등록");
			}
			if(dto.getConsentTy().equals("W00002")){
				row.createCell(i++).setCellValue("녹취");
			}else{
				row.createCell(i++).setCellValue("서면");
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
