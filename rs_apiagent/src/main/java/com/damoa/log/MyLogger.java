package com.damoa.log;

/**
 * <p>Title: AegisAtmClient  </p>
 * <p>Description: Aegis Cash Management System</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: AegisHyosung </p>
 *
 * @author KSS
 * @version 1.0
 */


import com.damoa.comm.ClientInfo;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MyLogger {
    public MyLogger() {
        strFile = ClientInfo.LOG_PATH + "SLog" + getNowDate() + ".log";
    }

    public MyLogger(String dataField, String workField) {
        strDataField = dataField;
        strWorkField = workField;
    }

    public String getNowDate() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(calendar.get(1));
        stringbuffer.append(calendar.get(2) + 1 >= 10 ? String.valueOf(calendar.get(2) + 1) : "0" + (calendar.get(2) + 1));
        stringbuffer.append(calendar.get(5) >= 10 ? String.valueOf(calendar.get(5)) : "0" + calendar.get(5));
        return stringbuffer.toString();
    }

    public PrintWriter getWriter() {

        PrintWriter printwriter = null;
        try {
            printwriter = new PrintWriter(new FileOutputStream(MyLogger.strFile, true));
//            printwriter = new PrintWriter(System.out);
        } catch (Exception _ex) {
            System.out.println("ERR:SendLog#getWriter");
        }
        return printwriter;
    }

    private String getDate(String format) {
        try {
            Date date = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
            return simpledateformat.format(date);
        } catch (Exception ex) {
            return "000000000000";
        }
    }

    public void out(String s, boolean flag) {
        PrintWriter printwriter = null;
        try {
            String _s = toMsg(s);
            System.out.println(_s);

            printwriter = getWriter();
            if (flag) {
                printwriter.println(_s);
            } else
                printwriter.print(_s);
        } catch (Exception _ex) {
            System.out.println("ERR:SendLog#out");
        } finally {
            if (printwriter != null)
                printwriter.close();
        }
    }

    private String toMsg(String s) {
        return "[" + getDate("HH:mm:ss") + "][" + packetno + "]" + strDataField + "-" + strWorkField + " " + s;
    }

    public void print(String s) {
        out(s, true);
    }

    public void printStackTrace(Exception exception) {
        PrintWriter printwriter = null;
        try {
            printwriter = getWriter();
            exception.printStackTrace(printwriter);
        } catch (Exception _ex) {
            System.out.println("ERR:SendLog#printStackTrace");
        } finally {
            if (printwriter != null)
                printwriter.close();
        }
//        LOG.error(e.getMessage(), e);
    }

    public void println(String s) {
        out(s, true);
    }

    public void setStrDataField(String strDataField) {
        MyLogger.strDataField = strDataField;
    }

    public void setStrWorkField(String strWorkField) {
        MyLogger.strWorkField = strWorkField;
    }

    public void setPacketno(String packetno) {
        MyLogger.packetno = packetno;
    }

    private static String strDataField = "";
    private static String strWorkField = "";
    private static String packetno = "";
    private static String strFile = "";
}
