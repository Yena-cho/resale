package com.ghvirtualaccount.cmmn;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsxUserParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(XlsxUserParser.class);
    
    /**
     * 
     * @param inputStream
     * @param startRow 데이터가 시작되는 줄 첫번째 라인이 0
     * @return
     */
    public static List<List<Object>> parse(InputStream inputStream, int startRow, int columnCount) {
        final List<List<Object>> itemList = new ArrayList<>();

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return itemList;
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            return itemList;
        }
        
        if(!(workbook instanceof XSSFWorkbook)) {
            return itemList;
        }
        
        final Sheet sheet = workbook.getSheetAt(0);

        //to evaluate cell type
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        for(int rowNo=startRow;; rowNo++) {
            final List<Object> item = new ArrayList<>();
            final Row row = sheet.getRow(rowNo);
            if(row == null) {
                break;
            }

            boolean dataExists = false;
            for(int columnNo=0; columnNo<columnCount; columnNo++) {
                final Cell cell = row.getCell(columnNo);
                LOGGER.trace("cell: {}", cell);

                Object value = null;
                if(cell == null) {
                    value = StringUtils.EMPTY;
                } else {
                    switch (cell.getCellTypeEnum()) {
                        case STRING:
                            value = cell.getRichStringCellValue().getString();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
                            } else {
                                value = String.valueOf((int)cell.getNumericCellValue());
                            	//value = cell.getStringCellValue();
                            }
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
//                        case FORMULA:
//                            value = cell.getCellFormula();
//                            break;
//                        case BLANK:
//                            value = StringUtils.EMPTY;
//                            break;
                        default:
                            value = StringUtils.EMPTY;
                    }
                }
                item.add(value);
                
                dataExists |= StringUtils.isNotBlank(String.valueOf(value));
            }
            
            if(!dataExists) {
                break;
            }
            
            itemList.add(item);
        }

        return itemList;
    }
    
}
