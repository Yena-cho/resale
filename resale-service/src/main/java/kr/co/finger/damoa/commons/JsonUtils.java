package kr.co.finger.damoa.commons;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        OBJECT_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
    }

    public static String toJson(Object in) {
        String result = "";
        try {
            result = OBJECT_MAPPER.writeValueAsString(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> T toObject(String json,Class<T> tClass) throws IOException {
        return OBJECT_MAPPER.readValue(json.getBytes(), tClass);
    }

}
