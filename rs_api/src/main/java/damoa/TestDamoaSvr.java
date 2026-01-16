package damoa;

import damoa.comm.data.ServerInfo;
import damoa.conf.PropertiesLoader;
import damoa.server.ServerMain;

public class TestDamoaSvr {
    public static void main(String args[]) {

        boolean testFlag   = true;
        ServerInfo.isTest = testFlag;
        PropertiesLoader.getInstance();
        ServerMain sMain = new ServerMain(testFlag);
        sMain.start();
        return;
    }
}
