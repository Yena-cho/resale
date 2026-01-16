package kr.co.finger.msgio.layout;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 */
public class MessageFormatConverter implements Converter {

    /**
     *
     */
    private final static Logger bioLogger = LoggerFactory.getLogger(MessageFormatConverter.class);

    /**
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return (type != null && type.equals(Message.class));
    }

    /**
     * @param obj
     * @param hsw
     * @param mc
     */
    @Override
    public void marshal(Object obj, HierarchicalStreamWriter hsw, MarshallingContext mc) {
        // do Nothing
    }

    /**
     * @param hsr
     * @param uc
     * @return
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader hsr, UnmarshallingContext uc) {
        Message message = new Message();
        message.setDataFields(new ArrayList<DataField>());

        message.setId(hsr.getAttribute("id"));
        message.setMsgType(hsr.getAttribute("msgType"));
        message.setDesc(hsr.getAttribute("desc"));


        /**
         * message 취득
         */
        hsr.moveDown();


        while ("field".equals(hsr.getNodeName())) {
            if ("field".equals(hsr.getNodeName())) {
                DataField field = new DataField();

                field.setFieldId(hsr.getAttribute("id"));

                if (StringUtils.isEmpty(hsr.getAttribute("ref"))) {
                    field.setFieldLength(Integer.valueOf(hsr.getAttribute("length")));
                    field.setFieldType(hsr.getAttribute("type"));
                    field.setFieldDesc(hsr.getAttribute("desc"));
                    String convertType = hsr.getAttribute("convertType");
                    if (isEmpty(convertType)) {
                        convertType = "String";
                    }
                    field.setConvertType(convertType);
                } else {
                    field.setFieldRef(hsr.getAttribute("ref"));
                }

                message.addDataField(field);
            }
            /**
             * 한줄 진행
             */
            hsr.moveUp();
            if (hsr.hasMoreChildren()) {
                hsr.moveDown();
            }
        }

        return message;
    }

    private boolean isEmpty(String value) {

        if (value == null || "".equals(value)) {
            return true;
        } else {
            return false;
        }
    }
}
