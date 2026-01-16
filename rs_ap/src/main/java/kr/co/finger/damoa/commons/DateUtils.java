package kr.co.finger.damoa.commons;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private DateUtils() {

    }
    
    public static final String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }
    
    public static final String format(long millis, String pattern) {
        return DateFormatUtils.format(millis, pattern);
    }
    
    public static String toString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
    public static String toDateString(Date date) {
        return toString(date, "yyyyMMdd");
    }

    public static String toTimeString(Date date) {
        return toString(date, "HHmmssSSS");
    }
    public static String to6TimeString(Date date) {
        return toString(date, "HHmmss");
    }
    public static String toNowString() {
        return toNowString(new Date());
    }
    public static String toNowString(Date date) {
        return toString(date, "yyyyMMddHHmmssSSS");
    }
    public static String to14NowString() {
        return to14NowString(new Date());
    }
    public static String to14NowString(Date date) {
        return format(date, "yyyyMMddHHmmss");
    }
    public static String toString(Timestamp timestamp, String format) {
        return new SimpleDateFormat(format).format(timestamp);
    }

    public static String toFileName(String fileType, Date date) {
        return fileType + toString(date, "MMdd");
    }

    public static String previousMonth() {
        Date previous = org.apache.commons.lang3.time.DateUtils.addMonths(new Date(), -1);
        return toString(previous, "yyyyMM");
    }

    public static String thisMonth() {
        return toString(new Date(), "MM");
    }

    public static String toDTType(Date date) {
        return toString(date, "yyyy-MM-dd HH:mm:ss");
    }
    public static String toDTType() {
        return toDTType(new Date());
    }
    public static Timestamp now() {
        return toTimestamp(new Date());
    }

    public static Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }


    public static Date toDate(String date, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(date);
    }
}
