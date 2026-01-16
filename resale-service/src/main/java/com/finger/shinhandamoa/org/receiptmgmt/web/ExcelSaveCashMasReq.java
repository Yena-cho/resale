package com.finger.shinhandamoa.org.receiptmgmt.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;

public class ExcelSaveCashMasReq extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = chaCd + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        
        List<ReceiptMgmtDTO> listExcel = (List<ReceiptMgmtDTO>)model.get("list"); // 목록
        String type = (String) model.get("type");
        List<CodeDTO> cdList = (List<CodeDTO>)model.get("cusGbList");	// 구분코드
		for (int i = 0 ; i<listExcel.size();i++){
			listExcel.get(i).setCusName(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusName()));
			listExcel.get(i).setCusGubn1(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn1()));
			listExcel.get(i).setCusGubn2(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn2()));
			listExcel.get(i).setCusGubn3(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn3()));
			listExcel.get(i).setCusGubn4(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn4()));
		}
		for (int i = 0 ; i<cdList.size();i++){
			cdList.get(i).setCodeName(StringEscapeUtils.unescapeHtml4(cdList.get(i).getCodeName()));
		}
        
        int idx = (Integer) model.get("cusGbCnt") + 9 - 1; // 컬럼수 - 변경내역 제거
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
       	// 컬럼 사이즈 설정
        int columnIndex = 0;
        
        for(int i = 0; i < idx; i++) {
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000); 
        	for(CodeDTO code : cdList) {
    			String col = code.getCode();
    			switch (col) {
				case "CUSGUBN1":
					worksheet.setColumnWidth(i++, 5000); 
					break;
				case "CUSGUBN2":
					worksheet.setColumnWidth(i++, 5000); 
					break;
				case "CUSGUBN3":
					worksheet.setColumnWidth(i++, 5000); 
					break;
				case "CUSGUBN4":
					worksheet.setColumnWidth(i++, 5000); 
					break;
					
				default:
					break;
				}
    		}
        	worksheet.setColumnWidth(i++, 4000);
    		worksheet.setColumnWidth(i++, 4000); 
    		worksheet.setColumnWidth(i++, 4000); 
    		worksheet.setColumnWidth(i++, 4000); 
    		worksheet.setColumnWidth(i++, 4000); 
    		//worksheet.setColumnWidth(i++, 7000); 
        }
        
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("청구월");
        	hRow.createCell(i++).setCellValue("고객명");
        	
        	for(CodeDTO code : cdList) {
    			String col = code.getCode();
    			switch (col) {
				case "CUSGUBN1":
					hRow.createCell(i++).setCellValue(code.getCodeName());
					break;
				case "CUSGUBN2":
					hRow.createCell(i++).setCellValue(code.getCodeName());
					break;
				case "CUSGUBN3":
					hRow.createCell(i++).setCellValue(code.getCodeName());
					break;
				case "CUSGUBN4":
					hRow.createCell(i++).setCellValue(code.getCodeName());
					break;
					
				default:
					break;
				}
    		}
    		hRow.createCell(i++).setCellValue("청구항목");
        	hRow.createCell(i++).setCellValue("청구금액(원)");
        	hRow.createCell(i++).setCellValue("입금액(원)");
        	hRow.createCell(i++).setCellValue("환불액(원)");
        	hRow.createCell(i++).setCellValue("환불 처리일");
        	//hRow.createCell(i++).setCellValue("변경내역");
        }
        
        int rowIndex = 1;
        for(ReceiptMgmtDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(dto.getMasMonth());
        	row.createCell(i++).setCellValue(StringEscapeUtils.unescapeHtml4(dto.getCusName()));// 고객명
        	
        	for(CodeDTO code : cdList) {
    			String col = code.getCode();
    			switch (col) {
				case "CUSGUBN1":
					row.createCell(i++).setCellValue(StringEscapeUtils.unescapeHtml4(dto.getCusGubn1()));
					break;
				case "CUSGUBN2":
					row.createCell(i++).setCellValue(StringEscapeUtils.unescapeHtml4(dto.getCusGubn2()));
					break;
				case "CUSGUBN3":
					row.createCell(i++).setCellValue(StringEscapeUtils.unescapeHtml4(dto.getCusGubn3()));
					break;
				case "CUSGUBN4":
					row.createCell(i++).setCellValue(StringEscapeUtils.unescapeHtml4(dto.getCusGubn4()));
					break;
					
				default:
					break;
				}
    		}
        	logger.debug(StrUtil.nullToVoid(dto.getItemCnt()) + " 1");
        	row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getItemCnt()) );
        	logger.debug("dto.getSumAmt()) " + dto.getSumAmt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getSumAmt()));
        	logger.debug("dto.getSumAmt()) " + dto.getSumAmt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getRcpAmt());
        	logger.debug("dto.getRcpAmt() " + dto.getRcpAmt());
        	row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getRePayAmt()));
    		if(dto.getRePayDay() != null) {
    			logger.debug("StrUtil.nullToVoid(dto.getRePayDay()) " + StrUtil.nullToVoid(dto.getRePayDay()));
    			row.createCell(i++).setCellValue(StrUtil.nullToVoid(dto.getRePayDay()));
    			logger.debug("StrUtil.nullToVoid(dto.getRePayDay()) " + StrUtil.nullToVoid(dto.getRePayDay()));
    			//row.createCell(i++).setCellValue(StrUtil.nullToVoid(dto.getRePayDay()) +"환불액" + dto.getRePayAmt() + "원 처리");
    		}else {
    			row.createCell(i++).setCellValue("");
    			//row.createCell(i++).setCellValue("");
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
