package com.finger.shinhandamoa.org.transformer;

import com.finger.shinhandamoa.common.NoticeExcelTransformer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 엑셀 등록 파일 변환 컨트롤러
 *
 * @author wisehouse@finger.co.kr
 * @since 2018. 8. 12.
 */
@Controller
@RequestMapping(value = "/org/transformer")
public class TransformerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerController.class);

    private static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";


    /**
     * 고객 파일 경로
     */
    @Value("${file.path.transform.customer}")
    private String customerPath;

    /**
     * 청구 파일 경로
     */
    @Value("${file.path.transform.notice}")
    private String noticePath;
    
    private static String CUSTOMER_PREFIX = "notice_customer_";
    private static String NOTICE_PREFIX = "notice_notice_";

    /**
     * 기존 포맷 등록 및 변환
     * @param cusGbcnt 
     * 
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> postLegacyExcelFile(MultipartHttpServletRequest request, int cusGbcnt) throws Exception {
        
        System.out.println("#################### cusGbcnt : " + cusGbcnt);
        final Map<String, MultipartFile> fileMap = request.getFileMap();
        final MultipartFile file = fileMap.get("file");
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        IOUtils.copy(file.getInputStream(), os);
        final NoticeExcelTransformer transformer = new NoticeExcelTransformer();

        final File customerFile = File.createTempFile("notice_customer_", "");
        transformer.extractCustomer(new ByteArrayInputStream(os.toByteArray()), FileUtils.openOutputStream(customerFile), cusGbcnt);
        
        //FileUtils.moveToDirectory(customerFile, new File(customerPath), true);
        FileUtils.copyFileToDirectory(customerFile, new File(customerPath), true);
        //FileUtils.copyDirectory(customerFile, new File(customerPath), true);

        final File noticeFile = File.createTempFile("notice_notice_", "");
        transformer.extractNotice(new ByteArrayInputStream(os.toByteArray()), FileUtils.openOutputStream(noticeFile));
        //FileUtils.moveToDirectory(noticeFile, new File(noticePath), true);
        FileUtils.copyFileToDirectory(noticeFile, new File(noticePath), true);
        //FileUtils.copyDirectory(noticeFile, new File(noticePath), true);

        final Map<String, String> resultMap = new HashMap<>();
        final String noticeId = StringUtils.substringAfter(noticeFile.getName(), "notice_notice_");
        final String customerId = StringUtils.substringAfter(customerFile.getName(), "notice_customer_");

        resultMap.put("noticeId", noticeId);
        resultMap.put("noticeFileName", "청구목록_" + noticeId + ".xls");
        resultMap.put("customerId", customerId);
        resultMap.put("customerFileName", "고객목록_" + customerId + ".xls");
        
        return resultMap;
    }

    /**
     * 고객목록 다운로드
     * 
     * @param customerId
     * @return
     */
    @RequestMapping(value="/customer/{id}")
    public DownloadView downloadCustomer(@PathVariable(value="id") String customerId) throws FileNotFoundException {
        final File file = new File(customerPath, CUSTOMER_PREFIX + customerId);
        final long fileLength = file.length();

        final DownloadView downloadView = new DownloadView("고객목록_" + customerId + ".xls", new FileInputStream(file));
        downloadView.setFileLength(fileLength);
        downloadView.setContentType(CONTENT_TYPE_XLS);
        
        return downloadView;
    }

    /**
     * 청구목록 다운로드
     * 
     * @param noticeId
     * @return
     */
    @RequestMapping(value="/notice/{id}")
    public DownloadView downloadNotice(@PathVariable(value="id") String noticeId) throws FileNotFoundException {
        final File file = new File(noticePath, NOTICE_PREFIX + noticeId);
        final long fileLength = file.length();

        final DownloadView downloadView = new DownloadView("청구목록_" + noticeId + ".xls", new FileInputStream(file));
        downloadView.setFileLength(fileLength);
        downloadView.setContentType(CONTENT_TYPE_XLS);

        return downloadView;
    }

}
