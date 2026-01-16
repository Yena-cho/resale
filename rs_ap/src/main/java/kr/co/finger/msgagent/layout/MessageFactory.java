package kr.co.finger.msgagent.layout;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kr.co.finger.damoa.commons.Damoas;
import kr.co.finger.damoa.model.corp.CorpInfoMsg;
import kr.co.finger.damoa.model.corp.Data;
import kr.co.finger.damoa.model.corp.Head;
import kr.co.finger.damoa.model.corp.Trailer;
import kr.co.finger.damoa.model.msg.Code;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.msgagent.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전문 관리 클래스
 * initFactory 메소드를 통해 초기화 처리함.
 */
public class MessageFactory {

    private String messageFormatFileName;
    private TranTypeCodeResolver tranTypeCodeResolver;

    private Charset charset;

    private boolean isCorpInfo;

    private final Logger LOG = LoggerFactory.getLogger(MessageFactory.class);

    /**
     * 메세지 정보 by Id
     */
    private Map<String, Message> messageFormatsById = new HashMap<String, Message>();

    /**
     * 메세지 정보 by TranTypeCode
     */
    private Map<String, Message> messageFormatsByTranTypeCode = new HashMap<String, Message>();
    private Map<String, Integer> messageLengthByTranTypeCode = new HashMap<String, Integer>();

    private Map<String, MsgIF> messageInstanceByTranTypeCode = new HashMap<String, MsgIF>();

    /**
     * 메세지 포맷 정보를 취득합니다.
     *
     * @param id message.xml 매핑 id
     * @return 메세지 포맷 정보
     */
    private Message getMessageFormatById(String id) {
        return messageFormatsById.get(id);
    }

    /**
     * 메세지 포맷 정보를 취득합니다.
     *
     * @param tranTypeCode message.xml 매핑 tranTypeCode
     * @return 메세지 포맷 정보
     */
    public Message getMessageFormatByTranTypeCode(String tranTypeCode) {
        return messageFormatsByTranTypeCode.get(tranTypeCode);
    }

    public void setCorpInfo(boolean corpInfo) {
        isCorpInfo = corpInfo;
    }

    /**
     * 메세지들을 생성합니다.
     * <p>
     * 참고> 메세지가 생성되기 전까지 메세지 recieve를 제한
     *
     * @param messageFormatFileName 메세지 포맷 파일명
     * @return 메세지 포맷 생성 성공 여부
     * @throws Exception
     */


    public boolean initFactory(String messageFormatFileName, Charset charset) throws Exception {
        setCharset(charset);
        setMessageFormatFileName(messageFormatFileName);
        Messages messages = fromXml(messageFormatFileName);
        List<Message> messageList = messages.getMessageList();
        for (Message message : messageList) {

            List<DataField> dataFields = message.getDataFields();
            for (DataField dataField : dataFields) {
                LOG.info(dataField.toString());
            }
            message.setSize(message.msgSize());
            LOG.info(message.toString());
            LOG.info(":::::::::::::::::::::::::::::::::::::\n");
        }

        LOG.info("다모아 전문 로딩 성공 : [" + messages.getMessageList().size() + "]");

        modifyId(messageList);
        setupSomeMap(messageList);
        setupRefInfo(messageList);
        if (isCorpInfo == false) {
            setupMessageInstanceMap(messageList);
        }

        setupIndex(messageList);

        LOG.info("다모아 전문 초기화 완료");

        return true;
    }

    private void setupIndex(List<Message> messageList) {
        for (Message message : messageList) {
            List<DataField> dataFields = message.getDataFields();
            int index = 0;
            for (DataField dataField : dataFields) {
                dataField.setIndex(index++);
            }
        }

    }

    /**
     * fieldId는 처음문자가 소문자인데 쉽게 사용하기 위해서 처음문자를 대문자로 변경함.
     *
     * @param messageList
     */
    private void modifyId(List<Message> messageList) {
        for (Message message : messageList) {
            List<DataField> dataFields = message.getDataFields();
            for (DataField dataField : dataFields) {
                String id = dataField.getFieldId();
                if ("common".equals(id) == false) {
                    dataField.setFieldId(toFirstCapital(id));
                }
            }
        }
    }

