package damoa;

import damoa.comm.data.ServerInfo;
import damoa.conf.PropertiesLoader;
import damoa.server.ServerMain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Locale;

public class DamoaSvr {
    public static void main(String args[]) {

        boolean testFlag   = false;
        ServerInfo.isTest = testFlag;
        System.out.println("Default Locale    : " + Locale.getDefault());
        System.out.println("Default Charset   : " + Charset.defaultCharset());
        System.out.println("file.encoding     : " + System.getProperty("file.encoding"));
        System.out.println("sun.jnu.encoding  : " + System.getProperty("sun.jnu.encoding"));
        System.out.println("Default Encoding  : " + getEncoding());

        PropertiesLoader.getInstance();
        ServerMain sMain = new ServerMain(testFlag);
        sMain.start();
        return;
    }

    public static String getEncoding()
    {
        final byte [] bytes = {'D'};
        final InputStream inputStream = new ByteArrayInputStream(bytes);
        final InputStreamReader reader = new InputStreamReader(inputStream);
        final String encoding = reader.getEncoding();
        return encoding;
    }

}
