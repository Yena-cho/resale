package com.damoa.comm;

import com.damoa.util.JceDesUtil;
import com.damoa.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class ClientInfo {

	public static boolean isDEBUG = false;
    public static String TRUST_STORE = "";
    public static String TRUST_STORE_PW = "";
    // xc_conf.txt PATH
    public static String XC_CONF_PATH = "";
    public static String HOME_PATH = "";
    public static String LOG_PATH = "";
    public static String CLIENT_OS = "";

    public static String SERVICE_NAME = "";
    public static String USER_ID = "";
    public static String SERVER_IP = "";
    public static int APPLY_PORT = 0;

    // 전문 DB 연결
    public static String DB_TYPE = "";
    public static String DB_URL = "";
    public static String DB_USER = "";
    public static String DB_PW = "";
    public static String DB_TABLE = "";
    public static String DB_ROWCNT = "";
    public static String API_HOME = "";
    public static String DB_CLASS_NAME = "";
    public static String PROCESS_NAME = "";
    public static int SEND_INTERVAL = 0;
    
    public static String CHAR_ENCODING = "EUC-KR";

    public ClientInfo() {
        Properties dbProps = new Properties();
        InputStreamReader is = null;

        HOME_PATH = System.getProperty("user.dir");
        LOG_PATH = HOME_PATH + "/logs/";

        try {
            System.out.println(HOME_PATH);
            //is = new InputStreamReader(new FileInputStream(HOME_PATH + "/config/" + "damoaclient.properties"),"UTF-8");
            is = new InputStreamReader(new FileInputStream(new File(HOME_PATH + "/config/" + "damoaclient.properties")),"UTF-8");
            dbProps.load(is);
            
            isDEBUG = Boolean.parseBoolean(dbProps.getProperty("LOG_DEBUGGING", "false"));

            API_HOME = dbProps.getProperty("API_HOME");
            //System.out.println("API_HOME        : "+API_HOME);
            CLIENT_OS = dbProps.getProperty("CLIENT_OS").toUpperCase();
            if ("WINDOWS".equals(CLIENT_OS)) {
                XC_CONF_PATH = HOME_PATH + "/xc3_windows/conf/";
            } else if ("LINUX".equals(CLIENT_OS)) {
                XC_CONF_PATH = HOME_PATH + "/xc3_linux/conf/";
            } else if ("UNIX".equals(CLIENT_OS)) {
                XC_CONF_PATH = HOME_PATH + "/xc3_unix/conf/";
            }

            SERVICE_NAME = dbProps.getProperty("SERVICE_NAME").toUpperCase();
            USER_ID = dbProps.getProperty("USER_ID");
            SERVER_IP = dbProps.getProperty("SERVER_IP");

            APPLY_PORT = Integer.parseInt(dbProps.getProperty("SERVER_PORT"));
//            if ("N".equals(dbProps.getProperty("TEST_FLAG"))) {
//            } else {
//                APPLY_PORT = Integer.parseInt(dbProps.getProperty("SERVER_PORT_TEST"));
//            }

            DB_TYPE = StringUtil.null2void(dbProps.getProperty("DB_TYPE")).toUpperCase();
            DB_URL = StringUtil.null2void(dbProps.getProperty("DB_URL"));
            DB_USER = StringUtil.null2void(dbProps.getProperty("DB_USER"));
            DB_PW = StringUtil.null2void(dbProps.getProperty("DB_PW"));
            DB_TABLE = StringUtil.null2void(dbProps.getProperty("DB_TABLE"));
            DB_ROWCNT = StringUtil.null2void(dbProps.getProperty("DB_ROWCNT"));
            TRUST_STORE = StringUtil.null2void(dbProps.getProperty("TRUST_STORE"));
            TRUST_STORE = API_HOME + "/" + TRUST_STORE;

            TRUST_STORE_PW = JceDesUtil.getInstance().decode(StringUtil.null2void(dbProps.getProperty("TRUST_STORE_PW")));

            if ("ORACLE".equals(DB_TYPE)) {
                DB_CLASS_NAME = "com.damoa.comm.db.OracleConn";
                PROCESS_NAME = "com.damoa.process.ApplyOracleDB";
            } else if ("MSSQL".equals(DB_TYPE)) {
                DB_CLASS_NAME = "com.damoa.comm.db.MSSqlConn";
                PROCESS_NAME = "com.damoa.process.ApplyMSSqlDB";
            }else if ("MYSQL".equals(DB_TYPE)) {
                DB_CLASS_NAME = "com.damoa.comm.db.MySqlConn";
                PROCESS_NAME = "com.damoa.process.ApplyMySqlDB";
            }else {
                DB_CLASS_NAME = "com.damoa.comm.db.MariaConn";
                PROCESS_NAME = "com.damoa.process.ApplyMySqlDB";
            }

            SEND_INTERVAL = StringUtil.null2zero(dbProps.getProperty("SEND_INTERVAL"));
            if (SEND_INTERVAL <= 0) SEND_INTERVAL = 300; 
//            else if (SEND_INTERVAL < 120) {
//                SEND_INTERVAL = 120;
//            } else if (SEND_INTERVAL > 600) {
//                SEND_INTERVAL = 600;
//            }
        } catch (Exception e) {
            System.err.println("Can't read the properties file. " + "Make sure client.properties is in the CLASSPATH");
            e.printStackTrace();
            return;
        }
    }


    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("API_HOME        : " + HOME_PATH).append("\n");
        builder.append("LOG_PATH        : " + LOG_PATH).append("\n");
        //builder.append("XC_CONF_PATH    : " + XC_CONF_PATH).append("\n");
        builder.append("CLIENT_OS       : " + CLIENT_OS).append("\n");
        builder.append("SERVICE_NAME    : " + SERVICE_NAME).append("\n");
        builder.append("USER_ID         : " + USER_ID).append("\n");
        builder.append("SERVER_IP       : " + SERVER_IP).append("\n");
        builder.append("APPLY_PORT      : " + APPLY_PORT).append("\n");
        builder.append("DB_TYPE         : " + DB_TYPE).append("\n");
        builder.append("DB_URL          : " + DB_URL).append("\n");
        builder.append("DB_USER         : " + DB_USER).append("\n");
        builder.append("DB_PW           : " + DB_PW).append("\n");
        builder.append("DB_TABLE        : " + DB_TABLE).append("\n");
        builder.append("DB_ROWCNT       : " + DB_ROWCNT).append("\n");
        builder.append("DB_CLASS_NAME   : " + DB_CLASS_NAME).append("\n");
        builder.append("PROCESS_NAME    : " + PROCESS_NAME).append("\n");
        builder.append("SEND_INTERVAL   : " + SEND_INTERVAL).append("\n");
        builder.append("TRUST_STORE     : " + TRUST_STORE).append("\n");
        builder.append("TRUST_STORE_PW  : " + TRUST_STORE_PW).append("\n");
        builder.append("LOG_DEBUGGING   : " + isDEBUG).append("\n");

        return builder.toString();

    }

}
