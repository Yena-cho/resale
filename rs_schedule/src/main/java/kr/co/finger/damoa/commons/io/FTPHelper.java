package kr.co.finger.damoa.commons.io;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FTPHelper {

    private String host;
    private int port = 0;
    private String user;
    private String password;
    private String charset = "EUC-KR";
    private boolean binaryTransfer = false;
    private boolean useEpsvWithIPv4 = false;
    private boolean localActive = false;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    public FTPHelper(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }
    public FTPHelper(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setBinaryTransfer(boolean binaryTransfer) {
        this.binaryTransfer = binaryTransfer;
    }


    public void setLocalActive(boolean localActive) {
        this.localActive = localActive;
    }

    public void setUseEpsvWithIPv4(boolean useEpsvWithIPv4) {
        this.useEpsvWithIPv4 = useEpsvWithIPv4;
    }

    public void send(String filePath, String remote) throws IOException {
        FTPClient client = null;
        try {
            client = createAndConnect();
            login(client);
            setup(client);
            send(client,remote,filePath);
        } finally {
            disconnect(client);
        }
    }

    /**
     * 여러개 파일 전송함.
     * filePaths,remotes 사이즈는 동일해야 함.
     * 각 매핑되는 걸로 전송처리함.
     * @param filePaths 로컬파일리스트
     * @param remotes 리모트파일리스트
     * @throws IOException
     */
    public void sendList(List<String> filePaths, List<String> remotes) throws IOException {
        FTPClient client = null;
        try{
            client = createAndConnect();
            login(client);
            setup(client);
            int size = filePaths.size();
            for (int i = 0; i < size; i++) {
                String filePath = filePaths.get(i);
                String remote = remotes.get(i);
                send(client,remote,filePath);
            }
        } finally {
            disconnect(client);
        }
    }

    private void send(FTPClient client,String remote,String filePath) throws IOException {
        try(InputStream input = new FileInputStream(filePath)) {
            send(client, remote, input);
        }

    }

    private void send(FTPClient client,String remote,InputStream input) throws IOException {
        LOG.info("send... to "+remote);
//        makeDir(client,remote);
        LOG.info("storeFile .... "+remote);
        client.storeFile(remote, input);
    }


    private void makeDir(FTPClient client,String remote) throws IOException {
        String dir = FilenameUtils.normalizeNoEndSeparator(FilenameUtils.getFullPath(remote));
        if (client.cwd(dir) == 550) {
            // 디렉토리가 없다면 생성함.
            LOG.info("no exist "+dir);
            client.makeDirectory(dir);
            LOG.info("makeDirectory "+dir);
        } else {
            LOG.info("exist "+dir);
        }
    }

    private void setup(FTPClient client) throws IOException {
        LOG.info("binaryTransfer "+binaryTransfer);
        LOG.info("localActive "+localActive);
        LOG.info("useEpsvWithIPv4 "+useEpsvWithIPv4);
        if (binaryTransfer) {
            client.setFileType(FTP.BINARY_FILE_TYPE);
        } else {
            // in theory this should not be necessary as servers should default to ASCII
            // but they don't all do so - see NET-500
            client.setFileType(FTP.ASCII_FILE_TYPE);
        }
        if (localActive) {
            client.enterLocalActiveMode();
        } else {
            client.enterLocalPassiveMode();
        }
        client.setUseEPSVwithIPv4(useEpsvWithIPv4);

    }

    private FTPClient createAndConnect() throws IOException {
        FTPClient client = new FTPClient();
        client.setControlEncoding(charset);
        if (port > 0) {
            LOG.info("connect "+host+" "+port+" "+charset);
            client.connect(host, port);
        } else {
            client.connect(host);
            LOG.info("connect "+host+" "+charset);
        }
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            System.err.println("FTP server refused connection.");
            throw new IOException("FTP server refused connection.");
        }

        return client;
    }

    private void login(FTPClient client) throws IOException {
        LOG.info("login "+user+" ***");
        if (!client.login(user, password)) {
            client.logout();
            LOG.info("cant login....");
            throw new IOException("cant login....");
        }
    }

    public void disconnect(FTPClient client) {
        LOG.info("disconnect ");
        if (client != null && client.isConnected()) {
            try {
                client.disconnect();
            } catch (IOException f) {
                // do nothing
            }
        }
    }

}