    private String toFirstCapital(String id) {
        char[] chars = id.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public void setMessageFormatFileName(String messageFormatFileName) {
        this.messageFormatFileName = messageFormatFileName;
    }


    //    @PostConstruct
//    public boolean initFactory() throws Exception {
//        if ("NO".equals(messageFormatFileName) == false) {
//            return initFactory(messageFormatFileName);
//        } else {
//            return false;
//        }
//    }

    private void setupRefInfo(List<Message> messageList) throws Exception {
        /**
         * 메세지 -> ref참조 injection 와 각 메세지 사전 validation
         */
        for (Message messageFormat : messageList) {
            messageFormat.extendRefInfoAndValidate(messageFormatsById);
        }
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    private void setupSomeMap(List<Message> messageList) throws Exception {
        /**
         * 메세지들 -> 메세지맵
         */
        for (Message message : messageList) {
            if (messageFormatsById.containsKey(message.getId())) {
                throw new SystemException("XML정의된 메시지중 중복된 메시지 ID가 존재합니다. ID : " + message.getId());
            }

            if (messageFormatsByTranTypeCode.containsKey(message.getTranTypeCode())) {
                throw new SystemException("XML정의된 메시지중 중복된 메시지 TranTypeCode가 존재합니다. TranTypeCode : " + message.getTranTypeCode());
            }

            messageFormatsById.put(message.getId(), message);

            if (!"common".equals(message.getId())) {
                String tranTypeCode = message.getTranTypeCode();
                messageFormatsByTranTypeCode.put(tranTypeCode, message);
                messageLengthByTranTypeCode.put(tranTypeCode, message.msgSize());
            }
        }
    }

    private void setupMessageInstanceMap(List<Message> messageList) throws Exception {
        for (Message message : messageList) {
            if (!"common".equals(message.getId())) {
                message.setSize(message.msgSize());
                LOG.info(message.toString());
                String tranTypeCode = message.getTranTypeCode();
                messageInstanceByTranTypeCode.put(tranTypeCode, message.createMessage());
            }
        }
    }

    private Messages fromXml(String messageFormatFileName) throws SystemException {

        try {
            XStream xstream = setupXstream();
            ClassPathResource resource = new ClassPathResource(messageFormatFileName);
            LOG.info("resource]" + resource.getURL().getPath());
            return (Messages) xstream.fromXML(resource.getInputStream());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new SystemException("XML 구조가 이상합니다. message.xml 파일 우클릭->validation 하십시오.", e);
        }
    }

    private void copyFile(ClassPathResource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            File somethingFile = File.createTempFile("message", ".xml");
            FileUtils.copyInputStreamToFile(inputStream, somethingFile);
        }
    }

    private XStream setupXstream() {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.addPermission(NullPermission.NULL);
        xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        xstream.allowTypeHierarchy(List.class);
        xstream.allowTypesByWildcard(new String[]{Messages.class.getPackage().getName() + ".*"});
        xstream.registerConverter(new MessageFormatConverter());
        //
        xstream.setClassLoader(Messages.class.getClassLoader());
        xstream.addImplicitCollection(Messages.class, "messageList");
        xstream.alias("messages", Messages.class);
        xstream.alias("message", Message.class);
        xstream.alias("field", DataField.class);

        return xstream;
    }

    //    @Deprecated
//    /**
//     * ByteBuf를 사용하여 디코딩한다.
//     */
//    public MsgIF decode(String msg) {
//        try {
//            String tranTypeCode = getTranTypeCode(msg);
//            Message message = getMessageFormatByTranTypeCode(tranTypeCode);
//            if (message != null) {
//                ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes(charset));
//                MsgIF damoaMessage = getMessageObject(tranTypeCode);
//                message.decode(buf, msg, damoaMessage,charset);
//                return damoaMessage;
//            } else {
//                LOG.error("[tranTypeCode]" + tranTypeCode + "에 해당하는 전문정보가 없음..");
//                return null;
//            }
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//            return null;
//        }
//    }
    public MsgIF decode(final String message) {
        final String tranTypeCode;
        if (message.startsWith("HDR")) {
            tranTypeCode = message.substring(0, 10);
        } else {
            tranTypeCode = getTranTypeCode(message);
        }

        return decode(message, tranTypeCode);
    }

    public MsgIF decode(String msg, String tranTypeCode) {
        try {
            Message message = getMessageFormatByTranTypeCode(tranTypeCode);
            if (message != null) {
                MsgIF damoaMessage = getMessageObject(tranTypeCode);
                LOG.debug("decode " + damoaMessage.getClass().getName());
                message.decode(msg, damoaMessage, charset);
                LOG.debug("decode " + damoaMessage);
                return damoaMessage;
            } else {
                LOG.error("[tranTypeCode]" + tranTypeCode + "에 해당하는 전문정보가 없음..");
                return null;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public int validateFormat(MsgIF msgIF) {
        String tranTypeCode = getTranTypeCode(msgIF);
        Message message = getMessageFormatByTranTypeCode(tranTypeCode);
        List<DataField> dataFields = message.getDataFields();
        for (DataField dataField : dataFields) {
            try {
                if ("M".equalsIgnoreCase(dataField.getReqChk())) {
                    String value = dataField.getValue(msgIF, charset);
                    String fieldType = dataField.getFieldType();
                    if ("AN".equalsIgnoreCase(fieldType)) {
                        if (StringUtils.isAlphanumeric(value) == false) {
                            return dataField.getIndex();
                        }
                    } else if ("N".equalsIgnoreCase(fieldType)) {
                        if (StringUtils.isNumeric(value)==false) {
                            return dataField.getIndex();
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
                return dataField.getIndex();
            }
        }

        return -1;
    }

    public int findFormatIndex(MsgIF msgIF, String fieldId) {
        String tranTypeCode = getTranTypeCode(msgIF);
        Message message = getMessageFormatByTranTypeCode(tranTypeCode);
        DataField dataField = findDataField(message.getDataFields(), fieldId);
        if (dataField == null) {
            return 9999;
        } else {
            return dataField.getIndex();
        }
    }


    private DataField findDataField(List<DataField> dataFields, String fieldId) {
        for (DataField dataField : dataFields) {
            if (fieldId.equalsIgnoreCase(dataField.getFieldId())) {
                return dataField;
            }
        }

        return null;
    }
    public List<Layout> parse(String msg) {
        List<Layout> layouts = new ArrayList<>();
        try {
            String tranTypeCode = getTranTypeCode(msg);
            Message message = getMessageFormatByTranTypeCode(tranTypeCode);
            if (message != null) {
                byte[] bytes = msg.getBytes(charset);
                layouts.add(new Layout(message.getId(), message.getTranTypeCode(), "", message.getDesc(), bytes.length + ""));
                ByteBuf buf = Unpooled.copiedBuffer(bytes);
                message.parse(buf, layouts, charset);
            } else {
                LOG.error("[tranTypeCode]" + tranTypeCode + "에 해당하는 전문정보가 없음..");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            layouts.add(new Layout(e.getMessage(), ".", "파싱할수없음.", "파싱에러.", ""));
        }
        return layouts;
    }

    private MsgIF getMessageObject(String tranTypeCode) {
        try {
            MsgIF msgIF = messageInstanceByTranTypeCode.get(tranTypeCode);
            return (MsgIF) msgIF.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encode(MsgIF msgIF) throws Exception {
        String tranTypeCode = getTranTypeCode(msgIF);

        return encode(msgIF, tranTypeCode);
    }

    public String encode(MsgIF msgIF, String tranTypeCode) throws Exception {
        LOG.debug("tranTypeCode=]" + tranTypeCode + "[");
        Message message = getMessageFormatByTranTypeCode(tranTypeCode);
        if (message == null) {
            LOG.error("[tranTypeCode]" + tranTypeCode + "에 해당하는 전문정보가 없음..");
            viewMessageFormatByTranTypeCode();
            return null;
        }

        return message.encode(msgIF, charset);
    }

    private void viewMessageFormatByTranTypeCode() {
        for (String key : messageFormatsByTranTypeCode.keySet()) {
            LOG.info("KEY ]" + key + "[ " + messageFormatsByTranTypeCode.get(key));
        }
    }

    public String encodeStartWithLength(MsgIF msgIF, String tranTypeCode) throws Exception {
        String result = encode(msgIF, tranTypeCode);
        return Damoas.appendLength(result, charset);
    }

    public String encode(Message message, String field, String value) {
        try {
            return message.encode(field, value, charset);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return value;
        }
    }

    public String keys(MsgIF msgIF) throws Exception {
        String tranTypeCode = getTranTypeCode(msgIF);
        Message message = getMessageFormatByTranTypeCode(tranTypeCode);
        String[] keys = msgIF.keyFields();
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (String key : keys) {
            DataField dataField = message.findDataField(key);
            if (dataField != null) {
                String _value = dataField.getValue(msgIF, charset);
                if (index == 0) {
                    builder.append(_value);
                } else {
                    builder.append("_").append(_value);
                }
                index++;
            }
        }
        return builder.toString();
    }


    public MsgIF createMessage(String msgTypeCode, String dealTypeCode) throws Exception {
        MsgIF msgIF = createMessage(msgTypeCode + dealTypeCode);
//        damoaMessage.setMsgTypeCode(msgTypeCode);
//        damoaMessage.setDealTypeCode(dealTypeCode);
        return msgIF;
    }

    public MsgIF createMessage(String tranTypeCode) throws Exception {
        Message message = getMessageFormatByTranTypeCode(tranTypeCode);
        return message.createMessage();
    }

    public MsgIF createMessage(Code code) throws Exception {
        return createMessage(code.getId());
    }

    public String getTranTypeCode(MsgIF msgIF) {
        return msgIF.getMsgTypeCode() + msgIF.getDealTypeCode();
    }


    public String getTranTypeCode(String msg) {
        return tranTypeCodeResolver.getTranTypeCode(msg);
    }

    public void setTranTypeCodeResolver(TranTypeCodeResolver tranTypeCodeResolver) {
        this.tranTypeCodeResolver = tranTypeCodeResolver;
    }

    public void initializeMessage(MessageInitializer messageInitializer) {
        messageInitializer.initialize(messageInstanceByTranTypeCode);
        setTranTypeCodeResolver(messageInitializer.getTranTypeCodeResolver());
    }

    /**
     * 응답메세지 생성.
     * xml 설정에 따라 값을 처리
     * 전문유형에 따라 기본적인 값 설정
     *
     * @param msgIF 원본전문
     * @return 응답전문
     */
    public MsgIF response(MsgIF msgIF) {
        try {
            MsgIF response = (MsgIF) msgIF.clone();
//        MsgIF response = SerializationUtils.clone(msgIF);
            response.response();
            return response;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }


    /**
     * Xml 설정에 있는 resChk 에 따라서 복사는 그대로 두고 나머지는 추후 설정해야 하므로 값을 없앤다.
     * 살펴보니 추후 설정값도 이전값을 알아야 처리할 수 있으므로 아래 처리는 하지 않는 걸로...
     *
     * @param response
     */
    private void setpuResponse(MsgIF response) {
        Message message = getMessageFormatByTranTypeCode(response.getType());
        List<DataField> dataFields = message.getDataFields();
        for (DataField dataField : dataFields) {
            dataField.setpuResponse(response);
        }
    }

    private Message findMessage(String key) {
        return getMessageFormatById(key);
    }

    public Charset getCharset() {
        return charset;
    }


    public String encodeCorpInfo(CorpInfoMsg msg) throws Exception {
        StringBuilder result = new StringBuilder();
        Head head = msg.getHead();
        Message message = findMessage("Head");
        result.append(message.encode(head, charset)).append("\r\n");

        message = findMessage("Data");
        List<Data> dataList = msg.getDataList();
        for (Data data : dataList) {
            result.append(message.encode(data, charset)).append("\r\n");
        }
        message = findMessage("Trailer");
        Trailer trailer = msg.getTrailer();
        result.append(message.encode(trailer, charset)).append("\r\n");

        return result.toString();
    }

    public CorpInfoMsg decodeCorpInfo(String msg) {
        String[] lines = StringUtils.split(msg, "\r\n");
        return decodeCorpInfo(lines);
    }

    public CorpInfoMsg decodeCorpInfo(String[] lines) {
        CorpInfoMsg corpInfoMsg = new CorpInfoMsg();
        for (String line : lines) {
            if (line.startsWith("H")) {
                Head head = new Head();
                Message message = findMessage("Head");
                message.decode(line, head, charset);
                corpInfoMsg.setHead(head);
            } else if (line.startsWith("D")) {
                Message message = findMessage("Data");
                Data data = new Data();
                message.decode(line, data, charset);
                corpInfoMsg.add(data);
            } else if (line.startsWith("T")) {
                Message message = findMessage("Trailer");
                Trailer trailer = new Trailer();
                message.decode(line, trailer, charset);
                corpInfoMsg.setTrailer(trailer);
            } else {

            }
        }
        return corpInfoMsg;
    }

    public static void main(String[] args) throws Exception {
        Charset _charset = Charset.forName("KSC5601");
        MessageFactory messageFactory = new MessageFactory();
        messageFactory.initFactory("message2.xml", _charset);
        messageFactory.setCharset(_charset);
        messageFactory.setTranTypeCodeResolver(new TranTypeCodeResolver() {

            @Override
            public String getTranTypeCode(String msg) {
                byte[] bytes = msg.getBytes(_charset);
                byte[] result = new byte[7];
                System.arraycopy(bytes, 21, result, 0, 7);
                String _result = new String(result, _charset);
                System.out.println("trantype " + _result);
                return _result;
            }
        });
        String msg = "transacti다모아      90001009999000000000000000000100";
        System.out.println(msg.getBytes(_charset).length);
        MsgIF msgIF = messageFactory.decode(msg);
        System.out.println("msgIF " + msgIF);
        String _msg = messageFactory.encode(msgIF);
        System.out.println(_msg);
        if (msg.equals(_msg)) {
            System.out.println("OK");
        } else {
            System.out.println("NO");
        }
    }
}
