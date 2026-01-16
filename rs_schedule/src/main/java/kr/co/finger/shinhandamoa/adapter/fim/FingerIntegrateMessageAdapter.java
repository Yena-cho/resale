package kr.co.finger.shinhandamoa.adapter.fim;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import kr.co.finger.shinhandamoa.common.ListResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 핑거 통합 메시지
 *
 * @author wisehouse@finger.co.kr
 */
@Component
public class FingerIntegrateMessageAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FingerIntegrateMessageAdapter.class);

    private static final JsonSerializer<Date> DATE_SERIALIZER = (date1, type, jsonSerializationContext) -> date1 == null ? null : new JsonPrimitive(date1.getTime());
    private static final JsonDeserializer<Date> DATE_DESERIALIZER = (jsonElement, type, jsonDeserializationContext) -> jsonElement == null ? null : new Date(jsonElement.getAsLong());

    private static final String FILE = "/api/message";

    @Value("${fim.server.host}")
    private String host;

    @Value("${fim.server.port}")
    private int port;

    @Value("${fim.access-token}")
    private String accessToken;

    private Gson gson;


    public FingerIntegrateMessageAdapter() {
        gson = new GsonBuilder().registerTypeAdapter(Date.class, DATE_SERIALIZER).registerTypeAdapter(Date.class, DATE_DESERIALIZER).create();
    }

    public ListResult<Message> createMessage(MessageType messageType, String title, String body, Date date, String sourceId, String destinationId, String destnationName) {
        PostMessageParam param = new PostMessageParam();
        param.setMessageType(messageType);
        param.setTitle(title);
        param.setBody(body);
        param.setDate(date);
        param.setSourceId(sourceId);
        param.setDestinationList(Arrays.asList(new Destination(destinationId, destnationName)));

        String jsonString = gson.toJson(param);
        LOGGER.debug("requestString: {}", jsonString);

        LOGGER.debug("host  :" + host);
        LOGGER.debug("port  :" + port);
        LOGGER.debug("accessToken  :" + accessToken);

        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        basicHttpEntity.setContent(new ByteArrayInputStream(jsonString.getBytes(Charset.forName("UTF-8"))));
        basicHttpEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

        HttpPost httpPost = new HttpPost("http://" + host + ":" + port + FILE + "?accessToken=" + accessToken);
        httpPost.setEntity(basicHttpEntity);

        ListResult<Message> listResult;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            List<String> readLInes = IOUtils.readLines(inputStream);
            String responseString = StringUtils.join(readLInes, '\n');
            LOGGER.trace("responseString: {}", responseString);
            Type listResultType = new TypeToken<ListResult<Message>>() {
            }.getType();
            listResult = gson.fromJson(responseString, listResultType);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
            listResult = new ListResult<Message>(0, new ArrayList<Message>());/////????
        }

        return listResult;
    }

    public Message getResult(long id) {
        LOGGER.debug("host  :" + host);
        LOGGER.debug("port  :" + port);
        LOGGER.debug("accessToken  :" + accessToken);

        HttpGet httpGet = new HttpGet("http://" + host + ":" + port + FILE + "/" + id + "?accessToken=" + accessToken);

        Message message = new Message();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            List<String> readLInes = IOUtils.readLines(inputStream);
            String responseString = StringUtils.join(readLInes, '\n');
            LOGGER.debug("responseString: {}", responseString);
            message = gson.fromJson(responseString, Message.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
            message = null;
        }

        return message;
    }

    private class PostMessageParam {
        private MessageType messageType;
        private String title;
        private String body;
        private Date date;
        private String sourceId;
        private List<Destination> destinationList;

        public MessageType getMessageType() {
            return messageType;
        }

        public void setMessageType(MessageType messageType) {
            this.messageType = messageType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public List<Destination> getDestinationList() {
            return destinationList;
        }

        public void setDestinationList(List<Destination> destinationList) {
            this.destinationList = destinationList;
        }
    }

    public enum MessageType {
        //SMS, LMS, EMERGENCY_SMS, EMERGENCY_LMS
        FCM, MMS, EMERGENCY_MMS, SMS, EMERGENCY_SMS, EMAIL
    }

    public class Destination {
        private String id;
        private String name;

        public Destination(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class Message {
        private long id;
        private MessageType type;
        private String title;
        private String body;
        private Date date;
        private Code status;
        private Source source;
        private Destination destination;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public MessageType getType() {
            return type;
        }

        public void setType(MessageType type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Code getStatus() {
            return status;
        }

        public void setStatus(Code status) {
            this.status = status;
        }

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public Destination getDestination() {
            return destination;
        }

        public void setDestination(Destination destination) {
            this.destination = destination;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public class Code {
        private String code;
        private String name;

        public Code(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public class Source {
        private String id;
        private String name;

        public Source(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
