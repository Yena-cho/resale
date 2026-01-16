package kr.co.finger.msgio.layout;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import kr.co.finger.msgio.msg.EvidenceData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * BioMessageFormat.xml의 <message> 메세지 정보
 */
@XStreamAlias("message")
public class Message {


    /**
     * 메세지 아이디
     */
    @XStreamAlias("id")
    @XStreamAsAttribute
    private String id;


    private int msgSize;
//    /**
//     * 거래구분코드
//     */
//    @XStreamAlias("tranTypeCode")
//    @XStreamAsAttribute
//    private String tranTypeCode;

    @XStreamAlias("msgType")
    @XStreamAsAttribute
    private String msgType;

    /**
     * 설명
     */
    @XStreamAlias("desc")
    @XStreamAsAttribute
    private String desc;

    /**
     * dataField 단일부
     */
    @XStreamAlias(value = "field")
    @XStreamImplicit(itemFieldName = "field")
    private List<DataField> dataFields;

    @XStreamOmitField
    private Logger LOG = LoggerFactory.getLogger(getClass());


    public int getMsgSize() {
        return msgSize;
    }

    public void setMsgSize(int msgSize) {
        this.msgSize = msgSize;
    }

    /**
     * id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * id
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the dataFields
     */
    public List<DataField> getDataFields() {
        return dataFields;
    }

    /**
     * dataFields
     *
     * @param dataFields the dataFields to set
     */
    public void setDataFields(List<DataField> dataFields) {
        this.dataFields = dataFields;
    }

    /**
     * dataFields
     *
     * @param dataField
     */
    public void addDataField(DataField dataField) {
        this.dataFields.add(dataField);
    }

    public void initMsgSize() {
        this.msgSize = msgSize();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("id='").append(id).append('\'');
        sb.append(", msgSize=").append(msgSize);
        sb.append(", msgType='").append(msgType).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void decode(String message, Object o, Charset charset) throws Exception {
        byte[] bytes = toBytes(message, charset);
        int offset = 0;
        for (DataField dataField : dataFields) {
            offset = dataField.decode(o, bytes, offset, charset);
        }
    }
    public void decodeWithoutFullHalfProcess(String message, Object o, Charset charset) throws Exception {
        byte[] bytes = toBytes(message, charset);
        int offset = 0;
        for (DataField dataField : dataFields) {
            offset = dataField.decodeWithoutFullHalfProcess(o, bytes, offset, charset);
        }
    }

    public void decode(ByteBuffer byteBuf, Object target, Charset charset) throws Exception {
        LOG.debug("[ID]"+id+"[MsgType]"+msgType);
        if ("EvidenceData".equalsIgnoreCase(id)) {
            decodeEvidenceData(byteBuf, target, charset);
        } else {

            for (DataField dataField : dataFields) {
                dataField.decode(target, byteBuf, charset);
            }
        }
    }

    private void decodeEvidenceData(ByteBuffer byteBuf, Object target, Charset charset) {
        EvidenceData evidenceData = (EvidenceData) target;
        for (DataField dataField : dataFields) {
            LOG.info(dataField.toString());
            if ("evidenceData".equalsIgnoreCase(dataField.getFieldId())) {
                int length = Integer.valueOf(evidenceData.getEvidenceLength());
                byte[] data = read(byteBuf, length);
                evidenceData.setupEvidenceData(data,LOG);
                LOG.debug(dataField.getFieldId() + " " + length + " value=].....[");
            } else if ("filler2".equalsIgnoreCase(dataField.getFieldId())) {
                dataField.setFieldLength(evidenceData.getFiller2Size());
                dataField.decode(evidenceData, byteBuf, charset);
            } else {
                dataField.decode(evidenceData, byteBuf, charset);
            }
        }
    }

    private String[] split(String tranTypeCode) {
        String[] strings = new String[2];
        strings[0] = tranTypeCode.substring(0, 4);
        strings[1] = tranTypeCode.substring(3, 7);

        return strings;
    }

    public String encode(Object o, Charset charset) throws Exception {
        StringBuilder builder = new StringBuilder();
        encode(builder, o, charset);
        String result = builder.toString();

        return result;
    }
    public String encodeWithoutFullHalfProcess(Object o, Charset charset) throws Exception {
        StringBuilder builder = new StringBuilder();
        encodeWithoutFullHalfProcess(builder, o, charset);
        String result = builder.toString();

        return result;
    }
    public byte[] encodeToByteArray(Object o, Charset charset) throws Exception {
        if ("EvidenceData".equalsIgnoreCase(id)) {
            return encodeEvidenceData(o, charset);
        } else {
            int size = getMsgSize();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            encode(byteBuffer, o, charset);
            byteBuffer.flip();
            return read(byteBuffer, size);
        }

    }

    private byte[] encodeEvidenceData(Object o, Charset charset) throws Exception {
        EvidenceData evidenceData = (EvidenceData) o;
        int size = evidenceData.size();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        for (DataField dataField : dataFields) {
            if ("evidenceData".equalsIgnoreCase(dataField.getFieldId())) {
                List<byte[]> bytes = evidenceData.getEvidenceDataList();
                for (byte[] _bytes : bytes) {
                    byteBuffer.put(_bytes);
                }
            } else if ("filler2".equalsIgnoreCase(dataField.getFieldId())) {
                dataField.setFieldLength(evidenceData.getFiller2Size());
                dataField.encode(byteBuffer, o, charset);
            } else {
                dataField.encode(byteBuffer, o, charset);
            }
        }

        LOG.debug("");
        byteBuffer.flip();
        byte[] bytes = read(byteBuffer, size);
        return bytes;
    }

    private byte[] read(ByteBuffer byteBuffer, int length) {
        byte[] bytes = new byte[length];
        byteBuffer.get(bytes);
        return bytes;
    }

    private void encode(StringBuilder builder, Object o, Charset charset) throws Exception {
        for (DataField dataField : dataFields) {
            dataField.encode(builder, o, charset);
        }
        LOG.debug("");
    }
    private void encodeWithoutFullHalfProcess(StringBuilder builder, Object o, Charset charset) throws Exception {
        for (DataField dataField : dataFields) {
            dataField.encodeWithoutFullHalfProcess(builder, o, charset);
        }
        LOG.debug("");
    }
    private void encode(ByteBuffer byteBuffer, Object o, Charset charset) throws Exception {
        for (DataField dataField : dataFields) {
            dataField.encode(byteBuffer, o, charset);
        }
    }

    public int msgSize() {
        int size = 0;
        for (DataField dataField : dataFields) {
            size += dataField.getFieldLength();
        }

        return size;
    }

    public byte[] toBytes(String message, Charset charset) {
        return message.getBytes(charset);
    }

    public String encode(String field, String value, Charset charset) throws Exception {
        DataField dataField = findDataField(field);
        if (dataField == null) {
            return value;
        } else {
            return dataField.getValue(value, charset);
        }
    }

    public DataField findDataField(String field) {
        for (DataField dataField : dataFields) {
            if (field.equalsIgnoreCase(dataField.getFieldId())) {
                return dataField;
            }
        }

        return null;
    }

    public Object createMessage() throws Exception {
        Class<?> c = Class.forName(msgType);
        Object msgIF = ConstructorUtils.invokeConstructor(c);
        return msgIF;
    }

}
