package com.damoa.net;

import java.nio.charset.Charset;

import com.damoa.comm.ClientInfo;
import com.damoa.comm.Xc3Socket;
import com.damoa.comm.Xc3SocketImpl;
import com.damoa.log.MyLogger;
import com.damoa.util.StringUtil;

//import com.damoa.comm.Xc3SocketApi;
//import com.damoa.comm.Xc3SocketV3;


public class ApplySocket {

    private Xc3Socket xc3Ssocket;
    private MyLogger log = new MyLogger();
    public ApplySocket() throws Exception {
//        if("WINDOWS".equals(ClientInfo.CLIENT_OS)){
//            this.xc3Ssocket = new Xc3SocketApi();
//        }else if("LINUX".equals(ClientInfo.CLIENT_OS)){
//            this.xc3Ssocket = new Xc3SocketV3();
//        }else if("UNIX".equals(ClientInfo.CLIENT_OS)){
//            this.xc3Ssocket = new Xc3SocketApi();
//        }
        this.xc3Ssocket = new Xc3SocketImpl();
    }
    
    public boolean isOpenSocket() throws Exception {
    	return xc3Ssocket.isAlive();
    }

    public boolean openSocket() throws Exception {

        boolean flag = true;
        int i;
        for(i = 0; flag && i < 10;) {
            i = i++;
            xc3Ssocket.xc3Connect(ClientInfo.APPLY_PORT);

            boolean flag1 = xc3Ssocket.xc3Init();
            if(flag1) {
                flag = false;
            } else {
                log.println("▶서버로부터 새인증서를 다운로드 받았습니다.");
                log.println("▶재시도하겠습니다.");
                xc3Ssocket.xc3Close();
                // 10초후 재시도
                Thread.sleep(10 * 1000);
            }
        }

        if(flag && i == 10) {
            //log.println("▶Key처리에 실패하였습니다.");
            log.println("▶서버접속에 실패했습니다.(재시도 "+i+"번) \n\n서버IP, Port, 방화벽 및 인증서를 확인하세요!!!");
            return false;
        } else {
            return true;
        }
    }

    public boolean closeSocket() throws Exception {
        this.xc3Ssocket.xc3Close();
        return true;
    }

    public boolean sendSocket(String sendData) throws Exception {
        byte sndByte[] = new byte[1024];
        int sndLen = 0;

        sndByte = sendData.getBytes(Charset.forName(ClientInfo.CHAR_ENCODING));
        sndLen = sndByte.length;

        log.println(sndLen + ">>> ["+sendData+"]");

		try {
			int startLen = xc3Ssocket.xc3NhSend(sndByte, sndLen);
			if(startLen >= 1){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			//log.printStackTrace(e);
			// 오류가 나면 소켓을 닫는다.
			//xc3Ssocket.xc3Close();
			return false;
		}
    }

    public String recvSocket() throws Exception {
        try {
			byte rcvByte[] = new byte[1024];
			int rcvLen = 0;
			rcvLen = xc3Ssocket.xc3NhReceive(rcvByte);
			String result = new String(rcvByte, 0, rcvLen, Charset.forName(ClientInfo.CHAR_ENCODING));
			log.println(rcvLen + " <<< ["+result+"]");
			return result;
		} catch (Exception e) {
			//log.printStackTrace(e);
			// 오류가 나면 소켓을 닫는다.
			//xc3Ssocket.xc3Close();
			return "";
		}

    }
}
