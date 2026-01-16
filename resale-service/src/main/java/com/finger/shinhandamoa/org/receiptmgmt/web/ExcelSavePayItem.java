package com.finger.shinhandamoa.org.receiptmgmt.web;

import com.finger.shinhandamoa.org.receiptmgmt.dto.PayMgmtDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
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

@Deprecated
public class ExcelSavePayItem extends AbstractExcelView2 {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chaCd = authentication.getName();

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        String excelName = chaCd + "_" + sCurTime + "_항목별입금내역.xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row = null;

        List<PayMgmtDTO> listExcel = (List<PayMgmtDTO>) model.get("list");
        List<CodeDTO> cusGbList = (List<CodeDTO>) model.get("cusGbList");
        for (int i = 0 ; i<listExcel.size();i++){
            listExcel.get(i).setCusName(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusName()));
            listExcel.get(i).setCusGubn1(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn1()));
            listExcel.get(i).setCusGubn2(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn2()));
            listExcel.get(i).setCusGubn3(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn3()));
            listExcel.get(i).setCusGubn4(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn4()));
        }
        for (int i = 0 ; i<cusGbList.size();i++){
            cusGbList.get(i).setCodeName(StringEscapeUtils.unescapeHtml4(cusGbList.get(i).getCodeName()));
        }
        int cusGbCnt = cusGbList.size();

        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");

        int idx = 11 + cusGbCnt;

        worksheet.setColumnWidth(0, 4000);
        worksheet.setColumnWidth(1, 8000);
        worksheet.setColumnWidth(2, 4000);
        worksheet.setColumnWidth(3, 6000);
        worksheet.setColumnWidth(4, 8000);

        int listCnt = 0;
        for (CodeDTO code : cusGbList) {
            String col = code.getCode();
            switch (col) {
                case "CUSGUBN1":
                    worksheet.setColumnWidth(5 + listCnt, 5000);
                    listCnt++;
                    break;
                case "CUSGUBN2":
                    worksheet.setColumnWidth(5 + listCnt, 5000);
                    listCnt++;
                    break;
                case "CUSGUBN3":
                    worksheet.setColumnWidth(5 + listCnt, 5000);
                    listCnt++;
                    break;
                case "CUSGUBN4":
                    worksheet.setColumnWidth(5 + listCnt, 5000);
                    listCnt++;
                    break;
                default:
                    break;
            }
        }
        worksheet.setColumnWidth(5 + cusGbCnt, 7000);
        worksheet.setColumnWidth(6 + cusGbCnt, 5000);
        worksheet.setColumnWidth(7 + cusGbCnt, 7000);
        worksheet.setColumnWidth(8 + cusGbCnt, 5000);
        worksheet.setColumnWidth(9 + cusGbCnt, 5000);
        if (model.get("adjAccYn").equals("Y")){
            worksheet.setColumnWidth(10 + cusGbCnt, 5000);
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short) 500);
        hRow.createCell(0).setCellValue("청구월");
        hRow.createCell(1).setCellValue("입금일시");
        hRow.createCell(2).setCellValue("고객명");
        hRow.createCell(3).setCellValue("고객번호");
        hRow.createCell(4).setCellValue("가상계좌번호");

        listCnt = 0;
        for (CodeDTO code : cusGbList) {
            String col = code.getCode();
            String colName = code.getCodeName();
            switch (col) {
                case "CUSGUBN1":
                    hRow.createCell(5 + listCnt).setCellValue(cusGbList.get(0 + listCnt).getCodeName());
                    listCnt++;
                    break;
                case "CUSGUBN2":
                    hRow.createCell(5 + listCnt).setCellValue(cusGbList.get(0 + listCnt).getCodeName());
                    listCnt++;
                    break;
                case "CUSGUBN3":
                    hRow.createCell(5 + listCnt).setCellValue(cusGbList.get(0 + listCnt).getCodeName());
                    listCnt++;
                    break;
                case "CUSGUBN4":
                    hRow.createCell(5 + listCnt).setCellValue(cusGbList.get(0 + listCnt).getCodeName());
                    listCnt++;
                    break;
                default:
                    break;
            }
        }
        hRow.createCell(5 + cusGbCnt).setCellValue("청구항목명");
        hRow.createCell(6 + cusGbCnt).setCellValue("청구금액(원)");
        hRow.createCell(7 + cusGbCnt).setCellValue("비고");
        hRow.createCell(8 + cusGbCnt).setCellValue("입금금액(원)");
        hRow.createCell(9 + cusGbCnt).setCellValue("입금수단");
        if (model.get("adjAccYn").equals("Y")){
            hRow.createCell(10 + cusGbCnt).setCellValue("입금통장명");
        }

        int rowIndex = 1;
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        for (PayMgmtDTO dto : listExcel) {
            row = worksheet.createRow(rowIndex);

            Date date = yyyyMMddHHmmss.parse(dto.getPayDay() + dto.getPayTime());
            String dateString = simpleDateFormat.format(date);

            row.createCell(0).setCellValue(dto.getMasMonth());
            row.createCell(1).setCellValue(dateString);
            row.createCell(2).setCellValue(StringEscapeUtils.unescapeHtml4(dto.getCusName()));
            row.createCell(3).setCellValue(dto.getCusKey());
            row.createCell(4).setCellValue(dto.getVaNo());

            listCnt = 0;
            for (CodeDTO code : cusGbList) {
                String col = code.getCode();
                switch (col) {
                    case "CUSGUBN1":
                        row.createCell(5 + listCnt).setCellValue(dto.getCusGubn1());
                        listCnt++;
                        break;
                    case "CUSGUBN2":
                        row.createCell(5 + listCnt).setCellValue(dto.getCusGubn2());
                        listCnt++;
                        break;
                    case "CUSGUBN3":
                        row.createCell(5 + listCnt).setCellValue(dto.getCusGubn3());
                        listCnt++;
                        break;
                    case "CUSGUBN4":
                        row.createCell(5 + listCnt).setCellValue(dto.getCusGubn4());
                        listCnt++;
                        break;
                    default:
                        break;
                }
            }

            row.createCell(5 + cusGbCnt).setCellValue(dto.getPayItemName());
            row.createCell(6 + cusGbCnt, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getNotiAmt()));
            row.createCell(7 + cusGbCnt).setCellValue(dto.getPtrItemRemark());
            row.createCell(8 + cusGbCnt, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getPayAmt()));
            row.createCell(9 + cusGbCnt).setCellValue(dto.getSveCd());
            if (model.get("adjAccYn").equals("Y")){
                row.createCell(10 + cusGbCnt).setCellValue(dto.getGrpadjName());
            }
            rowIndex++;
        }
        try {
            response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
