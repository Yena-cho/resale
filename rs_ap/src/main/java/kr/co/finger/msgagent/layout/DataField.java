package kr.co.finger.msgagent.layout;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import io.netty.buffer.ByteBuf;
import kr.co.finger.damoa.model.msg.MsgIF;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.List;

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
     * desc
     */
    @XStreamAlias("desc")
    @XStreamAsAttribute
    private String fieldDesc;

    /**
     * reqChk
     */
    @XStreamAlias("reqChk")
    @XStreamAsAttribute
    private String reqChk;

    /**
     * resChk
     * 값이 없으면 복사처리
     * M 이면 값을 변경해야 한다는 의미
     */
    @XStreamAlias("resChk")
    @XStreamAsAttribute
    private String resChk;

    /**
     * ref
     */
    @XStreamAlias("ref")
    @XStreamAsAttribute
    private String fieldRef;

    @XStreamAlias("convertType")
    @XStreamAsAttribute
    private String convertType = "String";

    @XStreamOmitField
    private Logger LOG = LoggerFactory.getLogger(getClass());

    @XStreamOmitField
    private int index;
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


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    /**
     * fieldDesc
     *
     * @return
     */
    public String getFieldRef() {
        return fieldRef;
    }

    /**
     * fieldRef
     *
     * @param fieldRef
     */
    public void setFieldRef(String fieldRef) {
        this.fieldRef = fieldRef;
    }

    /**
     * reqChk
     *
     * @return
     */
    public String getReqChk() {
        return reqChk;
    }

    /**
     * @param reqChk
     */
    public void setReqChk(String reqChk) {
        this.reqChk = reqChk;
    }

    public String getResChk() {
        return resChk;
    }

    public void setResChk(String resChk) {
        this.resChk = resChk;
    }

    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
    }

//    public void setup(MsgIF msgIF, ByteBuf byteBuf,Charset charset) {
//        try {
//            Object o = getValue(byteBuf,charset);
//            if (o != null) {
//                setValue(msgIF, o);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public int setup(Object msgIF,byte[] bytes, int offset,Charset charset) {
        try {
            String value = new String(bytes, offset, fieldLength, charset);
            LOG.debug(fieldId+" "+fieldLength+" value=]"+value+"[");
            Object o = getValue(value);
            if (o != null) {
                setValue(msgIF, o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offset+fieldLength;
    }

    public void setValue(Object msgIF,Object o) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        MethodUtils.invokeMethod(msgIF, getSetMethodName(),o);
    }
    public String getSetMethodName() {
        return "set"+fieldId;
    }

    public String getGetMethodName() {
        return "get"+fieldId;
    }
    private Object getValue(String value) {
        Object o = null;
       if ("Integer".equals(convertType)) {
            o = Integer.valueOf(value);
        } else if ("Long".equals(convertType)) {
            o = Long.valueOf(value);
        } else {
           if("AN".equalsIgnoreCase(fieldType)){

               o = value;
           }else {
               o = value.trim();
           }
        }
        return o;
    }

    private String getString(ByteBuf buf, int length,Charset charset) {
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes, charset);
    }

    public void setString(StringBuilder builder,Object o,Charset charset) throws Exception {
        String value = getValue(o,charset);
        LOG.debug(fieldId+" "+fieldLength+" value=]"+value+"[");
        builder.append(value);
    }
//    public void setString2(ByteBuf buf,Object o,Charset charset) throws Exception {
//        String value = getValue(o,charset);
//        buf.writeBytes(value.getBytes(charset));
//    }
    /**
     * encode할 때 사용함.
     * @param o
     * @return
     * @throws Exception
     */
    public String getValue(Object o,Charset charset) throws Exception {
        Object value = getValue(o,getGetMethodName());
        if (value == null) {
            return pad("", fieldLength, fieldType,charset);
        } else {
            return pad(value.toString(), fieldLength, fieldType,charset);
        }
    }

    private String pad(String value, int length, String dataType,Charset charset) {
        if ("AN".equals(dataType)) {
            return byteArrayPad(value, length, " ",charset);
        } else if ("N".equals(dataType)) {
            return leftPad(value, length, "0");
        } else if ("AH".equals(dataType)) {
            return byteArrayPad(value, length, " ", charset);
        } else {
            return rightPad(value, length, " ");
        }
    }

    private String rightPad(String value,int length,String padStr) {
        return StringUtils.rightPad(value, length, padStr);
    }
    private String leftPad(String value,int length,String padStr) {
        return StringUtils.leftPad(value, length, padStr);
    }


    private String byteArrayPad(String string,int minLength,String padString,Charset charset) {
        byte[] bytes = string.getBytes(charset);
        int size = minLength - bytes.length;
        if (size <= 0) {
            return string;
        } else {
            byte[] _bytes = padString.getBytes(charset);
            byte[] results = bytes;
            for(int i =0;i<size;i++) {
                results = concat(results, _bytes);
            }

            return new String(results,charset);
        }
    }

    private byte[] concat(byte[]... arrays) {
        int length = 0;
        byte[][] var2 = arrays;
        int pos = arrays.length;

        for(int var4 = 0; var4 < pos; ++var4) {
            byte[] array = var2[var4];
            length += array.length;
        }

        byte[] result = new byte[length];
        pos = 0;
        byte[][] var9 = arrays;
        int var10 = arrays.length;

        for(int var6 = 0; var6 < var10; ++var6) {
            byte[] array = var9[var6];
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }

        return result;
    }
//
    private Object getValue(Object o,String methodName) throws Exception {
        return MethodUtils.invokeMethod(o, methodName);
    }

    public Object getValue(Object o) throws Exception {
        return getValue(o, getGetMethodName());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fieldId", fieldId)
                .append("fieldLength", fieldLength)
                .append("fieldType", fieldType)
                .append("fieldDesc", fieldDesc)
                .append("reqChk", reqChk)
                .append("resChk", resChk)
                .append("fieldRef", fieldRef)
                .append("convertType", convertType)
                .append("index", index)
                .toString();
    }

    public void parse(ByteBuf buf, List<Layout> layouts,Charset charset) {
        String value = getString(buf, fieldLength,charset);
        String _value = StringUtils.replace(value," ","[]");
        layouts.add(new Layout(fieldId, fieldType, _value, fieldDesc,fieldLength+""));
    }

    public void setpuResponse(MsgIF response) {
        //값을 변경해야 하므로 일단 공백처리.
        if ("M".equals(getResChk())) {
            try {
                setValue(response, "");
//                LOG.debug(fieldId+"  setValue=\"\"");
            } catch (Exception e) {

            }
        } else {
//            LOG.debug(fieldId+" do nothing.");
        }
    }
}
