package com.finger.shinhandamoa.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CooconArsAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(CooconArsAPI.class);
    
    private String accessToken = "xk4JUVLHp1MaXhSvyrYr";
    
    private String corpCode = "20066";
    
    private String file = "http://dev.coocon.co.kr/sol/gateway/ars_wapi.jsp";
    
    public CooconArsAPI(String corpCode, String accessToken, String file) {
        this.corpCode = corpCode;
        this.accessToken = accessToken;
        this.file = file;
    }

    public Output createArs(String trCd, String contactNo, String authNo, String authInquery, boolean fileSaveYn, String fileName) {
        LOGGER.debug("accessToken  :"+ accessToken);

        Gson gson = new GsonBuilder().create();
        String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        final Input param = new Input();
        param.setTR_CD(trCd);
        param.setSECR_KEY(accessToken);
        param.setORG_CD(corpCode);
        param.setDATE(yyyyMMddHHmmss);
        param.setPHONE(contactNo);
        param.setAUTH_NO(authNo);
        param.setAUTH_INQUERY(authInquery);
        param.setFILE_SAVE_YN(fileSaveYn ? "Y" : "N");
        param.setFILE_NM(StringUtils.leftPad(fileName, 50, "0"));

        String jsonString = gson.toJson(param);
        LOGGER.debug("requestString: {}", jsonString);


        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("JSONData", jsonString));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, Charset.forName("EUC-KR"));

        HttpPost httpPost = new HttpPost(file);
        httpPost.setEntity(urlEncodedFormEntity);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;
        Output output;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            List<String> readLInes = IOUtils.readLines(inputStream, Charset.forName("EUC-KR"));
            String responseString = StringUtils.trim(StringUtils.join(readLInes, ""));
            LOGGER.trace("responseString: {}", responseString);
            output = gson.fromJson(responseString, Output.class);
            output.setInput(param);
            
            LOGGER.trace("ARS RESULT[{}: {}]", output.getRSLT_CD(), output.getRSLT_MSG());
        } catch (IOException e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }
            
            output = null;
        }

        return output;
    }

    public static class Input {
        private String SECR_KEY;
        private String TR_CD;
        private String ORG_CD;
        private String DATE;
        private String PHONE;
        private String AUTH_NO;
        private String AUTH_INQUERY;
        private String FILE_SAVE_YN;
        private String FILE_NM;

        public String getSECR_KEY() {
            return SECR_KEY;
        }

        public void setSECR_KEY(String SECR_KEY) {
            this.SECR_KEY = SECR_KEY;
        }

        public String getTR_CD() {
            return TR_CD;
        }

        public void setTR_CD(String TR_CD) {
            this.TR_CD = TR_CD;
        }

        public String getORG_CD() {
            return ORG_CD;
        }

        public void setORG_CD(String ORG_CD) {
            this.ORG_CD = ORG_CD;
        }

        public String getDATE() {
            return DATE;
        }

        public void setDATE(String DATE) {
            this.DATE = DATE;
        }

        public String getPHONE() {
            return PHONE;
        }

        public void setPHONE(String PHONE) {
            this.PHONE = PHONE;
        }

        public String getAUTH_NO() {
            return AUTH_NO;
        }

        public void setAUTH_NO(String AUTH_NO) {
            this.AUTH_NO = AUTH_NO;
        }

        public String getAUTH_INQUERY() {
            return AUTH_INQUERY;
        }

        public void setAUTH_INQUERY(String AUTH_INQUERY) {
            this.AUTH_INQUERY = AUTH_INQUERY;
        }

        public String getFILE_SAVE_YN() {
            return FILE_SAVE_YN;
        }

        public void setFILE_SAVE_YN(String FILE_SAVE_YN) {
            this.FILE_SAVE_YN = FILE_SAVE_YN;
        }

        public String getFILE_NM() {
            return FILE_NM;
        }

        public void setFILE_NM(String FILE_NM) {
            this.FILE_NM = FILE_NM;
        }
    }
    
    public static class Output {
        private List<ResponseData> RESP_DATA;
        private String RSLT_MSG;
        private String RSLT_CD;
        private Input input;

        public List<ResponseData> getRESP_DATA() {
            return RESP_DATA;
        }

        public void setRESP_DATA(List<ResponseData> RESP_DATA) {
            this.RESP_DATA = RESP_DATA;
        }

        public String getRSLT_MSG() {
            return RSLT_MSG;
        }

        public void setRSLT_MSG(String RSLT_MSG) {
            this.RSLT_MSG = RSLT_MSG;
        }

        public String getRSLT_CD() {
            return RSLT_CD;
        }

        public void setRSLT_CD(String RSLT_CD) {
            this.RSLT_CD = RSLT_CD;
        }

        public Input getInput() {
            return input;
        }

        public void setInput(Input input) {
            this.input = input;
        }
    }
    
    public static class ResponseData {
        private String TR_CD;
        private String RECORD_DATA;
        private String TXT_NO;

        public String getTR_CD() {
            return TR_CD;
        }

        public void setTR_CD(String TR_CD) {
            this.TR_CD = TR_CD;
        }

        public String getRECORD_DATA() {
            return RECORD_DATA;
        }

        public void setRECORD_DATA(String RECORD_DATA) {
            this.RECORD_DATA = RECORD_DATA;
        }

        public String getTXT_NO() {
            return TXT_NO;
        }

        public void setTXT_NO(String TXT_NO) {
            this.TXT_NO = TXT_NO;
        }
    }
}
