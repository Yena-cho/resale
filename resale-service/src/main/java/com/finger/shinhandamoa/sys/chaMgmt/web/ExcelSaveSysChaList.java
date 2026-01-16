package com.finger.shinhandamoa.sys.chaMgmt.web;

import com.finger.shinhandamoa.common.AbstractExcelView2;
import com.finger.shinhandamoa.sys.chaMgmt.dto.SysChaMgmtDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExcelSaveSysChaList extends AbstractExcelView2 {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        String sCurTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        String excelName = "이용기관조회" + "_" + sCurTime + ".xlsx";
        Sheet worksheet = null;
        Row hRow = null;
        Row row = null;

        List<SysChaMgmtDTO> list = (List<SysChaMgmtDTO>) model.get("list");

        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("sheet1");

        int idx = 33; // 컬럼수

        // 컬럼 사이즈 설정
        int columnIndex = 0;

        while (columnIndex < idx) {
            if (columnIndex == 0) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 1) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 2) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 3) {
                worksheet.setColumnWidth(columnIndex, 3000);
            } else if (columnIndex == 4) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 5) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 6) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 7) {
                worksheet.setColumnWidth(columnIndex, 4000);
            } else if (columnIndex == 8) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 9) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 10) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 11) {
                worksheet.setColumnWidth(columnIndex, 8000);
            } else if (columnIndex == 12) {
                worksheet.setColumnWidth(columnIndex, 10000);
            } else if (columnIndex == 13) {
                worksheet.setColumnWidth(columnIndex, 3000);
            } else if (columnIndex == 14) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 15) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 16) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else if (columnIndex == 17) {
                worksheet.setColumnWidth(columnIndex, 5000);
            } else {
                worksheet.setColumnWidth(columnIndex, 4000);
            }

            columnIndex++;
        }

        hRow = worksheet.createRow(0);
        hRow.setHeight((short) 500);
        for (int i = 0; i < idx; i++) {
            hRow.createCell(i++).setCellValue("은행코드");
            hRow.createCell(i++).setCellValue("기관코드");
            hRow.createCell(i++).setCellValue("기관명");
            hRow.createCell(i++).setCellValue("방식");
            hRow.createCell(i++).setCellValue("사업자번호");
            hRow.createCell(i++).setCellValue("등록일자");
            hRow.createCell(i++).setCellValue("사용목적");
            hRow.createCell(i++).setCellValue("대표자명");
            hRow.createCell(i++).setCellValue("대표전화번호");
            hRow.createCell(i++).setCellValue("담당자명");
            hRow.createCell(i++).setCellValue("담당자전화번호");
            hRow.createCell(i++).setCellValue("이메일");
            hRow.createCell(i++).setCellValue("주소");
            hRow.createCell(i++).setCellValue("상태");
            hRow.createCell(i++).setCellValue("업태");
            hRow.createCell(i++).setCellValue("업종");
            hRow.createCell(i++).setCellValue("은행명");
            hRow.createCell(i++).setCellValue("출금계좌번호");
            hRow.createCell(i++).setCellValue("선취여부");
            hRow.createCell(i++).setCellValue("다계좌사용여부");
            hRow.createCell(i++).setCellValue("부분납사용여부");
            hRow.createCell(i++).setCellValue("현금영수증발행여부");
            hRow.createCell(i++).setCellValue("현금영수증자동발행");
            hRow.createCell(i++).setCellValue("수기수납현금영수증사용여부");
            hRow.createCell(i++).setCellValue("수기수납현금영수증자동발행");
            hRow.createCell(i++).setCellValue("의무발행업체여부");
            hRow.createCell(i++).setCellValue("과세대상업체여부");
            hRow.createCell(i++).setCellValue("계좌1 은행");
            hRow.createCell(i++).setCellValue("계좌1 계좌번호");
            hRow.createCell(i++).setCellValue("계좌2 은행");
            hRow.createCell(i++).setCellValue("계좌2 계좌번호");
            hRow.createCell(i++).setCellValue("계좌3 은행");
            hRow.createCell(i++).setCellValue("계좌3 계좌번호");
        }

        int rowIndex = 1;
        for (SysChaMgmtDTO dto : list) {
            int i = 0;

            row = worksheet.createRow(rowIndex);

            row.createCell(i++).setCellValue(dto.getFgCd());                                                    // 기관코드
            row.createCell(i++).setCellValue(dto.getChaCd());                                                   // 기관코드
            row.createCell(i++).setCellValue(dto.getChaName());                                                 // 기관명
            if ("Y".equals(dto.getAmtChkTy())) {                                                                // 기관방식
                row.createCell(i++).setCellValue("승인");
            } else {
                row.createCell(i++).setCellValue("통지");
            }
            row.createCell(i++).setCellValue(dto.getChaOffNo());                                                // 사업자번호
            row.createCell(i++).setCellValue(dto.getRegDt());                                                   // 등록일자
            row.createCell(i++).setCellValue(dto.getUsePurpose());                                              // 대표자
            row.createCell(i++).setCellValue(dto.getOwner());                                                   // 대표자
            row.createCell(i++).setCellValue(dto.getOwnerTel());                                                // 대표전화번호
            row.createCell(i++).setCellValue(dto.getChrName());                                                 // 담당자
            row.createCell(i++).setCellValue(dto.getChrTelNo());                                                // 담당자전화번호
            row.createCell(i++).setCellValue(dto.getChrMail());                                                 // 담당자이메일
            row.createCell(i++).setCellValue("(" + dto.getChaZipCode() + ")" + dto.getChaAddress1() + " " + dto.getChaAddress2());          // 주소
            row.createCell(i++).setCellValue(getChastNameDamoa(dto.getChast()));                                // 상태
            row.createCell(i++).setCellValue(dto.getChaStatus());                                               // 업태
            row.createCell(i++).setCellValue(dto.getChaType());                                                 // 업종
            row.createCell(i++).setCellValue(dto.getFingerFeeBankValue());                                      // 출금은행
            row.createCell(i++).setCellValue(dto.getFingerFeeAccountNo());                                      // 출금계좌
            if ("PRE".equals(dto.getFingerFeePayty())) {                                                        // 선취여부
                row.createCell(i++).setCellValue("선취");
            } else if ("CUR".equals(dto.getFingerFeePayty())) {
                row.createCell(i++).setCellValue("후취");
            } else {
                row.createCell(i++).setCellValue("자동이체");
            }

            row.createCell(i++).setCellValue("Y".equals(dto.getAdjaccyn()) ? "사용" : "미사용");                 // 다계좌사용여부
            row.createCell(i++).setCellValue("Y".equals(dto.getPartialPayment()) ? "사용" : "미사용");           // 부분납사용여부
            row.createCell(i++).setCellValue("Y".equals(dto.getRcpReqYn()) ? "사용" : "미사용");                 // 현금영수증발행여부
            row.createCell(i++).setCellValue("A".equals(dto.getRcpReqTy()) ? "사용" : "미사용");                 // 현금영수증자동발행
            row.createCell(i++).setCellValue("01".equals(dto.getRcpReqSveTy()) ? "사용" : "미사용");              // 수기수납현금영수증사용여부
            row.createCell(i++).setCellValue("A".equals(dto.getMnlRcpReqTy()) ? "사용" : "미사용");              // 수기수납현금영수증자동발행
            row.createCell(i++).setCellValue("Y".equals(dto.getMandRcpYn()) ? "사용" : "미사용");                // 의무발행업체여부
            row.createCell(i++).setCellValue("Y".equals(dto.getNoTaxYn()) ? "사용" : "미사용");                  // 과세대상업체여부

            if (StringUtils.isNotEmpty(dto.getAccStr())) {
                String[] arr = dto.getAccStr().split(",");
                for (String acc : arr) {
                    String[] accInfo = acc.split("/");
                    row.createCell(i++).setCellValue(accInfo[0]);
                    row.createCell(i++).setCellValue(accInfo[1]);
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

    private String getChastNameDamoa(String chast) {
        String retVal = "";
        if ("ST01".equals(chast)) {
            retVal = "이용대기[다모아]";
        } else if ("ST06".equals(chast)) {
            // retVal = "정상[다모아]";
            retVal = "정상";
        } else if ("ST07".equals(chast)) {
            retVal = "거부[다모아]";
        } else if ("ST08".equals(chast)) {
            // retVal = "정지[다모아]";
            retVal = "정지";
        } else if ("ST02".equals(chast)) {
            // retVal = "해지[은행]";
            retVal = "해지";
        } else if ("ST03".equals(chast)) {
            retVal = "승인거부[은행]";
        } else if ("ST04".equals(chast)) {
            // retVal = "정지[은행]";
            retVal = "정지";
        } else if ("ST05".equals(chast)) {
            retVal = "승인대기[은행]";
        }
        return retVal;
    }
}
