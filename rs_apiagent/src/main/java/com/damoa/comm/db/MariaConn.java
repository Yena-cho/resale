package com.damoa.comm.db;

import com.damoa.comm.ClientInfo;
import com.damoa.log.MyLogger;

import java.sql.DriverManager;


public class MariaConn extends DBConn {
	
    private MyLogger log = new MyLogger();
    
    public void init() {
        try {
        	if (super.isClose() == false) {
        		super.disconnect();
        	}
        	
            Class.forName("org.mariadb.jdbc.Driver");
            super.conn = DriverManager.getConnection(ClientInfo.DB_URL, ClientInfo.DB_USER, ClientInfo.DB_PW);
            log.println("getConnection "+ClientInfo.DB_URL+" "+ClientInfo.DB_USER);
        } catch (Exception e) {
        	super.conn = null;
            log.println("DB Connection Error : " + e.toString());
            log.printStackTrace(e);
        }
   }
    
}
