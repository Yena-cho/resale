package com.damoa.process;

import com.damoa.comm.ClientInfo;
import com.damoa.comm.UserException;
import com.damoa.comm.db.DBConn;
import com.damoa.msg.AegisDMsg;
import com.damoa.msg.AegisRMsg;
import com.damoa.util.MirRecord;

import java.util.Vector;

public class ApplyMSSqlDB extends ApplyDB{
	private DBConn conn = null;

	@SuppressWarnings("rawtypes")
	private Vector vt = null;
	
	public ApplyMSSqlDB(){
		
	}

	@SuppressWarnings("rawtypes")
	public void setInfo(DBConn conn){
		this.conn = conn;
		vt = new Vector();
	}
	
	public MirRecord selectPayData() throws UserException, Exception{
		MirRecord record = null;
		
		String qry = "";
    	qry	= "\n SELECT * "
        	+ "\n FROM   (SELECT   top " + ClientInfo.DB_ROWCNT
        	+ "\n 				   recordtype, "
        	+ "\n                  serviceid, "
        	+ "\n                  workfield, "
        	+ "\n                  successyn, "
        	+ "\n                  resultcd, "
        	+ "\n                  resultmsg, "
        	+ "\n                  trno, "
        	+ "\n                  vano, "
        	+ "\n                  paymasmonth, "
        	+ "\n                  paymasdt, "
        	+ "\n                  rcpstartdt, "
        	+ "\n                  rcpstarttm, "
        	+ "\n                  rcpenddt, "
        	+ "\n                  rcpendtm, "
        	+ "\n                  rcpprtenddt, "
        	+ "\n                  rcpprtendtm, "
        	+ "\n                  cusnm, "
        	+ "\n                  smsyn, "
        	+ "\n                  mobileno, "
        	+ "\n                  emailyn, "
        	+ "\n                  email, "
        	+ "\n                  content1, "
        	+ "\n                  content2, "
        	+ "\n                  content3, "
        	+ "\n                  content4, "
        	+ "\n                  chatrno, "
        	+ "\n                  adjfigrpcd, "
        	+ "\n                  payitemnm, "
        	+ "\n                  payitemamt, "
        	+ "\n                  payitemyn, "
        	+ "\n                  rcpitemyn, "
        	+ "\n                  rcpbusinessno, "
        	+ "\n                  rcppersonno, "
        	+ "\n                  prtpayitemnm, "
        	+ "\n                  prtcontent1, "
        	+ "\n                  prtseq, "
        	+ "\n                  transt, "
        	+ "\n                  regseq "
        	+ "\n         FROM     " + ClientInfo.DB_TABLE
        	+ "\n         WHERE    transt = '01' "
        	+ "\n         ORDER BY regseq) a "
        	;
	
    	record = conn.selectRecord(qry);
    	
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public boolean UpdatePayData(AegisDMsg dMsg) throws UserException, Exception{
		boolean result = true;
		int rtn = 0;
		
		String qry	= "";
		qry = "update " + ClientInfo.DB_TABLE 
			+ " set transt = '07', "
    		+ "     successyn = ?, "
			+ "     resultcd = ?, "
			+ "     resultmsg = ? "
			+ "where regseq = ? ";

		vt.clear();
		vt.add(dMsg.successYN);
		vt.add(dMsg.resultCd);
		vt.add(dMsg.resultMsg);
		vt.add(dMsg.regSeq);
		rtn = conn.update(qry, vt);
		
		if(rtn != 1){
			result = false;
		}
		return result;
	}

    @SuppressWarnings("unchecked")
	public boolean UpdatePayFailedData(String regSeq) throws UserException, Exception{
    	boolean result = true;
    	int rtn = 0;
    	
    	String qry	= "";
    	qry = "update " + ClientInfo.DB_TABLE
    			+ " set transt = '99' "
    			+ "where regseq = ? ";
    	
    	vt.clear();
    	vt.add(regSeq);
    	rtn = conn.update(qry, vt);
    	
    	if(rtn != 1){
    		result = false;
    	}
    	return result;
    }
	
	@SuppressWarnings("unchecked")
	public boolean InsertRcpData(AegisRMsg rMsg) throws UserException, Exception{
		boolean result = true;
		int rtn = 0;
		vt.clear();
		
		String qry	= "";
		if("DAMOA".equalsIgnoreCase(ClientInfo.SERVICE_NAME)){
    		qry = "\n INSERT INTO " + ClientInfo.DB_TABLE + " ( "
            	+ "\n    recordtype, serviceid, workfield,  "
            	+ "\n    successyn, resultcd, resultmsg,  "
            	+ "\n    trno, vano, paymasmonth,  "
            	+ "\n    paymasdt, chatrno, adjfigrpcd,  "
            	+ "\n    payitemnm, payitemamt, transt,  "
            	+ "\n    payday, rcpusrname, bnkcd,  "
            	+ "\n    bchcd, mdgubn, "
            	+ "\n 	 trnday, rcpamt,  "
            	+ "\n 	 content1, content2, content3, content4,  "
            	+ "\n    regdt, svecd "
            	+ "\n    ) VALUES ( "
    			+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, '03', "
            	+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, "
            	+ "\n     ?, ?,  "
    			+ "\n     ?, ?, ?, ?,  "
    			+ "\n     getdate (), ? "
        		+ "\n    ) ";
    		
    		vt.add(rMsg.DrecordType);
    		vt.add(rMsg.serviceId);
    		vt.add(rMsg.workField);
    		vt.add(rMsg.seccessYN);
    		vt.add(rMsg.resultCd);
    		vt.add(rMsg.resultMsg);
    		vt.add(rMsg.trNo);
    		vt.add(rMsg.vaNo);
    		vt.add(rMsg.payMasMonth);
    		vt.add(rMsg.payMasDt);
    		vt.add(rMsg.chaTrNo);
    		vt.add(rMsg.adjfiGrpCd);
    		vt.add(rMsg.payItemNm);
    		vt.add(rMsg.payItemAmt);
    		vt.add(rMsg.rcpDt);
    		vt.add(rMsg.rcpUsrName);
    		vt.add(rMsg.bnkCd);
    		vt.add(rMsg.bchCd);
    		vt.add(rMsg.mdGubn);
    		vt.add(rMsg.trnDay);
    		vt.add(rMsg.rcpAmt);
    		vt.add(rMsg.content1);
    		vt.add(rMsg.content2);
    		vt.add(rMsg.content3);
    		vt.add(rMsg.content4);
    		vt.add(rMsg.svecd);

    		
        }else if("THEBILL".equalsIgnoreCase(ClientInfo.SERVICE_NAME)){
        	qry = "\n INSERT INTO " + ClientInfo.DB_TABLE + " ( "
            	+ "\n    recordtype, serviceid, workfield,  "
            	+ "\n    successyn, resultcd, resultmsg,  "
            	+ "\n    trno, vano, paymasmonth,  "
            	+ "\n    paymasdt, chatrno, adjfigrpcd,  "
            	+ "\n    payitemnm, payitemamt, transt,  "
            	+ "\n    payday, rcpusrname, bnkcd,  "
            	+ "\n    bchcd, mdgubn, "
            	+ "\n 	 trnday, rcpamt, vanobnkcd, svecd, "
            	+ "\n    regdt ) VALUES (  "
            	+ "\n 	  ?, ?, ?, "
            	+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, '03', "
            	+ "\n     ?, ?, ?, "
            	+ "\n     ?, ?, "
            	+ "\n     ?, ?, ?, ?, "
        		+ "\n     getdate ()  "
        		+ "\n     ) ";
        	
        	vt.add(rMsg.DrecordType);
        	vt.add(rMsg.serviceId);
        	vt.add(rMsg.workField);
        	vt.add(rMsg.seccessYN);
        	vt.add(rMsg.resultCd);
        	vt.add(rMsg.resultMsg);
        	vt.add(rMsg.trNo);
        	vt.add(rMsg.vaNo);
        	vt.add(rMsg.payMasMonth);
        	vt.add(rMsg.payMasDt);
        	vt.add(rMsg.chaTrNo);
        	vt.add(rMsg.adjfiGrpCd);
        	vt.add(rMsg.payItemNm);
        	vt.add(rMsg.payItemAmt);
        	vt.add(rMsg.rcpDt);
        	vt.add(rMsg.rcpUsrName);
        	vt.add(rMsg.bnkCd);
        	vt.add(rMsg.bchCd);
        	vt.add(rMsg.mdGubn);
        	vt.add(rMsg.trnDay);
        	vt.add(rMsg.rcpAmt);
        	vt.add(rMsg.vaNoBnkCd);
        	vt.add(rMsg.svecd);
        }

		rtn = conn.update(qry, vt);
		
		if(rtn != 1){
			result = false;
		}
		
		return result;
	}
}
