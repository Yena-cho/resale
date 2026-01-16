package com.finger.shinhandamoa.org.receiptmgmt.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;

@Deprecated
public class ExcelSaveReceiptReg extends AbstractExcelView2 {

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
		for (int i = 0 ; i<listExcel.size();i++){
			listExcel.get(i).setCusName(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusName()));
			listExcel.get(i).setCusGubn1(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn1()));
			listExcel.get(i).setCusGubn2(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn2()));
			listExcel.get(i).setCusGubn3(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn3()));
			listExcel.get(i).setCusGubn4(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn4()));
		}
        List<CodeDTO> cdList = (List<CodeDTO>)model.get("cusGbList");	// 구분코드
		for (int i = 0 ; i<cdList.size();i++){
			cdList.get(i).setCodeName(StringEscapeUtils.unescapeHtml4(cdList.get(i).getCodeName()));
		}
        
     // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        int idx = (Integer) model.get("cusGbCnt") + 13; // 컬럼수
        
     // 컬럼 사이즈 설정
        int columnIndex = 0;
        
        for(int i = 0; i < idx; i++) {
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000); 
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
    		worksheet.setColumnWidth(i++, 7000); 
    		worksheet.setColumnWidth(i++, 5000); 
    		worksheet.setColumnWidth(i++, 5000); 
    		worksheet.setColumnWidth(i++, 5000); 
    		worksheet.setColumnWidth(i++, 5000); 
    		worksheet.setColumnWidth(i++, 5000);
    		worksheet.setColumnWidth(i++, 5000);
    		worksheet.setColumnWidth(i++, 5000);
        }
        
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("고객명");
        	hRow.createCell(i++).setCellValue("청구월");
        	hRow.createCell(i++).setCellValue("수납일자");
        	hRow.createCell(i++).setCellValue("수납상태");
        	if((Integer) model.get("cusGbCnt") == 1 || (Integer) model.get("cusGbCnt") > 1) {
    			hRow.createCell(i++).setCellValue(cdList.get(0).getCodeName());
    		}
    		if((Integer) model.get("cusGbCnt") == 2 || (Integer) model.get("cusGbCnt") > 2) {
    			hRow.createCell(i++).setCellValue(cdList.get(1).getCodeName());
    		}
    		if((Integer) model.get("cusGbCnt") == 3 || (Integer) model.get("cusGbCnt") > 3) {
    			hRow.createCell(i++).setCellValue(cdList.get(2).getCodeName());
    		}
    		if((Integer) model.get("cusGbCnt") == 4 || (Integer) model.get("cusGbCnt") > 4) {
    			hRow.createCell(i++).setCellValue(cdList.get(3).getCodeName());
    		}
    		hRow.createCell(i++).setCellValue("청구항목건수");
        	hRow.createCell(i++).setCellValue("청구금액(원)");
        	hRow.createCell(i++).setCellValue("수납금액(원)");
        	hRow.createCell(i++).setCellValue("미납금액(원)");
        	hRow.createCell(i++).setCellValue("납부수단상세");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("환불액(원)");
        }
        hRow = worksheet.createRow(1);
        
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	for(CodeDTO code : cdList) {
    			String col = code.getCode();
    			switch (col) {
				case "CUSGUBN1":
					hRow.createCell(i++).setCellValue("");
					break;
				case "CUSGUBN2":
					hRow.createCell(i++).setCellValue("");
					break;
				case "CUSGUBN3":
					hRow.createCell(i++).setCellValue("");
					break;
				case "CUSGUBN4":
					hRow.createCell(i++).setCellValue("");
					break;
					
				default:
					break;
				}
    		}
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("");
        	hRow.createCell(i++).setCellValue("가상계좌");
        	hRow.createCell(i++).setCellValue("온라인카드");
        	hRow.createCell(i++).setCellValue("현금");
        	hRow.createCell(i++).setCellValue("오프라인카드");
        	hRow.createCell(i++).setCellValue("");
        }
        int rowIndex = 2;
        for(ReceiptMgmtDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(StringEscapeUtils.unescapeHtml4(dto.getCusName()));// 고객명
        	row.createCell(i++).setCellValue(dto.getMasMonth());
        	row.createCell(i++).setCellValue(dto.getPayDay());
        	row.createCell(i++).setCellValue(dto.getNotiMasStNm());
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
    		if(dto.getItemCnt()!=null) {
    			row.createCell(i++).setCellValue(dto.getItemCnt()+"건");
    		}else {
    			row.createCell(i++).setCellValue("");
    		}
    		row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getAmt());
    		row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getRcpAmt());
    		row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getUnAmt());
    		row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getVasAmt());
    		row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getOcdAmt());
    		row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getDcsAmt());
    		row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getDcdAmt());
    		row.createCell(i++, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getRePayAmt()));
    		rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		    }catch (UnsupportedEncodingException e) {
		    	e.printStackTrace();
		    }
	}

}
