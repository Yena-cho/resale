package kr.co.finger.msgagent.layout;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import io.netty.buffer.ByteBuf;
import kr.co.finger.damoa.model.msg.MsgIF;
import kr.co.finger.msgagent.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * BioMessageFormat.xml의 <message> 메세지 정보
 */
@XStreamAlias("message")
public class Message {

    /**
     * 'N' pattern
     */
    private final Pattern patternNum = Pattern.compile("[0-9]*");

    /**
     * 메세지 아이디
     */
    @XStreamAlias("id")
    @XStreamAsAttribute
    private String id;

    /**
     * 거래구분코드
     */
    @XStreamAlias("tranTypeCode")
    @XStreamAsAttribute
    private String tranTypeCode;

    /**
     * recieve Vo Type(수신 처리시 Vo Class)
     */
    @XStreamAlias("recieveVoType")
    @XStreamAsAttribute
    private String recieveVoType;

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
    @XStreamOmitField
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    /**
     * tranTypeCode
     *
     * @return the tranTypeCode
     */
    public String getTranTypeCode() {
        return tranTypeCode;
    }

    /**
     * tranTypeCode
     *
     * @param tranTypeCode the tranTypeCode to set
     */
    public void setTranTypeCode(String tranTypeCode) {
        this.tranTypeCode = tranTypeCode;
    }

    /**
     * recieveVoType
     *
     * @return the recieveVoType
     */
    public String getRecieveVoType() {
        return recieveVoType;
    }

    /**
     * recieveVoType
     *
     * @param recieveVoType the recieveVoType to set
     */
    public void setRecieveVoType(String recieveVoType) {
        this.recieveVoType = recieveVoType;
    }
    /**
     * desc
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * desc
     *
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
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

    /**
     * ref자료형 확장
     *
     * @param messageFormatsById
     * @throws SystemException
     */
    public void extendRefInfoAndValidate(Map<String, Message> messageFormatsById) throws SystemException {
        if (StringUtils.isEmpty(id)) {
            throw new SystemException("XML - message에 id 항목이 누락되었습니다.");
        }

        /**
         * type 항목
         */
        String[] types = {"A", "N", "AN", "ANSH","AH"};

        boolean fieldEnter = false;
        List<DataField> dataFieldsRefCopy = new ArrayList<DataField>();
        for (DataField field : dataFields) {
            if (!StringUtils.isEmpty(field.getFieldRef())) {
                /**
                 * XML - ref Append의 일관성과 가시성을 위해서 ref와 field 가 섞이는 것을 제한
                 */
                if (fieldEnter) {
                    throw new SystemException("XML - ref는 field 이후에 참조될 수 없습니다.");
                }

                Message mf = messageFormatsById.get(field.getFieldRef());
                List<DataField> refFields = mf.getDataFields();
                for (DataField dataField : refFields) {
                    dataFieldsRefCopy.add(dataField);
                }
            } else {
                fieldEnter = true;
            }
        }

        /**
         * XML field 매핑 Validation
         */

        for (DataField field : dataFields) {
            if (StringUtils.isEmpty(field.getFieldRef())) {
                String id = field.getFieldId();
                String length = field.getFieldLength()+"";
                String type = field.getFieldType();
                String reqChk = field.getReqChk();
                String resChk = field.getResChk();

                if (StringUtils.isEmpty(id)) {
                    throw new SystemException("XML - field에 id 항목이 누락되었습니다.");
                }

                if (StringUtils.isEmpty(length)) {
                    throw new SystemException("XML - field에 length 항목이 누락되었습니다.");
                }

                if (!"V".equals(length) && !patternNum.matcher(length).matches()) {
                    throw new SystemException("XML - length 설정은 가변형(V) 혹은 숫자만 숫자만 가능합니다.");
                }

                if (StringUtils.isEmpty(type)) {
                    throw new SystemException("XML - field에 type 항목이 누락되었습니다.");
                }

                if (StringUtils.isEmpty(reqChk)) {
//                    throw new SystemException("XML - field에 reqChk(요청필수여부) 항목이 누락되었습니다.");
                }

//                if (!("M".equals(reqChk) || "NE".equals(reqChk) || "-".equals(reqChk))) {
//                    throw new SystemException("XML - field에 reqChk(요청필수여부) 설정은 'M'(필수), 'NE'(모든), '-'(공백필수) 만 가능합니다.");
//                }

//                if (StringUtils.isEmpty(resChk)) {
//                    throw new SystemException("XML - field에 resChk(응답필수여부) 항목이 누락되었습니다.");
//                }

//                if (!("M".equals(resChk) || "".equals(resChk))) {
//                    throw new SystemException("XML - field에 resChk(요청필수여부) 설정은 'M'(변경), ''(복사) 만 가능합니다.");
//                }

                boolean containsType = false;
                for (String typeValue : types) {
                    if (typeValue.equals(type)) {
                        containsType = true;
                        break;
                    }
                }

                if (!containsType) {
                    String message = "XML 의 type형식은 ";
                    for (String typeValue : types) {
                        message += "'";
                        message += typeValue;
                        message += "' ";
                    }
                    message += "만 가능합니다.";

                    throw new SystemException(message);
                }

                /**
                 * 정상필드 등록
                 */
                dataFieldsRefCopy.add(field);
            }
        }

        /**
         * ref 정보 갱신
         */
        dataFields.clear();
        dataFields.addAll(dataFieldsRefCopy);

        for (int i = 0; i < dataFields.size(); i++) {
            String fieldId = dataFields.get(i).getFieldId();
            for (int j = 0; j < dataFields.size(); j++) {
                if (i != j) {
                    if (fieldId.equals(dataFields.get(j).getFieldId())) {
                        throw new SystemException("XML 의 단일부에 중복된 id[" + fieldId + "]가 있습니다.");
                    }
                }
            }
        }

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("tranTypeCode", tranTypeCode)
                .append("recieveVoType", recieveVoType)
                .append("desc", desc)
                .append("size", size)
                .toString();
    }


