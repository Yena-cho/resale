package damoa.comm.db;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import damoa.comm.data.ServerInfo;


public class DBPoolConnector {
	
	private static DBPoolConnector instance;
	private BasicDataSource oracleDS;
	
	public static DBPoolConnector getInstance() {
		if (instance == null) {
			instance = new DBPoolConnector();
		}
		return instance;
	}
	
	private DBPoolConnector() {		
		
		String hostName = "";
		String props = "";
		
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
		}
		props = "v$session.program=damoa-acct-server;v$session.machine=" + hostName;
		
        oracleDS = new BasicDataSource();
        
		if (ServerInfo.isTest) {
			oracleDS.setUrl(ServerInfo.DB_TURL);
			oracleDS.setUsername(ServerInfo.DB_TUSER);
			oracleDS.setPassword(ServerInfo.DB_TPW);
		} else {
			oracleDS.setUrl(ServerInfo.DB_URL);
			oracleDS.setUsername(ServerInfo.DB_USER);
			oracleDS.setPassword(ServerInfo.DB_PW);


		}
		// 서버는 오라클을 사용하므로 이렇게 처리해도 됨
		oracleDS.setDriverClassName("org.postgresql.Driver");
		oracleDS.setMinIdle(5);
		oracleDS.setMaxIdle(5);
		oracleDS.setMaxTotal(15);
		oracleDS.setTestWhileIdle(true);
		oracleDS.setConnectionProperties(props);
		
		oracleDS.setValidationQuery("SELECT 1");
		oracleDS.setTimeBetweenEvictionRunsMillis(300000);
	}

	public DataSource getDataSource(){
        return oracleDS;
	}
}
