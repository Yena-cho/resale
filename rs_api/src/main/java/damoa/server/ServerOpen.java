package damoa.server;

import java.net.Socket;

import damoa.comm.Sock;
import damoa.comm.bean.Xc3Open;
import damoa.comm.log.GeneLog;
import damoa.comm.util.DateUtil;
import damoa.conf.PropertiesLoader;
import damoa.conf.Property;
import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerOpen implements Runnable {

    private int intPort;
    private Xc3Open xc3Ssocket;
    private String packetNo;
    private boolean testFlag;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    public ServerOpen(int intPort, boolean testFlag) {
        this.intPort = intPort;
        this.testFlag = testFlag;
    }

    public void run() {

        this.packetNo = (new DateUtil()).getTime("YYYYMMDDhhmmssmis");
        GeneLog log = new GeneLog(this.packetNo, "OPEN", "", this.testFlag, LOG);
        try {
            this.xc3Ssocket = new Xc3Open();

            xc3Ssocket.init(PropertiesLoader.getInstance());

            //XecureConnect 서버소켓오픈
            log.println("서버보안소켓오픈시작");
            Sock.serverOpen(this.xc3Ssocket, this.intPort);
            log.println("서버보안소켓포트 : " + this.intPort);
            log.println("서버보안소켓오픈완료");

            Socket sockCsocket = null;
            int hCtx = 0;

            while (Property.ACCEPT_CONTINUE) {
                try {
                    // 클라이언트 ACCEPT!!
                    sockCsocket = Sock.clientAccept(this.xc3Ssocket);
                    log.println("START :::::::::::::::::::::::::::");
                    log.println("CLIENT ACCEPT !");
                    log.println("ACCEPT IP ADDR =" + sockCsocket.getRemoteSocketAddress());
                    ServerProcess sProcess = new ServerProcess(sockCsocket, hCtx, this.testFlag);

                    // Thread 처리
                    Thread aThread = new Thread(sProcess);
                    aThread.start();
                } catch (Exception e) {
                    log.println("[소켓번호:" + sockCsocket.getLocalPort() + "] : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            log.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // XecureConnect 서버소켓종료
                log.println("서버보안소켓종료");
                Sock.serverClose(this.xc3Ssocket);
            } catch (Exception e) {
                log.println(e.getMessage());
            }
            log.println("보안소켓종료");
        }
        return;
    }
}