    public void decode(String message,MsgIF msgIF,Charset charset) throws Exception {
        byte[] bytes = toBytes(message, charset);
        int offset = 0;
        for (DataField dataField : dataFields) {
            offset = dataField.setup(msgIF,bytes,offset,charset);
        }
        msgIF.setType(tranTypeCode);
        msgIF.setOriginMessage(message);
        msgIF.setDesc(desc);
    }

    public void decode(String message, Object target, Charset charset) {
        byte[] bytes = toBytes(message, charset);
        int offset = 0;
        for (DataField dataField : dataFields) {
            offset = dataField.setup(target,bytes,offset,charset);
        }
    }
    public MsgIF createMessage() throws Exception {
        Class<?> c = Class.forName(recieveVoType);
        MsgIF msgIF = (MsgIF)ConstructorUtils.invokeConstructor(c);
        LOG.debug(recieveVoType+" "+msgSize());
        msgIF.setLength(msgSize());
        return msgIF;
    }

    private String[] split(String tranTypeCode) {
        String[] strings = new String[2];
        strings[0] = tranTypeCode.substring(0, 4);
        strings[1] = tranTypeCode.substring(3, 7);

        return strings;
    }
    public String encode(Object msgIF, Charset charset) throws Exception {
        int msgSize = msgSize();
        StringBuilder builder = new StringBuilder();
        encode(builder, msgIF,charset);
        String result = builder.toString();
        return result;
    }


    private void encode(StringBuilder builder, Object msgIF,Charset charset) throws Exception {
        for (DataField dataField : dataFields) {
                dataField.setString(builder, msgIF,charset);
        }
    }
    public int msgSize() {
        int size = 0;
        for (DataField dataField : dataFields) {
            size+=dataField.getFieldLength();
        }

        return size;
    }

    public void parse(ByteBuf buf, List<Layout> layouts, Charset charset) {
        for (DataField field : dataFields) {
            field.parse(buf,layouts,charset);
        }
    }

    public byte[] toBytes(String message,Charset charset) {
        return message.getBytes(charset);
    }

    public String encode(String field, String value,Charset charset) throws Exception {
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
}
