package com.finger.shinhandamoa.bank.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.finger.shinhandamoa.bank.dto.BankReg01DTO;
import com.finger.shinhandamoa.common.CashUtil;

public class ExcelSaveChalist extends AbstractExcelView2 {
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
		String fileName = "";
		if("signManage".equals(model.get("flag"))) {
			fileName = "가입승인";
		} else if("orgManage".equals(model.get("flag"))) {
			fileName = "이용기관";
		} else {
			fileName = "수수료";
		}
		
        String excelName = fileName + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row  = null;
        
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");
        // sell 스타일 생성
        CellStyle style = workbook.createCellStyle();     //스타일 생성
        style.setAlignment(HorizontalAlignment.RIGHT);
        
        // 가입승인관리 엑셀 생성
        if("signManage".equals(model.get("flag"))) {
        	List<BankReg01DTO> listExcel = (List<BankReg01DTO>)model.get("list");
        	int idx = 10;
	        for(int i = 0; i < idx; i++) {
	        	worksheet.setColumnWidth(i++, 4000); 
	        	worksheet.setColumnWidth(i++, 4000); 
	        	worksheet.setColumnWidth(i++, 4000); 
	        	worksheet.setColumnWidth(i++, 7000); 
	        	worksheet.setColumnWidth(i++, 4000); 
	        	worksheet.setColumnWidth(i++, 4000); 
	        	worksheet.setColumnWidth(i++, 4000); 
	        	worksheet.setColumnWidth(i++, 6000); 
	        	worksheet.setColumnWidth(i++, 4000); 
	        	worksheet.setColumnWidth(i++, 14000);
	        }
	        hRow = worksheet.createRow(0);
	        hRow.setHeight((short)500);
	        
	        for(int i = 0; i < idx; i++) {
	        	hRow.createCell(i++).setCellValue("No");
	        	hRow.createCell(i++).setCellValue("등록일자");
	        	hRow.createCell(i++).setCellValue("기관코드");
	        	hRow.createCell(i++).setCellValue("기관명");
	        	hRow.createCell(i++).setCellValue("이용방식");
	        	hRow.createCell(i++).setCellValue("발급요청좌수");
	        	hRow.createCell(i++).setCellValue("지점코드");
	        	hRow.createCell(i++).setCellValue("지점명");
	        	hRow.createCell(i++).setCellValue("승인상태");
	        	hRow.createCell(i++).setCellValue("메모");
	        }
	        
	    	int rowIndex = 1;
	        for(BankReg01DTO dto : listExcel) {
	        	int i = 0;
	
	        	row = worksheet.createRow(rowIndex);        				
	        	row.createCell(i++).setCellValue(rowIndex);									// NO
	        	row.createCell(i++).setCellValue(dto.getBnkRegiDt());  						// 등록일자
				row.createCell(i++).setCellValue(dto.getChaCd());        					// 기관코드                 
				row.createCell(i++).setCellValue(dto.getChaName());        					// 기관명
				if("Y".equals(dto.getAmtChkTy())){											// 이용방식
					row.createCell(i++).setCellValue("승인");
				}else{
					row.createCell(i++).setCellValue("통지");
				}
				row.createCell(i++).setCellValue(dto.getAccNoCnt());       					// 발급요청좌수
				row.createCell(i++).setCellValue(dto.getAgtCd());          					// 지점코드
				row.createCell(i++).setCellValue(dto.getAgtName());        					// 지졈명
				if("ST01".equals(dto.getChast())){											// 승인상태
					row.createCell(i++).setCellValue("승인완료");
				}else if("ST02".equals(dto.getChast())){
					row.createCell(i++).setCellValue("해지");
				}else if("ST07".equals(dto.getChast())) {
					row.createCell(i++).setCellValue("승인거부");  
				}else if("ST04".equals(dto.getChast())) {
					row.createCell(i++).setCellValue("정지");
				}else if("ST05".equals(dto.getChast())) {
					row.createCell(i++).setCellValue("승인대기");
				}
				row.createCell(i++).setCellValue(dto.getRemark());  						// 메모
				
				rowIndex++;
			}
         
        } else if("orgManage".equals(model.get("flag"))) {
        	List<BankReg01DTO> listExcel = (List<BankReg01DTO>)model.get("list");
        	int idx = 9;
 	        for(int i = 0; i < idx; i++) {
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 7000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        }
 	        
 	        hRow = worksheet.createRow(0);
 	        hRow.setHeight((short)500);
 	        
 	        for(int i = 0; i < idx; i++) {
 	        	hRow.createCell(i++).setCellValue("No");
 	        	hRow.createCell(i++).setCellValue("등록일자");
 	        	hRow.createCell(i++).setCellValue("기관코드");
 	        	hRow.createCell(i++).setCellValue("기관명");
 	        	hRow.createCell(i++).setCellValue("지점코드");
 	        	hRow.createCell(i++).setCellValue("지점명");
 	        	hRow.createCell(i++).setCellValue("기관상태");
 	        	hRow.createCell(i++).setCellValue("기관분류");
 	        	hRow.createCell(i++).setCellValue("사용방식");
 	        	hRow.createCell(i++).setCellValue("기관검증");
 	        }
 	        
 	        int rowIndex = 1;
	        for(BankReg01DTO dto : listExcel) {
	        	int i = 0;
	        	row = worksheet.createRow(rowIndex);        				
	        	row.createCell(i++).setCellValue(rowIndex);					// NO
	        	row.createCell(i++).setCellValue(dto.getBnkRegiDt());   	// 등록일자
				row.createCell(i++).setCellValue(dto.getChaCd());        	// 기관코드                 
				row.createCell(i++).setCellValue(dto.getChaName());        	// 기관명
				row.createCell(i++).setCellValue(dto.getAgtCd());       	// 지점코드                
				row.createCell(i++).setCellValue(dto.getAgtName());       	// 지점명
				if("ST06".equals(dto.getChast())){							// 기관상태
					row.createCell(i++).setCellValue("정상");
				}else if("ST02".equals(dto.getChast())){
					row.createCell(i++).setCellValue("해지");
				}else if("ST04".equals(dto.getChast()) || "ST08".equals(dto.getChast())){
					row.createCell(i++).setCellValue("정지");
				}else if("ST01".equals(dto.getChast())){
					row.createCell(i++).setCellValue("승인");
				}
				if("01".equals(dto.getChatrty())){							//기관분류
					row.createCell(i++).setCellValue("WEB");
				}else{
					row.createCell(i++).setCellValue("API");
				}
				if("Y".equals(dto.getAmtChkTy())){							//사용방식
					row.createCell(i++).setCellValue("승인");
				}else{
					row.createCell(i++).setCellValue("통지");
				}
				if("N".equals(dto.getChaCloseChk())){						//기관검증
					row.createCell(i++).setCellValue("적합");
				}else{
					row.createCell(i++).setCellValue("부적합");
				}
				rowIndex++;
			}
        } else {
        	List<BankReg01DTO> listExcel = (List<BankReg01DTO>)model.get("list");
        	int idx = 11;
 	        for(int i = 0; i < idx; i++) {
 	        	worksheet.setColumnWidth(i++, 4000);
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 8000); 
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        	worksheet.setColumnWidth(i++, 6000); 
 	        	worksheet.setColumnWidth(i++, 6000); 
 	        	worksheet.setColumnWidth(i++, 6000); 
 	        	worksheet.setColumnWidth(i++, 6000); 
 	        	worksheet.setColumnWidth(i++, 6000); 
 	        	worksheet.setColumnWidth(i++, 6000);
 	        	worksheet.setColumnWidth(i++, 6000);
 	        	worksheet.setColumnWidth(i++, 4000); 
 	        }
 	        
