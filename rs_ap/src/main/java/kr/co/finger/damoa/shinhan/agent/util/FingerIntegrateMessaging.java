package kr.co.finger.damoa.shinhan.agent.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FingerIntegrateMessaging {
    private static final Logger LOGGER = LoggerFactory.getLogger(FingerIntegrateMessaging.class);
    
//    @Value("${fim.server.host}")
    private String host;
    
//    @Value("${fim.server.port}")
    private int port;
    
//    @Value("${fim.accessToken}")
    private String accessToken;

    
    private static final String file = "/api/message";

    public ListResult<Message> createMessage(MessageType messageType, String title, String body, Date date, String sourceId, String destinationId, String destnationName) {
        JsonSerializer<Date> serializer = new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
                return date == null ? null : new JsonPrimitive(date.getTime());
            }
        };
//MessageType messageType,
        JsonDeserializer<Date> deserializer = new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return jsonElement == null ? null : new Date(jsonElement.getAsLong());
            }
        };

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, serializer).registerTypeAdapter(Date.class, deserializer).create();

        PostMessageParam param = new PostMessageParam();
        param.setMessageType(messageType);
        param.setTitle(title);
        param.setBody(body);
        param.setDate(date);
        param.setSourceId(sourceId);
        param.setDestinationList(Arrays.asList(new Destination(destinationId, destnationName)));

        String jsonString = gson.toJson(param);
        LOGGER.debug("requestString: {}", jsonString);

        LOGGER.debug("host  :"+ host);
        LOGGER.debug("port  :"+ port);
        LOGGER.debug("accessToken  :"+ accessToken);

        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        basicHttpEntity.setContent(new ByteArrayInputStream(jsonString.getBytes(Charset.forName("UTF-8"))));
        basicHttpEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

        HttpPost httpPost = new HttpPost("http://" + host + ":" + port + file + "?accessToken=" + accessToken);
        httpPost.setEntity(basicHttpEntity);

        ListResult<Message> listResult;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;
        InputStream inputStream = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            List<String> readLInes = IOUtils.readLines(inputStream);
            String responseString = StringUtils.join(readLInes, '\n');
            LOGGER.trace("responseString: {}", responseString);
            Type listResultType = new TypeToken<ListResult<Message>>(){}.getType();
            listResult = gson.fromJson(responseString, listResultType);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
            listResult = new ListResult<Message>(0, new ArrayList<Message>());/////????
        } finally {
            if(inputStream != null) {
                try {
                    IOUtils.closeQuietly(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return listResult;
    }

    public Message getResult(long id) {

    	Gson gson = new Gson();

        LOGGER.debug("host  :"+ host);
        LOGGER.debug("port  :"+ port);
        LOGGER.debug("accessToken  :"+ accessToken);


        HttpGet httpGet = new HttpGet("http://" + host + ":" + port + file + "/" + id + "?accessToken=" + accessToken);

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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
