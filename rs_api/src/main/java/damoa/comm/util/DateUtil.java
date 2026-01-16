/*
 * 작성된 날짜: 2004. 3. 10.
 *
 *
 */
package damoa.comm.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * @author Mir
 *
 *<br>날짜 조작을 위한 클래스
 */
public class DateUtil {

    public DateUtil()    {
    } // end constructor

    private String nowTime = "";
    private String year, month, day, hour24, hour12, min, sec, ampm, milisec;

    private StringUtil st = new StringUtil();

    /**
     * 현재 시간을 구해서 각각의 값을 세팅한다.
     * @return void 시간에 해당하는 변수값 세팅
     */
    private void setTime() {
        //TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Calendar rightNow = Calendar.getInstance();
        year = String.valueOf(rightNow.get(Calendar.YEAR));
        month = st.setDigit(String.valueOf(rightNow.get(Calendar.MONTH) + 1), 2); // java에서 월은 0부터 시작한다.
        day = st.setDigit(String.valueOf(rightNow.get(Calendar.DATE)), 2);
        hour24 = st.setDigit(String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY)), 2);
        hour12 = st.setDigit(String.valueOf(rightNow.get(Calendar.HOUR)), 2);
        min = st.setDigit(String.valueOf(rightNow.get(Calendar.MINUTE)), 2);
        sec = st.setDigit(String.valueOf(rightNow.get(Calendar.SECOND)), 2);
        ampm = (rightNow.get(Calendar.AM_PM) == 0) ? "AM" : "PM";
        milisec = st.setDigit(String.valueOf(rightNow.get(Calendar.MILLISECOND)), 3);
        //log.ptln(ampm);
    }

    /**
     * 현재 시간을 구한다.
     * @return String YYYYMMDDhhmmss
     */
    public String getTime() {
        setTime();
        nowTime = year + month + day + hour24 + min + sec + milisec;
        return nowTime;
    }

    /**
     * 원하는 타입의 현재 시간을 구한다.
     * <br> String YYYY
     * <br> String YY
     * <br> String MM
     * <br> String DD
     * <br> String hh
     * <br> String mm
     * <br> String ss
     * <br> String mis
     * <br> String YYYYMMDD
     * <br> String YYYYMMDDhh
     * <br> String YYYYMMDDhhmm
     * <br> String YYYYMMDDhhmmss
     * <br> String YYYYMMDDhhmmssmis
     * <br> String YYMMDD
     * <br> String YYMMDDhh
     * <br> String YYMMDDhhmm
     * <br> String YYMMDDhhmmss
     * <br> String YYMMDDhhmmssmis
     * <br> String MMDDhhmmss
     * <br> String hhmmss
     * <br> String MMDD
     * @return String YYYYMMDDhhmmss (default)
     */
    public String getTime(String type) {
        setTime();

        if (type.equals("YYYY"))
            nowTime = year;
        else if (type.equals("YY"))
            nowTime = year.substring(2);
        else if (type.equals("MM"))
            nowTime = month;
        else if (type.equals("DD"))
            nowTime = day;
        else if (type.equals("hh"))
            nowTime = hour24;
        else if (type.equals("mm"))
            nowTime = min;
        else if (type.equals("ss"))
            nowTime = sec;
        else if (type.equals("mis"))
            nowTime = milisec;
        else if (type.equals("YYYYMMDD"))
            nowTime = year + month + day;
        else if (type.equals("YYYYMMDDhh"))
            nowTime = year + month + day + hour24;
        else if (type.equals("YYYYMMDDhhmm"))
            nowTime = year + month + day + hour24 + min;
        else if (type.equals("YYYYMMDDhhmmss"))
            nowTime = year + month + day + hour24 + min + sec;
        else if (type.equals("YYYYMMDDhhmmssmis"))
            nowTime = year + month + day + hour24 + min + sec + milisec;
        else if (type.equals("YYMMDD"))
            nowTime = year.substring(2) + month + day;
        else if (type.equals("YYMMDDhh"))
            nowTime = year.substring(2) + month + day + hour24;
        else if (type.equals("YYMMDDhhmm"))
            nowTime = year.substring(2) + month + day + hour24 + min;
        else if (type.equals("YYMMDDhhmmss"))
            nowTime = year.substring(2) + month + day + hour24 + min + sec;
        else if (type.equals("YYMMDDhhmmssmis"))
            nowTime = year.substring(2) + month + day + hour24 + min + sec + milisec;
        else if (type.equals("MMDDhhmmss"))
            nowTime = month + day + hour24 + min + sec;
        else if (type.equals("hhmmss"))
            nowTime = hour24 + min + sec;
        else if (type.equals("MMDD"))
            nowTime = month + day;
        else {
            System.out.println("type에 맞는 값이 없어서 기본값 YYYYMMDDhhmmss을 사용합니다.");
            nowTime = year + month + day + hour24 + min + sec;
        }

        return nowTime;
    } // end getTime

    /**
     * 날짜를 계산한다.(일단위)
     * @param Date  String  YYYYMMDD 형식으로 입력해야 함.
     * @param amount  int 계산할 날짜.
     * @return String 계산된 날짜
     */
    public String getComputeDate(String Date, int amount) {
        String yyyy = Date.substring(0,4);
        String mm = Date.substring(4,6);
        String dd = Date.substring(6,8);

        int y1 = Integer.parseInt(yyyy);
        int m1 = Integer.parseInt(mm) - 1;
        int d1 = Integer.parseInt(dd);

        java.util.GregorianCalendar gc = new GregorianCalendar(y1, m1, d1);
        gc.add(Calendar.DATE, amount);

        return String.valueOf(gc.get(Calendar.YEAR)) + st.setDigit(String.valueOf(gc.get(Calendar.MONTH) + 1), 2) + st.setDigit(String.valueOf(gc.get(Calendar.DATE)), 2);
    } // end getComputeDate

    /**
     * 날짜를 계산한다(일단위).
     * <br>만약 해당 결과의 달에 날짜가 없다면(예: 2월 30일) 가장 가까운 날짜로 계산(예: 2월 28일).
     * @param Date  String  YYYYMMDD 형식으로 입력해야 함.
     * @param amount  int 계산할 날짜.
     * @return String 계산된 날짜
     */
    public String dateCal(String Date, int amount) {
        if (Date.length() != 8) {
            //System.out.println("날짜포맷이 잘못되었습니다.");
            return "dtError!";
        }

        try {
            if (Integer.parseInt(Date) < 0) {
                //System.out.println("날짜포맷이 잘못되었습니다.");
                return "dtError!";
            }
        } catch (Exception e) {
            //System.out.println("날짜포맷이 잘못되었습니다." + e.toString());
            return "dtError!";
        }

        Date = correctDate(Date);

        String yyyy = Date.substring(0,4);
        String mm = Date.substring(4,6);
        String dd = Date.substring(6,8);

        int y1 = Integer.parseInt(yyyy);
        int m1 = Integer.parseInt(mm) - 1;
        int d1 = Integer.parseInt(dd);

        java.util.GregorianCalendar gc = new GregorianCalendar(y1, m1, d1);
        gc.add(Calendar.DATE, amount);

        return String.valueOf(gc.get(Calendar.YEAR)) + st.setDigit(String.valueOf(gc.get(Calendar.MONTH) + 1), 2) + st.setDigit(String.valueOf(gc.get(Calendar.DATE)), 2);
    } // end dateCal

    /**
     * 날짜를 계산한다(월단위).
     * <br>날짜는 입력한 값과 같고 달만 다르게 해서 리턴.
     * <br>만약 해당 결과의 달에 날짜가 없다면(예: 2월 30일) 가장 가까운 날짜를 리턴(예: 2월 28일).
     * @param Date  String  YYYYMMDD 형식으로 입력해야 함.
     * @param amount  int 계산할 개월수.
     * @return String 계산된 날짜
     */
    public String monthCal(String Date, int amount) {
        if (Date.length() != 8) {
            //System.out.println("날짜포맷이 잘못되었습니다.");
            return "dtError!";
        }

        try {
            if (Integer.parseInt(Date) < 0) {
                //System.out.println("날짜포맷이 잘못되었습니다.");
                return "dtError!";
            }
        } catch (Exception e) {
            //System.out.println("날짜포맷이 잘못되었습니다." + e.toString());
            return "dtError!";
        }

        Date = correctDate(Date);

        String yyyy = Date.substring(0,4);
        String mm = Date.substring(4,6);
        String dd = Date.substring(6,8);

        int y1 = Integer.parseInt(yyyy);
        int m1 = Integer.parseInt(mm) - 1;
        int d1 = Integer.parseInt(dd);

        java.util.GregorianCalendar gc = new GregorianCalendar(y1, m1, d1);
        //gc.add(Calendar.DATE, amount);
        gc.add(Calendar.MONTH, amount);

        return String.valueOf(gc.get(Calendar.YEAR)) + st.setDigit(String.valueOf(gc.get(Calendar.MONTH) + 1), 2) + st.setDigit(String.valueOf(gc.get(Calendar.DATE)), 2);
    } // end monthCal

    /**
     * 두 날짜 사이의 날짜를 순서대로 뽑아낸다.
     * <br>만약 해당 결과의 달에 날짜가 없다면(예: 2월 30일) 가장 가까운 날짜로 계산(예: 2월 28일).
     * <br>무한 루프를 막기 위해 날짜수는 365로 제한한다.
     * @param sdt  String  YYYYMMDD 형식으로 입력해야 함. 시작일.
     * @param edt  String  YYYYMMDD 형식으로 입력해야 함. 종료일.
     * @return String[] 입력된 두 날짜를 포함하여 그 사이에 있는 날짜들(YYYYMMDD 형식)의 배열.
     */
    public String[] dateDiff(String sdt, String edt) {
        if (sdt.length() != 8) return null;
        if (edt.length() != 8) return null;

        try {
            if (Integer.parseInt(sdt) < 0) {
                //System.out.println("날짜포맷이 잘못되었습니다.");
                return null;
            }
            if (Integer.parseInt(edt) < 0) {
                //System.out.println("날짜포맷이 잘못되었습니다.");
                return null;
            }
        } catch (Exception e) {
            //System.out.println("날짜포맷이 잘못되었습니다." + e.toString());
            return null;
        }

        sdt = correctDate(sdt);
        edt = correctDate(edt);

        Vector rltvt = dateDiffVT(sdt, edt);
        String[] rlt = new String[rltvt.size()];
        for (int i=0;i<rltvt.size();i++) {
            rlt[i] = (String)rltvt.get(i);
        } // end for
        return rlt;
    } // end dateDiff

    /**
     * 두 날짜 사이의 날짜를 순서대로 뽑아낸다.
     * <br>만약 해당 결과의 달에 날짜가 없다면(예: 2월 30일) 그달의 가장 가까운 날짜로 계산(예: 2월 28일).
     * <br>무한 루프를 막기 위해 날짜수는 365로 제한한다.
     * @param sdt  String  YYYYMMDD 형식으로 입력해야 함. 시작일.
     * @param edt  String  YYYYMMDD 형식으로 입력해야 함. 종료일.
     * @return Vector 입력된 두 날짜를 포함하여 그 사이에 있는 날짜들(YYYYMMDD 형식)의 벡터.
     */
    public Vector dateDiffVT(String sdt, String edt) {
        if (sdt.length() != 8) return null;
        if (edt.length() != 8) return null;

        try {
            if (Integer.parseInt(sdt) < 0) {
                //System.out.println("날짜포맷이 잘못되었습니다.");
                return null;
            }
            if (Integer.parseInt(edt) < 0) {
                //System.out.println("날짜포맷이 잘못되었습니다.");
                return null;
            }
        } catch (Exception e) {
            //System.out.println("날짜포맷이 잘못되었습니다." + e.toString());
            return null;
        }

        sdt = correctDate(sdt);
        edt = correctDate(edt);

        Vector rlt = new Vector();
        String tmp;
        if (Integer.parseInt(sdt) > Integer.parseInt(edt)) {
            tmp = sdt;
            sdt = edt;
            edt = tmp;
        }

        /*
        long sTime = new java.util.Date(Integer.parseInt(sdt.substring(0,4)), Integer.parseInt(sdt.substring(4,6)), Integer.parseInt(sdt.substring(6))).getTime();
        long eTime = new java.util.Date(Integer.parseInt(edt.substring(0,4)), Integer.parseInt(edt.substring(4,6)), Integer.parseInt(edt.substring(6))).getTime();

        long btw = (eTime - sTime)/(24*3600000);

        for (int i=0;i<=btw;i++) {
            System.out.println(dateCal(sdt, i));
        } // end for
        */

        int idx = 0;
        while (true) {
            if (idx >= 365) break;
            rlt.add(dateCal(sdt, idx));
            if (((String)rlt.get(idx)).equals(edt)) break;
            idx++;
        } // end while

        return rlt;
    } // end dateDiffVT


    /**
     * 해당월의 마지막 날짜를 구한다. 입력값이 엄하면 31반환
     * @param YYYY  String  YYYY 형식으로 입력해야 함.
     * @param MM  String MM 형식으로 입력해야 함.
     * @return String 해당월 마지막 날짜
     */
    public String lastDay(String YYYY, String MM) {
        try {
            if (Integer.parseInt(MM) == 2) {
                if (Integer.parseInt(YYYY)%4 == 0) return "29";
                else return "28";
            } else {
                if ((Integer.parseInt(MM) == 4) || (Integer.parseInt(MM) == 6) || (Integer.parseInt(MM) == 9) || (Integer.parseInt(MM) == 11)) return "30";
                else return "31";
            }
        } catch (Exception e) {
            //System.out.println("날짜포맷이 잘못되었습니다.");
            return "31";
        }
    }

    /**
     * 엉뚱한 날짜를 넣었을 경우 맞는 그 달의 가장 가까운 날을 찾아서 리턴.
     * <br>20050245 ==> 20050228
     * @param dt  String  YYYYMMDD 형식으로 입력해야 함.
     * @return String 보정된 날짜(YYYYMMDD 형식).
     */
    public String correctDate(String dt) {
        if (dt.length() != 8) return "dtError!";

        try {
            if (Integer.parseInt(dt) < 0) {
                //System.out.println("날짜포맷이 잘못되었습니다.");
                return "dtError!";
            }
        } catch (Exception e) {
            //System.out.println("날짜포맷이 잘못되었습니다." + e.toString());
            return "dtError!";
        }

        int yyyy = Integer.parseInt(dt.substring(0,4));
        int mm = Integer.parseInt(dt.substring(4,6));
        int dd = Integer.parseInt(dt.substring(6));

        if (yyyy < 0) return "dtError!";
        if (mm < 0) return "dtError!";
        if (dd < 0) return "dtError!";

        if (mm > 12) return "dtError!";

        if ((mm==1) || (mm==3) || (mm==5) || (mm==7) || (mm==8) || (mm==10) || (mm==12)) {
            // 31일까지 있는 달
            if (dd > 31) dd = 31;
        } else if (mm==2) {
            // 윤년이라면 29일까지 있고, 아니면 28일까지 있다.
            if (yyyy%4 == 0) {
                if (dd > 29) dd = 29;
            } else {
                if (dd > 28) dd = 28;
            }
        } else {
            // 30일까지 있는 달.
            if (dd > 30) dd = 30;
        }

        return String.valueOf(yyyy)+st.setDigit(String.valueOf(mm), 2)+st.setDigit(String.valueOf(dd), 2);

    } // end correctDate

} // end class DateUtil
