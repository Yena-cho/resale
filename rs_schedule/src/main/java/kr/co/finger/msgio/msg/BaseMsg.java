package kr.co.finger.msgio.msg;

import kr.co.finger.damoa.commons.DateUtils;
import kr.co.finger.damoa.commons.Maps;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

public class BaseMsg implements Serializable, Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    byte[] readFile(String filePath) throws IOException {
        return FileUtils.readFileToByteArray(new File(filePath));
    }

    String getValue(Map<String, Object> map, String key) {
        return Maps.getValue(map, key);
    }
//
    String getDateValue(Map<String, Object> map, String key, String format) {
        return Maps.getDateValue(map, key,format);
    }


}
