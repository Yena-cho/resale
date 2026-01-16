package kr.co.finger.shinhandamoa.adapter.fim;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 * 핑거 알림톡
 *
 * @author mljeong@finger.co.kr
 */
@Component
public class FingerAlarmTalkAdapter {
    private static final Logger log = LoggerFactory.getLogger(FingerAlarmTalkAdapter.class);

    private static final JsonSerializer<Date> DATE_SERIALIZER = (date1, type, jsonSerializationContext) -> date1 == null ? null : new JsonPrimitive(date1.getTime());
    private static final JsonDeserializer<Date> DATE_DESERIALIZER = (jsonElement, type, jsonDeserializationContext) -> jsonElement == null ? null : new Date(jsonElement.getAsLong());

    @Value("${at.host}")
    private String url;

    @Value("${at.serviceKey}")
    private String serviceKey;

    private static final String API_SEND = "/api/send";
    private static final String API_status = "/api/status/";
    private static final String API_statusList = "/api/status/list";

    private Gson gson;

    public FingerAlarmTalkAdapter() {
        gson = new GsonBuilder().registerTypeAdapter(Date.class, DATE_SERIALIZER).registerTypeAdapter(Date.class, DATE_DESERIALIZER).create();
    }

    public Message createMessage(String ServiceKey, String destPhone, String sendPhone, String msgBody, String templateCode) {
        JsonSerializer<Date> serializer = new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
                return date == null ? null : new JsonPrimitive(date.getTime());
            }
        };

        JsonDeserializer<Date> deserializer = new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return jsonElement == null ? null : new Date(jsonElement.getAsLong());
            }
        };

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, serializer).registerTypeAdapter(Date.class, deserializer).create();

        String requestTime = "";
        String sendTime = "";

        TalkMessageParam param = new TalkMessageParam();
        param.setRequestTime(requestTime);
        param.setMsgType(6);
        param.setSendTime(sendTime);
        param.setDestPhone(destPhone);
        param.setSendPhone(sendPhone);
        param.setMsgBody(msgBody);
        param.setTemplateCode(templateCode);

        String jsonString = gson.toJson(param);

        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        basicHttpEntity.setContent(new ByteArrayInputStream(jsonString.getBytes(Charset.forName("UTF-8"))));
        basicHttpEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

        HttpPost httpPost = new HttpPost(url + API_SEND);
        httpPost.setEntity(basicHttpEntity);
        httpPost.addHeader("RG-Service-Key", serviceKey);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;

        String responseString = "";
        Message listResult;

        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            List<String> readLInes = IOUtils.readLines(inputStream);
            responseString = StringUtils.join(readLInes, '\n');

            listResult = gson.fromJson(responseString, Message.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();

            listResult = null;
        }

        return listResult;
    }

    public Message getResult(String cmid) {
        Gson gson = new Gson();

        HttpGet httpGet;
        if ("0".equals(cmid)) {
            httpGet = new HttpGet(url + API_statusList);
        } else {
            httpGet = new HttpGet(url + API_status + cmid);
        }
        httpGet.addHeader("RG-Service-Key", serviceKey);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;
        String responseString = "";

        Message listResult;

        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            List<String> readLInes = IOUtils.readLines(inputStream);
            responseString = StringUtils.join(readLInes, '\n');
            log.debug("responseString: {}", responseString);

            listResult = gson.fromJson(responseString, Message.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();

            listResult = null;
        }

        return listResult;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    private class TalkMessageParam {
        private String requestTime;
        private String sendTime;
        private String destPhone;
        private String sendPhone;
        private String msgBody;
        private int msgType;

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        private String templateCode;

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getDestPhone() {
            return destPhone;
        }

        public void setDestPhone(String destPhone) {
            this.destPhone = destPhone;
        }

        public String getSendPhone() {
            return sendPhone;
        }

        public void setSendPhone(String sendPhone) {
            this.sendPhone = sendPhone;
        }

        public String getMsgBody() {
            return msgBody;
        }

        public void setMsgBody(String msgBody) {
            this.msgBody = msgBody;
        }

        public String getTemplateCode() {
            return templateCode;
        }

        public void setTemplateCode(String templateCode) {
            this.templateCode = templateCode;
        }
    }

    public static class Message {
        private String trxKey;
        private String resultCode;
        private String resultMsg;
        private ResultObject resultObject;

        public String getTrxKey() {
            return trxKey;
        }

        public void setTrxKey(String trxKey) {
            this.trxKey = trxKey;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public ResultObject getResultObject() {
            return resultObject;
        }

        public void setResultObject(ResultObject resultObject) {
            this.resultObject = resultObject;
        }
    }

    public static class ResultObject {
        private int trxLogNo;
        private int servcieKeyNo;
        private String cmid;
        private String requestTime;
        private String reportTime;
        private int status;
        private String callStatus;

        public int getTrxLogNo() {
            return trxLogNo;
        }

        public void setTrxLogNo(int trxLogNo) {
            this.trxLogNo = trxLogNo;
        }

        public int getServcieKeyNo() {
            return servcieKeyNo;
        }

        public void setServcieKeyNo(int servcieKeyNo) {
            this.servcieKeyNo = servcieKeyNo;
        }

        public String getCmid() {
            return cmid;
        }

        public void setCmid(String cmid) {
            this.cmid = cmid;
        }

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getReportTime() {
            return reportTime;
        }

        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCallStatus() {
            return callStatus;
        }

        public void setCallStatus(String callStatus) {
            this.callStatus = callStatus;
        }
    }
}
