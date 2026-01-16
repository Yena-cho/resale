package com.damoa.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public interface Xc3Socket {
	public void xc3Close() throws Exception;

	/**
	 * 소켓연결과 스트림 설정.
	 * @param i
	 * @throws Exception
	 */
	public void xc3Connect(int i) throws Exception;
	public boolean xc3Init() throws Exception;
	public int xc3KeyMsgProc() throws Exception;
	public void xc3LibInit() throws Exception;
	public int xc3NhReceive(byte abyte0[]) throws Exception;
	public int xc3NhSend(byte abyte0[], int i) throws Exception;
	public int xc3Read(byte abyte0[]);
	public byte[] xc3Read(Socket _sockClient, DataInputStream _dis) throws Exception;
	public int xc3Write(byte abyte0[], int i, int j);
	public int xc3Write(Socket _sockClient, DataOutputStream _dos, byte[] buf, int type);
	public boolean isAlive() throws Exception;
}
