package kr.co.finger.msgio.layout;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import kr.co.finger.damoa.commons.io.HexUtil;
import kr.co.finger.msgio.cash.CashMessage;
import kr.co.finger.msgio.cash.Data;
import kr.co.finger.msgio.cash.Head;
import kr.co.finger.msgio.cash.Trailer;
import kr.co.finger.msgio.corpinfo.CorpInfo;
import kr.co.finger.msgio.exception.MsgException;
import kr.co.finger.msgio.msg.BaseMsg;
import kr.co.finger.msgio.msg.EI13;
import kr.co.finger.msgio.msg.EvidenceData;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전문 관리 클래스
 */
public class LayoutFactory {

    private String messageFormatFileName;

    private Charset charset;

    private final Logger LOG = LoggerFactory.getLogger(LayoutFactory.class);

    /**
     * 메세지 정보 by Id
     */
    private Map<String, Message> messageFormatsById = new HashMap<String, Message>();
    private Map<String, Object> messageInstanceByMsgType = new HashMap<String, Object>();


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
        Messages messages = fromXml(messageFormatFileName);
        List<Message> messageList = messages.getMessageList();
        for (Message message : messageList) {
            message.initMsgSize();
            List<DataField> dataFields = message.getDataFields();
            for (DataField dataField : dataFields) {
                LOG.info(dataField.toString());
            }
            LOG.info(message.toString());
            LOG.info(":::::::::::::::::::::::::::::::::::::\n");
        }

        LOG.info("다모아 전문 로딩 성공 : [" + messages.getMessageList().size() + "]");

        modifyId(messageList);
        setupSomeMap(messageList);
        setupRefInfo(messageList);
        setupMessageInstanceMap(messageList);
        LOG.info("다모아 전문 초기화 완료");

