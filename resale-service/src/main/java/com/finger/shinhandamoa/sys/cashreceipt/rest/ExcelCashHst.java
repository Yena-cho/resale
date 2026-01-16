package com.finger.shinhandamoa.sys.cashreceipt.rest;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.common.CashUtil;
import com.finger.shinhandamoa.org.receiptmgmt.dto.ReceiptMgmtDTO;
import com.finger.shinhandamoa.sys.cashreceipt.service.CashReceiptHistoryDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExcelCashHst extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
     
        String excelName = "현금영수증 이용내역" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;

		List<CashReceiptHistoryDTO> listExcel = (List<CashReceiptHistoryDTO>)model.get("list"); // 목록

        int idx = 22; // 컬럼수
        worksheet = workbook.createSheet("sheet1");
        int columnIndex = 0;
		final DecimalFormat df = new DecimalFormat("#,##0");
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
    		worksheet.setColumnWidth(i++, 5000);
    		worksheet.setColumnWidth(i++, 5000);
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

				hRow.createCell(0).setCellValue("기관코드");
				hRow.createCell(1).setCellValue("기관명");
				hRow.createCell(2).setCellValue("과세여부");
				hRow.createCell(3).setCellValue("입금일자");
				hRow.createCell(4).setCellValue("승인일자");
				hRow.createCell(5).setCellValue("승인번호");
				hRow.createCell(6).setCellValue("승인여부");
				hRow.createCell(7).setCellValue("취소 승인일자");
				hRow.createCell(8).setCellValue("취소 승인번호");
				hRow.createCell(9).setCellValue("사업자등록번호");
				hRow.createCell(10).setCellValue("현금영수증 발행번호");
				hRow.createCell(11).setCellValue("고객명");
				hRow.createCell(12).setCellValue("입금자명");
				hRow.createCell(13).setCellValue("거래금액");
				hRow.createCell(14).setCellValue("공급가액");
				hRow.createCell(15).setCellValue("부가세액");
				hRow.createCell(16).setCellValue("입금구분");
				hRow.createCell(17).setCellValue("발행상태");
				hRow.createCell(18).setCellValue("조작일시");
				hRow.createCell(19).setCellValue("조작자");
				hRow.createCell(20).setCellValue("실패사유");
				hRow.createCell(21).setCellValue("CASHMASCD");

        	int rowIndex = 1;
            for(CashReceiptHistoryDTO dto : listExcel) {
            	int i = 0;

            	row = worksheet.createRow(rowIndex);

				row.createCell(i++).setCellValue(dto.getChaCd());
				row.createCell(i++).setCellValue(dto.getChaName());
				row.createCell(i++).setCellValue(dto.getNoTaxYn());
				row.createCell(i++).setCellValue(dto.getPayDay());
				row.createCell(i++).setCellValue(dto.getTxDate());
				row.createCell(i++).setCellValue(dto.getTxNo());
				row.createCell(i++).setCellValue(dto.getTxTypeCode());
				row.createCell(i++).setCellValue(dto.getCancelTxDate());
				row.createCell(i++).setCellValue(dto.getCancelTxNo());
				row.createCell(i++).setCellValue(dto.getHostIdentityNo());
				row.createCell(i++).setCellValue(dto.getClientIdentityNo());
				row.createCell(i++).setCellValue(dto.getCusName());
				row.createCell(i++).setCellValue(dto.getRcpusrName());
				row.createCell(i++, CellType.NUMERIC).setCellValue(df.format(dto.getTxAmount()));
				row.createCell(i++, CellType.NUMERIC).setCellValue(df.format(dto.getTaxFreeAmount()));
				row.createCell(i++, CellType.NUMERIC).setCellValue(df.format(dto.getTax()));
				row.createCell(i++).setCellValue(dto.getSvecdNm());
				row.createCell(i++).setCellValue(dto.getReqStatus());
				row.createCell(i++).setCellValue(dto.getRequestDt());
				row.createCell(i++).setCellValue(dto.getRequestUser());
				row.createCell(i++).setCellValue(dto.getResponseMessage());
				row.createCell(i++).setCellValue(dto.getCashMasCd());
        		rowIndex++;
            }

        try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
	    }catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	}

}
