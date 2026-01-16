package com.finger.shinhandamoa.org.transformer;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 다운로드 뷰
 * 
 * @author wisehouse@finger.co.kr
 * @since 2018. 8. 12.
 */
public class DownloadView extends AbstractView {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadView.class);

    public static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    public static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";

    private String fileName;
    private InputStream inputStream;
    private long fileLength;
    
    public DownloadView(String fileName, InputStream inputStream) {
    	//String prefixName = "현금영수증 발행내역[%s]_%s.xlsx";
        this.fileName = fileName;
        this.inputStream = inputStream;
        this.fileLength = 0L;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    @Override
    protected void renderMergedOutputModel(final Map<String, Object> map, final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws Exception {
        initContentType(httpServletRequest, httpServletResponse);
        initContentTransferEncoding(httpServletRequest, httpServletResponse);
        initContentLength(httpServletRequest, httpServletResponse);
        initContentDisposition(httpServletRequest, httpServletResponse);
        
        transfer(httpServletRequest, httpServletResponse);
    }

    private void transfer(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(this.inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(httpServletResponse.getOutputStream());
        IOUtils.copy(bis, bos);

        bis.close();
        bos.flush();
        bos.close();
    }

    private void initContentDisposition(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException {
        final String userAgent = httpServletRequest.getHeader("User-Agent");

        LOGGER.debug("filename: {}", this.fileName);
        LOGGER.debug("userAgent: {}", userAgent);

        // attachment; 가 붙으면 IE의 경우 무조건 다운로드창이 뜬다. 상황에 따라 써야한다.
        if (userAgent != null && userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
        	LOGGER.debug("----- filename: {}", this.fileName);
            httpServletResponse.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(fileName, "UTF-8") + ";");
        } else if (userAgent != null && userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ";");
        } else { // 모질라나 오페라 & IE 11
            //httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "latin1") + ";");
        	httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ";");
            
        }
    }

    private void initContentLength(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if(fileLength > 0) {
            httpServletResponse.setHeader("Content-Length", String.valueOf(fileLength));
        }
    }

    private void initContentTransferEncoding(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Content-Transfer-Encoding", "binary;");
    }

    private void initContentType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        final String contentType = getContentType();
        httpServletResponse.setHeader("Content-Type", contentType);
    }
}