        return true;
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
                dataField.setFieldId(toFirstCapital(id));
            }
        }
    }

    private void setupRefInfo(List<Message> messageList) throws Exception {
        /**
         * 메세지 -> ref참조 injection 와 각 메세지 사전 validation
         */
        for (Message messageFormat : messageList) {
        }
    }

    private String toFirstCapital(String id) {
        char[] chars = id.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }


    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    private void setupSomeMap(List<Message> messageList) throws Exception {
        /**
         * 메세지들 -> 메세지맵
         */
        for (Message message : messageList) {
            if (messageFormatsById.containsKey(message.getId())) {
                throw new MsgException("XML정의된 메시지중 중복된 메시지 ID가 존재합니다. ID : " + message.getId());
            }
            messageFormatsById.put(message.getId(), message);
        }
    }

    private void setupMessageInstanceMap(List<Message> messageList) throws Exception {
        for (Message message : messageList) {
            String id = message.getId();

            messageInstanceByMsgType.put(id, message.createMessage());
        }
    }

    private Messages fromXml(String messageFormatFileName) throws MsgException {

        try {
            XStream xstream = setupXstream();
            ClassPathResource resource = new ClassPathResource(messageFormatFileName);
            LOG.info("resource]" + resource.getURL().getPath());
            return (Messages) xstream.fromXML(resource.getInputStream());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new MsgException("XML 구조가 이상합니다. message.xml 파일 우클릭->validation 하십시오.", e);
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
        xstream.setClassLoader(Messages.class.getClassLoader());
        xstream.addImplicitCollection(Messages.class, "messageList");
        xstream.alias("messages", Messages.class);
        xstream.alias("message", Message.class);
        xstream.alias("field", DataField.class);

        return xstream;
    }

    public Object decode(String msg, String msgType) {
        try {
            byte[] bytes = msg.getBytes(charset);
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            Message message = getMessageFormatById(msgType);
            Object target = getMessageObject(message.getId());
            if (message != null) {
                List<DataField> dataFields = message.getDataFields();
                for (DataField dataField : dataFields) {
                    LOG.info(dataField.toString());
                    String ref = dataField.getFieldRef();
                    String id = dataField.getFieldId();
                    if ("dataList".equalsIgnoreCase(id)) {
                        Message _message = getMessageFormatById(ref);
                        int count = parseDataCount(bytes.length, _message.getMsgSize());
                        System.out.println("dataListSize " + count);
                        for (int i = 0; i < count; i++) {
                            Object o = getMessageObject(ref);
                            _message.decode(buf, o, charset);
                            setValue(target, "add" + id, o);
                        }
                    } else {
                        Message _message = getMessageFormatById(ref);
                        Object o = getMessageObject(ref);
                        _message.decode(buf, o, charset);
                        setValue(target, "set" + id, o);
                    }
                }

                return target;
            } else {
                LOG.error("[msgType]" + msgType + "에 해당하는 전문정보가 없음..");
                return null;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public EI13 decodeToEI13(byte[] bytes, String msgType) {
        try {
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            Message message = getMessageFormatById(msgType);
            EI13 target = new EI13();
            if (message != null) {

                List<DataField> dataFields = message.getDataFields();
                for (DataField dataField : dataFields) {
                    String ref = dataField.getFieldRef();
                    String id = dataField.getFieldId();
                    if ("dataList".equalsIgnoreCase(id)) {
                        Message _message = getMessageFormatById(ref);
                        int count = Integer.valueOf(target.getHeader().getTotalEvidenceCount());
                        for (int i = 0; i < count; i++) {
                            EvidenceData o = new EvidenceData();
                            _message.decode(buf, o, charset);
                            target.addDataList(o);
                        }
                    } else {
                        Message _message = getMessageFormatById(ref);
                        Object o = getMessageObject(ref);
                        _message.decode(buf, o, charset);
                        setValue(target, "set" + id, o);

                    }
                }
                return target;
            } else {
                LOG.error("[msgType]" + msgType + "에 해당하는 전문정보가 없음..");
                return null;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * dataList 사이즈를 구함.
     * 전문 전체크기를 각 레코드 크기로 나누고 헤더,트레일러를 뺀 결과...
     *
     * @param size
     * @param v
     * @return
     */
    private int parseDataCount(int size, int v) {
        return size / v - 2;
    }

    private Object getMessageObject(String msgType) {
        try {
            BaseMsg msgIF = (BaseMsg) messageInstanceByMsgType.get(msgType);
            Object result = msgIF.clone();
            System.out.println(msgIF + "\t" + result);
            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encode(Object o, String msgType) throws Exception {
        LOG.debug("msgType=" + msgType);
        StringBuilder result = new StringBuilder();
        Message message = getMessageFormatById(msgType);
        for (DataField dataField : message.getDataFields()) {
            String ref = dataField.getFieldRef();
            String id = dataField.getFieldId();
            if ("dataList".equalsIgnoreCase(id)) {
                //데이터부 처리.
                List<Object> dataList = (List<Object>) getValue(o, "getDataList");
                for (Object data : dataList) {
                    String value = encode(data, getMessageFormatById(ref), charset);
                    result.append(value);
                }
            } else {
                //헤더와 트레일러 처리.
                String value = encode(o, id, getMessageFormatById(ref), charset);
                result.append(value);
            }
        }
        return result.toString();
    }

    public byte[] encodeToByteArray(EI13 o, String msgType) throws Exception {
        LOG.debug("msgType=" + msgType);
        Message message = getMessageFormatById(msgType);
        List<byte[]> bytes = new ArrayList<>();

        for (DataField dataField : message.getDataFields()) {
            String ref = dataField.getFieldRef();
            String id = dataField.getFieldId();
            if ("dataList".equalsIgnoreCase(id)) {
                //데이터부 처리.
                List<Object> dataList = (List<Object>) getValue(o, "getDataList");
                for (Object data : dataList) {
                    bytes.add(encodeToByteArray(data, getMessageFormatById(ref), charset));
                }
            } else {
                //헤더와 트레일러 처리.
                bytes.add(encodeToByteArray(o, id, getMessageFormatById(ref), charset));
            }
        }

        return HexUtil.concat(bytes);
    }


    private String encode(Object o, String id, Message message, Charset charset) throws Exception {
        return message.encode(getValue(o, "get" + id), charset);
    }

    private String encode(Object o, Message message, Charset charset) throws Exception {
        return message.encode(o, charset);
    }

    private byte[] encodeToByteArray(Object o, String id, Message message, Charset charset) throws Exception {
        return message.encodeToByteArray(getValue(o, "get" + id), charset);
    }

    private byte[] encodeToByteArray(Object o, Message message, Charset charset) throws Exception {
        return message.encodeToByteArray(o, charset);
    }

    private Object getValue(Object o, String methodName) throws Exception {
        return MethodUtils.invokeMethod(o, methodName);
    }

    private void setValue(Object o, String methodName, Object value) throws Exception {
        MethodUtils.invokeMethod(o, methodName, value);
    }


    public String encodeCash(CashMessage cashMessage) throws Exception {

        StringBuilder result = new StringBuilder();
        Head head = cashMessage.getHead();
        Message message = findMessage("Head");
        result.append(message.encode(head, charset)).append("\n");

        message = findMessage("Data");
        List<Data> dataList = cashMessage.getDataList();
        for (Data data : dataList) {
            result.append(message.encode(data, charset)).append("\n");
        }
        message = findMessage("Trailer");
        Trailer trailer = cashMessage.getTrailer();
        result.append(message.encode(trailer, charset)).append("\n");

        return result.toString();
    }
    public String encodeCash(Object o,String key) throws Exception {

        StringBuilder result = new StringBuilder();
        Message message = findMessage(key);
        result.append(message.encode(o, charset)).append("\n");
        return result.toString();
    }
    private Message findMessage(String key) {
        return getMessageFormatById(key);
    }

    public static void main(String[] args) throws Exception {
        Charset _charset = Charset.forName("KSC5601");
        LayoutFactory layoutFactory = new LayoutFactory();
        layoutFactory.initFactory("message.xml", _charset);
    }


    public CashMessage decodeCash(List<String> lines) throws Exception {
        CashMessage cashMessage = new CashMessage();
        for (String line : lines) {
            if (line.startsWith("10")) {
                Head head = new Head();
                Message message = findMessage("Head");
                message.decode(line,head, charset);
                cashMessage.setHead(head);
            } else if (line.startsWith("20")) {
                Message message = findMessage("Data");
                Data data = new Data();
                message.decode(line,data, charset);
                cashMessage.addData(data);
            } else if (line.startsWith("30")) {
                Message message = findMessage("Trailer");
                Trailer trailer = new Trailer();
                message.decode(line,trailer, charset);
                cashMessage.setTrailer(trailer);
            } else {

            }
        }
        return cashMessage;
    }

    /**
     * 기관정보객체 문자열로 변환..
     * 한글이 들어가지만 전반각 처리하지 않으므로 encodeWithoutFullHalfProcess 로 처리.
     * 한글이 없으면 굳이 encode를 사용해도 됨..
     * @param corpInfo
     * @return
     * @throws Exception
     */
    public String encodeCorpInfo(CorpInfo corpInfo) throws Exception {

        StringBuilder result = new StringBuilder();
        kr.co.finger.msgio.corpinfo.Head head = corpInfo.getHead();
        Message message = findMessage("Head");
        result.append(message.encodeWithoutFullHalfProcess(head, charset)).append("\r\n");

        message = findMessage("Data");
        List<kr.co.finger.msgio.corpinfo.Data> dataList = corpInfo.getDataList();
        for (kr.co.finger.msgio.corpinfo.Data data : dataList) {
            result.append(message.encodeWithoutFullHalfProcess(data, charset)).append("\r\n");
        }
        message = findMessage("Trailer");
        kr.co.finger.msgio.corpinfo.Trailer trailer = corpInfo.getTrailer();
        result.append(message.encodeWithoutFullHalfProcess(trailer, charset)).append("\r\n");

        return result.toString();
    }

    /**
     * 기관정보파일 기관정보 객체로 변환..
     * 한글이 있지만 전반각이 없으므로 decodeWithoutFullHalfProcess 사용함.
     * 한글이 없으면 decode를 사용해도 됨..
     * @param lines
     * @return
     * @throws Exception
     */
    public CorpInfo decodeCorpInfo(List<String> lines) throws Exception {
        CorpInfo corpInfo = new CorpInfo();
        for (String line : lines) {
            if (line.startsWith("H")) {
                kr.co.finger.msgio.corpinfo.Head head = new kr.co.finger.msgio.corpinfo.Head();
                Message message = findMessage("Head");
                message.decodeWithoutFullHalfProcess(line,head, charset);
                corpInfo.setHead(head);
            } else if (line.startsWith("D")) {
                Message message = findMessage("Data");
                kr.co.finger.msgio.corpinfo.Data data = new kr.co.finger.msgio.corpinfo.Data();
                message.decodeWithoutFullHalfProcess(line,data, charset);
                corpInfo.add(data);
            } else if (line.startsWith("T")) {
                Message message = findMessage("Trailer");
                kr.co.finger.msgio.corpinfo.Trailer trailer = new kr.co.finger.msgio.corpinfo.Trailer();
                message.decodeWithoutFullHalfProcess(line,trailer, charset);
                corpInfo.setTrailer(trailer);
            } else {

            }
        }
        return corpInfo;
    }


}
