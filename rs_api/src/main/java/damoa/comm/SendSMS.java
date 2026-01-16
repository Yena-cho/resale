package damoa.comm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import damoa.comm.db.DBPoolConnector;
import damoa.comm.log.GeneLog;
import damoa.comm.util.StringUtil;

public class SendSMS {

	private GeneLog gLog;
	private DataSource ds;
	private Connection conn = null;
	private String custId = "";
	private String procDate = "";
	private String procSeq = "";
	private StringUtil su = new StringUtil();

	public SendSMS(String custId, String procDate, String procSeq, String string, GeneLog gLog, boolean testFlag) {
		this.gLog = gLog;
		this.custId = custId;
		this.procDate = procDate;
		this.procSeq = procSeq;
	}

	public void SMSSend() {

		PreparedStatement pstmt = null;

		try {
			this.ds = DBPoolConnector.getInstance().getDataSource();
			this.conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//TODO 수정해야 함.
		String SQLStr = "";
		SQLStr  = "      INSERT INTO SMSREQ (SMSREQCD  "
				+ "\n                        ,HPNO  "
				+ "\n                        ,CALLBACK  "
				+ "\n                        ,STATUSCD  "
				+ "\n                        ,SENDDT  "
				+ "\n                        ,RESULTCD  "
				+ "\n                        ,MSG  "
				+ "\n                        ,MAKEDT  "
				+ "\n                        ,MAKER  "
				+ "\n                        ,REGDT  "
				+ "\n                        ,SYSTEMCD  "
				+ "\n                        ,SMSGROUPCD  "
				+ "\n                        )  "
				+ "\n        SELECT smsreqcd_seq.NEXTVAL  "
				+ "\n              ,hpno  "
				+ "\n              ,'16617335' as callback  "
				+ "\n              ,'0'  "
				+ "\n              ,TO_CHAR (SYSDATE, 'yyyymmdd')  "
				+ "\n              ,'0000' AS resultcd  "
				+ "\n              ,'다모아 연동오류', '" + procDate + "-" + procSeq + "." + custId +  "다모아 결과파일 전송실패' AS msg  "
				+ "\n              ,SYSDATE  "
				+ "\n              ,'BATCHCHECK' AS maker  "
				+ "\n              ,SYSDATE  "
				+ "\n              ,'INB016' AS systemcd  "
				+ "\n              ,smsgroupcd  "
				+ "\n          FROM billmaster.smslist s  "
				+ "\n         WHERE smsgroupcd = '3' "
		;

		try {
			pstmt   = this.conn.prepareStatement(SQLStr);
			pstmt.executeUpdate();

			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			gLog.println("SMS 전송실패 : "+SQLStr);
		}finally {
			try { if (pstmt != null)  pstmt.close(); }    catch (Exception ne) {ne.printStackTrace(); }
			try{ if(conn!=null)conn.close(); }catch(Exception e) {}
		}
	}

}
