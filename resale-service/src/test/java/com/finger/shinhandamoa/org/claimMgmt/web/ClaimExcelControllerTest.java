package com.finger.shinhandamoa.org.claimMgmt.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/*.xml", "/spring/appServlet/*.xml"}) //xml 경로
public class ClaimExcelControllerTest {
    @Autowired
    private ClaimExcelController claimExcelController;

    @Test
    public void excelUpload() throws Exception {
        MultipartHttpServletRequest multi = new MockMultipartHttpServletRequest();

        String fileDir = "C:\\Users\\eFinance\\Downloads";
        String fileName = "20190405104950_20000809_청구양식2.xlsx";
        String fileFullPath = String.format("%s/%s", fileDir, fileName);

        MultipartFile multipartFile = new MockMultipartFile("file", fileName, "xlsx", new FileInputStream(new File(fileFullPath)));

        ((MockMultipartHttpServletRequest) multi).addFile(multipartFile);

        // 테스트 시 claimexcelcontroller 에 chacd 하드코딩 필요
        claimExcelController.excelUpload(multi, false);
    }
}