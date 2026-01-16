package damoa.comm.data;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerInfo {

    public static final int READ_C_RECORD_BYTE = 402;
    public static final int READ_P_RECORD_BYTE = 2002;

    // 디렉토리 구성
    public static String HOME_PATH = "F:\\tmp\\sdamoa";
//    public static String HOME_PATH = "/data/thebill/app_file/edi/sdamoa_acct";
    public static String HOME_TEST_PATH = "/data/thebill/app_file/edi_test/sdamoa_acct";

    public static String PAY_DATA_PATH = HOME_PATH + "/recv/";
    public static String PAY_DATA_TESTPATH = HOME_TEST_PATH + "/recv/";

    // 수신 정상 파일 - 배치 요청 자료
    public static String RCP_DATA_PATH = HOME_PATH + "/send/";
    public static String RCP_DATA_TESTPATH = HOME_TEST_PATH + "/send/";

    //로그 파일 구성
    public static String LOG_PATH       = "F:\\tmp\\sdamoa\\";
//    public static String LOG_PATH       = "/data/thebill/logs/edi/sdamoa_acct/";
    public static String LOG_TESTPATH   = "/data/thebill/logs/edi_test/sdamoa_acct/";

    //  DB 연결
    public static String DB_URL      = "";
//    public static String DB_URL      = "jdbc:oracle:thin:@211.232.21.13:1521:ORCL";
    public static String DB_TURL     = "jdbc:oracle:thin:@211.232.21.13:1521:ORCL";
// public static String DB_TURL     = "jdbc:oracle:thin:@(DESCRIPTION= "
//            + "    (ADDRESS_LIST= "
//            + "        (LOAD_BALANCE=OFF)(FAILOVER=ON) "
//            + "        (ADDRESS=(PROTOCOL=TCP)(HOST=000.000.000.000)(PORT=1521)) "
//            + "        (ADDRESS=(PROTOCOL=TCP)(HOST=000.000.000.000)(PORT=1521)) "
//            + "    ) "
//            + "    (CONNECT_DATA=(SERVICE_NAME=DBTEST)) "
//            + ") ";

    public static String DB_USER     = "damoa";
    public static String DB_PW       = "finger2018!";
    public static String DB_TUSER    = "dbdaemon";
    public static String DB_TPW      = "";

    // xc_conf.txt PATH
    public static final String XC_CONF_PATH = "/home/thebill/edi/lib/softforum/xc30/conf/";

    public static boolean isLogDebug = true;
    public static boolean isTest = false;
    private static Logger LOG = LoggerFactory.getLogger(ServerInfo.class);

    public static void init(Configuration configuration) {

        HOME_PATH = configuration.getString("damoa.home.path");

        HOME_TEST_PATH = configuration.getString("damoa.home.test.path");
        LOG_PATH = configuration.getString("damoa.log.path");
        LOG_TESTPATH = configuration.getString("damoa.log.test.path");

        DB_URL=configuration.getString("damoa.db.url");
        DB_TURL=configuration.getString("damoa.db.turl");
        DB_USER=configuration.getString("damoa.db.user");
        DB_PW=configuration.getString("damoa.db.pw");
        DB_TUSER=configuration.getString("damoa.db.tuser");
        DB_TPW=configuration.getString("damoa.db.tpw");


        LOG.info("HOME_PATH      ="+HOME_PATH);
        LOG.info("HOME_TEST_PATH ="+HOME_TEST_PATH);

        LOG.info("LOG_PATH       ="+LOG_PATH);
        LOG.info("LOG_TESTPATH   ="+LOG_TESTPATH);
        
        /**
         * FIXME DB접속정보는 로그에서 제외
         * @modified 2018. 09. 06 jhjeong@finger.co.kr
         */
        //LOG.info("DB_URL="+DB_URL);
        //LOG.info("DB_TURL="+DB_TURL);
        //LOG.info("DB_USER="+DB_USER);
        //LOG.info("DB_PW="+DB_PW);
        //LOG.info("DB_TUSER="+DB_TUSER);
        //LOG.info("DB_TPW="+DB_TPW);

        PAY_DATA_PATH = HOME_PATH + "/recv/";
        RCP_DATA_PATH = HOME_PATH + "/send/";
        
        java.io.File dataDir = new java.io.File(PAY_DATA_PATH);
        if (!dataDir.exists()) {
        	dataDir.mkdirs();
        }
        dataDir = new java.io.File(RCP_DATA_PATH);
        if (!dataDir.exists()) {
        	dataDir.mkdirs();
        }
        
        LOG.info("PAY_DATA_PATH="+PAY_DATA_PATH);
        LOG.info("RCP_DATA_PATH="+RCP_DATA_PATH);
    }

}
