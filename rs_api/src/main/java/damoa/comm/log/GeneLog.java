package damoa.comm.log;

/**
 * <p>Title: SDSI - Automatic Payment  Management System</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Aegis Hyosung - Allthegate</p>
 *
 * @author ags2111402@hyosung.com
 * @version 1.0
 */

import damoa.comm.data.ServerInfo;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneLog {

    private String logFilePath;
    private boolean DEBUG = false;

    private String packetNo;
    private String dataKind;
    private String workKind;
    private Logger LOG;
    public GeneLog(String packetNo, boolean testFlag,Logger LOG) {
        this.packetNo = packetNo;
        if (testFlag) this.logFilePath = ServerInfo.LOG_TESTPATH;
        else this.logFilePath = ServerInfo.LOG_PATH;
        this.DEBUG = ServerInfo.isLogDebug;
        this.LOG = LOG;
    }

    public GeneLog(String packetNo, String dataKind, String workKind, boolean testFlag,Logger LOG) {
        this(packetNo, testFlag,LOG);
        this.dataKind = dataKind;
        this.workKind = workKind;
    }

    public void println(String log) {

        String msg = null;

        msg = logMsg(log);

        LOG.info(msg);
//       FileWriter filewriter;
//
//        try {
//            String fileName = logFilePath + getDate("yyyyMMdd") ;
//            filewriter = new FileWriter(fileName+ ".log", true);
//
//            PrintWriter printwriter = new PrintWriter(filewriter);
//            printwriter.print("[" + getDate("kk:mm:ss") + "] " + msg+"\n");
//            System.out.println("[" + getDate("kk:mm:ss") + "] " + msg+"\n");
//            printwriter.flush();
//            filewriter.close();
//            printwriter.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void debugln(String log) {
        if (DEBUG){
            LOG.debug(log);
        }
    }

    private String logMsg(String log) {
        return "[" + this.packetNo + "]" + this.dataKind + "-" + this.workKind + " " + log;
    }

    static private String getDate(String format) {
        try {
            Date date = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
            return simpledateformat.format(date);
        } catch (Exception ex) {
            return "000000000000";
        }
    }
}
