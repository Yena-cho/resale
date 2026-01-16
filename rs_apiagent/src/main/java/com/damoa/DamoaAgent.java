package com.damoa;

import com.damoa.comm.ClientInfo;
import com.damoa.process.ApplyProcess;

public class DamoaAgent {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		ClientInfo ci = new ClientInfo();
//		System.setProperty("file.encoding", "KSC5601");
		System.out.println(ci);
		//System.out.println("file.encoding=" + System.getProperty("file.encoding"));
		System.out.println("");
		int interval = ClientInfo.SEND_INTERVAL;
		int activeID = 0;
		while(true){
			++activeID;
			System.out.println("\nDamoaAgent Process Start...[" + activeID+"]");
			ApplyProcess ap = new ApplyProcess();
			ap.process();
			System.out.println("DamoaAgent Process end...[" + activeID+"]");
			Thread.sleep(interval * 1000);
		}
	}
}
