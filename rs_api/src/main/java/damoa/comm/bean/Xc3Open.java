package damoa.comm.bean;

import damoa.comm.util.JceDesUtil;
import damoa.comm.util.UserException;
import damoa.conf.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.net.Socket;

public class Xc3Open {
    private SSLServerSocket sockServer = null;
    private String keyStorePath;
    private String keyStroePw;
    private Logger LOG = LoggerFactory.getLogger(getClass());
    public void xc3ServerOpen(int intPort) throws Exception {
        try {
            System.setProperty("javax.net.ssl.keyStore", keyStorePath);
//            System.setProperty("javax.net.ssl.keyStore", "F:\\tmp\\newfolder\\simplejsondatagenerator\\ssl\\server.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", keyStroePw);
//            System.setProperty("javax.net.ssl.keyStorePassword", "damoa12343");
//            System.setProperty("javax.net.debug", "ssl");

            SSLServerSocketFactory sslserversocketfactory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            sockServer =
                    (SSLServerSocket) sslserversocketfactory.createServerSocket(intPort);
//            this.sockServer = new ServerSocket(intPort);

        } catch (Exception e) {
            String strExceptionMsg = ">>서버소켓초기화를 실패했습니다.";
            UserException ue = new UserException(strExceptionMsg);
            throw ue;
        }

        String passwd = "";
        return;
    }

    public Socket xc3ClientAccept() throws Exception {
        Socket sockClient = null;
        sockClient = this.sockServer.accept();

        return sockClient;
    }

    public void xc3ServerClose() throws Exception {
        if (this.sockServer != null) {
            this.sockServer.close();
        }
        return;
    }

    public void init(PropertiesLoader instance) throws Exception {
        keyStorePath = instance.getConfig("damoa.store.path");
        keyStroePw = JceDesUtil.getInstance().decode(instance.getConfig("damoa.sotre.pw"));
        LOG.info("keyStorePath="+keyStorePath);
        LOG.info("keyStroePw="+keyStroePw);

    }
}
