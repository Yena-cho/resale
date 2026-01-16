package com.ghvirtualaccount.cmmn;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public class XlsxBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(XlsxBuilder.class);

    private SXSSFWorkbook workbook;
    private SXSSFSheet sheet;
    private SXSSFRow row;
    private int lastRowNo = -1;
    
    private CellStyle headerStyle;
    private CellStyle contentStyle;

    public XlsxBuilder() {
        workbook = new SXSSFWorkbook();
        
        initHeaderStyle(workbook);
        initContentStyle(workbook);
    }

    public void newSheet(String name) {
        sheet = workbook.createSheet(name);
        
        sheet.setDefaultColumnWidth(16);
    }
    
    public void setColumnWidth(int columnNo, int width) {
        sheet.setColumnWidth(columnNo, width);
    }

    public void addHeader(final int startRowNo, final int endRowNo, final int startColumnNo, final int endColumnNo, final String value) {
        if(startRowNo != endRowNo || startColumnNo != endColumnNo) {
            final CellRangeAddress cellRangeAddress = new CellRangeAddress(startRowNo, endRowNo, startColumnNo, endColumnNo);

            RegionUtil.setBorderTop(headerStyle.getBorderTop(), cellRangeAddress, sheet);
            RegionUtil.setBorderLeft(headerStyle.getBorderLeft(), cellRangeAddress, sheet);
            RegionUtil.setBorderRight(headerStyle.getBorderRight(), cellRangeAddress, sheet);
            RegionUtil.setBorderBottom(headerStyle.getBorderBottom(), cellRangeAddress, sheet);
            
            sheet.addMergedRegion(cellRangeAddress);
        }
        
        final Cell cell = getCell(startRowNo, startColumnNo);
        cell.setCellValue(value);
        cell.setCellStyle(headerStyle);

        lastRowNo = Math.max(lastRowNo, endRowNo);
    }
    
    public void setColumnAlign(int rowNo, int columnNo, HorizontalAlignment alignment) {
    	getCell(rowNo, columnNo).getCellStyle().setAlignment(alignment);		
    }
    
    private Cell getCell(int rowNo, int columnNo) {
        SXSSFRow row = sheet.getRow(rowNo);
        if (row == null) {
            row = sheet.createRow(rowNo);
        }

        SXSSFCell cell = row.getCell(columnNo);
        if(cell == null) {
            cell = row.createCell(columnNo);
        }
        
        return cell;
    }

    private void initHeaderStyle(Workbook wb) {
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();

        Font font = wb.createFont();
        font.setBold(true);

        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);

        headerStyle = style;
    }

    private void initContentStyle(Workbook wb) {
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();

        Font font = wb.createFont();
//        font.setBold(true);

        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
//        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font);
//        style.setDataFormat((short)0x31);

        contentStyle = style;
    }

    public void addHeader(final int rowNo, final int columnNo, final String value) {
        this.addHeader(rowNo, rowNo, columnNo, columnNo, value);
    }


    public void newDataRow() {
        lastRowNo++;
        row = sheet.createRow(lastRowNo);
    }

    public void addData(final int columnNo, final String value) {
        if (row == null) {
            newDataRow();
        }

        final SXSSFCell cell = row.createCell(columnNo);
        cell.setCellStyle(contentStyle);
        if(value!=null){
        	cell.setCellValue(value.trim());
        }else{
        	cell.setCellValue(value);
        }
    }

    public void addData(final int columnNo, final long value) {
        if (row == null) {
            newDataRow();
        }


        final SXSSFCell cell = row.createCell(columnNo);
        cell.setCellStyle(contentStyle);
        cell.setCellValue(value);
    }

    public void writeTo(final OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
    }
    
    public SXSSFWorkbook getWorkBook() {
        return workbook;
    }
}
