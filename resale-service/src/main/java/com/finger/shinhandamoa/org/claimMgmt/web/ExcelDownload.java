package com.finger.shinhandamoa.org.claimMgmt.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;

public class ExcelDownload extends AbstractExcelView2 {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String chaCd = authentication.getName();

		String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

		String excelName = sCurTime + "_" + chaCd + "_청구양식.xlsx";
		Sheet worksheet = null;
		Row row = null;

		List<ClaimDTO> iList = (List<ClaimDTO>) model.get("iList");
		for (int i = 0 ; i<iList.size();i++){
			iList.get(i).setRemark(StringEscapeUtils.unescapeHtml4(iList.get(i).getRemark()));

		}
		List<ClaimDTO> cList = (List<ClaimDTO>) model.get("cList");
		for (int i = 0 ; i<cList.size();i++){
			cList.get(i).setCusName(StringEscapeUtils.unescapeHtml4(cList.get(i).getCusName()));
			cList.get(i).setCusGubn1(StringEscapeUtils.unescapeHtml4(cList.get(i).getCusGubn1()));
			cList.get(i).setCusGubn2(StringEscapeUtils.unescapeHtml4(cList.get(i).getCusGubn2()));
			cList.get(i).setCusGubn3(StringEscapeUtils.unescapeHtml4(cList.get(i).getCusGubn3()));
			cList.get(i).setCusGubn4(StringEscapeUtils.unescapeHtml4(cList.get(i).getCusGubn4()));
			cList.get(i).setRemark(StringEscapeUtils.unescapeHtml4(cList.get(i).getRemark()));
		}

		List<ClaimDTO> cusGbList = (List<ClaimDTO>) model.get("cusGbList");

		List<CodeDTO> cdList = (List<CodeDTO>) model.get("cusGbList");
		for (int i = 0 ; i<cdList.size();i++){
			cdList.get(i).setCodeName(StringEscapeUtils.unescapeHtml4(cdList.get(i).getCodeName()));
		}
		int cusGbCnt = (Integer) model.get("cusGbCnt");
		int iListCnt = iList.size();

