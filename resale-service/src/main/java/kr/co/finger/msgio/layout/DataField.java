package kr.co.finger.msgio.layout;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import kr.co.finger.damoa.commons.FullHalfWidthUtils;
import kr.co.finger.damoa.exception.InvalidValueException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static kr.co.finger.damoa.commons.io.HexUtil.concat;


/**
 * BioMessageFormat.xml의 <field> 정보
 */
@XStreamAlias("field")
public class DataField {
    /**
     * id
     */
    @XStreamAlias("id")
    @XStreamAsAttribute
    private String fieldId;

    /**
     * length
     */
    @XStreamAlias("length")
    @XStreamAsAttribute
    private int fieldLength;

    /**
     * type
     */
    @XStreamAlias("type")
    @XStreamAsAttribute
    private String fieldType;
    /**
     * ref
     */
    @XStreamAlias("ref")
    @XStreamAsAttribute
    private String fieldRef;
    /**
     * desc
     */
    @XStreamAlias("desc")
    @XStreamAsAttribute
    private String fieldDesc;

    @XStreamAlias("convertType")
    @XStreamAsAttribute
    private String convertType = "String";

    @XStreamOmitField
    private Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * fieldId
     *
     * @return fieldId
     */
    public String getFieldId() {
        return fieldId;
    }

    /**
     * fieldId
     *
     * @param fieldId
     */
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * fieldLength
     *
     * @return
     */
    public int getFieldLength() {
        return fieldLength;
    }

    /**
     * fieldLength
     *
     * @param fieldLength
     */
    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    /**
     * fieldType
     *
     * @return
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * fieldType
     *
     * @param fieldType
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * fieldDesc
     *
     * @return
     */
    public String getFieldDesc() {
        return fieldDesc;
    }

