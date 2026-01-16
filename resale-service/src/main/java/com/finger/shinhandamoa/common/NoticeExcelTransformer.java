package com.finger.shinhandamoa.common;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

/**
 * 엑셀 업로드 구 양식을 새로운 양식으로 변경한다
 * <p>
 * TODO 1. header를 읽어 파일을 구분한다.
 * 2. 구분한 파일을 각각의 엑셀로 생성한다.
 * 3. 구분에 실패한 정보를 별도의 에러엑셀로 생성한다.
 *
 * @author jhjeong@finger.co.kr
 */
public class NoticeExcelTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeExcelTransformer.class);

    private static final DateFormat DATE_FORMAT_FULL_DATE = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateFormat DATE_FORMAT_MONTH = new java.text.SimpleDateFormat("yyyyMM");

    final String[] chkName = {
            "CUSKEY",        /* 고객거래번호[고객KEY] */
            "CUSNAME",       /* 고객명 */
            "CUSGUBN1",      /* 참조1 */
            "CUSGUBN2",      /* 참조2 */
            "CUSGUBN3",      /* 참조3 */
            "CUSGUBN4",      /* 참조4 */
            "CUSHP",         /* 고객핸드폰번호 */
            "CUSMAIL",       /* 고객메일주소 */
            "CUSOFFNO",      /* 현금영수증발행고객정보 */
            "STARTDATE",     /* 납부시작일 */
            "ENDDATE",       /* 납부마감일 */
            "PRINTDATE",     /* 출력용 납부마감일 */
            "VANO",          /* 가상계좌번호 */
            "AMT",           /* 청구금액 */
            "REMARK"         /* 청구비고 */
    };

    private LinkedHashMap<Integer, Column> amtItemMap = new LinkedHashMap<>();

    /**
     * 청구항목 헤더 생성
     *
     * @param sheet
     * @throws RowsExceededException
     * @throws WriteException
     */
    private void makeCellHeaderToNOTIDET(WritableSheet sheet) throws RowsExceededException, WriteException {

        final WritableCellFormat headerFormat0 = new WritableCellFormat();
        headerFormat0.setAlignment(Alignment.CENTRE);
        headerFormat0.setVerticalAlignment(VerticalAlignment.CENTRE);
        headerFormat0.setBackground(Colour.GREY_25_PERCENT);

        final WritableCellFormat headerFormat1 = new WritableCellFormat();
        headerFormat1.setAlignment(Alignment.CENTRE);
        headerFormat1.setVerticalAlignment(VerticalAlignment.CENTRE);
        headerFormat1.setBackground(Colour.ICE_BLUE);

        // 첫행
        sheet.addCell(new Label(0, 0, "month", headerFormat0));
        sheet.addCell(new Label(1, 0, "cusname", headerFormat0));
        sheet.addCell(new Label(2, 0, "vano", headerFormat0));
        sheet.addCell(new Label(3, 0, "startdate", headerFormat0));
        sheet.addCell(new Label(4, 0, "enddate", headerFormat0));
        sheet.addCell(new Label(5, 0, "printdate", headerFormat0));

        // 두번째행
        sheet.addCell(new Label(0, 1, "청구월*", headerFormat1));
        sheet.addCell(new Label(1, 1, "고객명*", headerFormat1));
        sheet.addCell(new Label(2, 1, "가상계좌*", headerFormat1));
        sheet.addCell(new Label(3, 1, "납부시작일*", headerFormat1));
        sheet.addCell(new Label(4, 1, "납부마감일*", headerFormat1));
        sheet.addCell(new Label(5, 1, "고지서용표시마감일", headerFormat1));

        int cursor = 6; // 생성되어야 할 컬럼 인덱스
        for (int key : amtItemMap.keySet()) {
            Column column = amtItemMap.get(key);
            sheet.addCell(new Label(cursor, 0, column.code, headerFormat0)); // 청구항목 금액,비고
            sheet.addCell(new Label(cursor, 1, column.value, headerFormat1)); // 청구항목 금액,비고
            cursor++;
        }

        sheet.addCell(new Label(cursor + 0, 0, "추가안내사항1", headerFormat0));
        sheet.addCell(new Label(cursor + 0, 1, "(60자 이내)", headerFormat1));
        sheet.addCell(new Label(cursor + 1, 0, "추가안내사항2", headerFormat0));
        sheet.addCell(new Label(cursor + 1, 1, "(60자 이내)", headerFormat1));
        sheet.addCell(new Label(cursor + 2, 0, "추가안내사항3", headerFormat0));
        sheet.addCell(new Label(cursor + 2, 1, "(60자 이내)", headerFormat1));

    }

    /**
     * 고객용 헤더 생성
     *
     * @param sheet
     * @throws Exception
     */
    private void makeCellHeaderToCUSMAS(WritableSheet sheet, int cusGbcnt) throws RowsExceededException, WriteException {

        WritableCellFormat headerFormat0 = new WritableCellFormat();
        headerFormat0.setAlignment(Alignment.CENTRE);
        headerFormat0.setVerticalAlignment(VerticalAlignment.CENTRE);
        headerFormat0.setBackground(Colour.GREY_25_PERCENT);

        WritableCellFormat headerFormat1 = new WritableCellFormat();
        headerFormat1.setAlignment(Alignment.CENTRE);
        headerFormat1.setVerticalAlignment(VerticalAlignment.CENTRE);
        headerFormat1.setBackground(Colour.ICE_BLUE);

        // 두번째행
        sheet.addCell(new Label(0, 0, "고객명(필수)", headerFormat1));
        sheet.addCell(new Label(1, 0, "고객번호", headerFormat1));
        sheet.addCell(new Label(2, 0, "납부대상(필수)(Y:대상 / N:제외)", headerFormat1));
        sheet.addCell(new Label(3, 0, "연락처(휴대폰)", headerFormat1));
        sheet.addCell(new Label(4, 0, "연락자이메일", headerFormat1));

        int startColumnNo = 5;
        for (int j = 0; j < cusGbcnt; j++) {
            sheet.addCell(new Label(startColumnNo + j, 0, "고객구분" + (j + 1), headerFormat1)); // "CUSGUBN"
        }

        sheet.addCell(new Label(9 - 4 + cusGbcnt, 0, "발급용도(1=소득공제 / 2=지출증빙)", headerFormat1));
        sheet.addCell(new Label(10 - 4 + cusGbcnt, 0, "현금영수증 자동발급대상 여부(A=자동발급 / M=수동발급)", headerFormat1));
        sheet.addCell(new Label(11 - 4 + cusGbcnt, 0, "현금영수증발급방법(11=휴대폰번호, 21=사업자번호)", headerFormat1));
        sheet.addCell(new Label(12 - 4 + cusGbcnt, 0, "현금영수증발급번호", headerFormat1));
        sheet.addCell(new Label(13 - 4 + cusGbcnt, 0, "메모(고객 특이사항)", headerFormat1));
    }

    private WritableCellFormat getCellFormat() {
        WritableCellFormat cellFormat = new WritableCellFormat();
        try {
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        } catch (WriteException e1) {
            e1.printStackTrace();
        }

        return cellFormat;
    }

    private Workbook loadWorkbook(InputStream inputStream) throws BiffException, IOException {
        Workbook loadWorkbook;
        try {
            loadWorkbook = Workbook.getWorkbook(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

            throw e;
        } catch (IOException e) {
            e.printStackTrace();

            throw e;
        }

        return loadWorkbook;
    }

    public void extractCustomer(InputStream inputStream, OutputStream outputStream, int cusGbcnt) throws Exception {
        final Workbook workbook = loadWorkbook(inputStream);
        final WritableCellFormat cellFormat = getCellFormat();

        final WritableWorkbook myWorkbook = Workbook.createWorkbook(outputStream);

        try {
            final WritableSheet cusSheet = myWorkbook.createSheet("고객등록정보", 0);

            Sheet sourceSheet = workbook.getSheet(0);

//            LOGGER.info("시트이름=" + sourceSheet.getName());

            int row = sourceSheet.getRows(); // 행의 총 개수 (행은 0부터 시작함)
            int cellLastIndex = sourceSheet.getColumns();
            int nColStartUserIndex = sourceSheet.findCell("cusname").getColumn() + 1;
            int nColEndUserIndex = sourceSheet.findCell("cushp").getColumn() - 1;
            int nGubnCnt = nColEndUserIndex - nColStartUserIndex + 1;
            int bGubnCnt = 4 - (nColEndUserIndex - nColStartUserIndex + 1);

//            LOGGER.info("행의 갯수=" + row + " / " + cellLastIndex);

            // amt 정보 처리
            this.checkHeaderValues(sourceSheet.getRow(0));
            // 1,2의 정보로 청구정보 헤더값을 생성
            this.makeHeaderValues(sourceSheet.getRow(1));
            // 신규 엑셀의 헤더를 생성
            this.makeCellHeaderToCUSMAS(cusSheet, cusGbcnt); // 고객용 헤더 생성

            //엑셀루프시작
            for (int i = 2; i < row; i++) {
                Cell[] rowCells = sourceSheet.getRow(i);

                final int rowNo = i - 1;

                // 고객정보 생성
                cusSheet.addCell(new Label(0, rowNo, rowCells[1].getContents(), cellFormat)); // "CUSNAME"
                cusSheet.addCell(new Label(1, rowNo, rowCells[0].getContents(), cellFormat)); // "CUSKEY"
                cusSheet.addCell(new Label(2, rowNo, "Y", cellFormat)); // "RCPGUBN"
                cusSheet.addCell(new Label(3, rowNo, rowCells[6 - bGubnCnt].getContents(), cellFormat)); // "CUSHP"
                cusSheet.addCell(new Label(4, rowNo, rowCells[7 - bGubnCnt].getContents(), cellFormat)); // "CUSMAIL"

                int startColumnNo = 5;
                String contents;

                if (cusGbcnt == nGubnCnt) {                                            // DB 구분값 = 엑셀 구분값
                    for (int j = 0; j < cusGbcnt; j++) {
                        contents = rowCells[2 + j].getContents();

                        cusSheet.addCell(new Label(startColumnNo + j, rowNo, contents, cellFormat));
                    }
                }

                if (cusGbcnt > nGubnCnt) {                                                // DB 구분값 > 엑셀 구분값
                    for (int j = 0; j < nGubnCnt; j++) {
                        contents = rowCells[2 + j].getContents();
                        for (int z = 0; z < (cusGbcnt - nGubnCnt); z++) {
                            contents = StringUtils.EMPTY;
                        }
                        cusSheet.addCell(new Label(startColumnNo + j, rowNo, contents, cellFormat));
                    }
                }

                if (cusGbcnt < nGubnCnt) {                                                // DB 구분값 < 엑셀 구분값
                    for (int j = 0; j < cusGbcnt; j++) {
                        contents = rowCells[2 + j].getContents();
                        cusSheet.addCell(new Label(startColumnNo + j, rowNo, contents, cellFormat));
                    }
                }

                cusSheet.addCell(new Label(9 - 4 + cusGbcnt, rowNo, StringUtils.EMPTY, cellFormat)); // "RCPREQTY"
                cusSheet.addCell(new Label(10 - 4 + cusGbcnt, rowNo, "M", cellFormat)); // "RCPREQTY"
                cusSheet.addCell(new Label(11 - 4 + cusGbcnt, rowNo, StringUtils.EMPTY, cellFormat)); // "CONFIRM"
                cusSheet.addCell(new Label(12 - 4 + cusGbcnt, rowNo, rowCells[8 - bGubnCnt].getContents(), cellFormat)); // "CUSOFFNO"
                cusSheet.addCell(new Label(13 - 4 + cusGbcnt, rowNo, StringUtils.EMPTY, cellFormat)); // "MEMO"
            }

//            LOGGER.info(String.format("고객등록 정보 %d개 생성", cusSheet.getRows()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close(); // 업로드 엑셀 Closing

            if (myWorkbook != null) {
                myWorkbook.write();  // 변환 엑셀 파일생성
                try {
                    myWorkbook.close(); // 변환 엑셀 Closing
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void extractNotice(InputStream inputStream, OutputStream outputStream) throws Exception {
        final Workbook workbook = loadWorkbook(inputStream);
        final WritableCellFormat cellFormat = getCellFormat();

        final WritableWorkbook myWorkbook = Workbook.createWorkbook(outputStream);

        try {
            final String formattedDate = DATE_FORMAT_MONTH.format(new java.util.Date());

            final WritableSheet itemSheet = myWorkbook.createSheet("청구정보", 0);

            final Sheet sourceSheet = workbook.getSheet(0);

//            LOGGER.info("시트이름=" + sourceSheet.getName());

            int row = sourceSheet.getRows(); // 행의 총 개수 (행은 0부터 시작함)
            int cellLastIndex = sourceSheet.getColumns();

            int nColStartUserIndex = sourceSheet.findCell("cusname").getColumn() + 1;
            int nColEndUserIndex = sourceSheet.findCell("cushp").getColumn() - 1;
            int nGubnCnt = 4 - (nColEndUserIndex - nColStartUserIndex + 1);

//            LOGGER.info("행의 갯수=" + row + " / " + cellLastIndex);

            // amt 정보 처리
            this.checkHeaderValues(sourceSheet.getRow(0));
            // 1,2의 정보로 청구정보 헤더값을 생성
            this.makeHeaderValues(sourceSheet.getRow(1));

            // 신규 엑셀의 헤더를 생성
            this.makeCellHeaderToNOTIDET(itemSheet); // 청구항목 헤더 생성

            //엑셀루프시작
            for (int i = 2; i < row; i++) {
                Cell[] rowCells = sourceSheet.getRow(i);

                final int rowNo = i;

                // 청구항목 생성
                itemSheet.addCell(new Label(0, rowNo, formattedDate, cellFormat)); // "month"
                itemSheet.addCell(new Label(1, rowNo, rowCells[1].getContents(), cellFormat)); // "cusname"
                itemSheet.addCell(new Label(2, rowNo, rowCells[12 - nGubnCnt].getContents(), cellFormat)); // "vano"
                itemSheet.addCell(new Label(3, rowNo, rowCells[9 - nGubnCnt].getContents(), cellFormat)); // "startdate"
                itemSheet.addCell(new Label(4, rowNo, rowCells[10 - nGubnCnt].getContents(), cellFormat)); // "enddate"
                itemSheet.addCell(new Label(5, rowNo, rowCells[11 - nGubnCnt].getContents(), cellFormat)); // "printdate"

                int cursor = 6; // 생성되어야 할 컬럼 인덱스
                for (int key : amtItemMap.keySet()) {
                    itemSheet.addCell(new Label(cursor, rowNo, rowCells[key].getContents(), cellFormat)); // 청구 금액, 비고
                    cursor++;
                }

                itemSheet.addCell(new Label(cursor + 0, rowNo, StringUtils.EMPTY, cellFormat)); // "추가안내사항1"
                itemSheet.addCell(new Label(cursor + 1, rowNo, StringUtils.EMPTY, cellFormat)); // "추가안내사항2"
                itemSheet.addCell(new Label(cursor + 2, rowNo, StringUtils.EMPTY, cellFormat)); // "추가안내사항3"

            }

//            LOGGER.info(String.format("청구정보 %d개 생성", itemSheet.getRows()));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.close(); // 업로드 엑셀 Closing

            if (myWorkbook != null) {
                myWorkbook.write();  // 변환 엑셀 파일생성
                try {
                    myWorkbook.close(); // 변환 엑셀 Closing
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 소스의 첫행으로 amt 코드를 읽어온다.
     *
     * @param cells
     * @throws Exception
     */
    private void checkHeaderValues(jxl.Cell[] cells) throws Exception {

        try {

            for (int i = 0; i < cells.length; i++) {

                String columnName = cells[i].getContents();

                Column column = new Column();

                if (!columnName.equals("") && columnName.split("=").length == 2) {
                    String[] values = columnName.split("=");
                    column.idx = i;
                    column.code = values[1];

                    amtItemMap.put(i, column);
                }
            }

        } catch (Exception e) {
            throw new Exception("F02:헤더정보 체크중 오류..." + e.toString());
        }
    }

    /**
     * 소스의 두번째 행으로 amt 값을 읽어온다.
     *
     * @param cells
     * @throws Exception
     */
    private void makeHeaderValues(jxl.Cell[] cells) throws Exception {

        try {
            for (int key : amtItemMap.keySet()) {
                Column column = amtItemMap.get(key);
                column.value = cells[key].getContents();
                amtItemMap.put(key, column);
            }
        } catch (Exception e) {
            throw new Exception("F02:헤더정보 체크중 오류..." + e.toString());
        }
    }

    class Column {
        int idx;
        String code;
        String value;

        public Column() {
            this.idx = -1;
        }

        public Column(String[] arr) {
            if (arr != null && arr.length == 2) {
                this.code = arr[0].toUpperCase();
                this.value = arr[1].toUpperCase();
            } else {
                this.code = StringUtils.EMPTY;
                this.value = StringUtils.EMPTY;
            }
        }

        public Column(String str) {
            this.code = str.toUpperCase();
            this.value = str.toUpperCase();
        }

        public boolean equals() {
            return (StringUtils.equalsIgnoreCase(code, value));
        }
        public boolean isPayment() {
            return !(StringUtils.equalsIgnoreCase(code, value));
        }
    }
}
