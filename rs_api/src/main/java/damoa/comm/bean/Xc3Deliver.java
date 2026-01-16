package damoa.comm.bean;


/**
 * <p>Title: SDSI - Socket Data Standard Interface</p>
 * <p>Description: Wizard Cash Management System</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Nautilus Hyosung</p>
 * @author not attributable
 * @version 1.0
 */

import java.io.*;
import java.net.Socket;


import damoa.Constants;
import damoa.comm.util.UserException;
import damoa.conf.Property;


public class Xc3Deliver {
    private static final PrintStream ErrLog = null;

    private int msgType = 0;


    public int xc3KeyMsgProc(Socket _sockClient, BufferedReader _dis, DataOutputStream _dos) throws Exception {
        byte in[] = null;
        byte out[] = null;
        int ret = 0;
        int hCtx = 0;

        return hCtx;
    }

    public int xc3NhReceive(Socket _sockClient, BufferedReader _dis, DataOutputStream _dos, int _hCtx, byte[] _rcvByte) throws Exception {
//        byte in[] = null;
//        int inLen = 0;
//        int ret = 0;

        String input = xc3Read(_sockClient, _dis);
        byte[] in = input.getBytes(Constants.MY_CHARSET);
        System.arraycopy(in,0,_rcvByte,0,in.length);

        return in.length;
    }

    public int xc3NhSend(Socket _sockClient, BufferedReader _dis, DataOutputStream _dos, int _hCtx, byte[] _sndByte, int _sndLen) throws Exception {
        _dos.write(_sndByte);
        return _sndLen;
    }

    public String xc3Read(Socket _sockClient, BufferedReader _dis) throws Exception {
//        byte[] head = new byte[3];
//        byte[] buf = null;
//        int ret = 0;
//        int inLen = 0;
        return _dis.readLine()+"\r\n";
    }

    public int xc3Write(Socket _sockClient, DataOutputStream _dos, byte[] buf, int type) {
        byte head[] = new byte[3];
        int len = 0;

        len = buf.length;

        return len;
    }

    private int xc3WriteErrMsg(Socket sockClient, DataOutputStream _dos, int type) {
        byte head[] = new byte[3];
        int ret= 0;


        return ret;
    }

    public void xc3ClientClose(Socket _sockClient, BufferedReader _dis, DataOutputStream _dos) throws Exception {
        if (_sockClient != null) {
            _dos.close();
            _dis.close();
            _sockClient.close();
        }
        return;
    }
}
