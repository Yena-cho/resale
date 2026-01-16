package com.damoa.comm;

import com.damoa.log.MyLogger;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class Xc3SocketImpl implements Xc3Socket {
    private Socket socket;
    DataOutputStream dos;
    BufferedReader dis;
    private MyLogger log = new MyLogger();
    /**
     * 자원해제.
     *
     * @throws Exception
     */
    public void xc3Close() throws Exception {
        log.println("소켓 종료..");
        
        try {
			if (this.dos != null) {
			    this.dos.close();
			}
			if (this.dis != null) {
				this.dis.close();
			}
			if (this.socket != null) {
				this.socket.close();
			}
		} catch (Exception e) {
			log.println(e.getLocalizedMessage());
		} finally {
			this.dos = null;
			this.dis = null;
			this.socket = null;
		}
        
        log.println("▷서버접속을 종료하였습니다.");
    }

    /**
     * 소켓 연결, Timeout 설정, 스트림 서정
     *
     * @param port
     * @throws Exception
     */
    public void xc3Connect(int port) throws Exception {

        File file = new File(ClientInfo.TRUST_STORE);
        ClientInfo.TRUST_STORE = file.getCanonicalPath();

        //log.println("xc3Connect ...." +ClientInfo.TRUST_STORE);
        System.setProperty("javax.net.ssl.trustStore", ClientInfo.TRUST_STORE);
        System.setProperty("javax.net.ssl.trustStorePassword", ClientInfo.TRUST_STORE_PW);
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = sslsocketfactory.createSocket(ClientInfo.SERVER_IP, port);
//        socket = new Socket(ClientInfo.SERVER_IP, port);
        this.socket.setSoTimeout(10 * 60 * 1000);
        this.socket.setSoLinger(true, 0); // TIME WAIT 없애기 위해

        this.dis = new BufferedReader(new InputStreamReader(socket.getInputStream(),Charset.forName(ClientInfo.CHAR_ENCODING)));
        this.dos = new DataOutputStream(this.socket.getOutputStream());
        log.println("▷서버접속에 성공하였습니다.");
    }
    
    /**
     * 소켓 활성화 여부
     * @return true 면 활성화
     * @throws Exception
     */
    public boolean isAlive() throws Exception {
    	if (this.socket == null)  return false;
    	return  !this.socket.isClosed();
    }

    /**
     * 딱히 할 건 없음.
     *
     * @return
     * @throws Exception
     */
    public boolean xc3Init() throws Exception {
        return true;
    }

    /**
     * xc3Init에서 호출
     * 딱히 할 건 없음.
     *
     * @return
     * @throws Exception
     */
    public int xc3KeyMsgProc() throws Exception {
        return 0;
    }

    /**
     * xc3Init에서 호출
     * 딱히..
     *
     * @throws Exception
     */
    public void xc3LibInit() throws Exception {

    }

    /**
     * xc3Read 이용하여 데이터수신.
     * 결과는 abyte0 에 설정.
     *
     * @param _rcvByte 읽은 데이터 크기.
     * @return
     * @throws Exception
     */
    public int xc3NhReceive(byte[] _rcvByte) throws Exception {
        String _in = xc3Read(socket, dis);
        byte[] in = _in.getBytes(ClientInfo.CHAR_ENCODING);
        System.arraycopy(in, 0, _rcvByte, 0, in.length);
        return in.length;
    }

    /**
     * xc3Write 이용하여 데이터 송신.
     *
     * @param abyte0 송신할 데이터
     * @param i      송신할 데이터 크기
     * @return 송신한 데이터 크기.
     * @throws Exception
     */
    public int xc3NhSend(byte[] abyte0, int i) throws Exception {
        return xc3Write(this.socket, dos, abyte0, 0);
    }

    /**
     * 딱히..
     *
     * @param abyte0
     * @return
     */
    public int xc3Read(byte[] abyte0) {
        return 0;
    }

    /**
     * 소켓에서 데이터 읽기..
     *
     * @param _sockClient
     * @param _dis
     * @return
     * @throws Exception
     */
    public byte[] xc3Read(Socket _sockClient, DataInputStream _dis) throws Exception {
        return new byte[0];
    }

    public String xc3Read(Socket _sockClient, BufferedReader _dis) throws Exception {
        return _dis.readLine();
    }

    /**
     * 딱히
     *
     * @param abyte0
     * @param i
     * @param j
     * @return
     */
    public int xc3Write(byte[] abyte0, int i, int j) {
        return 0;
    }

    /**
     * 소켓에 데이터를 쓰기..
     *
     * @param _sockClient
     * @param _dos
     * @param buf
     * @param type
     * @return
     */
    public int xc3Write(Socket _sockClient, DataOutputStream _dos, byte[] buf, int type) {
        try {
            _dos.write(buf, 0, buf.length);
            _dos.flush();
            return buf.length;
        } catch (Exception e) {
        	log.printStackTrace(e);
            return -2;
        }
    }
}
