package com.finger.shinhandamoa.sys.addServiceMgmt.web;

import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimDTO;
import com.finger.shinhandamoa.org.claimMgmt.dto.ClaimItemDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import com.finger.shinhandamoa.common.AbstractExcelView2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelSaveTransClaim extends AbstractExcelView2 {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
        String chaCd = (String)model.get("chaCd");
        String excelName = chaCd + "_" + sCurTime + "_청구대상목록.xlsx";
        Sheet worksheet = null;
        Row TophRow = null;
        Row hRow = null;
        Row row = null;

        List<Map<String, Object>> listExcel = (List<Map<String, Object>>) model.get("list");

        List<ClaimDTO> iList = (List<ClaimDTO>) model.get("iList");
        List<CodeDTO> cdList = (List<CodeDTO>) model.get("cusGbList");
        for (int i = 0 ; i<cdList.size();i++){
            cdList.get(i).setCodeName(cdList.get(i).getCodeName());
        }

        String flag = (String) model.get("view");

        int cusGbCnt = (Integer) model.get("cusGbCnt");
        int iListCnt = iList.size();

        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");

        int idx = cusGbCnt + 8; // 컬럼수

        // 컬럼 사이즈 설정
        int columnIndex = 0;
        worksheet.setColumnWidth(0, 4000);
        worksheet.setColumnWidth(1, 4000);

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

        worksheet.setColumnWidth(2 + cusGbCnt, 7000);
        worksheet.setColumnWidth(3 + cusGbCnt, 5000);
        worksheet.setColumnWidth(4 + cusGbCnt, 5000);
        worksheet.setColumnWidth(5 + cusGbCnt, 5000);

        int cCnt = 6 + cusGbCnt;
        for (int i = 0; i < iListCnt; i++) {
            worksheet.setColumnWidth(cCnt++, 7000);
            worksheet.setColumnWidth(cCnt++, 7000);
        }

        int hCnt = 6 + cusGbCnt + (iListCnt * 2);

        worksheet.setColumnWidth(hCnt++, 5000);
        worksheet.setColumnWidth(hCnt++, 5000);
        worksheet.setColumnWidth(hCnt++, 5000);

        hRow = worksheet.createRow(0);
        hRow.createCell(0).setCellValue("month");

        hRow.createCell(1).setCellValue("cusName");

        if (cusGbCnt == 1) {
            for (int i = 0; i < cusGbCnt; i++) {
                hRow.createCell(2 + i).setCellValue("cusgubn" + i);
            }
        }
        if (cusGbCnt == 2) {
            for (int i = 0; i < cusGbCnt; i++) {
                hRow.createCell(2 + i).setCellValue("cusgubn" + i);
            }
        }
        if (cusGbCnt == 3) {
            for (int i = 0; i < cusGbCnt; i++) {
                hRow.createCell(2 + i).setCellValue("cusgubn" + i);
            }
        }
        if (cusGbCnt == 4) {
            for (int i = 0; i < cusGbCnt; i++) {
                hRow.createCell(2 + i).setCellValue("cusgubn" + i);
            }
        }

        hRow.createCell(2 + cusGbCnt).setCellValue("vano");
        hRow.createCell(3 + cusGbCnt).setCellValue("startdate");
        hRow.createCell(4 + cusGbCnt).setCellValue("enddate");
        hRow.createCell(5 + cusGbCnt).setCellValue("printdate");

        cCnt = 6 + cusGbCnt;
        for (int i = 0; i < iListCnt; i++) {
            hRow.createCell(cCnt++).setCellValue(iList.get(i).getPayItemCd());
            hRow.createCell(cCnt++).setCellValue(iList.get(i).getPayItemCd());
        }

        int hHCnt = 6 + cusGbCnt + (iListCnt * 2);

        hRow.createCell(hHCnt++).setCellValue("추가안내사항1");
        hRow.createCell(hHCnt++).setCellValue("추가안내사항2");
        hRow.createCell(hHCnt++).setCellValue("추가안내사항3");


        TophRow = worksheet.createRow(1);
        TophRow.createCell(0).setCellValue("청구월");
        TophRow.createCell(1).setCellValue("고객명");

        if (cusGbCnt == 1) {
            for (int i = 0; i < cusGbCnt; i++) {
                TophRow.createCell(2 + i).setCellValue(cdList.get(i).getCodeName());
            }
        }
        if (cusGbCnt == 2) {
            for (int i = 0; i < cusGbCnt; i++) {
                TophRow.createCell(2 + i).setCellValue(cdList.get(i).getCodeName());
            }
        }
        if (cusGbCnt == 3) {
            for (int i = 0; i < cusGbCnt; i++) {
                TophRow.createCell(2 + i).setCellValue(cdList.get(i).getCodeName());
            }
        }
        if (cusGbCnt == 4) {
            for (int i = 0; i < cusGbCnt; i++) {
                TophRow.createCell(2 + i).setCellValue(cdList.get(i).getCodeName());
            }
        }

        TophRow.createCell(2 + cusGbCnt).setCellValue("가상계좌");
        TophRow.createCell(3 + cusGbCnt).setCellValue("납부시작일");
        TophRow.createCell(4 + cusGbCnt).setCellValue("납부마감일");
        TophRow.createCell(5 + cusGbCnt).setCellValue("고지서용표시마감일");

        cCnt = 6 + cusGbCnt;
        for (int i = 0; i < iListCnt; i++) {
            TophRow.createCell(cCnt++).setCellValue(iList.get(i).getPayItemName());
            TophRow.createCell(cCnt++).setCellValue(iList.get(i).getPayItemName() + "비고 (25자 이내)");
        }

        int ThHCnt = 6 + cusGbCnt + (iListCnt * 2);

        TophRow.createCell(ThHCnt++).setCellValue("(60자 이내)");
        TophRow.createCell(ThHCnt++).setCellValue("(60자 이내)");
        TophRow.createCell(ThHCnt++).setCellValue("(60자 이내)");

        int rowIndex = 2;
        for (Map<String, Object> dto : listExcel) {
            ClaimDTO cdto = (ClaimDTO) dto.get("cdto");
            int i = 0;

            row = worksheet.createRow(rowIndex);
        if (cdto != null) {
            row.createCell(0).setCellValue(dto.get("masMonth").toString());
            row.createCell(1).setCellValue(dto.get("cusName").toString());

            if (cusGbCnt == 1) {
                row.createCell(2).setCellValue(cdto.getCusGubn1());
            }
            if (cusGbCnt == 2) {
                row.createCell(2).setCellValue(cdto.getCusGubn1());
                row.createCell(3).setCellValue(cdto.getCusGubn2());
            }
            if (cusGbCnt == 3) {
                row.createCell(2).setCellValue(cdto.getCusGubn1());
                row.createCell(3).setCellValue(cdto.getCusGubn2());
                row.createCell(4).setCellValue(cdto.getCusGubn3());
            }
            if (cusGbCnt == 4) {
                row.createCell(2).setCellValue(cdto.getCusGubn1());
                row.createCell(3).setCellValue(cdto.getCusGubn2());
                row.createCell(4).setCellValue(cdto.getCusGubn3());
                row.createCell(5).setCellValue(cdto.getCusGubn4());
            }

            row.createCell(2 + cusGbCnt).setCellValue(cdto.getVano());
            row.createCell(3 + cusGbCnt).setCellValue(dto.get("startDt").toString());
            row.createCell(4 + cusGbCnt).setCellValue(dto.get("endDt").toString());
            row.createCell(5 + cusGbCnt).setCellValue(dto.get("prtDt").toString());

            int dCnt = 6 + cusGbCnt;
            for (int j = 0; j < iListCnt; j++) {
                if (iList.get(j).getPayItemCd().equals(dto.get("prtItemCd").toString())) {
                    row.createCell(dCnt + (j * 2), CellType.NUMERIC).setCellValue(Long.parseLong(dto.get("payItemAmt").toString()));
                    row.createCell(dCnt + (j * 2) + 1).setCellValue(dto.get("prtItemRemark").toString());
                }
            }

            int hHhCnt = 6 + cusGbCnt + (iListCnt * 2);

            String remark = (String) dto.get("remark");
            String[] remarkArr;
            if (remark == null || remark.equals("") || remark.isEmpty()) {
                row.createCell(hHhCnt++).setCellValue("");
                row.createCell(hHhCnt++).setCellValue("");
                row.createCell(hHhCnt++).setCellValue("");
            } else {
                remark = remark.trim();
                remarkArr = remark.split("\\n");

                if (remarkArr.length == 1) {
                    row.createCell(hHhCnt++).setCellValue(remarkArr[0]);
                    row.createCell(hHhCnt++).setCellValue("");
                    row.createCell(hHhCnt++).setCellValue("");
                }

                if (remarkArr.length == 2) {
                    row.createCell(hHhCnt++).setCellValue(remarkArr[0]);
                    row.createCell(hHhCnt++).setCellValue(remarkArr[1]);
                    row.createCell(hHhCnt++).setCellValue("");
                }

                if (remarkArr.length == 3) {
                    row.createCell(hHhCnt++).setCellValue(remarkArr[0]);
                    row.createCell(hHhCnt++).setCellValue(remarkArr[1]);
                    row.createCell(hHhCnt++).setCellValue(remarkArr[2]);
                }
            }
        }else{
            row.createCell(0).setCellValue(dto.get("masMonth").toString());
            row.createCell(1).setCellValue(dto.get("cusName").toString());

            if (cusGbCnt == 1) {
                row.createCell(2).setCellValue("");
            }
            if (cusGbCnt == 2) {
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
            }
            if (cusGbCnt == 3) {
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
                row.createCell(4).setCellValue("");
            }
            if (cusGbCnt == 4) {
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
            }

            row.createCell(2 + cusGbCnt).setCellValue("");
            row.createCell(3 + cusGbCnt).setCellValue(dto.get("startDt").toString());
            row.createCell(4 + cusGbCnt).setCellValue(dto.get("endDt").toString());
            row.createCell(5 + cusGbCnt).setCellValue(dto.get("prtDt").toString());

            int dCnt = 6 + cusGbCnt;
            for (int j = 0; j < iListCnt; j++) {
                if (iList.get(j).getPayItemCd().equals(dto.get("prtItemCd").toString())) {
                    row.createCell(dCnt + (j * 2), CellType.NUMERIC).setCellValue(Long.parseLong(dto.get("payItemAmt").toString()));
                    row.createCell(dCnt + (j * 2) + 1).setCellValue(dto.get("prtItemRemark").toString());
                }
            }

            int hHhCnt = 6 + cusGbCnt + (iListCnt * 2);

            String remark = (String) dto.get("remark");
            String[] remarkArr;
            if (remark == null || remark.equals("") || remark.isEmpty()) {
                row.createCell(hHhCnt++).setCellValue("");
                row.createCell(hHhCnt++).setCellValue("");
                row.createCell(hHhCnt++).setCellValue("");
            } else {
                remark = remark.trim();
                remarkArr = remark.split("\\n");

                if (remarkArr.length == 1) {
                    row.createCell(hHhCnt++).setCellValue(remarkArr[0]);
                    row.createCell(hHhCnt++).setCellValue("");
                    row.createCell(hHhCnt++).setCellValue("");
                }

                if (remarkArr.length == 2) {
                    row.createCell(hHhCnt++).setCellValue(remarkArr[0]);
                    row.createCell(hHhCnt++).setCellValue(remarkArr[1]);
                    row.createCell(hHhCnt++).setCellValue("");
                }

                if (remarkArr.length == 3) {
                    row.createCell(hHhCnt++).setCellValue(remarkArr[0]);
                    row.createCell(hHhCnt++).setCellValue(remarkArr[1]);
                    row.createCell(hHhCnt++).setCellValue(remarkArr[2]);
                }
            }
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
