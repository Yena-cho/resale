package damoa.comm.util;

import damoa.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ComCheck {

    public ComCheck() {
    }

    public static boolean chkAccounNo(String s, String s1) {
        if (s.equals("02")) {
            if (s1.length() != 11 && s1.length() != 14)
                return false;
        } else if (s.equals("03")) {
            if (s1.length() != 10 && s1.length() != 11 && s1.length() != 12
                    && s1.length() != 14)
                return false;
        } else if (s.equals("04")) {
            if (s1.length() != 12 && s1.length() != 14)
                return false;
        } else if (s.equals("05")) {
            if (s1.length() != 11 && s1.length() != 12 && s1.length() != 13)
                return false;
        } else if (s.equals("07")) {
            if (s1.length() != 11)
                return false;
        } else if (s.equals("11")) {
            if (s1.length() != 11 && s1.length() != 12 && s1.length() != 14)
                return false;
        } else if (s.equals("20")) {
            if (s1.length() != 11 && s1.length() != 12 && s1.length() != 14
                    && s1.length() != 15 && s1.length() != 13)
                return false;
        } else if (s.equals("21")) {
            if (s1.length() != 11 && s1.length() != 13)
                return false;
        } else if (s.equals("23")) {
            if (s1.length() != 11)
                return false;
        } else if (s.equals("26")) {
            if (s1.length() != 11 && s1.length() != 14)
                return false;
        } else if (s.equals("27")) {
            if (s1.length() != 11)
                return false;
        } else if (s.equals("31")) {
            if (s1.length() != 11 && s1.length() != 12 && s1.length() != 13
                    && s1.length() != 14)
                return false;
        } else if (s.equals("32")) {
            if (s1.length() != 12)
                return false;
        } else if (s.equals("34")) {
            if (s1.length() != 12)
                return false;
        } else if (s.equals("35")) {
            if (s1.length() != 10)
                return false;
        } else if (s.equals("37")) {
            if (s1.length() != 12)
                return false;
        } else if (s.equals("39")) {
            if (s1.length() != 12)
                return false;
        } else if (s.equals("45")) {
            if (s1.length() != 13)
                return false;
        } else if (s.equals("48")) {
            if (s1.length() != 13)
                return false;
        } else if (s.equals("50")) {
            if (s1.length() != 14)
                return false;
        } else if (s.equals("53")) {
            if (s1.length() != 10 && s1.length() != 12 && s1.length() != 13
                    && s1.length() != 14)
                return false;
        } else if (s.equals("71")) {
            if (s1.length() != 14 && s1.length() != 15)
                return false;
        } else if (s.equals("81") && s1.length() != 12 && s1.length() != 14)
            return false;
        return true;
    }

    public static boolean chkDataKind(String s, String s1) {
        String s2 = getStr2Byte2Str(s, 33, 3);
        return s1.equals(s2);
    }

    public static String null2space(String s) {
        String sret = s;
        if(sret==null) sret = "";
        return sret;
    }

    public static boolean chkDateFormat(String s) {
        boolean flag = false;
        String s1 = s.substring(0, 4);
        String s2 = s.substring(4, 6);
        String s3 = s.substring(6, 8);
        int i = Integer.parseInt(s1);
        int j = Integer.parseInt(s2);
        int k = Integer.parseInt(s3);
        if (j < 1 || j > 12)
            return false;
        if (k < 1)
            return false;
        if (i % 4 == 0 || i % 100 == 0 || i % 400 == 0)
            flag = true;
        if (j == 2 && flag && k > 29)
            return false;
        if (j == 2 && !flag && k > 28)
            return false;
        if (k > 31
                && (s2.equals("01") || s2.equals("03") || s2.equals("05")
                || s2.equals("07") || s2.equals("08")
                || s2.equals("10") || s2.equals("03")))
            return false;
        return k <= 30 || !s2.equals("04") && !s2.equals("06")
                && !s2.equals("09") && !s2.equals("11");
    }

    public static boolean chkRecordCRLF(byte abyte0[], int i, int size) {

        int length = size;

        byte byte0 = abyte0[length-2];
        if (byte0 != 13)
            return false;
        byte byte1 = abyte0[length-1];
        return byte1 == 10;
    }

    public static boolean chkRecordKind(String s, String s1) {
        String s2 = getStr2Byte2Str(s, 0, 1);
        return s1.equals(s2);
    }

    public static boolean chkRecordLen(byte abyte0[], int i, int size) {
        int length = size;
        return i == length;
    }

    public static String getStr2Byte2Str(String s, int i, int j) {
        try {
            byte abyte0[] = s.getBytes(Constants.MY_CHARSET);
            return new String(abyte0, i, j,Constants.MY_CHARSET);

        } catch (Exception ex) {
            return "";
        }
    }

    public static boolean isAllNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            String s1 = s.substring(i, i + 1);
            if ("1234567890".indexOf(s1) == -1)
                return false;
        }

        return true;
    }

    public static boolean isAllPhoneNo(String s) {
        for (int i = 0; i < s.length(); i++) {
            String s1 = s.substring(i, i + 1);
            if ("1234567890-".indexOf(s1) == -1)
                return false;
        }

        return true;
    }

    public static boolean isAllEmail(String s) {
        for (int i = 0; i < s.length(); i++) {
            String s1 = s.substring(i, i + 1);
            if ("@.".indexOf(s1) != -1)
                return true;
        }

        return false;
    }

    public static boolean isAlpha(String s) {
        String s1 = s.toUpperCase();
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(s1) != -1;
    }

    public static boolean isDate(String s) {
        Calendar calendar = null;
        try {
            calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(s.substring(0, 4)), Integer
                    .parseInt(s.substring(4, 6)) - 1, Integer.parseInt(s
                    .substring(6, 8)), 0, 0, 0);
            calendar.getTime();
        } catch (Exception ex) {
            calendar = null;
        }
        return calendar != null;
    }

    public static boolean isNumber(String s) {
        return "1234567890".indexOf(s) != -1;
    }

    public static boolean isSpecial(String s) {
        return "\"!@#$%^&*+=|]}[{;:'/?.>,<`~".indexOf(s) != -1;
    }

    public static boolean isSpecialWord(String s) {
        return "'\"\\".indexOf(s) != -1;
    }

    public static String makeSpaceData(String s, int i) {
        String s1;
        for (s1 = s; s1.getBytes(Constants.MY_CHARSET).length < i; s1 = s1 + " ")
            ;
        return s1;
    }

    /**
     * String 왼쪽부터 padding 문자열을 채움
     * @param source 원래 문자열
     * @param padding 필러
     * @param length 자리수
     * @return String 패딩된 문자열
     */
    public static String lPad(String source, String padding, int length) {
        if (source == null) source = "";
        if (padding.equals("")) padding = " ";
        if (source.length() > length) return source.substring(0, length);
        StringBuffer res = new StringBuffer();
        int gap = length - source.length();
        for (int i = 0; i < gap; i++){
            res.append(padding);
        } // end for
        res.append(source);
        return res.toString();
    } // end lPad


    /**
     * Right Padding 한다.
     *
     * @param           source              패딩할 원본
     * @param           padding             패딩 문자
     * @param           length              되어야할 자리수
     */
    public static String rPad(String source, String padding, int length)
    {
        int sourceLen = (source.getBytes(Constants.MY_CHARSET)).length;

        if ( sourceLen >= length)
            return source;

        StringBuffer res = new StringBuffer();
        res.append(source);

        int gap = length - sourceLen;
        for (int i = 0; i < gap; i++)
        {
            res.append(padding);
        }

        return res.toString();
    }

    // 파일 처리일자 check
    public static boolean isProcDate(String workField, String date) {

        boolean result = false;
        String curDate = "";

        DateUtil dt = new DateUtil();
        curDate = dt.getTime("YYYYMMDD");

        if ("A".equalsIgnoreCase(workField)) {
            if (Integer.parseInt(date)>=Integer.parseInt(curDate)
                    && Integer.parseInt(date)<=Integer.parseInt(dt.getComputeDate(curDate,31))) {
                result = true;
            }
        } else if ("R".equalsIgnoreCase(workField)) {
            if (Integer.parseInt(date)<=Integer.parseInt(curDate)) {
                result = true;
            }
        }

        return result;
    }

    public static String getWhatDayString( String YearMonthDay ) {

        String sYear    = YearMonthDay.substring(0,4);
        int iYear       = Integer.parseInt(sYear);
        String sMonth   = YearMonthDay.substring(4,6);
        int iMonth      = Integer.parseInt(sMonth) - 1;
        String sDay     = YearMonthDay.substring(6,8);
        int iDay        = Integer.parseInt(sDay);

        GregorianCalendar addedDay = new GregorianCalendar(iYear, iMonth, iDay);
        java.util.Date d = addedDay.getTime();

        return d.toString().substring ( 0, 3 ) ;
    }

    public static String addDay(String YearMonthDay, int amount){

        String sYear    = YearMonthDay.substring(0,4);
        int iYear       = Integer.parseInt(sYear);
        String sMonth   = YearMonthDay.substring(4,6);
        int iMonth      = Integer.parseInt(sMonth) - 1;
        String sDay     = YearMonthDay.substring(6,8);
        int iDay        = Integer.parseInt(sDay);

        GregorianCalendar addedDay = new GregorianCalendar(iYear, iMonth, iDay);
        addedDay.add(GregorianCalendar.DATE, amount);
        java.util.Date d = addedDay.getTime();
        SimpleDateFormat  df =  new SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
        return df.format(d) ;
    }
}
