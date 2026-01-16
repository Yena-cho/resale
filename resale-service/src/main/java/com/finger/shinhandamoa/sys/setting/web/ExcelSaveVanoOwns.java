package com.finger.shinhandamoa.sys.setting.web;

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
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.finger.shinhandamoa.sys.setting.dto.VanoMgmtDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;

public class ExcelSaveVanoOwns extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = "가상계좌보유현황" + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        
        List<VanoMgmtDTO> listExcel = (List<VanoMgmtDTO>)model.get("list");
        
        List<CodeDTO> cdList = (List<CodeDTO>)model.get("cusGbList");	// 구분코드
        String flag = (String) model.get("view");						// 화면구분 - 청구조회
        
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        int idx = 9; // 컬럼수
        
        // 컬럼 사이즈 설정
        int columnIndex = 0;
        
        while (columnIndex < 9) {
            
            if(columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 4000);
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
    		
    		hRow.createCell(i++).setCellValue("기관코드  "); 
    		hRow.createCell(i++).setCellValue("기관명    "); 
    		hRow.createCell(i++).setCellValue("담당자명  "); 
    		hRow.createCell(i++).setCellValue("담당자번호"); 
    		hRow.createCell(i++).setCellValue("기관상태  "); 
    		hRow.createCell(i++).setCellValue("기관분류  ");
    		hRow.createCell(i++).setCellValue("총보유건수"); 
    		hRow.createCell(i++).setCellValue("사용건수  "); 
    		hRow.createCell(i++).setCellValue("잔여건수  "); 
    		hRow.createCell(i++).setCellValue("해지건수  "); 
    	}
    	
    	int rowIndex = 1;
        for(VanoMgmtDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
			row.createCell(i++).setCellValue(dto.getChacd());             // 기관코드      
			row.createCell(i++).setCellValue(dto.getChaName());           // 기관명        
			row.createCell(i++).setCellValue(dto.getChrname());           // 담당자명      
			row.createCell(i++).setCellValue(dto.getChrhp());             // 담당자번호 
			String _chast  = dto.getChast();
			String chastName = "";
			if("ST06".equals(_chast))
			{
				chastName = "정상";
			}
			else if("ST08".equals(_chast))
			{
				chastName = "정지";
			}
			else if("ST02".equals(_chast))
			{
				chastName = "해지";				
			}
			else {
				chastName = _chast;								
			}
			row.createCell(i++).setCellValue(chastName);             // 기관상태      
			
			//기관분류
			String _chatrty  = dto.getChatrty();
			String chatrtyName  = "";
			if("01".equals(_chatrty)) {
				chatrtyName = "WEB";
			} else {
				chatrtyName = "API";
			}
			row.createCell(i++).setCellValue(chatrtyName);	
			
			row.createCell(i++).setCellValue(dto.getTotcnt());            // 총보유건수    
			row.createCell(i++).setCellValue(dto.getUsedcnt());           // 사용건수      
			row.createCell(i++).setCellValue(dto.getNotusedcnt());        // 잔여건수      
			row.createCell(i++).setCellValue(dto.getDelcnt());            // 해지건수      
			
			rowIndex++;
		}
		 
		try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		  } catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		      }
		}

}
