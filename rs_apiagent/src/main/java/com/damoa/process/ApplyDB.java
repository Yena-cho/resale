package com.damoa.process;

import com.damoa.comm.UserException;
import com.damoa.comm.db.DBConn;
import com.damoa.msg.AegisDMsg;
import com.damoa.msg.AegisRMsg;
import com.damoa.util.MirRecord;

public abstract class ApplyDB {
	
	public abstract void setInfo(DBConn conn);
	
	public abstract MirRecord selectPayData() throws UserException, Exception;
	
	public abstract boolean UpdatePayData(AegisDMsg dMsg) throws UserException, Exception;
	
	public abstract boolean UpdatePayFailedData(String regseq) throws UserException, Exception;
	
	public abstract boolean InsertRcpData(AegisRMsg rMsg) throws UserException, Exception;
	
}
