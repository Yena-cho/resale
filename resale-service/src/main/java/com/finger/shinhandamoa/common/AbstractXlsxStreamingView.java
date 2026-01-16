package com.finger.shinhandamoa.common;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

//20250410 : 박한우
//기존에 있던 XlsxBuilder 클래스와 AbstractExcelView 를 병합한 새로운 추상 클래스
//해당 클래스를 상속 받는 클래스를 만들고 컨트롤러에서 View로 리턴하면 엑셀 다운로드 가능
public abstract class AbstractXlsxStreamingView extends AbstractView {
    public AbstractXlsxStreamingView() {
        setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return super.generatesDownloadContent();
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        XlsxBuilder builder = new XlsxBuilder();
        OutputStream out = null;
        try {
            String encodedFilename = URLEncoder.encode(getFilename(model), "UTF-8");
            response.setHeader("Content-Disposition", "attachement; filename=\"" + encodedFilename + "\"; charset=UTF-8");
            response.setContentType(getContentType());

            buildExcelDocument(model, builder, request, response);

            out = response.getOutputStream();
            builder.writeTo(out);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
            builder.disposeTo();
        }


    }

    protected String getFilename(Map<String, Object> model) {
        String baseName = (String) model.getOrDefault("excelName", "excel");
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());
        return baseName + "_" + timeStamp + ".xlsx";
    }

    protected abstract void buildExcelDocument(Map<String, Object> model,
                                               XlsxBuilder workbook,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception;
}
