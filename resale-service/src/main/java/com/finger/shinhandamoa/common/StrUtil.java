package com.finger.shinhandamoa.common;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class StrUtil {

    //Object가 null 일 경우, "" 값을 리턴 한다.
    public static String nullToVoid(Object obj) {
        if (obj == null) {
            return "";
        }
        return (String) obj;
    }

    //Object 콤마 값을 리턴 한다.
    public static String strComma(Object obj) {
        if (obj == null) {
            return "";
        } else {
            long a = NumberUtils.toLong((String) obj);
            String str = String.format("%,d", a);
            return str;
        }
    }

    //현재 월도를 구해서 리턴 한다.
    public static String getCurrentMonthStr() {
        Date d3 = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMM");
        return transFormat.format(d3);
    }

    //현재 월도을 기준으로 month 값 만큼 이전 또는 이후 월도를 구해서 리턴 한다.
    public static String getCalMonthStr(int month) {
        Date d3 = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(d3);
        cal.add(Calendar.MONTH, month);

        return transFormat.format(cal.getTime());
    }

    //현재 월도을 기준으로 month 값 만큼 이전 또는 이후 월도를 구해서 리턴 한다. yyyy년MM월 형태로 리턴
    public static String getCalMonthFullStr(int month) {
        Date d3 = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년 MM월");
        Calendar cal = Calendar.getInstance();
        cal.setTime(d3);
        cal.add(Calendar.MONTH, month);

        return transFormat.format(cal.getTime());
    }

    //현재 날짜를 리턴 한다.
    public static String getCurrentDateStr() {
        Date d3 = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        return transFormat.format(d3);
    }

    //현재 날짜를 리턴 한다.
    public static String getCurrentDateStrFormat() {
        Date d3 = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        return transFormat.format(d3);
    }

    //현재 월도을 기준으로 month 값 만큼 이전 또는 이후 월도를 구해서 리턴 한다.
    public static String getCalMonthDateStr(int month) {
        Date d3 = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(d3);
        cal.add(Calendar.MONTH, month);

        return transFormat.format(cal.getTime());
    }

    //현재 일자를 기준으로 day 값 만큼 이전 또는 이후 월도를 구해서 리턴 한다.
    public String getCalDayDateStr(int day) {
        Date d3 = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(d3);
        cal.add(Calendar.DATE, day);

        return transFormat.format(cal.getTime());
    }

    //콤마(,)를 제거한다.
    public String getAmtString(String amt) {
        if (amt != null && !amt.equals("")) {
            return amt.replaceAll(",", "").trim();
        } else {
            return "0";
        }
    }

    //현재 월을 포함한 cnt만큼 월도 목록을 만들어 리턴 한다. keyStr=yyyymm, dpStr=yyyy년mm월
    public List<Map<String, Object>> getMonthList(int cnt) {
        List<Map<String, Object>> monthList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < cnt; i++) {
            Map<String, Object> monthMap = new HashMap<String, Object>();

            monthMap.put("keyStr", getCalMonthStr((i * -1)));
            monthMap.put("dpStr", getCalMonthFullStr((i * -1)));
            monthList.add(i, monthMap);
        }
        return monthList;
    }

    // 현재  달
    public static String getYear() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.YEAR));
    }

    // 현재 월
    public static String getMonth() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.MONTH) + 1);
    }

    // 현재 일
    public static String getDate() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.DATE));
    }

    /**
     * Byte 자를때 유니코드 중간에서 잘라야 하는 경우 쓰레기값 제거해서 반환.
     *
     * @param inputB 원래 문자열바이트
     * @param length 자리수
     * @return byte[] 자르고 난 문자열바이트
     */
    public static byte[] unicodeCut(byte[] inputB, int length) {
        boolean boo_middle_cut = false;
        int unicodeSize = 0;
        for (int i = 0; i < length; i++) {
            if (inputB[i] < 0x00) unicodeSize++;
        } // end for
        if (unicodeSize % 2 == 1) boo_middle_cut = true;

        // 유니코드 가운데서 잘림.
        if (boo_middle_cut) return new String(inputB, 0, length - 1).getBytes();
            // 유니코드가 아님. 그냥 자를수 있음.
        else return new String(inputB, 0, length).getBytes();
    }

    /**
     * 8자리 날짜 2018.01.01 형식으로 변환
     *
     * @param date
     * @return
     */
    public static String dateFormat(String date) {

        if (date == null) {
            return "";
        } else {
            String str = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
            return str;
        }
    }

    /*
     * 인증번호 생성
     */
    public static int generateNumber(int length) {

        String numStr = "1";
        String plusNumStr = "1";

        for (int i = 0; i < length; i++) {
            numStr += "0";

            if (i != length - 1) {
                plusNumStr += "0";
            }
        }

        Random random = new Random();
        int result = random.nextInt(Integer.parseInt(numStr)) + Integer.parseInt(plusNumStr);

        if (result > Integer.parseInt(numStr)) {
            result = result - Integer.parseInt(plusNumStr);
        }

        return result;
    }

    //input값 (ex.201805 , 20180501)
    public static String dotDate(String str) {
        if (str.length() < 7) {
            str = str.substring(0, 4) + "." + str.substring(4, 6);
        } else {
            str = str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8);
        }

        return str;
    }

    public static String toDTType(Date date) {
        return toString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /*
     *  엑셀 다운또는 테이블에 은행코드 값을 은행명으로 변경
     *  신재현
     */
    public static String bankNameChanger(String str) {
        String bnkNm = "";
        if (str.equals("003")) {
            bnkNm = "기업은행";
        } else if (str.equals("004")) {
            bnkNm = "국민은행";
        } else if (str.equals("005")) {
            bnkNm = "외환은행";
        } else if (str.equals("007")) {
            bnkNm = "수협";
        } else if (str.equals("011")) {
            bnkNm = "농협";
        } else if (str.equals("012")) {
            bnkNm = "지역농협";
        } else if (str.equals("013")) {
            bnkNm = "지역농협";
        } else if (str.equals("014")) {
            bnkNm = "지역농협";
        } else if (str.equals("015")) {
            bnkNm = "지역농협";
        } else if (str.equals("020")) {
            bnkNm = "우리은행";
        } else if (str.equals("023")) {
            bnkNm = "제일은행";
        } else if (str.equals("026")) {
            bnkNm = "신한은행";
        } else if (str.equals("027")) {
            bnkNm = "씨티은행";
        } else if (str.equals("031")) {
            bnkNm = "대구은행";
        } else if (str.equals("032")) {
            bnkNm = "부산은행";
        } else if (str.equals("034")) {
            bnkNm = "광주은행";
        } else if (str.equals("035")) {
            bnkNm = "제주은행";
        } else if (str.equals("037")) {
            bnkNm = "전북은행";
        } else if (str.equals("039")) {
            bnkNm = "경남은행";
        } else if (str.equals("045")) {
            bnkNm = "새마을금고";
        } else if (str.equals("048")) {
            bnkNm = "신협";
        } else if (str.equals("071")) {
            bnkNm = "우체국";
        } else if (str.equals("081")) {
            bnkNm = "하나은행";
        } else if (str.equals("088")) {
            bnkNm = "신한은행";
        } else if (str.equals("089")) {
            bnkNm = "케이뱅크";
        } else if (str.equals("090")) {
            bnkNm = "카카오뱅크";
        } else if (str.equals("102")) {
            bnkNm = "대신저축은행";
        } else if (str.equals("103")) {
            bnkNm = "에스비아저축은행";
        } else if (str.equals("104")) {
            bnkNm = "에이치케이저축은행";
        } else if (str.equals("105")) {
            bnkNm = "웰컴저축은행";
        } else if (!str.equals("")) {
            bnkNm = str;
        } else {
            bnkNm = "";
        }
        return bnkNm;
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
