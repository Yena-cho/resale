package com.finger.shinhandamoa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * ExcelHandler 테스트
 * 
 * @author wisehouse@finger.co.kr
 */
public class NoticeExcelTransformerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeExcelTransformerTest.class);

    public static void main(String[] args) throws IOException {
        String savePath;
        String fileName;
        if (args == null) {
//			LOGGER.info("변환할 파일 경로와 파일명을 입력하세요.");
//			System.exit(0);
            savePath = "/Users/wisehouse/temp"; // 원본엑셀 경로
            fileName = "as-is_data_20180710.xls";  // 원본엑셀 파일명
        } else {
            if (args.length > 1) {
                savePath = args[0]; // 원본엑셀 경로
                fileName = args[1];  // 원본엑셀 파일명
            } else {
                savePath = "/Users/wisehouse/temp"; // 원본엑셀 경로
                fileName = "as-is_data_20180710.xls";  // 원본엑셀 파일명
            }
        }
        
        File file = File.createTempFile("wisehouse_", "");
        LOGGER.info("fileName: {}", file.getName());


        NoticeExcelTransformer excel = new NoticeExcelTransformer();
        try {
            //excel.extractCustomer(new FileInputStream(new File(savePath, fileName)), new ByteArrayOutputStream());
            excel.extractNotice(new FileInputStream(new File(savePath, fileName)), new ByteArrayOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
