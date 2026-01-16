package damoa.server;

import damoa.comm.log.GeneLog;
import damoa.comm.util.ComCheck;
import damoa.comm.util.DateUtil;
import damoa.conf.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {

    private boolean testFlag;
    private Logger LOG = LoggerFactory.getLogger(getClass());
    public ServerMain(boolean testFlag) {
        this.testFlag   = testFlag;
    }

    private int intPort;

    public void start() {
        int iPort;
        if (testFlag) iPort = Property.DAMOA_TESTPORT;
        else iPort = Property.DAMOA_PORT;

        String packetNo = (new DateUtil()).getTime("YYYYMMDDhhmmssmis");
        GeneLog log = new GeneLog(packetNo, "MAIN", "", this.testFlag,LOG);

        this.intPort = iPort;
        ServerOpen sOpen = new ServerOpen(this.intPort, this.testFlag);
        if (testFlag) {
            log.println("# TESTSERVER MAIN START [" + this.intPort + "]");
        } else {
            log.println("# SERVER MAIN START [" + this.intPort + "]");
        }
        // Thread o
        Thread aThread = new Thread(sOpen);
        aThread.start();

        return;
    }
}
