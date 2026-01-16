package com.finger.shinhandamoa.sys.addServiceMgmt.web;

import com.finger.shinhandamoa.sys.addServiceMgmt.dto.AddServiceMgmtDTO;
import com.finger.shinhandamoa.sys.addServiceMgmt.dto.XCashmasDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

public class ExcelSavePastRcpHist extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
	     
        String excelName = (String)model.get("chaCd") + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        List<AddServiceMgmtDTO> listExcel = (List<AddServiceMgmtDTO>)model.get("list");
        String stchk = (String)model.get("statusCheck");
        int idx = 14;
		if(stchk.equals("rcpDet")) {
			idx = 15;
		}
        worksheet = workbook.createSheet("sheet1");

        for(int i = 0; i < idx; i++) {
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 7000);
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 5000);
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000); 
        	worksheet.setColumnWidth(i++, 4000);
        	if(stchk.equals("rcpDet")) {
				worksheet.setColumnWidth(i++, 4000);
			}
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000);
        	worksheet.setColumnWidth(i++, 4000);
        }
        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);
        
        for(int i = 0; i < idx; i++) {
        	hRow.createCell(i++).setCellValue("기관코드");
        	hRow.createCell(i++).setCellValue("기관명");
        	hRow.createCell(i++).setCellValue("청구월");
        	hRow.createCell(i++).setCellValue("입금일시");
        	hRow.createCell(i++).setCellValue("고객명");
        	hRow.createCell(i++).setCellValue("가상계좌번호");
        	hRow.createCell(i++).setCellValue("고객구분1");
        	hRow.createCell(i++).setCellValue("고객구분2");
        	hRow.createCell(i++).setCellValue("고객구분3");
        	hRow.createCell(i++).setCellValue("고객구분4");
			if(stchk.equals("rcpDet")) {
				hRow.createCell(i++).setCellValue("청구항목명");
			}
        	hRow.createCell(i++).setCellValue("청구금액");
        	hRow.createCell(i++).setCellValue("입금금액");
        	hRow.createCell(i++).setCellValue("입금수단");
        	hRow.createCell(i++).setCellValue("입금은행");
        }
        
        int rowIndex = 1;
        for(AddServiceMgmtDTO dto : listExcel) {
        	int i = 0;

        	row = worksheet.createRow(rowIndex);
			
        	row.createCell(i++).setCellValue(dto.getChaCd());
        	row.createCell(i++).setCellValue(dto.getChaName());
			row.createCell(i++).setCellValue(dto.getMasMonth());
			row.createCell(i++).setCellValue(dto.getPayDay2());
			row.createCell(i++).setCellValue(dto.getCusName());
        	row.createCell(i++).setCellValue(dto.getVaNo());
        	row.createCell(i++).setCellValue(dto.getCusGubn1());
        	row.createCell(i++).setCellValue(dto.getCusGubn2());
        	row.createCell(i++).setCellValue(dto.getCusGubn3());
        	row.createCell(i++).setCellValue(dto.getCusGubn4());
			if(stchk.equals("rcpDet")) {
				row.createCell(i++).setCellValue(dto.getPayItemNm());
			}
        	row.createCell(i++).setCellValue(dto.getPayItemAmt());
			row.createCell(i++).setCellValue(dto.getRcpAmt());
			row.createCell(i++).setCellValue(dto.getSveCd());
			row.createCell(i++).setCellValue(dto.getBnkNm());
        	rowIndex++;
        }
        
        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
        
	}

}
