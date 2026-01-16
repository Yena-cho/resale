package com.ghvirtualaccount.vo;

public class MsgUseStateInfoVO {
	
	private String	msgSendMonth;    //발송월
	private String	inUseYn;    	//사용중
	private String	useAmt;    		 //사용요금      
	private String	sendCnt;     	//이용건수      
	
	public String getMsgSendMonth() {
		return msgSendMonth;
	}
	public void setMsgSendMonth(String msgSendMonth) {
		this.msgSendMonth = msgSendMonth;
	}
	public String getUseAmt() {
		return useAmt;
	}
	public void setUseAmt(String useAmt) {
		this.useAmt = useAmt;
	}
	public String getSendCnt() {
		return sendCnt;
	}
	public void setSendCnt(String sendCnt) {
		this.sendCnt = sendCnt;
	}
	public String getInUseYn() {
		return inUseYn;
	}
	public void setInUseYn(String inUseYn) {
		this.inUseYn = inUseYn;
	}
	
	
}