    /**
     * fieldDesc
     *
     * @param fieldDesc
     */
    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }


    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
    }


    public String getFieldRef() {
        return fieldRef;
    }

    public void setFieldRef(String fieldRef) {
        this.fieldRef = fieldRef;
    }

    private String getString(ByteBuffer buf, int length, Charset charset) {
        byte[] bytes = new byte[length];
        buf.get(bytes);
        return new String(bytes, charset);
    }

    public void decode(Object msg, ByteBuffer byteBuf, Charset charset) {
        try {
            Object o = getString(byteBuf, fieldLength, charset);
            LOG.debug(fieldId + " " + fieldLength + " value=]" + o + "[");
            if (o != null) {
                setValue(msg, o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int decode(Object src, byte[] bytes, int offset, Charset charset) {
        try {
            String value = new String(bytes, offset, fieldLength, charset);
            LOG.debug(fieldId + " " + fieldLength + " value=]" + value + "[");
            Object _value = getValue(value);
            if (_value != null) {
                setValue(src, _value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offset + fieldLength;
    }

    public int decodeWithoutFullHalfProcess(Object src, byte[] bytes, int offset, Charset charset) {
        try {
            String value = new String(bytes, offset, fieldLength, charset);
            LOG.debug(fieldId + " " + fieldLength + " value=]" + value + "[");
            Object _value = getValue(value);
            if (_value != null) {
                setValueWithoutFullHalfProcess(src, _value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offset + fieldLength;
    }

    public void setValue(Object src, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if ("AH".equalsIgnoreCase(fieldType)) {
            // 전각처리된 항목은 반각처리하여 ...
            String _value = (String) value;
            _value = FullHalfWidthUtils.fullWidthToHalfWidth(_value);
            MethodUtils.invokeMethod(src, getSetMethodName(), _value);
        } else {
            MethodUtils.invokeMethod(src, getSetMethodName(), value);
        }
    }

    public void setValueWithoutFullHalfProcess(Object src, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        MethodUtils.invokeMethod(src, getSetMethodName(), value);
    }

    private void setValue(Object src, String _value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        MethodUtils.invokeMethod(src, getSetMethodName(), _value.trim());
    }

    public String getSetMethodName() {
        return "set" + fieldId;
    }

    public String getGetMethodName() {
        return "get" + fieldId;
    }

    private Object getValue(String value) {
        Object o = null;
        if ("String".equals(convertType)) {
            o = value.trim();
        } else if ("Integer".equals(convertType)) {
            o = Integer.valueOf(value);
        } else if ("Long".equals(convertType)) {
            o = Long.valueOf(value);
        } else {
            o = value.trim();
        }
        return o;
    }


    public void encode(StringBuilder builder, Object o, Charset charset) throws Exception {
        String value = getValue(o, charset);
        LOG.debug(fieldId + " " + fieldLength + " value=]" + value + "[");
        int length = value.getBytes(charset).length;
        if (length != fieldLength) {
            throw new InvalidValueException(fieldId + " " + fieldLength + " " + value + " " + length);
        }
        builder.append(value);
    }
    public void encodeWithoutFullHalfProcess(StringBuilder builder, Object o, Charset charset) throws Exception {
        String value = getValuWithoutFullHalfProcess(o, charset);
        LOG.info(fieldId + " " + fieldLength + " value=]" + value + "[");
        int length = value.getBytes(charset).length;
        if (length != fieldLength) {
            throw new InvalidValueException(fieldId + " " + fieldLength + " " + value + " " + length);
        }
        builder.append(value);
    }
    public void encode(ByteBuffer byteBuffer, Object o, Charset charset) throws Exception {
        String value = getValue(o, charset);
        LOG.debug(fieldId + " " + fieldLength + " value=]" + value + "[");
        byteBuffer.put(value.getBytes(charset));
    }

//    public void setString2(ByteBuf buf,Object o,Charset charset) throws Exception {
//        String value = getValue(o,charset);
//        buf.writeBytes(value.getBytes(charset));
//    }

    /**
     * encode할 때 사용함.
     *
     * @param o
     * @return
     * @throws Exception
     */
    public String getValue(Object o, Charset charset) throws Exception {
        Object value = getValue(o, getGetMethodName());
        if (value == null) {
            return pad("", fieldLength, fieldType, charset);
        } else {
            return pad(value.toString(), fieldLength, fieldType, charset);
        }
    }
    public String getValuWithoutFullHalfProcess(Object o, Charset charset) throws Exception {
        Object value = getValue(o, getGetMethodName());
        if (value == null) {
            return padWithoutFullHalfProcess("", fieldLength, fieldType, charset);
        } else {
            return padWithoutFullHalfProcess(value.toString(), fieldLength, fieldType, charset);
        }
    }
    private String pad(String value, int length, String dataType, Charset charset) {
        if ("AN".equals(dataType)) {
            return byteArrayPad(value, length, " ", charset);
        } else if ("N".equals(dataType)) {
            return leftPad(value, length, "0");
        } else if ("AH".equals(dataType)) {
            value = FullHalfWidthUtils.halfWidthToFullWidth(value);
            return byteArrayPad(value, length, makeFullSpace(), charset);
        } else {
            return rightPad(value, length, " ");
        }
    }
    private String padWithoutFullHalfProcess(String value, int length, String dataType, Charset charset) {
        if ("AN".equals(dataType)) {
            return byteArrayPad(value, length, " ", charset);
        } else if ("N".equals(dataType)) {
            return leftPad(value, length, "0");
        } else if ("AH".equals(dataType)) {
            return byteArrayPad(value, length, makeFullSpace(), charset);
        } else {
            return rightPad(value, length, " ");
        }
    }
    private String makeFullSpace() {
//        char[] chars = new char[1];
//        chars[0] = (char) 12288;
//        return new String(chars);
        return " ";
    }

    private String rightPad(String value, int length, String padStr) {
        return StringUtils.rightPad(value, length, padStr);
    }

    private String leftPad(String value, int length, String padStr) {
        return StringUtils.leftPad(value, length, padStr);
    }


    private String byteArrayPad(String string, int minLength, String padString, Charset charset) {
        byte[] bytes = string.getBytes(charset);
        int size = minLength - bytes.length;
        if (size <= 0) {
            return string;
        } else {
            byte[] _bytes = padString.getBytes(charset);
            int count = (size / _bytes.length);
            byte[] results = bytes;
            for (int i = 0; i < count; i++) {
                results = concat(results, _bytes);
            }

            return new String(results, charset);
        }
    }


    //
    private Object getValue(Object o, String methodName) throws Exception {
        return MethodUtils.invokeMethod(o, methodName);
    }

    public Object getValue(Object o) throws Exception {
        return getValue(o, getGetMethodName());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataField{");
        sb.append("fieldId='").append(fieldId).append('\'');
        sb.append(", fieldLength=").append(fieldLength);
        sb.append(", fieldType='").append(fieldType).append('\'');
        sb.append(", fieldRef='").append(fieldRef).append('\'');
        sb.append(", fieldDesc='").append(fieldDesc).append('\'');
        sb.append(", convertType='").append(convertType).append('\'');
        sb.append('}');
        return sb.toString();
    }


}
