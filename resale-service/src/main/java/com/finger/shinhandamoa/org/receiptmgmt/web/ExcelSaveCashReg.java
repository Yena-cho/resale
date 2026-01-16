package com.finger.shinhandamoa.org.receiptmgmt.web;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Deprecated
public class ExcelSaveCashReg extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
	     
        String excelName = "현금영수증_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        
        List<ReceiptMgmtDTO> listExcel = (List<ReceiptMgmtDTO>)model.get("list"); // 목록
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
        
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        
        int idx = (Integer) model.get("cusGbCnt") + 11; // 컬럼수
        
        // 컬럼 사이즈 설정
        int columnIndex = 0;
        
        for(int i = 0; i < idx; i++) {
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 5000); 
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
    		worksheet.setColumnWidth(i++, 7000); 
    		worksheet.setColumnWidth(i++, 5000); 
    		worksheet.setColumnWidth(i++, 5000);
    		worksheet.setColumnWidth(i++, 5000);
    		worksheet.setColumnWidth(i++, 5000);
    		worksheet.setColumnWidth(i++, 5000);
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("입금일자");
        	hRow.createCell(i++).setCellValue("승인일자");
        	hRow.createCell(i++).setCellValue("승인번호");
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
    		hRow.createCell(i++).setCellValue("현금영수증 발행번호");
    		hRow.createCell(i++).setCellValue("입금금액(원)");
    		hRow.createCell(i++).setCellValue("발행금액(원)");
    		hRow.createCell(i++).setCellValue("발행구분");
    		hRow.createCell(i++).setCellValue("입금구분");
        	hRow.createCell(i++).setCellValue("메세지");
        }
        
        int rowIndex = 1;
        
        for(ReceiptMgmtDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(dto.getPayDay());
        	row.createCell(i++).setCellValue(dto.getAppDay());
        	row.createCell(i++).setCellValue(dto.getAppNo());
        	row.createCell(i++).setCellValue(StringEscapeUtils.unescapeHtml4(dto.getCusName()));
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
        	String cusOffNo ="";
    		if(!StrUtil.nullToVoid(dto.getCusOffNo2()).equals("")) {
    			cusOffNo = StrUtil.nullToVoid(dto.getCusOffNo()) + "," + StrUtil.nullToVoid(dto.getCusOffNo2()); 
    		}else {
    			cusOffNo = StrUtil.nullToVoid(dto.getCusOffNo());
    		}
    		row.createCell(i++).setCellValue(cusOffNo);
    		row.createCell(i++, CellType.NUMERIC).setCellValue(dto.getRcpAmt());
			if(!StrUtil.nullToVoid(dto.getCusOffNo2()).equals("")) {
				row.createCell(i++).setCellValue(dto.getCshAmt() + "," + dto.getCshAmt2());
			}else {
				row.createCell(i++).setCellValue(dto.getCshAmt());
			}
    		row.createCell(i++).setCellValue(dto.getCashMasStNm());
    		row.createCell(i++).setCellValue(dto.getSveCdNm());
    		row.createCell(i++).setCellValue(dto.getAppMsg());
    		rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	}


}