 	        hRow = worksheet.createRow(0);
 	        hRow.setHeight((short)500);
 	        
 	        for(int i = 0; i < idx; i++) {
 	        	hRow.createCell(i++).setCellValue("No");
 	        	hRow.createCell(i++).setCellValue("이용월");
 	        	hRow.createCell(i++).setCellValue("기관코드");
 	        	hRow.createCell(i++).setCellValue("기관명");
 	        	hRow.createCell(i++).setCellValue("지점코드");
 	        	hRow.createCell(i++).setCellValue("지점명");
 	        	hRow.createCell(i++).setCellValue("관리수수료(청구건)_건수(건)");
 	        	hRow.createCell(i++).setCellValue("관리수수료(청구건)_금액(원)");
 	        	hRow.createCell(i++).setCellValue("입금수수료(입금건)_건수(건)");
 	        	hRow.createCell(i++).setCellValue("입금수수료(입금건)_금액(원)");
 	        	hRow.createCell(i++).setCellValue("이용요금(입금건)_은행(건)");
 	        	hRow.createCell(i++).setCellValue("이용요금(입금건)_핑거(원)");
 	        	hRow.createCell(i++).setCellValue("합계금액(원)");
 	        }
 	        
 	        int rowIndex = 1;
	        for(BankReg01DTO dto : listExcel) {
	        	int i = 0;
	        	row = worksheet.createRow(rowIndex);        		
	        	row.createCell(i++).setCellValue(rowIndex);										 // NO
	        	row.createCell(i++).setCellValue(dto.getMonth ());        		 				 // 이용월
				row.createCell(i++).setCellValue(dto.getChaCd ());        						 // 기관코드                 
				row.createCell(i++).setCellValue(dto.getChaName ());        					 // 기관명                
				row.createCell(i++).setCellValue(dto.getAgtCd ());       				  	     // 지점코드                
				row.createCell(i++).setCellValue(dto.getAgtName ());       						 // 등록지점                
				row.createCell(i++).setCellValue(dto.getNotiCnt ());        					 //관리비(청구건)_건수(건)
				row.createCell(i++).setCellValue(CashUtil.moneyFormat(dto.getNotiFee ()));       //관리비(청구건)_금액(원)
				row.createCell(i++).setCellValue(dto.getRcpCnt ());        						 //이용료(입금건)_건수(건)
				row.createCell(i++).setCellValue(CashUtil.moneyFormat(dto.getRcpFee ()));        //이용료(입금건)_금액(원)
				row.createCell(i++).setCellValue(CashUtil.moneyFormat(dto.getRcpBnkFee()));
				row.createCell(i++).setCellValue(CashUtil.moneyFormat(dto.getRcpFee() - dto.getRcpBnkFee()));
				row.createCell(i++).setCellValue(CashUtil.moneyFormat(dto.getTotFee()));     	 // 합계액(원)                
				rowIndex++;
			}
        }
		 
		try {
		    response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		  } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		      }
		}

}