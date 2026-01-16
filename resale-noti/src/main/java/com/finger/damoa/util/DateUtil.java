package com.finger.damoa.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class DateUtil {
    public static String getYeaterdayDate() {

        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH,-1);

        Date yesterday = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedYesterday = dateFormat.format(yesterday);


        return formattedYesterday;
    }


}
