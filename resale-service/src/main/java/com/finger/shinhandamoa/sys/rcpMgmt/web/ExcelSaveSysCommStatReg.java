package com.finger.shinhandamoa.sys.rcpMgmt.web;

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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysAutoTransDTO;
import com.finger.shinhandamoa.sys.rcpMgmt.dto.SysCommStatDTO;

public class ExcelSaveSysCommStatReg extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
	     
        String excelName = chaCd + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        

        	List<SysCommStatDTO> listExcel = (List<SysCommStatDTO>)model.get("list"); // 목록
        	List<SysCommStatDTO> tlistExcel = (List<SysCommStatDTO>)model.get("tlist"); // 목록

       if((int)model.get("distinct")==1) {
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        int idx = 11; // 컬럼수
        
        // 컬럼 사이즈 설정
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
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("이용월");
        	hRow.createCell(i++).setCellValue("이용기관수");
        	hRow.createCell(i++).setCellValue("관리비(원/건)");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("문자(원/건)");
        	hRow.createCell(i++).setCellValue("");
    		hRow.createCell(i++).setCellValue("은행수수료(원/건)");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("카드수수료(원/건)");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("합계(원)");
        }
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));

        hRow = worksheet.createRow(1);
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("핑거 자동출금");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("핑거 자동출금");
        	hRow.createCell(i++).setCellValue("");
    		hRow.createCell(i++).setCellValue("신한은행 자동출금");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("나이스 페이먼츠 차감");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        }
        worksheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        worksheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
        worksheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 9));
        worksheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
        
        int rowIndex = 2;
        
        for(SysCommStatDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(dto.getDay());
        	row.createCell(i++).setCellValue(dto.getCustCount());
        	row.createCell(i++).setCellValue(Integer.parseInt(dto.getMgmtFee()));
        	row.createCell(i++).setCellValue(dto.getMgmtFeeCount());
        	row.createCell(i++).setCellValue(Integer.parseInt(dto.getSmsFee()));
        	row.createCell(i++).setCellValue(dto.getSmsFeeCount());
        	row.createCell(i++).setCellValue(Integer.parseInt(dto.getBankFee()));
        	row.createCell(i++).setCellValue(dto.getBankFeeCount());
        	row.createCell(i++).setCellValue(Integer.parseInt(dto.getCardFee()));
        	row.createCell(i++).setCellValue(dto.getCardFeeCount());
    		rowIndex++;
    		row.createCell(i++).setCellFormula("SUM(C"+rowIndex+":E"+rowIndex+":G"+rowIndex+":I"+rowIndex+")");
        }
        
        for(SysCommStatDTO dto : tlistExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue("계");
        	row.createCell(i++).setCellValue("");
        	row.createCell(i++).setCellValue(Integer.parseInt(dto.getMfSum()));
        	row.createCell(i++).setCellValue(dto.getMfCSum());
        	row.createCell(i++).setCellValue(Integer.parseInt(dto.getSfSum()));
        	row.createCell(i++).setCellValue(dto.getSfCSum());
        	row.createCell(i++).setCellValue(Integer.parseInt(dto.getBfSum()));
        	row.createCell(i++).setCellValue(dto.getBfCSum());
        	row.createCell(i++).setCellValue(Integer.parseInt(dto.getCfSum()));
        	row.createCell(i++).setCellValue(dto.getCfCSum());
        	worksheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex , 0, 1));
    		rowIndex++;
    		row.createCell(i++).setCellFormula("SUM(C"+rowIndex+":E"+rowIndex+":G"+rowIndex+":I"+rowIndex+")");
        }
        
       }else {
    	   worksheet = workbook.createSheet("sheet1");
           
           int idx = 9; // 컬럼수
           
           // 컬럼 사이즈 설정
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
           }

           hRow = worksheet.createRow(0);
           hRow.setHeight((short)500);
           for(int i = 0; i < idx; i++) {
           	hRow.createCell(i++).setCellValue("이용월");
           	hRow.createCell(i++).setCellValue("이용기관수");
           	hRow.createCell(i++).setCellValue("핑거");
           	hRow.createCell(i++).setCellValue("");
           	hRow.createCell(i++).setCellValue("은행");
           	hRow.createCell(i++).setCellValue("");
       		hRow.createCell(i++).setCellValue("신용카드");
           	hRow.createCell(i++).setCellValue("");
           	hRow.createCell(i++).setCellValue("합계(원)");
           }
           worksheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
           worksheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
           worksheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));

           hRow = worksheet.createRow(1);
           for(int i = 0; i < idx; i++) {
           	hRow.createCell(i++).setCellValue("");
           	hRow.createCell(i++).setCellValue("");
           	hRow.createCell(i++).setCellValue("핑거 정산");
           	hRow.createCell(i++).setCellValue("");
           	hRow.createCell(i++).setCellValue("신한은행 정산");
           	hRow.createCell(i++).setCellValue("");
           	hRow.createCell(i++).setCellValue("나이스페이먼츠 정산");
           	hRow.createCell(i++).setCellValue("");
           	hRow.createCell(i++).setCellValue("");
           }
           worksheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
           worksheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
           worksheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
           worksheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
           worksheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
           worksheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
           
           int rowIndex = 2;
           
           for(SysCommStatDTO dto : listExcel) {
           	int i = 0;

           	row = worksheet.createRow(rowIndex);
   			
           	row.createCell(i++).setCellValue(dto.getFgday());
           	row.createCell(i++).setCellValue(dto.getFgcustCount());
           	row.createCell(i++).setCellValue(Integer.parseInt(dto.getFingerIC()));
           	row.createCell(i++).setCellValue(dto.getFingerICCount());
           	row.createCell(i++).setCellValue(Integer.parseInt(dto.getBankIC()));
           	row.createCell(i++).setCellValue(dto.getBankICCount());
           	row.createCell(i++).setCellValue(Integer.parseInt(dto.getCardIC()));
           	row.createCell(i++).setCellValue(dto.getCardICCount());
       		rowIndex++;
       		row.createCell(i++).setCellFormula("SUM(C"+rowIndex+":E"+rowIndex+":G"+rowIndex+")");
           }	 
           for(SysCommStatDTO dto : tlistExcel) {
           	int i = 0;

           	row = worksheet.createRow(rowIndex);

           	row.createCell(i++).setCellValue("계");
           	row.createCell(i++).setCellValue("");
           	row.createCell(i++).setCellValue(Integer.parseInt(dto.getFingerSum()));
           	row.createCell(i++).setCellValue(dto.getFingerCSum());
           	row.createCell(i++).setCellValue(Integer.parseInt(dto.getBankSum()));
           	row.createCell(i++).setCellValue(dto.getBankCSum());
           	row.createCell(i++).setCellValue(Integer.parseInt(dto.getCardSum()));
           	row.createCell(i++).setCellValue(dto.getFingerCSum());
           	worksheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex , 0, 1));
       		rowIndex++;
       		row.createCell(i++).setCellFormula("SUM(C"+rowIndex+":E"+rowIndex+":G"+rowIndex+")");
           }
       }
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	}

}
