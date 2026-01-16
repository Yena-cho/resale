package com.finger.shinhandamoa.org.mypage.web;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.org.mypage.dto.MyPageSumDTO;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
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

public class ExcelSaveMonthRcpSumReq extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

		final String excelName = "수납현황_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row hRow2 = null;
        Row row  = null;
        
        List<MyPageSumDTO> listExcel = (List<MyPageSumDTO>)model.get("list");
        MyPageSumDTO vo = (MyPageSumDTO)model.get("vo");
        //int idx = 19;
        int idx = 13;
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
	    	worksheet.setColumnWidth(i++, 4000); 
	    	worksheet.setColumnWidth(i++, 4000); 
	    	worksheet.setColumnWidth(i++, 4000); 
//	    	worksheet.setColumnWidth(i++, 4000); 
//	    	worksheet.setColumnWidth(i++, 4000);
	    }
	    hRow = worksheet.createRow(0);
	    hRow.setHeight((short)500);
		hRow.createCell(0).setCellValue("청구월");
		hRow.createCell(1).setCellValue("가상계좌");
		hRow.createCell(2).setCellValue("");
		hRow.createCell(3).setCellValue("온라인카드");
		hRow.createCell(4).setCellValue("");
		hRow.createCell(5).setCellValue("현금");
		hRow.createCell(6).setCellValue("");
		hRow.createCell(7).setCellValue("오프라인카드");
		hRow.createCell(8).setCellValue("");
		hRow.createCell(9).setCellValue("현금영수증신청");
		hRow.createCell(10).setCellValue("");
		hRow.createCell(11).setCellValue("합계");
		hRow.createCell(12).setCellValue("");

	    worksheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 10));
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 12));

        hRow = worksheet.createRow(1);
		hRow.createCell(0).setCellValue("");
		hRow.createCell(1).setCellValue("건수(건)");
		hRow.createCell(2).setCellValue("금액(원)");
		hRow.createCell(3).setCellValue("건수(건)");
		hRow.createCell(4).setCellValue("금액(원)");
		hRow.createCell(5).setCellValue("건수(건)");
		hRow.createCell(6).setCellValue("금액(원)");
		hRow.createCell(7).setCellValue("건수(건)");
		hRow.createCell(8).setCellValue("금액(원)");
		hRow.createCell(9).setCellValue("건수(건)");
		hRow.createCell(10).setCellValue("금액(원)");
		hRow.createCell(11).setCellValue("건수(건)");
		hRow.createCell(12).setCellValue("금액(원)");
		
        worksheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); 
        
        int rowIndex = 2;
        for(MyPageSumDTO dto : listExcel) {
        	int i = 0;
        	
        	row = worksheet.createRow(rowIndex);
        	row.createCell(i++).setCellValue(dto.getMasMonth());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getVasCnt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getVasAmt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getOcdCnt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getOcdAmt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getDcsCnt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getDcsAmt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getDcdCnt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getDcdAmt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getCashCnt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getCashAmt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getTotCnt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getTotAmt());
        	rowIndex++;
        }
        
        if(vo != null) {
        	for(int i = 0; i < idx; i++) {
        		row = worksheet.createRow(rowIndex);
	        	row.createCell(i++).setCellValue("합계");
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getVasCnt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getVasAmt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getOcdCnt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getOcdAmt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getDcsCnt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getDcsAmt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getDcdCnt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getDcdAmt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getCashCnt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getCashAmt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getTotCnt());
				row.createCell(i++, CellType.NUMERIC).setCellValue(vo.getTotAmt());
        	}
        }

		try {
			response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
	}

}
