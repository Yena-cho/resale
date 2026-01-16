package com.finger.shinhandamoa.common;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finger.shinhandamoa.sys.addServiceMgmt.web.AddServiceMgmtController;

public class PdfViewer {
    private static final Logger logger = LoggerFactory.getLogger(PdfViewer.class);

    /**
     * web(popup)으로 pdf 출력
     *
     * @param request
     * @param response
     * @param fileName
     * @return
     * @throws Exception
     */
    public String webPdfViewer(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {


        FileInputStream fis = null;
        BufferedOutputStream bos = null;
        PrintWriter out = null;
        String result = "";
        File pdfFile = null;
        try {
            pdfFile = new File(fileName);
            response.setContentType("application/pdf");
            fis = new FileInputStream(pdfFile);
            int size = fis.available();
            byte[] buf = new byte[size]; //버퍼설정
            int readCount = fis.read(buf);
            response.flushBuffer();
            bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(buf, 0, readCount);
            bos.flush();
            result = "0000";
        } catch (Exception e) {
            result = "9999";
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (fis != null) fis.close(); //close는 꼭! 반드시!
                if (bos != null) bos.close();
                if(pdfFile.exists()){
                    pdfFile.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * ie pdf 다운로드
     *
     * @param request
     * @param response
     * @param fileName
     * @return
     * @throws Exception
     */
    public String pdfIEDown(HttpServletRequest request, HttpServletResponse response, String strInFile, String fileName) throws Exception {

        FileInputStream fis = null;
        BufferedOutputStream bos = null;
        String result = "";
        File pdfFile = null;
        try {
            // 파일 다운로드 설정
            response.setContentType("application/pdf");
            response.setHeader("Content-Transper-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");
            response.setHeader("Pragma", "no-cache;");
            pdfFile = new File(strInFile);
            fis = new FileInputStream(pdfFile);
            int size = fis.available(); //지정 파일에서 읽을 수 있는 바이트 수를 반환
            byte[] buf = new byte[size]; //버퍼설정
            int readCount = fis.read(buf);
            response.flushBuffer();
            System.out.flush();
            bos = new BufferedOutputStream(response.getOutputStream());
            response.getOutputStream().flush();
            bos.write(buf, 0, readCount);
            bos.flush();
            result = "0000";
        } catch (Exception e) {
            e.printStackTrace();
            result = "9999";
        } finally {
            try {
                if (fis != null) fis.close(); //close는 꼭! 반드시!
                if (bos != null) bos.close();
                if(pdfFile.exists()){
                    pdfFile.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 파일 변환
     * pdf -> image
     */
    public static String conversionPdfToImg(InputStream is, String pdfTempPath, String fileId) {
        String format = "png";
        int scale = 130;
        int quality = 130;

        String imgFileName = "";

        try {
            PDDocument pdfDoc = PDDocument.load(is); //Document 생성
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);

            if (pdfDoc.getNumberOfPages() < 1) {
                throw new NullPointerException();
            }
            String resultImgPath = pdfTempPath; //이미지가 저장될 경로
            Files.createDirectories(Paths.get(resultImgPath)); //PDF 2 Img에서는 경로가 없는 경우 이미지 파일이 생성이 안되기 때문에 디렉토리를 만들어준다.

            // 이미지로 변환 처리
            imgFileName = resultImgPath + fileId + "_tmp." + format;

            //DPI 설정 : 1페이지만 보여줌(기획팀 확인)
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, scale, ImageType.RGB);

            // 이미지로 만든다.
            boolean writeRst = ImageIOUtil.writeImage(bim, imgFileName, quality);

            File imgFile = new File(imgFileName);
            if (imgFile != null && imgFile.isFile()) {
                // 이미지 파일 정상 생성
            } else {
                imgFileName = "";
            }
            pdfDoc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return imgFileName;
    }
}