		Cell cell;
		CellStyle style0 = workbook.createCellStyle();
		style0.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
		style0.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style0.setAlignment(HorizontalAlignment.CENTER);

		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);

		CellStyle styleEmptyRow = workbook.createCellStyle();
		styleEmptyRow.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("@"));

		/**
		 * 새로운 시트 생성
		 */
		worksheet = workbook.createSheet("sheet1");

		worksheet.setColumnWidth(0, 4000);
		worksheet.setColumnWidth(1, 5000);
		if (cusGbCnt == 1) {
			for (int i = 0; i < cusGbCnt; i++) {
				worksheet.setColumnWidth(2 + i, 5000);
			}
		}
		if (cusGbCnt == 2) {
			for (int i = 0; i < cusGbCnt; i++) {
				worksheet.setColumnWidth(2 + i, 5000);
			}
		}
		if (cusGbCnt == 3) {
			for (int i = 0; i < cusGbCnt; i++) {
				worksheet.setColumnWidth(2 + i, 5000);
			}
		}
		if (cusGbCnt == 4) {
			for (int i = 0; i < cusGbCnt; i++) {
				worksheet.setColumnWidth(2 + i, 5000);
			}
		}
		worksheet.setColumnWidth(2 + cusGbCnt, 6000);
		worksheet.setColumnWidth(3 + cusGbCnt, 6000);
		worksheet.setColumnWidth(4 + cusGbCnt, 6000);
		worksheet.setColumnWidth(5 + cusGbCnt, 6000);

		int aCnt = 6 + cusGbCnt;
		for (int i = 0; i < iListCnt; i++) {
			worksheet.setColumnWidth(aCnt++, 6000);
			worksheet.setColumnWidth(aCnt++, 6000);
		}

		int bCnt = 6 + cusGbCnt + (iListCnt * 2);
		worksheet.setColumnWidth(bCnt++, 7000);
		worksheet.setColumnWidth(bCnt++, 7000);
		worksheet.setColumnWidth(bCnt++, 7000);

		/**
		 * 1번째줄 헤더
		 */
		row = worksheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("month");
		cell.setCellStyle(style0);

		cell = row.createCell(1);
		cell.setCellValue("cusname");
		cell.setCellStyle(style0);

		if (cusGbCnt == 1) {
			for (int i = 0; i < cusGbCnt; i++) {
				cell = row.createCell(2 + i);
				cell.setCellValue("cusgubn" + (1 + i));
				cell.setCellStyle(style0);
			}
		}
		if (cusGbCnt == 2) {
			for (int i = 0; i < cusGbCnt; i++) {
				cell = row.createCell(2 + i);
				cell.setCellValue("cusgubn" + (1 + i));
				cell.setCellStyle(style0);
			}
		}
		if (cusGbCnt == 3) {
			for (int i = 0; i < cusGbCnt; i++) {
				cell = row.createCell(2 + i);
				cell.setCellValue("cusgubn" + (1 + i));
				cell.setCellStyle(style0);
			}
		}
		if (cusGbCnt == 4) {
			for (int i = 0; i < cusGbCnt; i++) {
				cell = row.createCell(2 + i);
				cell.setCellValue("cusgubn" + (1 + i));
				cell.setCellStyle(style0);
			}
		}

		cell = row.createCell(2 + cusGbCnt);
		cell.setCellValue("vano");
		cell.setCellStyle(style0);

		cell = row.createCell(3 + cusGbCnt);
		cell.setCellValue("startdate");
		cell.setCellStyle(style0);

		cell = row.createCell(4 + cusGbCnt);
		cell.setCellValue("enddate");
		cell.setCellStyle(style0);

		cell = row.createCell(5 + cusGbCnt);
		cell.setCellValue("printdate");
		cell.setCellStyle(style);

		int cCnt = 6 + cusGbCnt;
		for (int i = 0; i < iListCnt; i++) {
			cell = row.createCell(cCnt++);
			cell.setCellValue(iList.get(i).getPayItemCd());
			cell.setCellStyle(style0);

			cell = row.createCell(cCnt++);
			cell.setCellValue(iList.get(i).getPayItemCd());
			cell.setCellStyle(style);
		}

		int dCnt = 6 + cusGbCnt + (iListCnt * 2);

		cell = row.createCell(dCnt++);
		cell.setCellValue("추가안내사항1");
		cell.setCellStyle(style0);

		cell = row.createCell(dCnt++);
		cell.setCellValue("추가안내사항2");
		cell.setCellStyle(style0);

		cell = row.createCell(dCnt++);
		cell.setCellValue("추가안내사항3");
		cell.setCellStyle(style0);


		/**
		 * 2번째줄 헤더
		 */
		row = worksheet.createRow(1);

		cell = row.createCell(0);
		cell.setCellValue("청구월*");
		cell.setCellStyle(style0);

		cell = row.createCell(1);
		cell.setCellValue("고객명*");
		cell.setCellStyle(style0);

		if (cusGbCnt == 1) {
			for (int i = 0; i < cusGbCnt; i++) {
				cell = row.createCell(2 + i);
				cell.setCellValue(cdList.get(i).getCodeName());
				cell.setCellStyle(style0);
			}
		}
		if (cusGbCnt == 2) {
			for (int i = 0; i < cusGbCnt; i++) {
				cell = row.createCell(2 + i);
				cell.setCellValue(cdList.get(i).getCodeName());
				cell.setCellStyle(style0);
			}
		}
		if (cusGbCnt == 3) {
			for (int i = 0; i < cusGbCnt; i++) {
				cell = row.createCell(2 + i);
				cell.setCellValue(cdList.get(i).getCodeName());
				cell.setCellStyle(style0);
			}
		}
		if (cusGbCnt == 4) {
			for (int i = 0; i < cusGbCnt; i++) {
				cell = row.createCell(2 + i);
				cell.setCellValue(cdList.get(i).getCodeName());
				cell.setCellStyle(style0);
			}
		}

		cell = row.createCell(2 + cusGbCnt);
		cell.setCellValue("가상계좌*");
		cell.setCellStyle(style0);

		cell = row.createCell(3 + cusGbCnt);
		cell.setCellValue("납부시작일*");
		cell.setCellStyle(style0);

		cell = row.createCell(4 + cusGbCnt);
		cell.setCellValue("납부마감일*");
		cell.setCellStyle(style0);

		cell = row.createCell(5 + cusGbCnt);
		cell.setCellValue("고지서용표시마감일");
		cell.setCellStyle(style0);

		int eCnt = 6 + cusGbCnt;
		for (int i = 0; i < iListCnt; i++) {
			cell = row.createCell(eCnt++);
			cell.setCellValue(iList.get(i).getPayItemName());
			cell.setCellStyle(style0);

			cell = row.createCell(eCnt++);
			cell.setCellValue(iList.get(i).getPayItemName() + " 비고 (25자 이내)");
			cell.setCellStyle(style0);
		}

		int fCnt = 6 + cusGbCnt + (iListCnt * 2);
		cell = row.createCell(fCnt++);
		cell.setCellValue("(60자 이내)");
		cell.setCellStyle(style0);

		cell = row.createCell(fCnt++);
		cell.setCellValue("(60자 이내)");
		cell.setCellStyle(style0);

		cell = row.createCell(fCnt++);
		cell.setCellValue("(60자 이내)");
		cell.setCellStyle(style0);


		/**
		 * 엑셀 데이터
		 */
		int rowIndex = 2;
		for (ClaimDTO dto : cList) {
			final Row dataRow = worksheet.createRow(rowIndex);
			if (rowIndex == 0) {
				dataRow.setHeight((short) 500);
			}

			cell = dataRow.createCell(0);
			cell.setCellValue(dto.getMasMonth());
			cell.setCellStyle(styleEmptyRow);

			cell = dataRow.createCell(1);
			cell.setCellValue(dto.getCusName());
			cell.setCellStyle(styleEmptyRow);

			if (cusGbCnt == 1) {
				cell = dataRow.createCell(2);
				cell.setCellValue(dto.getCusGubn1());
				cell.setCellStyle(styleEmptyRow);
			}
			if (cusGbCnt == 2) {
				cell = dataRow.createCell(2);
				cell.setCellValue(dto.getCusGubn1());
				cell.setCellStyle(styleEmptyRow);

				cell = dataRow.createCell(3);
				cell.setCellValue(dto.getCusGubn2());
				cell.setCellStyle(styleEmptyRow);
			}
			if (cusGbCnt == 3) {
				cell = dataRow.createCell(2);
				cell.setCellValue(dto.getCusGubn1());
				cell.setCellStyle(styleEmptyRow);

				cell = dataRow.createCell(3);
				cell.setCellValue(dto.getCusGubn2());
				cell.setCellStyle(styleEmptyRow);

				cell = dataRow.createCell(4);
				cell.setCellValue(dto.getCusGubn3());
				cell.setCellStyle(styleEmptyRow);
			}
			if (cusGbCnt == 4) {
				cell = dataRow.createCell(2);
				cell.setCellValue(dto.getCusGubn1());
				cell.setCellStyle(styleEmptyRow);

				cell = dataRow.createCell(3);
				cell.setCellValue(dto.getCusGubn2());
				cell.setCellStyle(styleEmptyRow);

				cell = dataRow.createCell(4);
				cell.setCellValue(dto.getCusGubn3());
				cell.setCellStyle(styleEmptyRow);

				cell = dataRow.createCell(5);
				cell.setCellValue(dto.getCusGubn4());
				cell.setCellStyle(styleEmptyRow);
			}

			cell = dataRow.createCell(2 + cusGbCnt);
			cell.setCellValue(dto.getVano());
			cell.setCellStyle(styleEmptyRow);

			cell = dataRow.createCell(3 + cusGbCnt);
			cell.setCellValue(dto.getStartDate());
			cell.setCellStyle(styleEmptyRow);

			cell = dataRow.createCell(4 + cusGbCnt);
			cell.setCellValue(dto.getEndDate());
			cell.setCellStyle(styleEmptyRow);

			cell = dataRow.createCell(5 + cusGbCnt);
			cell.setCellValue(dto.getPrintDate());
			cell.setCellStyle(styleEmptyRow);

			cell = dataRow.createCell(6 + cusGbCnt);
			cell.setCellValue(dto.getRemark());
			cell.setCellStyle(styleEmptyRow);

			dataRow.setRowStyle(styleEmptyRow);
			rowIndex++;
		}

		try {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
