package com.finger.shinhandamoa.org.receiptmgmt.web;

import com.finger.shinhandamoa.common.CashUtil;
import com.finger.shinhandamoa.common.StrUtil;
import com.finger.shinhandamoa.org.receiptmgmt.dto.PayMgmtDTO;
import com.finger.shinhandamoa.util.dto.CodeDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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

/**
 * 입금내역 다운로드 뷰
 *
 * @author wisehouse@finger.co.kr
 */
@Deprecated
public class ExcelSavePaymentReg extends AbstractExcelView2 {

    /*
     *  엑셀 다운 입금내역
     *  신재현
     */
    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        final String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        final String excelName = "입금내역_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;

        List<PayMgmtDTO> listExcel = (List<PayMgmtDTO>)model.get("list");		// 목록
        for (int i = 0 ; i<listExcel.size();i++){
            listExcel.get(i).setCusName(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusName()));
            listExcel.get(i).setCusGubn1(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn1()));
            listExcel.get(i).setCusGubn2(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn2()));
            listExcel.get(i).setCusGubn3(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn3()));
            listExcel.get(i).setCusGubn4(StringEscapeUtils.unescapeHtml4(listExcel.get(i).getCusGubn4()));
        }
        List<CodeDTO> cdList = (List<CodeDTO>)model.get("cusGbList");			// 구분코드
        for (int i = 0 ; i<cdList.size();i++){
            cdList.get(i).setCodeName(StringEscapeUtils.unescapeHtml4(cdList.get(i).getCodeName()));
        }
        int cdListCnt = cdList.size();

        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");

        //int idx = (Integer) model.get("cusGbCnt") + 5; // 컬럼수

        // 컬럼 사이즈 설정
        int columnIndex = 0;

        worksheet.setColumnWidth(0, 2000);
        worksheet.setColumnWidth(1, 4000);
        worksheet.setColumnWidth(2, 6000);
        worksheet.setColumnWidth(3, 4000);
        worksheet.setColumnWidth(4, 7000);

        int temp1 = 0;
        for(CodeDTO code : cdList) {
            String col = code.getCode();
            switch (col) {
                case "CUSGUBN1":
                    worksheet.setColumnWidth(5 + temp1, 5000);
                    temp1++;
                    break;
                case "CUSGUBN2":
                    worksheet.setColumnWidth(5 + temp1, 5000);
                    temp1++;
                    break;
                case "CUSGUBN3":
                    worksheet.setColumnWidth(5 + temp1, 5000);
                    temp1++;
                    break;
                case "CUSGUBN4":
                    worksheet.setColumnWidth(5 + temp1, 5000);
                    temp1++;
                    break;
                default:
                    break;
            }
        }
        worksheet.setColumnWidth(5 + cdListCnt, 7000);
        worksheet.setColumnWidth(6 + cdListCnt, 7000);
        worksheet.setColumnWidth(7 + cdListCnt, 7000);

        hRow = worksheet.createRow(0);
        hRow.setHeight((short)500);

        hRow.createCell(0).setCellValue("NO");
        hRow.createCell(1).setCellValue("청구월");
        hRow.createCell(2).setCellValue("입금일시");
        hRow.createCell(3).setCellValue("고객명");
        hRow.createCell(4).setCellValue("가상계좌");

        int temp2 = 0;
        for(CodeDTO code : cdList) {
            String col = code.getCode();
            switch (col) {
                case "CUSGUBN1":
                    hRow.createCell(5 + temp2).setCellValue(code.getCodeName());
                    temp2++;
                    break;
                case "CUSGUBN2":
                    hRow.createCell(5 + temp2).setCellValue(code.getCodeName());
                    temp2++;
                    break;
                case "CUSGUBN3":
                    hRow.createCell(5 + temp2).setCellValue(code.getCodeName());
                    temp2++;
                    break;
                case "CUSGUBN4":
                    hRow.createCell(5 + temp2).setCellValue(code.getCodeName());
                    temp2++;
                    break;
                default:
                    break;
            }
        }
        hRow.createCell(5 + cdListCnt).setCellValue("입금액");
        hRow.createCell(6 + cdListCnt).setCellValue("입금자명");
        hRow.createCell(7 + cdListCnt).setCellValue("입금은행");

        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        for(int rowIndex = 1; rowIndex <= listExcel.size(); rowIndex++) {
            final PayMgmtDTO dto = listExcel.get(rowIndex - 1);
            final Row row = worksheet.createRow(rowIndex);

            Date date = yyyyMMddHHmmss.parse(dto.getPayDay()+dto.getPayTime());
            String dateString = simpleDateFormat.format(date);

            row.createCell(0).setCellValue(rowIndex);
            row.createCell(1).setCellValue(dto.getMasMonth());
            row.createCell(2).setCellValue(dateString);
            row.createCell(3).setCellValue(dto.getCusName());
            row.createCell(4).setCellValue(dto.getVaNo().substring(0,3) + "-" + dto.getVaNo().substring(3,6) + "-" + dto.getVaNo().substring(6));

            int temp3= 0;
            for (CodeDTO code : cdList) {
                String col = code.getCode();
                switch (col) {
                    case "CUSGUBN1":
                        row.createCell(5 +temp3).setCellValue(dto.getCusGubn1());
                        temp3++;
                        break;
                    case "CUSGUBN2":
                        row.createCell(5 +temp3).setCellValue(dto.getCusGubn2());
                        temp3++;
                        break;
                    case "CUSGUBN3":
                        row.createCell(5 +temp3).setCellValue(dto.getCusGubn3());
                        temp3++;
                        break;
                    case "CUSGUBN4":
                        row.createCell(5 +temp3).setCellValue(dto.getCusGubn4());
                        temp3++;
                        break;
                    default:
                        break;
                }
            }
            row.createCell(5 + cdListCnt, CellType.NUMERIC).setCellValue(Long.parseLong(dto.getRcpAmt()));
            row.createCell(6 + cdListCnt).setCellValue(dto.getRcpUserName());
            if(dto.getBnkCd() != null && !dto.getBnkCd().equals(" ")) {
                row.createCell(7 + cdListCnt).setCellValue(dto.getBnkCd());
            }else {
                row.createCell(7 + cdListCnt).setCellValue(dto.getFicd());
            }
        }

        try {
            response.setHeader("Content-Disposition", "attachement; filename=\"" + java.net.URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
        }catch (UnsupportedEncodingException e) {

        }

    }

}

