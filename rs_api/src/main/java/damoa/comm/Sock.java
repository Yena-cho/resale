package damoa.comm;

import damoa.Constants;
import damoa.comm.bean.Xc3Deliver;
import damoa.comm.bean.Xc3Open;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;


public class Sock {
    /**
     * 서버오픈
     * @param xc3Ssock       :    XC3
     * @param ServerPort    :     서버포트
     * @throws Exception
     */
    public static void serverOpen(Xc3Open xc3Ssock , int ServerPort) throws Exception{
        xc3Ssock.xc3ServerOpen(ServerPort);
    }

    public static void serverClose(Xc3Open xc3Ssock) throws Exception{
        xc3Ssock.xc3ServerClose();
    }

    /**
     * 클라이언트 접속
     * @param xc3Ssock
     * @return ClientSocket     :     클라이언트 접속번호
     * @throws Exception
     */
    public static Socket clientAccept(Xc3Open xc3Ssock) {
        Socket ClientSocket = null;
        try{
            ClientSocket = xc3Ssock.xc3ClientAccept();
        } catch(Exception e3){
            System.out.println(e3.toString());
        }
        return ClientSocket;
    }

    public static void clientClose(Xc3Deliver xc3Ssock, Socket sockClient, BufferedReader dis, DataOutputStream dos) {
        try{
            xc3Ssock.xc3ClientClose(sockClient, dis, dos);
        }catch(Exception e2){
            System.out.println(e2.toString());
        }

    }

    public static int handShake(Xc3Deliver xc3Ssock , Socket sockClient, BufferedReader dis, DataOutputStream dos){
        int ConText = 0;

        try{
            ConText = xc3Ssock.xc3KeyMsgProc(sockClient, dis, dos);
        } catch(Exception e3){
            System.out.println(e3.toString());
        }
        return ConText;
    }

    public static int rcvMessage(Xc3Deliver xc3Ssock, Socket sockClient, BufferedReader dis, DataOutputStream dos, int hCtx, byte rcvByte[]) throws Exception{
        int rmLen = 0;
        rmLen = xc3Ssock.xc3NhReceive(sockClient, dis, dos, hCtx, rcvByte);
        return rmLen;
    }

    public static int sndMessage(Xc3Deliver xc3Ssock, Socket sockClient, BufferedReader dis, DataOutputStream dos, int hCtx, String strMessage) throws Exception{
        try{
            int smLen = 0;
            int sndLen = 0;

            byte[] sndByte = strMessage.getBytes(Constants.MY_CHARSET);
            sndLen = sndByte.length;

            smLen = xc3Ssock.xc3NhSend(sockClient, dis, dos, hCtx, sndByte, sndLen);
            return smLen;
        }catch(Exception e){
            throw e;
        }
    }
}
