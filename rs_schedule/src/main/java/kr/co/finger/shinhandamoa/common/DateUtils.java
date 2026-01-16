package kr.co.finger.shinhandamoa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 날짜 관련 유틸리티
 * 
 * @author wisehouse@finger.co.kr
 */
public class DateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    private DateUtils() {
        throw new RuntimeException();
    }
    
    public static final Date parseDate(String dateString, String pattern) {
        return parseDate(pattern, dateString, new Date());
    }
    
    public static final Date parseDate(String dateString, String pattern, Date defaultDate) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(pattern, dateString);
        } catch (ParseException e) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.warn(e.getMessage(), e);
            } else {
                LOGGER.warn(e.getMessage());
            }
            
            return defaultDate;
        }
    }
    
    public static final Date trundateHour(Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);
    }
    
    public static final Date addDays(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
    }
    
    public static final Date addMonths(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
    }


    public static String formatDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date getLastMonthDate(Date date) {
        return addDays(addMonths(date, 1), -1);
    }
}
