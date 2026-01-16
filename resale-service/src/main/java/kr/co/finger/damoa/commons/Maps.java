package kr.co.finger.damoa.commons;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Maps {
    private Maps(){}

    public static Map<String,Object> hashmap() {
        return new HashMap<>();
    }

    public static String findCheckAmount(Map<String, Object> map) {
        return findKey(map, "AMTCHKTY");
    }

    public static String findAmount(Map<String, Object> map) {
        return findKey(map, "PAYITEMAMT");
    }

    public static String findKey(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return "";
        } else {
            return value.toString().trim();
        }
    }


    public static String findStartDate(Map<String, Object> map) {
        return findKey(map, "STARTDATE");
    }
    public static String findEndDate(Map<String, Object> map) {
        return findKey(map, "ENDDATE");
    }

    public static String findName(Map<String, Object> map) {
        return findKey(map, "CUSNAME");
    }

    public static String findMasMonth(Map<String, Object> map)  {
        return findKey(map, "MASMONTH");
    }

    public static String findNotimasd(Map<String, Object> map)  {
        return findKey(map, "NOTIMASCD");
    }
    public static String findNotidetcd(Map<String, Object> map)  {
        return findKey(map, "NOTIDETCD");
    }
    public static String findNotidetkey(Map<String, Object> map)  {
        return findKey(map, "ADJFIREGKEY");
    }
    public static String findRcpmasd(Map<String, Object> map)  {
        return findKey(map, "RCPMASCD");
    }
    public static String findRcpmasstate(Map<String, Object> map)  {
        return findKey(map, "RCPMASST");
    }
    public static String getValue(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o == null) {
                return "";
            } else {
                return o.toString();
            }
        } else {
            return "";
        }
    }

    public static String getDateValue(Map<String, Object> map, String key, String format) {
        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o == null) {
                return "";
            } else {
                Timestamp date = (Timestamp) o;
                return DateUtils.toString(date, format);
            }
        } else {
            return "";
        }
    }
    public  static String getCashValue(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o == null) {
                return "";
            } else {
                if (o instanceof Timestamp) {
                    Timestamp date = (Timestamp)o;
                    return DateUtils.toString(date, "yyyyMMdd");
                } else if(o instanceof java.sql.Date){
                    java.sql.Date date = (java.sql.Date)o;
                    return DateUtils.toString(date, "yyyyMMdd");
                }else {
                    return o.toString();
                }
            }
        } else {
            return "";
        }
    }

    public static int getIntValue(Map<String, Object> map, String key) {
        String value = getValue(map, key);
        if (StringUtils.isBlank(value)) {
            return 0;
        } else {
            return Integer.valueOf(value);
        }
    }

    public static long getLongValue(Map<String, Object> map, String key) {
        String value = getValue(map, key);
        if (StringUtils.isBlank(value)) {
            return 0;
        } else {
            return Long.valueOf(value);
        }
    }
}
