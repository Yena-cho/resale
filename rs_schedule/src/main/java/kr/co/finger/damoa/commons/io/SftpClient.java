package kr.co.finger.damoa.commons.io;

import com.jcraft.jsch.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * JSch 래퍼
 * 
 * @author lloydkwon@gmail.com
 * @author wisehouse@finger.co.kr
 */
public class SftpClient {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private String host;
    private int port;
    private String user;
    private String password;
    
    private Session session;
    private ChannelSftp channel;

    public SftpClient(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void connect() throws JSchException {
        LOGGER.info("connect..." + host + " " + port);
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();
        LOGGER.info("connected");
        this.session = session;
    }

    public void disconnect() {
        if (session != null) {
            if (session.isConnected()) {
                LOGGER.info("disconnect...");
                session.disconnect();
                LOGGER.info("disconnected");
            }
        } else {
            LOGGER.error("can't make session...");
        }
    }

    public void upload(String filePath, String remoteDir) throws Exception {
        try {
            connect();
            openChannel();
            cd(remoteDir);
            put(filePath);
        } finally {
            disconnect();
        }
    }

    public void openChannel() throws JSchException {
        LOGGER.info("openChannel ...");
        Channel channel = session.openChannel("sftp");
        channel.connect();
        LOGGER.info("openChanneled");
        this.channel = (ChannelSftp) channel;
    }

    private void cd(String remoteDir) throws SftpException {
        if(channel == null) {
        }
        LOGGER.info("cd..." + remoteDir);
        channel.cd(remoteDir);
    }

    private void put(String filePath) throws SftpException {
        LOGGER.info("put..." + filePath);
        channel.put(filePath, ".", new MyProgressMonitor(), ChannelSftp.OVERWRITE);
        LOGGER.info("put " + filePath);
    }

    public void get(String fileName, String localDir) throws SftpException {
        LOGGER.info("get..." + fileName + " to " + localDir);
        channel.get(fileName, localDir, new MyProgressMonitor(), ChannelSftp.OVERWRITE);
        LOGGER.info("got " + fileName);
    }

    /**
     * @param filePath 다운로드 받을 파일이 있는 경로...
     * @param localDir
     * @throws Exception
     */
    public List<String> download(String filePath, String localDir) throws Exception {
        try {
            connect();
            openChannel();
            cd(filePath);
            List<String> strings = ls();

            for (String name : strings) {
                get(name, localDir);
            }

            return strings;
        } finally {
            disconnect();
        }
    }

    public List<String> download(String filePath, Map<String, String> fileNames, String localDir) throws Exception {
        try {
            connect();
            openChannel();
            cd(filePath);
            List<String> strings = ls(fileNames);
            for (String name : strings) {
                get(name, localDir);
            }

            return strings;
        } finally {
            disconnect();
        }
    }


    private List<String> ls() throws SftpException {
        return listFileNames(".");
    }

    public List<String> listFileNames(String path) throws SftpException {
        LOGGER.debug("ls {}", path);
        final Vector resultVector = channel.ls(path);
        
        if(resultVector == null || resultVector.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> resultList = new ArrayList<>();
        for (Object each : resultVector) {
            if (each instanceof com.jcraft.jsch.ChannelSftp.LsEntry == false) {
                continue;
            }
            
            final ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) each;
            LOGGER.trace("{}\t{}\t{}", lsEntry.getAttrs().getPermissionsString(), new DecimalFormat("###,###,###,##0").format(lsEntry.getAttrs().getSize()), lsEntry.getFilename());

            final String fileName = lsEntry.getFilename();
            if (".".equalsIgnoreCase(fileName) || "..".equalsIgnoreCase(fileName)) {
                continue;
            }
            resultList.add(fileName);
        }
        
        return resultList;
    }

    private List<String> ls(Map<String, String> map) throws SftpException {
        LOGGER.info("ls...");
        List<String> names = new ArrayList<>();
        Vector vv = channel.ls(".");
        if (vv != null) {
            for (int ii = 0; ii < vv.size(); ii++) {
                Object obj = vv.elementAt(ii);
                if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                    ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) obj;
                    String fileName = lsEntry.getFilename();
                    if (".".equalsIgnoreCase(fileName) || "..".equalsIgnoreCase(fileName)) {
                        continue;
                    }
                    if (map.containsKey(fileName)) {
                        LOGGER.info("대상파일임 "+fileName);
                        names.add(fileName);
                    } else {
                        LOGGER.info("대상파일이 아님 "+fileName);
                    }
                }
            }
            return names;
        } else {
            return names;
        }
    }

    private String findRemoteDir(String filePath) {
        return FilenameUtils.getFullPath(filePath);
    }

    private String findFileName(String filePath) {
        return FilenameUtils.getName(filePath);
    }

    private class MyProgressMonitor implements SftpProgressMonitor {
        ProgressMonitor monitor;
        long count = 0;
        long max = 0;

        public void init(int op, String src, String dest, long max) {
            this.max = max;
            monitor = new ProgressMonitor(null,
                    ((op == SftpProgressMonitor.PUT) ?
                            "put" : "get") + ": " + src,
                    "", 0, (int) max);
            count = 0;
            percent = -1;
            monitor.setProgress((int) this.count);
            monitor.setMillisToDecideToPopup(1000);
        }

        private long percent = -1;

        public boolean count(long count) {
            this.count += count;

            if (percent >= this.count * 100 / max) {
                return true;
            }
            percent = this.count * 100 / max;

            monitor.setNote("Completed " + this.count + "(" + percent + "%) out of " + max + ".");
            monitor.setProgress((int) this.count);

            return !(monitor.isCanceled());
        }

        public void end() {
            monitor.close();
        }
    }


}
