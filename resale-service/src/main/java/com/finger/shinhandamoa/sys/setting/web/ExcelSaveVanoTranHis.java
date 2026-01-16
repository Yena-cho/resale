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

import com.finger.shinhandamoa.sys.setting.dto.VanoMgmtDTO;

public class ExcelSaveVanoTranHis extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = "가상계좌거래내역" + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        
        List<VanoMgmtDTO> listExcel = (List<VanoMgmtDTO>)model.get("list");
        
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        int idx = 9; // 컬럼수
        
        // 컬럼 사이즈 설정
        int columnIndex = 0;
        
        while (columnIndex < 9) {
            
            if(columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 6000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 6000);
            } else if (columnIndex == 2) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 3) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 4) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 5) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 6) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 7) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 8) {
                worksheet.setColumnWidth(columnIndex, 6000);
            } else if (columnIndex == 9) {
                worksheet.setColumnWidth(columnIndex, 8000);
            }else if (columnIndex == 10) {
                worksheet.setColumnWidth(columnIndex, 7000);
            }else if (columnIndex == 11) {
                worksheet.setColumnWidth(columnIndex, 7000);
            }else if (columnIndex == 12) {
                worksheet.setColumnWidth(columnIndex, 7000);
            }else if (columnIndex == 13) {
                worksheet.setColumnWidth(columnIndex, 7000);
            }else if (columnIndex == 14) {
                worksheet.setColumnWidth(columnIndex, 7000);
            }else if (columnIndex == 15) {
                worksheet.setColumnWidth(columnIndex, 7000);
            }
            
            columnIndex++;
        }
        
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
    	for(int i = 0; i < idx; i++) {
    		
    		hRow.createCell(i++).setCellValue("입금일자");
    		hRow.createCell(i++).setCellValue("입금시간");
    		hRow.createCell(i++).setCellValue("입금은행");
    		hRow.createCell(i++).setCellValue("입금인");
    		hRow.createCell(i++).setCellValue("가상계좌번호");
    		hRow.createCell(i++).setCellValue("기관코드");
    		hRow.createCell(i++).setCellValue("기관명");
    		hRow.createCell(i++).setCellValue("납부자명");
    		hRow.createCell(i++).setCellValue("금액(원)");

    	}
    	
    	int rowIndex = 1;
        for(VanoMgmtDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
			row.createCell(i++).setCellValue(dto.getPayday());        
			row.createCell(i++).setCellValue(dto.getPaytime());        
			if(dto.getBnkcd() != null && !dto.getBnkcd().equals(" ")) {
				row.createCell(i++).setCellValue(dto.getBnkcd());    
			}else {
				row.createCell(i++).setCellValue(dto.getFicd());    
			}
			row.createCell(i++).setCellValue(dto.getRcpusrname());        
			row.createCell(i++).setCellValue(dto.getVano());        
			row.createCell(i++).setCellValue(dto.getChacd());        
			row.createCell(i++).setCellValue(dto.getChaName());        
			row.createCell(i++).setCellValue(dto.getCusname());        
			row.createCell(i++).setCellValue(dto.getRcpamt());        
			
			rowIndex++;
		}
         
		try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		  } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
		      }
		}

}
