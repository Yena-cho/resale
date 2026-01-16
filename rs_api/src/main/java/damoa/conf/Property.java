package damoa.conf;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Property {
    public static final boolean ACCEPT_CONTINUE = true;
    public static final int MSGTYPE_SESSION_KEY = 3;
    public static final int MSGTYPE_SESSION_KEY_TEXT = 0;
    public static final int MSGTYPE_CIPHER = 4;
    public static final int MSGTYPE_CIPHER_TEXT = 0;
    public static final int MSGTYPE_FAILED = 255;
    public static final int CMS_YN = 128;
    public static final int XC_SMODE_SERVER = 0;
    public static int DAMOA_PORT = 20701;
    public static int DAMOA_TESTPORT = 10701;
    public static String XC_CONF_PATH;

    public static final int MSGSIZE_LENGTH = 700;
    private static Logger LOG = LoggerFactory.getLogger(Property.class);

    public static void init(Configuration configuration) {
        Property.DAMOA_PORT = configuration.getInt("damoa.port");
//        Property.DAMOA_TESTPORT = configuration.getInt("damoa.tport");

        LOG.info("DAMOA_PORT="+DAMOA_PORT);
//        LOG.info("DAMOA_TESTPORT="+DAMOA_TESTPORT);
    }
}
